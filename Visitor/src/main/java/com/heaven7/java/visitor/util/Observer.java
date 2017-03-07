package com.heaven7.java.visitor.util;

import com.heaven7.java.visitor.collection.CollectionVisitService;

/**
 * the observer used for {@linkplain CollectionVisitService}.
 * 
 * @author heaven7
 *
 * @param <T>
 *            the element type
 * @param <R>
 *            the result type of operate
 * @since 1.1.6
 */
public interface Observer<T, R> {

	/**
	 * called on operate success.
	 * @param param the extra parameter
	 * @param r the result when success.
	 */
	void onSucess(Object param, R r);

	/**
	 * called on operate failed.
	 * @param param the parameter.
	 * @param t the element
	 */
	void onFailed(Object param, T t);

	/**
	 * called on throwable during visit operation.
	 * @param param the parameter.
	 * @param t the element
	 * @param e the throwable.eveJAVA
	 */
	void onThrowable(Object param, T t, Throwable e);

}