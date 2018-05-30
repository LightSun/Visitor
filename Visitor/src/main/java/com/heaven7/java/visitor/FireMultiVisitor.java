package com.heaven7.java.visitor;

import java.util.List;

/**
 * the fire batch 'N' visitor.
 * @param <T> the element type
 */
public interface FireMultiVisitor<T> {

    /**
     * visit the 'N' count of elements
     * @param param the param
     * @param ts the elements
     */
    void visit(Object param, List<T> ts);
}
