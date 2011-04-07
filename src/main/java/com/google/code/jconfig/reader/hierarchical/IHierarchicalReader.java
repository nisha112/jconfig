/*
 * @(#)IHierarchicalReader.java          Data: 25/gen/2011
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

package com.google.code.jconfig.reader.hierarchical;

import java.util.Iterator;
import java.util.List;

/**
 * <p>
 *    A hierarchical representation of the configuration red by the parser.
 * </p>
 *
 * @author Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public interface IHierarchicalReader {

	/**
	 * <p>
	 *    Returns the attribute value associated to <em>name</em>. NULL if there
	 *    is no attribute with that name. 
	 * </p>
	 * 
	 * @param name the attribute's name
	 * @return the value associated or NULL if not found
	 */
	public String getAttribute(String name);
	
	/**
	 * <p>
	 *    Returns the number of attribute of this node.
	 * </p>
	 * 
	 * @return the number of attribute of this node
	 */
	public int getAttributeCount();
	
	/**
	 * <p>
	 *    Returns an iterator to the attribute names of this node.
	 * </p>
	 * 
	 * @return an iterator to the attribute names of this node
	 */
	public Iterator<String> getAttributeNames();
	
	/**
	 * <p>
	 *    Returns the node name.
	 * </p>
	 * 
	 * @return the node name
	 */
	public String getNodeName();
	
	/**
	 * <p>
	 *    Returns the value associated to this node.
	 * </p>
	 * 
	 * @return the value associated to this node
	 */
	public String getValue();
	
	/**
	 * <p>
	 *    Returns <em>true</em> if this nodes has children, <em>false</em>
	 *    otherwise.
	 * </p>
	 * 
	 * @return <em>true</em> if this nodes has children, <em>false</em>
	 *         otherwise
	 */
	public boolean hasChildren();
	
	/**
	 * <p>
	 *    Returns the list of this node's children.
	 * </p>
	 * 
	 * @return the list of this node's children
	 */
	public List<IHierarchicalReader> getChildren();
}
