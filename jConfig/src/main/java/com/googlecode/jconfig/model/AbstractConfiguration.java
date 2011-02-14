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

package com.googlecode.jconfig.model;

import com.rits.cloning.Cloner;

/**
 * <p>
 *    Allows to manage application configurations from an external file.
 * </p>
 *
 * @author Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public abstract class AbstractConfiguration implements IConfiguration {

	protected String id;
	protected static final Cloner cloner = new Cloner();
	
	/*
	 * (non-Javadoc)
	 * @see com.googlecode.jconfig.model.IConfiguration#getId()
	 */
	public String getId() {
		return id;
	}
}
