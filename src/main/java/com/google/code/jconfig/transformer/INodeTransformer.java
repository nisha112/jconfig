/*
 * @(#)INodeTransformer.java          Data: 16/set/2011
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

package com.google.code.jconfig.transformer;

import java.io.OutputStream;

import com.google.code.jconfig.exception.NodeTransformationException;
import com.google.code.jconfig.reader.hierarchical.IHierarchicalReader;

/**
 * <p>
 *    An implementation of this interface can transform any
 *    {@link IHierarchicalReader} node to an {@link OutputStream}
 * </p>
 *
 * @author Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public interface INodeTransformer {

	/**
	 * <p>
	 *    Set the property for omitting the xml declaration of the output or not.
	 * </p>
	 * 
	 * @param value the omit xml declaration value
	 */
	public void omitXmlDeclaration(boolean value);
	
	/**
	 * <p>
	 *    Set the property for pretty format of the output or not.
	 * </p>
	 * 
	 * @param value the pretty format value
	 */
	public void prettyFormat(boolean value);
	
	/**
	 * <p>
	 *    Transform a {@link IHierarchicalReader} root to an xml string
	 * </p>
	 * 
	 * @param root the initial node for the transformation 
	 * @return a string representing the <em>root<\em> element and its children
	 *         and attributes as an xml.
	 * @throws NodeTransformationException
	 */
	public String doTransformation(IHierarchicalReader root) throws NodeTransformationException;
}
