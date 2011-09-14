/*
 * @(#)Configuration.java          Data: 22/gen/2011
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
package com.google.code.jconfig.model;


import java.util.Map;

/**
 * <p>
 *   Represent a simple configuration with its id and properties.
 * <p>
 * 
 * <p>
 *    It extends the {@link AbstractConfiguration} class for deep cloning the
 *    configuration.
 * </p>
 *
 * @author: Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public class BasicConfiguration /*extends AbstractConfiguration*/ {

	private Map<String, String> properties;
		
	/**
	 * <p>
	 *    Construct a configuration instance with its id and properties.
	 * </p>
	 * 
	 * @param id the configuration identifier.
	 * @param properties the properties of this configuration.
	 */
	public BasicConfiguration(Map<String, String> properties) {
		this.properties = properties;
	}
	
	/**
	 * <p>
	 *    the value associated to <em>key</em> or <em>null</em> if the key
	 *    doesn't exist or there's no property for this configuration.
	 * </p>
	 * 
	 * @param key the property identifier
	 * @return the value associated to <em>key</em> or <em>null</em> if the
	 *          key doesn't exist or there's no property for this configuration.
	 */
	public String getProperty(String key) {
		return ( (properties != null)? properties.get(key) : null );
	}
	
	/**
	 * <p>
	 *    Return a cloned version of the properties of this configuration.
	 * </p>
	 * 
	 * <p>
	 *    Any changes made to the returned map doesn't affect the original
	 *    properties of this configuration.
	 * </p>
	 * 
	 * @return a cloned version of the properties of this configuration.
	 */
	public Map<String, String> getProperties() {
		return properties;
	}
}
