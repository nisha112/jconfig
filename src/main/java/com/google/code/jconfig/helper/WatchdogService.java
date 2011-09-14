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

import org.apache.log4j.Logger;

import com.google.code.jconfig.ConfigurationManager;

/**
 * <p>
 *    This abstract class will be used to take care of chnaged file used in the
 *    configuration. 
 * </p>
 *
 * @author Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public abstract class WatchdogService {

	private static final Logger logger = Logger.getLogger(WatchdogService.class);
	private static WatchDog watchDog;
	public static final long DEFAULT_DELAY = 60000; // 60 seconds delay
	
	/**
	 * <p>
	 *    Add the <em>file</em> to the current list of watched resources with 
	 *    the delays used for the very first resource added to the watch list.
	 * </p>
	 * 
	 * @param file the file to be watched
	 */
	public static void watch(ConfigurationManager singleInstance, Collection<String> filePathList) {
		watch(singleInstance, filePathList, DEFAULT_DELAY);
	}
	
	/**
	 * <p>
	 *    Add the <em>file</em> to the current list of watched resources with 
	 *    the expressed delays in ms.
	 * </p>
	 * 
	 * @param file the file to be watched
	 * @param delay teh delay in ms between two watch. 
	 */
	public static void watch(ConfigurationManager singleInstance, Collection<String> filePathList, long delay) {
		watchDog = new WatchDog(singleInstance, filePathList, delay);
		watchDog.start();
	}
	
	/**
	 *  <p>
	 *    Stop watching all the current files and clear the list.
	 * </p>
	 */
	public static void shutdown() {
		logger.info("Shutting down watchdog resources.");
		watchDog.interrupt();
	}
}
