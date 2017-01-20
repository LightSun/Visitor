package com.heaven7.java.visitor.collection;

import com.heaven7.java.visitor.Visitor1;

/**
 * the result visitor Created by heaven7 on 2017/1/16.
 */
public interface ResultVisitor<T, Result> extends Visitor1<T, Object, Result> {

	/**
	 * called when visit the target element.
	 *
	 * @param t
	 *            the object
	 * @param param
	 *            the param data to carry when visit
	 * @return the visit result.
	 */
	Result visit(T t, Object param);
	
}
