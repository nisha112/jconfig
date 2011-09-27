/*
 * @(#)Cacheable.java          Data: 27/set/2011 
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

package com.google.code.jconfig.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.code.jconfig.reader.plugins.IConfigurationPlugin;

/**
 * <p>
 *    Marking an implementation of {@link IConfigurationPlugin} with this
 *    annotation will made cacheable the instance. 
 * </p>
 * 
 * <p>
 *    It's only a marker annotation so no other needs but to annotate your class
 * </p>
 *
 * @author Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {

}
