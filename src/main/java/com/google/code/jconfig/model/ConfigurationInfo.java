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

public class ConfigurationInfo {

	private Map<String, Object> configurationMap;
	private List<String> confFileList;
	
	public Map<String, Object> getConfigurationMap() {
		if(configurationMap == null) {
			configurationMap = new HashMap<String, Object>();
		}
		return configurationMap;
	}

	public void setConfigurationMap(Map<String, Object> configurationMap) {
		this.configurationMap = configurationMap;
	}
	
	public void addConfigurationDetail(String id, Object configuration) {
		getConfigurationMap().put(id, configuration);
	}

	public List<String> getConfFileList() {
		if(confFileList == null) {
			confFileList = new ArrayList<String>();
		}
		return confFileList;
	}
	
	public void setConfFileList(List<String> confFileList) {
		this.confFileList = confFileList;
	}
	
	public void addConfigurationFilePath(String path) {
		getConfFileList().add(path);
	}
	
	public void add(ConfigurationInfo configurationInfo) {
		getConfigurationMap().putAll(configurationInfo.getConfigurationMap());
		getConfFileList().addAll(configurationInfo.getConfFileList());
	}
	
	public void clear() {
		getConfFileList().clear();
		configurationMap.clear();
	}
}
