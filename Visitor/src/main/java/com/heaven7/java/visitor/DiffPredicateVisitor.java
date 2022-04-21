package com.heaven7.java.visitor;

/**
 * use diff predicate visitor to judge if is different or not .
 * @author heaven7
 * @since 1.3.1
 */
public interface DiffPredicateVisitor<V, T> extends Visitor2<Object,V, T, Boolean> {

    /**
     * predicate the object t is diff from v or not.
     * @param param the parameter
     * @param t the object to predicate
     * @param v the value to predicate
     * @return true if is different. false otherwise.
     */
    Boolean visit(Object param, V v, T t);
}
