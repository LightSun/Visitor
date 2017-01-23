package com.heaven7.java.visitor.collection;

import java.util.List;

import com.heaven7.java.visitor.IterateVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;

/**
 * the super interface of 'Visit-Service'
 * @author heaven7
 *
 * @param <T> the type of element
 * @see AbstractVisitService
 * @see VisitService
 * @see ListVisitService
 */
//TODO need visitFirst/ visitLat / ?
public interface IVisitService<T> {
	
	/**
	 * the visit rule: visit all.
	 */
	public static final int VISIT_RULE_ALL = 11;
	/**
	 * the visit rule: until success. this is useless for the 'Set' of
	 * 'Collection'
	 */
	public static final int VISIT_RULE_UNTIL_SUCCESS = 12;
	/**
	 * the visit rule: until failed. this is useless for the 'Set' of
	 * 'Collection'
	 */
	public static final int VISIT_RULE_UNTIL_FAILED = 13;

	/**
	 * visit the collection for result .
	 *
	 * @param param
	 *            the extra parameter
	 * @param predicate
	 *            the predicate visitor
	 * @param resultVisitor
	 *            the result visitor
	 * @param <R>
	 *            the result type of visit.
	 * @return the result of this visit
	 */
	<R> R visitForResult(Object param, PredicateVisitor<? super T> predicate,
			ResultVisitor<? super T, R> resultVisitor);
	
	/**
	 * visit the collection for result , but carry no extra data.
	 *
	 * @param predicate
	 *            the predicate visitor
	 * @param resultVisitor
	 *            the result visitor
	 * @param <R>
	 *            the result type of visit.
	 * @return the result of this visit
	 */
	<R> R visitForResult(PredicateVisitor<? super T> predicate,
			ResultVisitor<? super T, R> resultVisitor);

	/**
	 * visit the all elements for result. which is matched by the target predicate visitor.
	 *
	 * @param param
	 *            the extra parameter for transport to the all visitors.
	 * @param predicate
	 *            the predicate visitor
	 * @param visitor
	 *            the result visitor
	 * @param out
	 *            the out list. can be null.
	 * @param <R>
	 *            the result type of visit.
	 * @return the collection of visit result
	 */
	<R> List<R> visitForResult(Object param, PredicateVisitor<? super T> predicate,
			ResultVisitor<? super T, R> resultVisitor, List<R> out);
	
	/**
	 * visit the all elements for result. which is matched by the target predicate visitor, but carry no extra data.
	 *
	 * @param predicate
	 *            the predicate visitor
	 * @param visitor
	 *            the result visitor
	 * @param out
	 *            the out list. can be null.
	 * @param <R>
	 *            the result type of visit.
	 * @return the collection of visit result
	 */
	<R> List<R> visitForResult(PredicateVisitor<? super T> predicate,
			ResultVisitor<? super T, R> resultVisitor, List<R> out);

	/**
	 * visit for query the all element which is match the PredicateVisitor.
	 * 
	 * @param param
	 *            the extra parameter for transport to the all visitors.
	 * @param predicate
	 *            the predicate visitor
	 * @param out
	 *            the out list, can be null.
	 * @return
	 */
	List<T> visitForQuery(Object param, PredicateVisitor<? super T> predicate, List<T> out);
	
	/**
	 * visit for query the all element which is match the PredicateVisitor, but carry no extra data.
	 * 
	 * @param predicate
	 *            the predicate visitor
	 * @param out
	 *            the out list, can be null.
	 * @return
	 */
	List<T> visitForQuery(PredicateVisitor<? super T> predicate, List<T> out);
	

	/**
	 * visit for query a element by the target parameter and predicate.
	 *
	 * @param param
	 *            the extra parameter for transport to the all visitors.
	 * @param predicate
	 *            the predicate visitor
	 * @return the target element by find in array.
	 * @since 1.1.0
	 */
	T visitForQuery(Object param, PredicateVisitor<? super T> predicate);
	
	/**
	 * visit for query a element by the target parameter and predicate, but carry no extra data.
	 *
	 * @param predicate
	 *            the predicate visitor
	 * @return the target element by find in array.
	 * @since 1.1.0
	 */
	T visitForQuery(PredicateVisitor<? super T> predicate);

	/**
	 * visit the all elements if possible.
	 * @param param the extra parameter.
	 * @return true if operate success.
	 */
	boolean visitAll(Object param);
	
	/**
	 * visit the all elements if possible, but carry no extra data.
	 * @return true if operate success.
	 */
	boolean visitAll();

	/**
	 * visit the all elements until success. by default this is same with {@linkplain #visitAll(Object)}.
	 * @param param the extra parameter.
	 * @param breakVisitor the break visitor
	 * @return true if operate success.
	 */
	boolean visitUntilSuccess(Object param, IterateVisitor<? super T> breakVisitor);
	
	/**
	 * visit the all elements until success, but carry no extra data. by default this is same with {@linkplain #visitAll(Object)}.
	 * @param breakVisitor the break visitor
	 * @return true if operate success.
	 */
	boolean visitUntilSuccess(IterateVisitor<? super T> breakVisitor);

	/**
	 * visit the all elements until failed. by default this is same with {@linkplain #visitAll(Object)}.
	 * @param param the extra parameter.
	 * @param breakVisitor the break visitor
	 * @return true if operate success.
	 */
	boolean visitUntilFailed(Object param, IterateVisitor<? super T> breakVisitor);
	/**
	 * visit the all elements until failed, but carry no extra data. by default this is same with {@linkplain #visitAll(Object)}.
	 * @param breakVisitor the break visitor
	 * @return true if operate success.
	 */
	boolean visitUntilFailed(IterateVisitor<? super T> breakVisitor);

}