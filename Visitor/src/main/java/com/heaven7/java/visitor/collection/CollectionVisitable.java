package com.heaven7.java.visitor.collection;


import java.util.Collection;

/**
 * collection visitable 
 * Created by heaven7 on 2017/1/16.
 *
 * @since 1.1.3
 */
public interface CollectionVisitable<T> {

    /**
     * accept the Result visitor visit.
     *
     * @param param   the extra param
     * @param predicate the predicate visitor
     * @param visitor the result visitor
     * @param count   the count to limit. -1 with the all.
     * @param out     the out list. can be null.
     * @param <R>     the result type  of visit.
     * @return the collection of visit result
     */
    <R> Collection<R> accept(Object param ,PredicateVisitor<T> predicate,
    		ResultVisitor<T, R> visitor, int count, Collection<R> out);
    
    <R> R accept(Object param, PredicateVisitor<T> predicate, ResultVisitor<T, R> visitor);
    

    /**
     * accept the visitor visit.
     *
     * @param rule    the visit rule, {@link VisitConstant#VISIT_RULE_UNTIL_SUCCESS} and etc.
     * @param visitor the element visitor
     * @param param   the param data when visit.
     * @return true if visit success base on the rule.
     * @since 1.1.0
     */
    boolean accept(@VisitConstant.VisitRuleType int rule, Object param, PredicateVisitor<T> visitor);

    /**
     * find a element by the target param and predicate.
     *
     * @param param     the param.
     * @param predicate the element predicate
     * @return the target element by find in array.
     * @since 1.1.0
     */
    T find(Object param, PredicateVisitor<T> predicate);
    
}
