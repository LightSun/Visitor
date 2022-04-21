package com.heaven7.java.visitor;

/**
 * the weight visitor
 * @author heaven7
 * @since 1.2.9
 */
public interface WeightVisitor<T> extends Visitor1<T, Object, Integer> {

    @Override
    Integer visit(T t, Object param);

}
