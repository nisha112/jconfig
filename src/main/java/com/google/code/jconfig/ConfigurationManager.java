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

import org.apache.log4j.Logger;

import com.google.code.jconfig.helper.WatchdogService;
import com.google.code.jconfig.listener.IConfigurationChangeListener;
import com.google.code.jconfig.model.IConfiguration;
import com.google.code.jconfig.reader.ConfigurationReader;

/**
 * <p>
 *    Allows to manage application configurations from an external file.
 * </p>
 *
 * @author Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public class ConfigurationManager {

	private String filepath;
	private ConfigurationReader configurationReader;
	private Map<String, IConfiguration> activeConfigurations;
	private Map<String, IConfigurationChangeListener> activeListeners;
	private static boolean parseInProgress = false;
	
	private static ConfigurationManager instance;
	private static final Logger logger = Logger.getLogger(ConfigurationManager.class);
	
	private ConfigurationManager() throws ConfigurationException { }
	
	private ConfigurationManager(Map<String, IConfigurationChangeListener> listeners, String filepath) throws ConfigurationException {
		logger.info("******* ConfigurationManager initialization *******");
		logger.info(" -> configuration: " + filepath);
		logger.info(" -> registered listeners: " + listeners);
		logger.info("**************************************************");
		
		this.filepath = filepath;
		activeListeners = listeners;
		configurationReader = new ConfigurationReader();
	}
	
	/**
	 * <p>
	 *    Configure the configuration manager to load a configuration file.
	 * </p>
	 * 
	 * @param listeners a map with the all the registered listener for a
	 *                  particular configuration
	 * @param filepath the real path of the configuration file
	 * @throws ConfigurationException
	 */
	public static void configure(Map<String, IConfigurationChangeListener> listeners, String filepath) throws ConfigurationException {
		if(instance == null) {
			instance = new ConfigurationManager(listeners, filepath);
		} else {
			instance.doConfiguration();
		}
	}
	
	/**
	 * <p>
	 *    Configure the configuration manager to load a configuration file and 
	 *    to notify changes to it.
	 * </p>
	 * 
	 * @param listeners a map with the all the registered listener for a
	 *                  particular configuration
	 * @param filepath the real path of the configuration file
	 * @throws ConfigurationException
	 */
	public static void configureAndWatch(Map<String, IConfigurationChangeListener> listeners, String filepath) throws ConfigurationException {
		configureAndWatch(listeners, filepath, WatchdogService.DEFAULT_DELAY);
	}
	
	/**
	 * <p>
	 *    Configure the configuration manager to load a configuration file and 
	 *    to notify changes to it.
	 * </p>
	 * 
	 * @param listeners a map with the all the registered listener for a
	 *                  particular configuration
	 * @param filepath the real path of the configuration file
	 * @param delay the time in millis for checking configuration changes
	 * @throws ConfigurationException
	 */
	public static void configureAndWatch(Map<String, IConfigurationChangeListener> listeners, String filepath, long delay) throws ConfigurationException {
		if(instance == null) {
			instance = new ConfigurationManager(listeners, filepath);
			WatchdogService.addToWatch(filepath, delay);
		} else {
			if( !parseInProgress ) {
				instance.doConfiguration();
			}
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
	
	private void doConfiguration() {
		try {
			parseInProgress = true;
			activeConfigurations = configurationReader.readConfiguration(filepath);
			parseInProgress = false;
			notifyListeners();
		} catch (ConfigurationException e) {
			/* TODO: da gestire il comportamento da tenere.
			 * se è la prima volta e non ho caricato alcuna configurazione
			 * lanciare mega super eccezione, altrimenti loggare l'errore
			 * e mantenere la vecchia conf.
			 */
			logger.error(e.getMessage(), e);
		}
	}
	
	private void notifyListeners() {
		logger.debug("Notify listeners about configuration changes");
		Iterator<Entry<String, IConfigurationChangeListener>> itr = activeListeners.entrySet().iterator();
		while(itr.hasNext()) {
			Entry<String, IConfigurationChangeListener> entry = itr.next();
			logger.debug("Notifying listener <" + entry.getValue().getClass().getName() + "> for configuratio id <" + entry.getKey() + ">");
			try {
				entry.getValue().loadConfiguration(activeConfigurations.get(entry.getKey()));
			} catch (Exception e) {
				// for not terminating the watchdog thread on configuration changes.
				logger.error("Received an uncaught exception", e);
			}
		}
	}
}
