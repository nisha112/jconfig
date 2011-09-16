/*
 * @(#)NodeTransformer.java          Data: 16/set/2011
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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.code.jconfig.exception.NodeTransformationException;
import com.google.code.jconfig.reader.hierarchical.IHierarchicalReader;

/**
 * <p>
 *    An implementation of {@link INodeTransformer} that uses DOM and standard
 *    java {@link Transformer} class to transform the data.
 * </p>
 *
 * @author Gabriele Fedeli (gabriele.fedeli@gmail.com)
 */
public class NodeTransformer implements INodeTransformer {

	private boolean omitXmlDeclaration = false;
	private boolean prettyFormat = false;
	private final static String INDENT_AMOUNT = "3";
	private final static String YES = "yes";
	private final static String NO = "no";
	
	private NodeTransformer() {}
	
	/**
	 * <p>
	 *    Return an instance of this class ready to use.
	 * </p>
	 * 
	 * @return a new instance of {@link NodeTransformer}
	 */
	public static NodeTransformer newInstance() {
		return new NodeTransformer();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.google.code.jconfig.transformer.INodeTransformer#omitXmlDeclaration(boolean)
	 */
	public void omitXmlDeclaration(boolean value) {
		this.omitXmlDeclaration = value;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.google.code.jconfig.transformer.INodeTransformer#prettyFormat(boolean)
	 */
	public void prettyFormat(boolean value) {
		this.prettyFormat = value;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.google.code.jconfig.transformer.INodeTransformer#doTransformation(com.google.code.jconfig.reader.hierarchical.IHierarchicalReader)
	 */
	public OutputStream doTransformation(IHierarchicalReader root) throws NodeTransformationException {
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, (omitXmlDeclaration? YES : NO) );
			transformer.setOutputProperty(OutputKeys.INDENT, (prettyFormat? YES : NO) );
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", INDENT_AMOUNT);
		
			Document domDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			domDocument.appendChild(traverseHierarchy(root, domDocument));
			
			StreamResult streamResult = new StreamResult(new ByteArrayOutputStream());
			transformer.transform(new DOMSource(domDocument), streamResult);
			return streamResult.getOutputStream();
		} catch (Throwable e) {
			throw new NodeTransformationException(e.getMessage(), e);
		}
	}
	
	private Element traverseHierarchy(IHierarchicalReader currentRoot, Document domDocument) {
		Element domElement = domDocument.createElement(currentRoot.getNodeName());
		// set the text value if present
		if(currentRoot.getValue() != null) {
			domElement.setTextContent(currentRoot.getValue());
		}
		
		// iterate over its attributes if any
		Iterator<String> attributeItr = currentRoot.getAttributeNames();
		while(attributeItr.hasNext()) {
			String attrName = attributeItr.next();
			domElement.setAttribute(attrName, currentRoot.getAttributeValue(attrName));
		}
		
		// iterate over its children if any
		for (IHierarchicalReader child : currentRoot.getChildren()) {
			Element childElem = traverseHierarchy(child, domDocument);
			domElement.appendChild(childElem);
		}
		
		return domElement;
	}
}
