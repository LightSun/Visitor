package com.heaven7.java.visitor;

/**
 * the simple visitor
 * @author heaven7
 *
 * @param <T> the object type to visit
 * @param <R> the result type
 */
public interface Visitor<T, R> {

	R visit(T t);
}
