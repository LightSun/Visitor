package com.heaven7.java.visitor;

/**
 * the predicate of element
 *
 * @param <T> the  item type
 */
public interface PredicateVisitor<T> extends ResultVisitor<T, Boolean> {

    /**
     * called when we want to predicate the item/pair with the param.
     *
     * @param t     the item
     * @param param the other param
     * @return true if predicate ok.
     */
    Boolean visit(T t, Object param);
}
