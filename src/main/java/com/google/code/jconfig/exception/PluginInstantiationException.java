package com.google.code.jconfig.exception;

public class PluginInstantiationException extends Exception {

	private static final long serialVersionUID = -5936058152587832508L;

	/**
	 * <p>
	 *    The default constructor.
	 * </p>
	 */
	public PluginInstantiationException() {
		super();
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public PluginInstantiationException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * @param message
	 */
	public PluginInstantiationException(String message) {
		super(message);
	}

	/**
	 * @param throwable
	 */
	public PluginInstantiationException(Throwable throwable) {
		super(throwable);
	}
}
