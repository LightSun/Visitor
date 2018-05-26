package com.heaven7.java.visitor;


/**
 * the result index visitor
 * @param <T> the element type
 * @param <R> the result type
 * @since 2.0.0
 */
public interface ResultIndexedVisitor<T, R>{

    /**
     * visit for result
     * @param t the element
     * @param index the index
     * @param param the extra param
     * @return the result of this visit
     */
    R visit(Object param, T t, int index, int size);
}
