package com.heaven7.java.visitor.util;
/**
 * indicate visit exception during visit.
 * @author heaven7
 *
 */
public class VisitException extends RuntimeException{

	private static final long serialVersionUID = 1l;

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
