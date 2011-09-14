/*
 * @(#)CacheConfigurationPlugin.java          Data: 25/gen/2011
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

package com.google.code.jconfig.reader.plugins;

import java.util.ArrayList;
import java.util.List;

import com.google.code.jconfig.model.ServerBean;
import com.google.code.jconfig.reader.hierarchical.IHierarchicalReader;
import com.google.code.jconfig.reader.plugins.IConfigurationPlugin;


public class CacheConfigurationPlugin implements IConfigurationPlugin<List<ServerBean>> {

	/*
	 * (non-Javadoc)
	 * @see com.google.code.jconfig.reader.plugins.IConfigurationPlugin#readConfiguration(com.google.code.jconfig.reader.hierarchical.IHierarchicalReader)
	 */
	public List<ServerBean> readConfiguration(IHierarchicalReader reader) {
		
		/**
		 * Il reader inizia dall'elemento <configuration> a scendere e contiene
		 * solamente il frammento di xml necessario per questa configurazione .
		 * 
		 * 	<configuration id="cache" plugin="com.google.code.jconfig.reader.plugins.CacheConfigurationPlugin">
		 *		<servers>
		 *			<server name="server 1" port="1000"/>
		 *			<server name="server 2" port="1010"/>
		 *			<server name="server 3" port="1020"/>
		 *		</servers>
		 *	</configuration>
		 * 
		 */
		
		IHierarchicalReader serversNode = reader.getChildren().get(0);
		List<ServerBean> servers = new ArrayList<ServerBean>();
		for (IHierarchicalReader child : serversNode.getChildren()) {
			servers.add(new ServerBean(child.getAttribute("name"), Integer.parseInt(child.getAttribute("port"))));
		}
		
		return servers;
	}

}
