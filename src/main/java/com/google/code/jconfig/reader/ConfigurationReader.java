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

package com.google.code.jconfig.reader;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.*;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.code.jconfig.ConfigurationException;
import com.google.code.jconfig.helper.WatchdogService;
import com.google.code.jconfig.model.IConfiguration;
import com.google.code.jconfig.reader.hierarchical.HierarchicalReader;
import com.google.code.jconfig.reader.hierarchical.IHierarchicalReader;
import com.google.code.jconfig.reader.plugins.IConfigurationPlugin;

/**
 * <p>
 *   Default implementation of {@link IConfigurationReader} using a SAX parser.
 * <p>
 *
 * @author: Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public class ConfigurationReader extends DefaultHandler implements IConfigurationReader {

	private StringBuilder currentConfigPath;
	private SAXParser parser;
	private ConfigurationReaderHandler readerHandler;
	private Map<String, IConfiguration> configurations;
	
	private static final Logger logger = Logger.getLogger(ConfigurationReader.class);
	
	private enum ELEMENT_TAGS {
		CONFIGURATIONS,
			IMPORT,
			CONFIGURATION,
	}
	
	private enum ATTRIBUTES {
		plugin, file
	}
	
	/**
	 * 
	 * @throws ConfigurationException
	 */
	public ConfigurationReader() throws ConfigurationException {
		logger.debug("Initializing configuration reader");
		try {
			currentConfigPath = new StringBuilder();
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
	 * @see com.google.code.jconfig.reader.IConfigurationReader#readConfiguration(java.lang.String)
	 */
	public Map<String, IConfiguration> readConfiguration(String absolutePath) throws ConfigurationException {
		try {
			logger.debug("Reading configuration: " + absolutePath);
			File configurationFile = new File(absolutePath);
			currentConfigPath.append(configurationFile.getParent())
			                 .append(File.separator);
			parser.parse(configurationFile, readerHandler);
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
				logger.debug("Found <configurations> tag start.");
			} else if(tagName.equals(ELEMENT_TAGS.IMPORT.name())) {
				logger.debug("Found <import> tag start.");
				String importedConfiguration = attributes.getValue(ATTRIBUTES.file.name());
				String absolutePath = currentConfigPath.append(importedConfiguration).toString();
				/* add the imported resource to the list of the watched files. */
				WatchdogService.addToWatch(absolutePath);
				try {
					ConfigurationReader innerReader = new ConfigurationReader();
					configurations.putAll(innerReader.readConfiguration(absolutePath));
				} catch(ConfigurationException e) {
					logger.error(e.getMessage(), e);
					// TODO: if imported conf is broken what to do??
					//   go ahead or delete the current conf process ??? 
				}
				
			} else if(tagName.equals(ELEMENT_TAGS.CONFIGURATION.name())) {
				logger.debug("Found <configuration> tag start.");
				String pluginClass = attributes.getValue(ATTRIBUTES.plugin.name());
				try {
					currentPlugin = newPlugin(pluginClass);
				} catch (ConfigurationException e) {
					logger.error(e.getMessage(), e);
				}
				configurationPluginStack.push(createHierarchicalReader(qName, attributes));
			} else { 
				logger.debug("Found <" + qName + "> tag start.");
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
				logger.debug("Found <configurations> tag end.");
			} else if(tagName.equals(ELEMENT_TAGS.CONFIGURATION.name())) {
				logger.debug("Found <configuration> tag end.");
				IHierarchicalReader rootConfiguration = configurationPluginStack.pop();
				IConfiguration configuration = currentPlugin.readConfiguration(rootConfiguration);
				configurations.put(configuration.getId(), configuration);
			} else if(tagName.equals(ELEMENT_TAGS.IMPORT.name())) {
				// DO NOTHING
				logger.debug("Found <import> tag end.");
			} else {
				logger.debug("Found <" + qName + "> tag end.");
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
