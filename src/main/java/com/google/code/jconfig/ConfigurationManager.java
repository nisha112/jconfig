/*
 * @(#)ConfigurationManager.java          Data: 22/gen/2011
 * 
 * 
 * Copyright 2011 Gabriele Fedeli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.google.code.jconfig;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.log4j.Logger;

import com.google.code.jconfig.exception.ConfigurationParsingException;
import com.google.code.jconfig.factory.ConfigurationReaderFactory;
import com.google.code.jconfig.helper.WatchdogService;
import com.google.code.jconfig.listener.IConfigurationChangeListener;
import com.google.code.jconfig.model.ConfigurationInfo;
import com.rits.cloning.Cloner;

/**
 * <p>
 *    Allows to manage application configurations from an external file.
 * </p>
 * 
 * <p>
 *    Upon configuration exception will be maintained the last working
 *    configuration, the error message will be logged, will be sent no
 *    notification to the registered listeners. <br/>
 *    To recover this situation simply change the configuration files in order
 *    to solve the logged errors.
 * </p>
 *
 * @author Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public class ConfigurationManager {

	private String filepath;
	private ConfigurationInfo currentConfigurationInfo;
	private Map<String, IConfigurationChangeListener> activeListeners;
	
	private long delay;
	private static ConfigurationManager instance;
	private static Cloner cloner = new Cloner();
	private static final Logger logger = Logger.getLogger(ConfigurationManager.class);
	private static final Executor poolExecutor = Executors.newSingleThreadExecutor();
	private static final Executor listenerPoolExecutor = Executors.newCachedThreadPool();
	
	private ConfigurationManager() { }
	
	private ConfigurationManager(Map<String, IConfigurationChangeListener> listeners, String filepath) {
		logger.debug("Running on machine with Java version: " + SystemUtils.JAVA_RUNTIME_VERSION);
		if( !SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_5) ) {
			logger.fatal("Current Java version: " + SystemUtils.JAVA_RUNTIME_VERSION + " - NEEDED " + JavaVersion.JAVA_1_5 + " or above.");
			throw new RuntimeException("You must have at leat Java 1.5 using this library.");
		}
		
		logger.info("******* ConfigurationManager initialization *******");
		logger.info(" -> configuration: " + filepath);
		logger.info(" -> registered listeners: " + listeners);
		logger.info("***************************************************");
		
		this.filepath = filepath;
		activeListeners = listeners;
		currentConfigurationInfo = new ConfigurationInfo();
	}
	
	/**
	 * <p>
	 *    Configure the configuration manager to load a configuration file.
	 *    Register a map of listener, that will be invoked on configuration
	 *    load.
	 * </p>
	 * 
	 * @param listeners a map with the registered listener
	 * @param filepath the real path of the configuration file
	 */
	public static synchronized void configure(Map<String, IConfigurationChangeListener> listeners, String filepath) {
		if(instance == null) {
			instance = new ConfigurationManager(listeners, filepath);
			instance.doConfigure();
		}
	}
	
	/**
	 * <p>
	 *    Configure the configuration manager to load a configuration file and 
	 *    to notify changes to to the appropriate listener.
	 * </p>
	 * 
	 * @param listeners a map with the registered listener
	 * @param filepath the real path of the configuration file
	 */
	public static void configureAndWatch(Map<String, IConfigurationChangeListener> listeners, String filepath) {
		configureAndWatch(listeners, filepath, WatchdogService.DEFAULT_DELAY);
	}
	
	/**
	 * <p>
	 *    Configure the configuration manager to load a configuration file and 
	 *    to notify changes to to the appropriate listener.
	 * </p>
	 * 
	 * @param listeners a map with the registered listener
	 * @param filepath the real path of the configuration file
	 * @param delay the time in millis for checking configuration changes
	 */
	public static synchronized void configureAndWatch(Map<String, IConfigurationChangeListener> listeners, String filepath, long delay) {
		if(instance == null) {
			instance = new ConfigurationManager(listeners, filepath);
			instance.delay = delay;
			instance.doConfigure();
		}
	}
	
	/**
	 * <p>
	 *    Release all resources allocated by this manager.
	 * </p>
	 */
	public static void shutdown() {
		logger.info("Shutdown resources");
		WatchdogService.shutdown();
	}
	
	/**
	 * <p>
	 *    Save the current configuration and send a notify only for changed ones
	 *    and a new ones registered. Of course for the latter case, a listener
	 *    had to be provided when calling <em>configureAndWatch<\em> method.
	 * </p>
	 * 
	 * @throws ConfigurationException
	 */
	public void doConfigure() {
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					ConfigurationInfo newConfigurationInfo = ConfigurationReaderFactory.read(filepath);
					Map<String, Object> confToBeNotified = new HashMap<String, Object>();
					Map<String, Object> curConfMap = currentConfigurationInfo.getConfigurationMap();
					if(newConfigurationInfo != null) {
						for (Entry<String, Object> aNewConfEntry : newConfigurationInfo.getConfigurationMap().entrySet()) {
							String key = aNewConfEntry.getKey();
							Object theConf = aNewConfEntry.getValue();
							/* only for new conf or changed configurations will be send a notify */
							if( ( curConfMap.containsKey(key) && !EqualsBuilder.reflectionEquals(theConf, curConfMap.get(key), false) ) || !curConfMap.containsKey(key) ) {
								confToBeNotified.put(key, theConf);
							}
						}
						
						/* clear the old infos, put in the new ones cloned then release resources of the reader */
						currentConfigurationInfo.clear();
						currentConfigurationInfo.add(newConfigurationInfo);
						newConfigurationInfo.clear();
						/* notify changes to listener */
						notifyListeners(confToBeNotified);
						/* start watch on files */
						WatchdogService.watch(instance, currentConfigurationInfo.getConfFileList(), delay);
					}
				} catch (ConfigurationParsingException e) {
					logger.error(e.getMessage(), e);
					WatchdogService.watch(instance, e.getFileParsedList(), delay);
				}
			}
		};
		
		poolExecutor.execute(runnable);
	}
	
	private void notifyListeners(Map<String, Object> confToBeNotified) {
		logger.debug("Notify listeners about configuration changes");
		if(confToBeNotified != null) {
			for (Entry<String, Object> aConfEntry : confToBeNotified.entrySet()) {
				final String key = aConfEntry.getKey();
				final Object confData = aConfEntry.getValue();
				final IConfigurationChangeListener listener = activeListeners.get(key);
				if( (listener != null) && (confData != null) ) {
					try {
						logger.debug("Notifying listener <" + listener.getClass().getName() + "> for configuratio id <" + key + ">");
						Runnable runnable = new Runnable() {
							public void run() {
								listener.loadConfiguration(cloner.deepClone(confData));
							}
						};
						
						listenerPoolExecutor.execute(runnable);
						
					} catch (Throwable e) {
						// for not terminating the watchdog thread on configuration changes.
						logger.error("Received an uncaught exception", e);
					}
				}
			}
		}
	}
}
