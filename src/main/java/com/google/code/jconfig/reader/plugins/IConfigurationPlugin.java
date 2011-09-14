/*
 * @(#)IConfigurationPlugin.java          Data: 25/gen/2011 
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

import com.google.code.jconfig.reader.hierarchical.IHierarchicalReader;

/**
 * <p>
 *    This interface represents a custom configuration plugin for parsing custom
 *    defined configuration.
 * </p>
 *
 * @author Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public interface IConfigurationPlugin<T> {

	/**
	 * <p>
	 *    Handle a particular user defined configuration fragment and returns
	 *    a user defined configuration object.
	 * </p>
	 * 
	 * @param reader a hierarchical reader for the current configuration handled
	 *               by this plugin.
	 * @return a user defined configuration object
	 */
	public T readConfiguration(IHierarchicalReader reader);
}
