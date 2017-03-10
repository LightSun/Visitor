package com.heaven7.java.visitor.collection;

import java.util.List;
import java.util.Map;
/**
 * the base operation. contains: delete, filter, update, inert or trim
 * @author heaven7
 *
 */
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

	/**
	 * the flag of 'if not exist'
	 * @since  1.1.6
	 */
	public static final int FLAG_CASE_IF_NOT_EXIST = 0x00000100;
	/**
	 * the flag of 'if exist'
	 * @since 1.1.6
	 */
	public static final int FLAG_CASE_IF_EXIST     = 0x00000200;
	private static final int MASK_OP               = 0x000000ff;
	private static final int MASK_FLAG             = 0xffffff00;

	/** you should not use member directly. use {@linkplain #setOperate(int)}, {@linkplain #getOperate()},
	 * {@linkplain #addFlags(int)} {@linkplain #deleteFlags(int)} instead.
	 * <h1>this will be private in v2.x .</h1>*/
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
	/**
	 * set operate
	 * @param op the operate
	 * @since  1.1.6
	 */
	public void setOperate(int op){
		this.mOp = getFlags() + op;
	}
	/**
	 * get operate
	 * @return the operate
	 * @since  1.1.6
	 */
	public int getOperate(){
		return mOp & MASK_OP;
	}
	/**
	 * get flags
	 * @return the flags
	 * @since  1.1.6
	 */
	public int getFlags(){
		return mOp & MASK_FLAG;
	}
	/**
	 * add flags
	 * @param flags the flags
	 * @since  1.1.6
	 */
	public void addFlags(int flags){
		int ops = getOperate();
		mOp = (getFlags() | flags) + ops;
	}
	/**
	 * has flags
	 * @param flags the flags
	 * @since  1.1.6
	 */
	public boolean hasFlags(int flags){
		return (getFlags() & flags ) != 0;
	}

	/**
	 * delete flags
	 * @param flags the flags
	 * @since  1.1.6
	 */
	public void deleteFlags(int flags){
		int rawFlags = getFlags();
		rawFlags &=~flags;
		mOp = getOperate() + rawFlags;
	}
/*
	public static void main(String[] args){
		Operation ops = new Operation() {
			@Override
			public void reset() {
				super.reset();
			}
		};
		ops.mOp =2;
		ops.addFlags(FLAG_CASE_IF_NOT_EXIST);

		System.out.println(ops.hasFlags(FLAG_CASE_IF_NOT_EXIST));
		System.out.println(ops.getFlags()==FLAG_CASE_IF_NOT_EXIST);
		System.out.println(ops.getOperate()==2);
		ops.setOperate(3);
		System.out.println(ops.getOperate()==2);
		System.out.println(ops.getOperate()==3);
		ops.deleteFlags(FLAG_CASE_IF_NOT_EXIST);
		System.out.println(ops.hasFlags(FLAG_CASE_IF_NOT_EXIST));
	}*/
	
}
