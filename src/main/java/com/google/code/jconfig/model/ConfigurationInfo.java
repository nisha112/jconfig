/*
 * @(#)ConfigurationReader.java          Data: 12/set/2011
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.jconfig.reader.IConfigurationReader;

/**
 * <p>
 *    Contains information about the configuration parsed by an implementation
 *    of {@link IConfigurationReader}
 * </p>
 *
 * @author Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public class ConfigurationInfo {

	private Map<String, Object> configurationMap;
	private List<String> confFileList;
	
	/**
	 * <p>
	 *    Returns the configuration map.
	 * </p>
	 * 
	 * @return the configuration map
	 */
	public Map<String, Object> getConfigurationMap() {
		if(configurationMap == null) {
			configurationMap = new HashMap<String, Object>();
		}
		return configurationMap;
	}

	/**
	 * <p>
	 *    Set the configuration map.
	 * </p>
	 * 
	 * @param configurationMap the configuration map
	 */
	public void setConfigurationMap(Map<String, Object> configurationMap) {
		this.configurationMap = configurationMap;
	}
	
	/**
	 * <p>
	 *    Add a configuration to the configuration map.
	 *    An <em>id</em> will be associated to the <em>configuration<\em>.
	 *    If the configuration map alredy contains a configuration with the same
	 *    id, then the existing configuration will be overwritten.  
	 * </p>
	 * 
	 * @param id an identification
	 * @param configuration the configuration to be added
	 */
	public void addConfigurationDetail(String id, Object configuration) {
		getConfigurationMap().put(id, configuration);
	}

	/**
	 * <p>
	 *    Return a list of all the configuration files parsed.
	 * </p>
	 * 
	 * @return a list of all the configuration files
	 */
	public List<String> getConfFileList() {
		if(confFileList == null) {
			confFileList = new ArrayList<String>();
		}
		return confFileList;
	}
	
	/**
	 * <p>
	 *    Set the list of all the configuration files parsed.
	 * </p>
	 * 
	 * @param confFileList the list of all the configuration files
	 */
	public void setConfFileList(List<String> confFileList) {
		this.confFileList = confFileList;
	}
	
	/**
	 * <p>
	 *    Add a new absolute file path to the list of the configuration files.
	 * </p>
	 * 
	 * @param path the absolute path of the configuration file to be added
	 */
	public void addConfigurationFilePath(String path) {
		getConfFileList().add(path);
	}
	
	/**
	 * <p>
	 *    Add an entire <em>configurationInfo<\em> to this.
	 * </p>
	 * 
	 * @param configurationInfo the configuration info to be added
	 */
	public void add(ConfigurationInfo configurationInfo) {
		getConfigurationMap().putAll(configurationInfo.getConfigurationMap());
		getConfFileList().addAll(configurationInfo.getConfFileList());
	}
	
	/**
	 * <p>
	 *    Release all the resources.
	 * </p>
	 */
	public void clear() {
		if(confFileList != null) {
			confFileList.clear();
		}
		
		if(configurationMap != null) {
			configurationMap.clear();
		}
	}
}
