/*
 * @(#)ConfigurationReaderFactory.java          Data: 12/set/2011
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

package com.google.code.jconfig.factory;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import com.google.code.jconfig.exception.ConfigurationParsingException;
import com.google.code.jconfig.model.ConfigurationInfo;
import com.google.code.jconfig.reader.ConfigurationReader;
import com.google.code.jconfig.reader.IConfigurationReader;

/**
 * <p>
 *    Factory for building a configuration reader
 * </p>
 *
 * @author Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public abstract class ConfigurationReaderFactory {

	private static ConcurrentLinkedQueue<IConfigurationReader> availableReader;
	private static Logger logger = Logger.getLogger(ConfigurationReaderFactory.class);
	
	static {
		availableReader = new ConcurrentLinkedQueue<IConfigurationReader>();
		
		try {
			availableReader.add(new ConfigurationReader());
			availableReader.add(new ConfigurationReader());
		} catch (ConfigurationParsingException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * <p>
	 *    Return an instance of {@link ConfigurationReader} class.
	 * </p>
	 * 
	 * @return an instance of {@link ConfigurationReader} class
	 * @throws ConfigurationParsingException
	 */
	public static IConfigurationReader getReader() throws ConfigurationParsingException {
		return new ConfigurationReader();
	}
	
	/**
	 * <p>
	 *    This method reads a configuration file using a cached instance
	 *    reader of {@link IConfigurationReader}. If no cached reader are found
	 *    then a new one instance of {@link IConfigurationReader} is created and
	 *    at the end of its work it will be put in the cache.
	 * </p>
	 * 
	 * @param resourcePath the absoluter path of the configuration to be red
	 * @return an instance of {@link ConfigurationInfo}
	 * @throws ConfigurationParsingException
	 */
	public static ConfigurationInfo read(String resourcePath) throws ConfigurationParsingException {
		IConfigurationReader reader = null;
		if(availableReader.isEmpty()) {
			logger.debug("No prepared reader found. Creating a new ones.");
			reader = new ConfigurationReader();
		} else {
			logger.debug("Getting a reader from the cache. Now available cached reader: " + availableReader.size());
			reader = availableReader.remove();
		}
		
		try {
			return reader.readConfiguration(resourcePath);
		} finally {
			if(reader != null) {
				availableReader.add(reader);
				logger.debug("Current reader cache size: " + availableReader.size());
			}
		}
	}
}
