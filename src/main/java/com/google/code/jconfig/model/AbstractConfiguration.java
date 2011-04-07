/*
 * @(#)AbstractConfiguration.java          Data: 25/gen/2011
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

import com.rits.cloning.Cloner;

/**
 * <p>
 *    This abstract base class expose an utility method for deep cloning
 *    configuration instances to be returned in order to avoid external changes
 *    to the configuration itself.
 * </p>
 * 
 * <p>
 *    All the concrete instance of {@link IConfiguration} should extends this
 *    class and should also use the <em>deepClone(..)</em> method for returning
 *    a cloned instance of their configuration.
 * </p>
 *
 * @author Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public abstract class AbstractConfiguration implements IConfiguration {

	protected String id;
	private static final Cloner cloner = new Cloner();
	
	/**
	 * <p>
	 *    Utility method for deep cloning instance of <em>configuration</em>
	 *    in order to prevent possible changes to the configuration itself.
	 * </p>
	 * 
	 * @param <T> cloned instance to be returned.
	 * @param configuration the instance to be cloned.
	 * @return a deep cloned instance of <em>configuration</em>
	 */
	protected <T> T deepClone(T configuration) {
		return cloner.deepClone(configuration);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.google.code.jconfig.model.IConfiguration#getId()
	 */
	public String getId() {
		return id;
	}
}
