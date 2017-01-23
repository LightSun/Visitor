package com.heaven7.java.visitor.collection;

/**
 * indicate the object can be update.
 * @author heaven7
 *
 * @param <T> the type
 */
public interface Updatable<T> {

	/**
	 * update current from the target t
	 * @param t the target object
	 */
	void updateFrom(T t);
}
