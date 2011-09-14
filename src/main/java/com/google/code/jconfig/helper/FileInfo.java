/*
 * @(#)FileInfo.java          Data: 12/set/2011
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

/**
 * <p>
 *    Rapresent some information about a file to be watched. 
 * </p>
 *
 * @author Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public class FileInfo {

	private File file;
	private long lastModify = 0;
	
	/**
	 * <p>
	 *   Constructor
	 * </p>
	 * 
	 * @param filePath the path of the file that will be watched.
	 */
	public FileInfo(String filePath) {
		file = new File(filePath);
		lastModify = file.lastModified();
	}

	/**
	 * <p>
	 *    Return the file
	 * </p>
	 * 
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * <p>
	 *    Return the last modify date in ms of the file
	 * </p>
	 * 
	 * @return the last modify date in ms of the file
	 */
	public long getLastModify() {
		return lastModify;
	}

	/**
	 * <p>
	 *    Set the new modify date in ms
	 * </p>
	 * 
	 * @param lastModify the new modify date in ms
	 */
	public void setLastModify(long lastModify) {
		this.lastModify = lastModify;
	}
}
