/*
 * @(#)ConfigurationManagerTest.java          Data: 25/gen/2011
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.google.code.jconfig.ConfigurationException;
import com.google.code.jconfig.ConfigurationManager;
import com.google.code.jconfig.listener.IConfigurationChangeListener;
import com.google.code.jconfig.model.BasicConfiguration;
import com.google.code.jconfig.model.IConfiguration;
import com.google.code.jconfig.model.ServerBean;

import junit.framework.TestCase;

/**
 * <p>
 *   Description goes here
 * <p>
 *
 * @author: Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public class ConfigurationManagerTest extends TestCase {
	
	/**
	 * Test method for {@link com.com.google.code.jconfig.ConfigurationManager#configure(java.util.Map, java.lang.String)}.
	 */
	public void testConfigure() {
		String filepath = getClass().getClassLoader().getResource("configuration.xml").getPath();
		String systemPath = filepath;
		try {
			systemPath = URLDecoder.decode(filepath, "UTF-8").substring(1);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	
		// NOTA: il processo di lettura della configurazione dura 11s perche nel
		//       CacheConfigurationPlugin cerco di creare 3 socket ad indirizzi
		//       che non esistono!!!!
		Map<String, IConfigurationChangeListener> listeners = new HashMap<String, IConfigurationChangeListener>();
		listeners.put("general", new TestConfigurationChangeListener());
		listeners.put("cache", new TestCacheConfigurationChangeListener());
		try {
			ConfigurationManager.configureAndWatch(listeners, systemPath, 200L);
		} catch (ConfigurationException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	private class TestConfigurationChangeListener implements IConfigurationChangeListener {

		public void loadConfiguration(IConfiguration configuration) {
			System.out.println(configuration.getId());
			System.out.println(((BasicConfiguration)configuration).getProperties());
			System.out.println("-----------------------------------------");
		}
	}
	
	private class TestCacheConfigurationChangeListener implements IConfigurationChangeListener {

		public void loadConfiguration(IConfiguration configuration) {
			System.out.println(configuration.getId());
			CacheConfiguration conf = (CacheConfiguration)configuration;
			for (ServerBean server : conf.getServers()) {
				System.out.println(server);
			}
			
			System.out.println("-----------------------------------------");
		}
	}
}
