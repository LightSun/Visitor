package com.heaven7.java.visitor;

/**
 * the simple visitor
 * @author heaven7
 *
 * @param <T> the object type to visit
 * @param <R> the result type
 */
public interface Visitor<T, R> {

	/**
	 * visit the target object
	 * @param t the object t.
	 * @return the result
	 */
	R visit(T t);
}
