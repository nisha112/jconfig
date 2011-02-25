/*
 * @(#)FileWatchdog.java          Data: 23/gen/2011
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


import java.io.File;

import org.apache.log4j.Logger;

import com.google.code.jconfig.ConfigurationException;
import com.google.code.jconfig.ConfigurationManager;

/**
 * <p>
 *   This class tracks the changes happened to the specified config file. 
 * </p>
 *
 * @author Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public class FileWatchdog extends Thread {

	public static final long DEFAULT_DELAY = 60000; // 60 seconds delay
	private long delay = DEFAULT_DELAY;
	
	private File file;
	private long lastModify = 0L;	
	private boolean interrupted = false;
	
	private static final Logger logger = Logger.getLogger(FileWatchdog.class);
	
	/**
	 * <p>
	 *    Constructor with the file absolute path to watch.
	 * </p>
	 * 
	 * @param filename the file absolute path to watch
	 */
	public FileWatchdog(String filename) {
		logger.info("Start watching file changes on: " + filename);
		file = new File(filename);
		setDaemon(true);
		checkAndConfigure();
	}
	
	/**
	 * <p>
	 *    Returns the delay in ms.
	 * </p>
	 * 
	 * @return the delay in ms
	 */
	public long getDelay() {
		return delay;
	}

	/**
	 * <p>
	 *    Set the delay in ms.
	 * </p>
	 * 
	 * @param delay the delay in ms
	 */
	public void setDelay(long delay) {
		this.delay = delay;
	}

	/**
	 * 
	 */
	private void checkAndConfigure() {
		try {
			file.exists();
		} catch(SecurityException e) {
			logger.error("File watched doesn't exist. Stop watching!!!");
			interrupted = true;
			return;
		}
		
		long l = file.lastModified();
		if(lastModify < l) {
			lastModify = l;
			try {
				logger.info("Found configuration changes.");
				// passing null because instance is already configured
				// used only for reloading configuration
				ConfigurationManager.configureAndWatch(null, null);
			} catch (ConfigurationException e) {
				// no interruption expected
			}
		}
	}
	
	@Override
	public void run() {
		while(!interrupted) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				// no interruption expected
			}
			checkAndConfigure();
		}
	}
	
}
