package com.google.code.jconfig.exception;

public class NodeTransformationException extends Exception {

	private static final long serialVersionUID = -5936058152587832508L;

	/**
	 * <p>
	 *    The default constructor.
	 * </p>
	 */
	public NodeTransformationException() {
		super();
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public NodeTransformationException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * @param message
	 */
	public NodeTransformationException(String message) {
		super(message);
	}

	/**
	 * @param throwable
	 */
	public NodeTransformationException(Throwable throwable) {
		super(throwable);
	}
}
