package com.heaven7.java.visitor;


/**
 * the result index visitor
 * @param <T> the element type
 * @param <R> the result type
 * @since 2.0.0
 */
public interface ResultIndexedVisitor<T, R> extends Visitor2<T, Integer, Object, R>  {

    @Override
    R visit(T t, Integer integer, Object o);
}
