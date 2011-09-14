/*
 * @(#)WatchDog.java          Data: 12/set/2011
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
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.google.code.jconfig.ConfigurationManager;
import com.google.code.jconfig.exception.ConfigurationException;

/**
 * <p>
 *    Watch a collection of file in search of changes. 
 * </p>
 *
 * @author Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public class WatchDog implements Runnable {

	private long delay = 0L;
	private ConfigurationManager singleInstance;
	private Collection<FileInfo> fileToBeWatched;
	private boolean interrupted = false;
	
	private static final Logger logger = Logger.getLogger(WatchDog.class);
	
	/**
	 * <p>
	 *    Constructor
	 * </p>
	 * 
	 * @param singleInstance the singleton instance of {@link ConfigurationManager}
	 * @param fileList a list of the file to be watched
	 * @param delay the delay in ms
	 */
	public WatchDog(ConfigurationManager singleInstance, Collection<String> fileList, long delay) {
		this.delay = delay;
		this.singleInstance = singleInstance;
		fileToBeWatched = new ArrayList<FileInfo>();
		for (String aFilePath : fileList) {
			fileToBeWatched.add(new FileInfo(aFilePath));
		}
		
		checkAndConfigure();
	}
	
	public void reloadFileList(Collection<String> fileList) {
		fileToBeWatched.clear();
		for (String aFilePath : fileList) {
			fileToBeWatched.add(new FileInfo(aFilePath));
		}
		interrupted = false;
	}

	/**
	 * 
	 */
	private void checkAndConfigure() {
		for (FileInfo aFileInfo : fileToBeWatched) {
			File file = aFileInfo.getFile();
			try {
				file.exists();
			} catch(SecurityException e) {
				logger.error("File watched doesn't exist. Stop watching!!!");
				interrupted = true;
				return;
			}

			long l = file.lastModified();
			if(aFileInfo.getLastModify() < l) {
				aFileInfo.setLastModify(l);
				logger.info("Found configuration changes.");
				try {
					interrupted = true;
					singleInstance.doConfigure();
					break;
				} catch (ConfigurationException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}
	
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
