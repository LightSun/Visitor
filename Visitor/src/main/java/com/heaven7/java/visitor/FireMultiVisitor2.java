package com.heaven7.java.visitor;

import java.util.List;

/**
 * the fire batch 'N' visitor.
 * @param <T> the element type
 * @since 1.2.9.1
 */
public interface FireMultiVisitor2<T> {

    /**
     * visit the 'N' count of elements
     * @param param the param
     * @param count the count of expect size
     * @param step the step of visit/travel offset.
     * @param ts the elements which is indicate by count. if left is not enough, the elements can be at least one,
     * @return true if should stop next visits. false otherwise.
     */
    boolean visit(Object param, int count, int step, List<T> ts);
}
