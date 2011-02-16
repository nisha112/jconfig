/*
 * @(#)IConfiguration.java          Data: 25/gen/2011
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

/**
 * <p>
 *    All configuration must implements this interface.
 * </p>
 * 
 * <p>
 *    The implementation MUST NOT permit to change the configuration poroperties
 *    Extending the {@link AbstractConfiguration} class will provide a deep
 *    cloning features.
 * </p>
 *
 * @author Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public interface IConfiguration {

	/**
	 * <p>
	 *    Returns the identifier of this configuration.
	 * </p>
	 * 
	 * @return the identifier of this configuration
	 */
	public String getId();
}
