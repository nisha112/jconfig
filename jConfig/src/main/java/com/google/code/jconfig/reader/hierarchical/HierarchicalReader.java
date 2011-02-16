/*
 * @(#)HierarchicalReader.java          Data: 25/gen/2011
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *    The default implementation of a hierarchical configuration node.
 * </p>
 *
 * @author Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public class HierarchicalReader implements IHierarchicalReader {

	private String nodeName;
	private String value;
	private Map<String, String> attributes;
	private List<IHierarchicalReader> children;
	
	/*
	 * (non-Javadoc)
	 * @see com.google.code.jconfig.reader.hierarchical.IHierarchicalReader#getAttribute(java.lang.String)
	 */
	public String getAttribute(String name) {
		return attributes.get(name);
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.code.jconfig.reader.hierarchical.IHierarchicalReader#getAttributeCount()
	 */
	public int getAttributeCount() {
		return attributes.size();
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.code.jconfig.reader.hierarchical.IHierarchicalReader#getAttributeNames()
	 */
	public Iterator<String> getAttributeNames() {
		return attributes.keySet().iterator();
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.code.jconfig.reader.hierarchical.IHierarchicalReader#getNodeName()
	 */
	public String getNodeName() {
		return nodeName;
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.code.jconfig.reader.hierarchical.IHierarchicalReader#getValue()
	 */
	public String getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.code.jconfig.reader.hierarchical.IHierarchicalReader#hasChildren()
	 */
	public boolean hasChildren() {
		return ( (children != null) && !children.isEmpty() );
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.code.jconfig.reader.hierarchical.IHierarchicalReader#getChildren()
	 */
	public List<IHierarchicalReader> getChildren() {
		return children;
	}
	
	/**
	 * 
	 * @param nodeName
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addAttribute(String key, String value) {
		if(attributes == null) {
			attributes = new HashMap<String, String>();
		}
		attributes.put(key, value);
	}
	
	/**
	 * 
	 * @param child
	 */
	public void addChild(IHierarchicalReader child) {
		if(children == null) {
			children = new ArrayList<IHierarchicalReader>();
		}
		children.add(child);
	}
}
