package com.heaven7.java.visitor.collection;

/**
 * the predicate of element
 *
 * @param <T> the  item type
 * @since 1.1.0
 */
public interface PredicateVisitor<T> extends ResultVisitor<T, Boolean> {

    /**
     * called when we want to predicate the item with the param.
     *
     * @param t     the item
     * @param param the other param
     * @return true if predicate ok.
     */
    Boolean visit(T t, Object param);
}
