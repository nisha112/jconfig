/*
 * @(#)WatchdogService.java          Data: 4/apr/2011
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

package com.google.code.jconfig.helper;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.google.code.jconfig.ConfigurationManager;

/**
 * <p>
 *    This abstract class will be used to take care of changed file used in the
 *    configuration. 
 * </p>
 *
 * @author Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public abstract class WatchdogService {

	private static final Logger logger = Logger.getLogger(WatchdogService.class);
	private static WatchDog watchDog;
	public static final long DEFAULT_DELAY = 60000; // 60 seconds delay
	private static ExecutorService executorService = Executors.newSingleThreadExecutor(new DaemonThreadFactory());
	
	/**
	 * <p>
	 *    Watch changes to a list of file used in the configuration.
	 * </p>
	 * 
	 * @param singleInstance the singleton instance of the {@link ConfigurationManager}
	 * @param filePathList the collection of configuration file paths to be
	 *                     watched for changes.
	 */
	public static void watch(ConfigurationManager singleInstance, Collection<String> filePathList) {
		watch(singleInstance, filePathList, DEFAULT_DELAY);
	}
	
	/**
	 * <p>
	 *    Watch changes to a list of file used in the configuration.
	 * </p>
	 * 
	 * @param singleInstance the singleton instance of the {@link ConfigurationManager}
	 * @param filePathList the collection of configuration file paths to be
	 *                     watched for changes.
	 * @param delay the delay in ms
	 */
	public static void watch(ConfigurationManager singleInstance, Collection<String> filePathList, long delay) {
		if(watchDog == null) {
			watchDog = new WatchDog(singleInstance, filePathList, delay);
			executorService.execute(watchDog);
		} else {
			watchDog.reloadFileList(filePathList);
		}
	}
	
	/**
	 *  <p>
	 *    Stop watching configuration files.
	 * </p>
	 */
	public static void shutdown() {
		logger.info("Shutting down watchdog resources.");
		executorService.shutdown();
	}
}
