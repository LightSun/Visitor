package com.heaven7.java.visitor.collection;

/**
 * the result visitor
 * Created by heaven7 on 2017/1/16.
 */
public interface ResultVisitor<T, Result> {

    /**
     * called when visit the target element.
     *
     * @param t     the object
     * @param param the param data to carry when visit
     * @return the visit result.
     */
    Result visit(T t, Object param);
}
