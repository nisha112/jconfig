package com.google.code.jconfig;


import java.net.InetSocketAddress;
import java.util.List;

import com.google.code.jconfig.model.AbstractConfiguration;

public class CacheConfiguration extends AbstractConfiguration {

	private List<InetSocketAddress> servers;
	
	public CacheConfiguration(String id, List<InetSocketAddress> servers) {
		this.id = id;
		this.servers = servers;
	}

	public List<InetSocketAddress> getServers() {
		return cloner.deepClone(servers);
	}
}
