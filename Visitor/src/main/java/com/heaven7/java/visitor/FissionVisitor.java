package com.heaven7.java.visitor;

import java.util.List;

/**
 * the fission visitor from one to more.
 * @param <T> the input type
 * @param <R> the result type.
 * @since 1.3.7
 * @author heaven7
 */
public interface FissionVisitor<T, R>{

    /**
     * called when visit the target element.
     *
     * @param t
     *            the object
     * @param param
     *            the param data to carry when visit
     * @return the visit result. or null if it is ignored.
     */
    List<R> visit(T t, Object param);
}
