/*
 * @(#)ConfigurationReader.java          Data: 22/gen/2011
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

package com.googlecode.jconfig.reader;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.*;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.googlecode.jconfig.ConfigurationException;
import com.googlecode.jconfig.model.IConfiguration;
import com.googlecode.jconfig.reader.hierarchical.HierarchicalReader;
import com.googlecode.jconfig.reader.hierarchical.IHierarchicalReader;
import com.googlecode.jconfig.reader.plugins.IConfigurationPlugin;

/**
 * <p>
 *   Default implementation of {@link IConfigurationReader} using a SAX parser.
 * <p>
 *
 * @author: Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public class ConfigurationReader extends DefaultHandler implements IConfigurationReader {

	private SAXParser parser;
	private ConfigurationReaderHandler readerHandler;
	private Map<String, IConfiguration> configurations;
	
	private static final Logger logger = Logger.getLogger(ConfigurationReader.class);
	
	private enum ELEMENT_TAGS {
		CONFIGURATIONS,
			CONFIGURATION,
	}
	
	private enum ATTRIBUTES {
		plugin
	}
	
	/**
	 * 
	 * @throws ConfigurationException
	 */
	public ConfigurationReader() throws ConfigurationException {
		logger.debug("Initializing configuration reader");
		try {
			parser = SAXParserFactory.newInstance().newSAXParser();
			readerHandler = new ConfigurationReaderHandler();
		} catch (ParserConfigurationException e) {
			throw new ConfigurationException(e.getMessage(), e);
		} catch (SAXException e) {
			throw new ConfigurationException(e.getMessage(), e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.googlecode.jconfig.reader.IConfigurationReader#readConfiguration(java.io.InputStream)
	 */
	public Map<String, IConfiguration> readConfiguration(InputStream inputStream) throws ConfigurationException {
		try {
			logger.debug("Reading configuration");
			parser.parse(inputStream, readerHandler);
		} catch (SAXException e) {
			logger.error(e.getMessage(), e);
			configurations.clear();
			throw new ConfigurationException(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			configurations.clear();
			throw new ConfigurationException(e.getMessage(), e);
		}
		
		return configurations;
	}
	
	/**
	 * <p>
	 *   The internal handler for SAX parser.
	 * </p>
	 */
	private class ConfigurationReaderHandler extends DefaultHandler {
		
		private IConfigurationPlugin currentPlugin;
		private Stack<IHierarchicalReader> configurationPluginStack;
		
		@Override
		public void startDocument() throws SAXException {
			configurations = new HashMap<String, IConfiguration>();
			configurationPluginStack = new Stack<IHierarchicalReader>();
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			String tagName = qName.toUpperCase();
			
			if(tagName.equals(ELEMENT_TAGS.CONFIGURATIONS.name())) {
				// DO NOTHING
			} else if(tagName.equals(ELEMENT_TAGS.CONFIGURATION.name())) {
				String pluginClass = attributes.getValue(ATTRIBUTES.plugin.name());
				try {
					currentPlugin = newPlugin(pluginClass);
				} catch (ConfigurationException e) {
					logger.error(e.getMessage(), e);
				}
				configurationPluginStack.push(createHierarchicalReader(qName, attributes));
			} else { 
				configurationPluginStack.push(createHierarchicalReader(qName, attributes));
			}
		}
		
		@Override
		public void characters(char[] characters, int start, int end) throws SAXException {
			if( !configurationPluginStack.isEmpty() ) {
				String value = new String(characters, start, end);
				((HierarchicalReader)configurationPluginStack.peek()).setValue(value);
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			String tagName = qName.toUpperCase();
			
			if(tagName.equals(ELEMENT_TAGS.CONFIGURATIONS.name())) {
				// DO NOTHING
			} else if(tagName.equals(ELEMENT_TAGS.CONFIGURATION.name())) {
				IHierarchicalReader rootConfiguration = configurationPluginStack.pop();
				IConfiguration configuration = currentPlugin.readConfiguration(rootConfiguration);
				configurations.put(configuration.getId(), configuration);
			} else {
				IHierarchicalReader child = configurationPluginStack.pop();
				((HierarchicalReader)configurationPluginStack.peek()).addChild(child);
			}
		}
		
		private IHierarchicalReader createHierarchicalReader(String nodeName, Attributes attributes) {
			HierarchicalReader hierarchicalReader = new HierarchicalReader();
			hierarchicalReader.setNodeName(nodeName);
			for(int i = 0; i < attributes.getLength(); i++) {
				hierarchicalReader.addAttribute(attributes.getQName(i), attributes.getValue(i));
			}
			
			return hierarchicalReader;
		}
		
		private IConfigurationPlugin newPlugin(String className) throws ConfigurationException {
			try {
				return (IConfigurationPlugin)Class.forName(className).newInstance();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ConfigurationException(e.getMessage(), e);
			}
		}
	}
}
