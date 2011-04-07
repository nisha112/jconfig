/*
 * @(#)CacheConfiguration.java          Data: 25/feb/2011
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

package com.google.code.jconfig;

import java.util.List;

import com.google.code.jconfig.model.AbstractConfiguration;
import com.google.code.jconfig.model.ServerBean;

public class CacheConfiguration extends AbstractConfiguration {

	private List<ServerBean> servers;
	
	public CacheConfiguration(String id, List<ServerBean> servers) {
		this.id = id;
		this.servers = servers;
	}

	public List<ServerBean> getServers() {
		return deepClone(servers);
	}
}
