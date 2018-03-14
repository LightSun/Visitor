package com.heaven7.java.visitor;

/**
 * the fire index visitor
 * @param <T> the element type
 * @since 1.2.0
 */
public interface FireIndexedVisitor<T> extends Visitor2<T, Integer, Object, Boolean> {

    /**
     * visit for result
     * @param t the element
     * @param index the index
     * @param param the extra param
     * @return the result of this visit
     */
    Boolean visit(T t, Integer index, Object param);
}
