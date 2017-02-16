package com.heaven7.java.visitor.util;

/**
 * called when catch throwable.
 * @author heaven7
 * @since 1.1.1
 */
public class VisitException extends RuntimeException {

	 static final long serialVersionUID = 1l;

	public VisitException() {
		super();
	}

	public VisitException(String message, Throwable cause) {
		super(message, cause);
	}

	public VisitException(String message) {
		super(message);
	}

	public VisitException(Throwable cause) {
		super(cause);
	}
	 
	 
	
}
