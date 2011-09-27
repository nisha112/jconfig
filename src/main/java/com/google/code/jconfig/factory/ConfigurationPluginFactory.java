/*
 * @(#)ConfigurationPluginFactory.java          Data: 27/set/2011 
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

package com.google.code.jconfig.factory;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;

import org.apache.commons.lang3.ClassUtils;
import org.apache.log4j.Logger;

import com.google.code.jconfig.annotation.Cacheable;
import com.google.code.jconfig.exception.PluginInstantiationException;
import com.google.code.jconfig.reader.plugins.IConfigurationPlugin;

/**
 * <p>
 *    Factory for the custom plugin creation.
 * </p>
 *
 * @author Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public abstract class ConfigurationPluginFactory {

	private static ConcurrentHashMap<String, FutureTask<IConfigurationPlugin<?>>> cache = new ConcurrentHashMap<String, FutureTask<IConfigurationPlugin<?>>>();
	private static Logger logger = Logger.getLogger(ConfigurationPluginFactory.class);
	
	/**
	 * <p>
	 *    Returns a plugin instance of type <em>classname</em>.
	 * </p>
	 * 
	 * <p>
	 *    If the implementation class use the {@link Cacheable} annotation, it
	 *    will be cached for future reuse otherwise a new instance will be
	 *    created at every request.
	 * </p>
	 * 
	 * @param classname the full name of an instance of
	 *                  {@link IConfigurationPlugin}.
	 * @return the plugin instance.
	 * @throws PluginInstantiationException
	 */
	public static IConfigurationPlugin<?> getPlugin(final String classname) throws PluginInstantiationException {
		
		try {
			if( ClassUtils.getClass(classname).getAnnotation(Cacheable.class) == null ) {
				logger.debug("Plugin <" + classname + "> is not cacheable. Creating a new one.");
				return (IConfigurationPlugin<?>)Class.forName(classname).newInstance();
			}
		} catch (Exception e) {
			throw new PluginInstantiationException(e.getMessage(), e);
		}
		
		// cacheable plugin
		FutureTask<IConfigurationPlugin<?>> theTask = cache.get(classname);
		if(theTask == null) {
			logger.debug("No plugin of class <" + classname + "> available. Creatine a new one.");
			Callable<IConfigurationPlugin<?>> eval = new Callable<IConfigurationPlugin<?>>() {

				public IConfigurationPlugin<?> call() throws Exception {
					IConfigurationPlugin<?> plugin = (IConfigurationPlugin<?>)Class.forName(classname).newInstance();
					return plugin; 
				}
			};
			
			FutureTask<IConfigurationPlugin<?>> thePluginExec = new FutureTask<IConfigurationPlugin<?>>(eval);
			theTask = cache.putIfAbsent(classname, thePluginExec);
			if(theTask == null) {
				theTask = thePluginExec;
				theTask.run();
			}
			logger.debug("New plugin of class <" + classname + "> ready to be used and cached.");
		} else {
			logger.debug("Plugin of class <" + classname + "> available in cache. Using the cached one.");
		}
		
		try {
			return theTask.get();
		} catch (Exception e) {
			throw new PluginInstantiationException(e.getMessage(), e);
		} finally {
			logger.debug("plugin cache size: " + cache.size() + " - cache detail: " + cache);
		}
	}
}
