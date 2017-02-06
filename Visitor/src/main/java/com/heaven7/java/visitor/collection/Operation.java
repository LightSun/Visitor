package com.heaven7.java.visitor.collection;

import java.util.List;
import java.util.Map;

public abstract class Operation {
	
	/** indicate the operate of delete */
	public static final int OP_DELETE = 1;
	/** indicate the operate of filter, when iterate the element (nt collection ) or key-value pair 
	 * (in map) just skip it without do anything (if the visitor return true).  */
	public static final int OP_FILTER = 2;
	/** indicate the operate of update */
	public static final int OP_UPDATE = 3;
	/**
	 * indicate the operate of insert. Note this in only used for {@linkplain List}, or else
	 * have nothing effect.
	 */
	public static final int OP_INSERT = 4;
	
	/**
	 * indicate the operate of trim. only used for {@linkplain Map}}.
	 */
	public static final int OP_TRIM   = 5;
	
	protected int mOp;
	/** the extra param for current operation. */
	protected Object mParam;
	
	
	protected Operation() {
		super();
	}
	
	public void reset(){
		mOp = 0;
		mParam = null;
	}
	
}
