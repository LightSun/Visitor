package com.heaven7.java.visitor.collection;

public interface VisitService {

	/** indicate the operate of delete */
	public static final int OP_DELETE = 1;
	/** indicate the operate of filter */
	public static final int OP_FILTER = 2;
	/** indicate the operate of update */
	public static final int OP_UPDATE = 3;
	/**
	 * indicate the operate of insert. Note this in only used for List, or else
	 * have nothing effect.
	 */
	public static final int OP_INSERT = 4;
	
	/**
	 * indicate the operate of trim. only used for map.
	 */
	public static final int OP_TRIM   = 5;
	
}
