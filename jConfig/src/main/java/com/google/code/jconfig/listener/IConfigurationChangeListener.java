/*
 * @(#)IConfigurationChangeListener.java          Data: 22/gen/2011
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
package com.google.code.jconfig.listener;

import com.google.code.jconfig.ConfigurationManager;
import com.google.code.jconfig.model.IConfiguration;

/**
 * <p>
 *   Implemented by classes interested in loading a custom {@link IConfiguration}
 * <p>
 *
 * @author: Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public interface IConfigurationChangeListener {

	/**
	 * <p>
	 *    Load a configuration passed by a {@link ConfigurationManager}
	 * </p>
	 * 
	 * @param configuration the configuration to be loaded.
	 */
	public void loadConfiguration(IConfiguration configuration);
}
