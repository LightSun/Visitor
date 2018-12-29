package com.heaven7.java.visitor;

import java.util.List;

/**
 * the diff visitor used to visit the diff between two collection.
 * @param <T> the raw element type.
 * @param <V> the element type after normalize.
 * @author heaven7
 * @since 1.3.1
 */
public interface DiffResultVisitor<V, T>{

    /**
     * called to diff two collection elements.
     * @param param the param
     * @param normalizeList the list contains all normalized elements
     * @param currentNonNormalizeList the list contains all non-normalized elements from first collection.
     * @param otherNonNormalizeList the list contains all non-normalized elements from other collection.
     */
    void visit(Object param, List<V> normalizeList, List<T> currentNonNormalizeList,  List<T> otherNonNormalizeList);
}
