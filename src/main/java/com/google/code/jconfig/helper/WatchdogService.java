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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

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
	
	public static final long DEFAULT_DELAY = 60000; // 60 seconds delay
	private static Map<Integer, FileWatchdog> activeWatchdogs = new HashMap<Integer, FileWatchdog>();
	private static long firstDelayUsed = DEFAULT_DELAY;
	
	/**
	 * <p>
	 *    Add the <em>file</em> to the current list of watched resources with 
	 *    the delays used for the very first resource added to the watch list.
	 * </p>
	 * 
	 * @param file the file to be watched
	 */
	public static void addToWatch(String file) {
		addToWatch(file, firstDelayUsed);
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
	public static void addToWatch(String file, long delay) {
		int hash = file.hashCode();
		firstDelayUsed = ( activeWatchdogs.isEmpty()? delay : firstDelayUsed );
		if( !activeWatchdogs.containsKey(hash) ) {
			logger.info("Watching file: " + file);
			FileWatchdog fileWatchdog = new FileWatchdog(file, delay);
			fileWatchdog.start();
			activeWatchdogs.put(hash, fileWatchdog);
		} else {
			logger.warn("File <" + file + "> already present in the watch list.");
		}
	}
	
	/**
	 * <p>
	 *    Stop watching all the current files and clear the list.
	 * </p>
	 */
	public static void reset() {
		logger.info("Resetting watched file list.");
		shutdown();
	}
	
	/**
	 *  <p>
	 *    Stop watching all the current files and clear the list.
	 * </p>
	 */
	public static void shutdown() {
		logger.info("Stop watching files.");
		Iterator<FileWatchdog> itr = activeWatchdogs.values().iterator();
		while(itr.hasNext()) {
			itr.next().interrupt();
		}
		activeWatchdogs.clear();
	}
}
