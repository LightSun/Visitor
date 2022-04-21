package com.heaven7.java.visitor;

/**
 * the visitor which focus on start or end .
 * @param <T> the element type
 * @since 1.2.0
 */
public interface StartEndVisitor<T> {

   boolean visit(Object param, T t, boolean start, boolean end);
}
