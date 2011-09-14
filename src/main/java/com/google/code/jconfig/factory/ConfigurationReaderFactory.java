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

import com.google.code.jconfig.exception.ConfigurationException;
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

	/**
	 * <p>
	 *    Return an instance of {@link ConfigurationReader} class.
	 * </p>
	 * 
	 * @return an instance of {@link ConfigurationReader} class
	 * @throws ConfigurationException
	 */
	public static IConfigurationReader getReader() throws ConfigurationException {
		return new ConfigurationReader();
	}
}
