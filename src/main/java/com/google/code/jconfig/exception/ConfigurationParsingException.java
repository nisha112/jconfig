package com.google.code.jconfig.exception;

import java.util.ArrayList;
import java.util.Collection;

import org.xml.sax.SAXException;

public class ConfigurationParsingException extends SAXException {

	private static final long serialVersionUID = 8192254081093079623L;
	private Collection<String> fileParsedList;

	/**
	 * <p>
	 *    The default constructor.
	 * </p>
	 */
	public ConfigurationParsingException() {
		super();
	}
	
	public ConfigurationParsingException(Collection<String> fileParsedList) {
		super();
		this.fileParsedList = fileParsedList;
	}

	/**
	 * @param message
	 */
	public ConfigurationParsingException(String message) {
		super(message);
	}
	
	public ConfigurationParsingException(String message, Collection<String> fileParsedList) {
		super(message);
		this.fileParsedList = fileParsedList;
	}

	public Collection<String> getFileParsedList() {
		if(fileParsedList == null) {
			fileParsedList = new ArrayList<String>();
		}
		return fileParsedList;
	}
}
