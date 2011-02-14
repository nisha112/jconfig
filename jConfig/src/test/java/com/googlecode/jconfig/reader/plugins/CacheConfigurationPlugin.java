package com.googlecode.jconfig.reader.plugins;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.jconfig.CacheConfiguration;
import com.googlecode.jconfig.model.IConfiguration;
import com.googlecode.jconfig.reader.hierarchical.IHierarchicalReader;
import com.googlecode.jconfig.reader.plugins.IConfigurationPlugin;


public class CacheConfigurationPlugin implements IConfigurationPlugin {

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.jconfig.reader.plugins.IConfigurationPlugin#readConfiguration(com.googlecode.jconfig.reader.hierarchical.IHierarchicalReader)
	 */
	public IConfiguration readConfiguration(IHierarchicalReader reader) {
		
		/**
		 * Il reader inizia dall'elemento <configuration> a scendere e contiene
		 * solamente il frammento di xml necessario per questa configurazione .
		 * 
		 * 	<configuration id="cache" plugin="com.googlecode.jconfig.reader.plugins.CacheConfigurationPlugin">
		 *		<servers>
		 *			<server name="server 1" port="1000"/>
		 *			<server name="server 2" port="1010"/>
		 *			<server name="server 3" port="1020"/>
		 *		</servers>
		 *	</configuration>
		 * 
		 */
		
		IHierarchicalReader serversNode = reader.getChildren().get(0);
		List<InetSocketAddress> servers = new ArrayList<InetSocketAddress>();
		for (IHierarchicalReader child : serversNode.getChildren()) {
			servers.add(new InetSocketAddress(child.getAttribute("name"), Integer.parseInt(child.getAttribute("port"))));
		}
		
		return new CacheConfiguration(reader.getAttribute("id"), servers);
	}

}
