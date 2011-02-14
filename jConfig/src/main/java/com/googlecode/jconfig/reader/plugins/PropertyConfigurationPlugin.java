/*
 * @(#)PropertyConfigurationPlugin.java          Data: 25/gen/2011 
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

package com.googlecode.jconfig.reader.plugins;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.jconfig.model.BasicConfiguration;
import com.googlecode.jconfig.model.IConfiguration;
import com.googlecode.jconfig.reader.hierarchical.IHierarchicalReader;


/**
 * <p>
 * </p>
 *
 * @author Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public class PropertyConfigurationPlugin implements IConfigurationPlugin {

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.jconfig.reader.plugins.IConfigurationPlugin#readConfiguration(com.googlecode.jconfig.reader.hierarchical.IHierarchicalReader)
	 */
	public IConfiguration readConfiguration(IHierarchicalReader reader) {
		BasicConfiguration configuration = null;
		if(reader.hasChildren()) {
			Map<String, String> properties = new HashMap<String, String>();
			for (IHierarchicalReader child : reader.getChildren()) {
				properties.put(child.getAttribute("key"), child.getAttribute("value"));
			}
			configuration = new BasicConfiguration(reader.getAttribute("id"), properties);
		}
		
		return configuration;
	}
}
