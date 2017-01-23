package com.heaven7.java.visitor.internal;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.anno.Nullable;
import com.heaven7.java.visitor.collection.IterationInfo;
import com.heaven7.java.visitor.collection.VisitService;

/**
 * the iterate state. help we fast do something.
 * 
 * @author heaven7
 *
 * @param <T>
 *            the type of collection
 * @param <R>
 *            the result type of visit for {@link #visitForResult(Collection, boolean, 
 *            com.heaven7.java.visitor.collection.VisitService.GroupOperateInterceptor, 
 *            IterationInfo, Object, PredicateVisitor, ResultVisitor, List)}
 */
public abstract class IterateState<T, R> {

	/**
	 * create and return a single iterate state
	 * @param <T>
     *            the type of collection
     * @param <R>
     *            the result type of visit for {@link #visitForResult(Collection, boolean, 
     *            com.heaven7.java.visitor.collection.VisitService.GroupOperateInterceptor, 
     *            IterationInfo, Object, PredicateVisitor, ResultVisitor, List)}
	 * @return an instance of IterateState
	 */
	public static <T, R> IterateState<T, R> singleIterateState() {
		return new SingleIterateState<T, R>();
	}
	/**
	 * create and return a multiple iterate state
	 * @param <T>
     *            the type of collection
     * @param <R>
     *            the result type of visit for {@link #visitForResult(Collection, boolean, 
     *            com.heaven7.java.visitor.collection.VisitService.GroupOperateInterceptor, 
     *            IterationInfo, Object, PredicateVisitor, ResultVisitor, List)}
	 * @return an instance of IterateState
	 */
	public static <T, R> IterateState<T, R> multipleIterateState() {
		return new MultipleIterateState<T, R>();
	}

	/**
	 * 
	 * execute the iteration of collection. contains the all operate in
	 * iteration.
	 * 
	 * @param collection
	 *            the collection to iterate
	 * @param hasExtra
	 *            whether has extra operate or not
	 * @param interceptor
	 *            the GroupOperateInterceptor
	 * @param info
	 *            the iteration info of count. which will used from some
	 *            operate. such as insert,final insert.
	 * @param param
	 *            the param for iterate.
	 * @param predicate
	 *            the predicate visitor
	 * @param out
	 *            the out list , can be null if is single.
	 * @return the result which is match the predicate visitor, if it's single
	 *         state. or else return null.
	 */
	public T visit(Collection<T> collection, boolean hasExtra, VisitService<T>.GroupOperateInterceptor interceptor,
			IterationInfo info, Object param, PredicateVisitor<? super T> predicate, @Nullable List<T> out) {
		if (hasExtra) {
			interceptor.begin();
		}
		final Iterator<T> it = (collection instanceof List) ? ((List<T>) collection).listIterator()
				: collection.iterator();

		T result = visitImpl(hasExtra, interceptor, param, predicate, it, info, out);
		if (hasExtra) {
			interceptor.end();
		}
		return result;
	}

	/**
	 * 
	 * execute the iteration of collection. contains the all operate in
	 * iteration.
	 * 
	 * @param collection
	 *            the collection to iterate
	 * @param hasExtra
	 *            whether has extra operate or not
	 * @param interceptor
	 *            the GroupOperateInterceptor
	 * @param info
	 *            the iteration info of count. which will used from some
	 *            operate. such as insert,final insert.
	 * @param param
	 *            the param for iterate.
	 * @param predicate
	 *            the predicate visitor
	 * @param resultVistor
	 *            the result visitor
	 * @param out
	 *            the out list , can be null if is single.
	 * @return the result which is match the predicate visitor, if it's single
	 *         state. or else return null.
	 */
	public R visitForResult(Collection<T> collection, boolean hasExtra,
			VisitService<T>.GroupOperateInterceptor interceptor, IterationInfo info, Object param,
			PredicateVisitor<? super T> predicate, ResultVisitor<? super T, R> resultVistor, @Nullable List<R> out) {
		if (hasExtra) {
			interceptor.begin();
		}
		final Iterator<T> it = (collection instanceof List) ? ((List<T>) collection).listIterator()
				: collection.iterator();

		R result = visitForResultImpl(hasExtra, interceptor, param, predicate, resultVistor, it, info, out);
		if (hasExtra) {
			interceptor.end();
		}
		return result;
	}

	/**
	 * visit the collection for result.
	 * 
	 * @param hasExtra
	 *            if has extra operation for current visit.
	 * @param interceptor
	 *            the operate interceptor
	 * @param param
	 *            the parameter
	 * @param predicate
	 *            the predicate visitor
	 * @param resultVistor
	 *            the result visitor
	 * @param it
	 *            the Iterator for collection
	 * @param info
	 *            the IterationInfo
	 * @param out
	 *            the out list. may be null if is {@link SingleIterateState}.
	 * @return the result , may be null if is {@link MultipleIterateState}}.
	 */
	protected abstract R visitForResultImpl(boolean hasExtra, VisitService<T>.GroupOperateInterceptor interceptor,
			Object param, PredicateVisitor<? super T> predicate, ResultVisitor<? super T, R> resultVistor,
			Iterator<T> it, IterationInfo info, List<R> out);

	/**
	 * visit the collection
	 * 
	 * @param hasExtra
	 *            if has extra operation for current visit.
	 * @param groupInterceptor
	 *            the operate interceptor
	 * @param param
	 *            the parameter
	 * @param predicate
	 *            the predicate visitor
	 * @param it
	 *            the Iterator for collection
	 * @param info
	 *            the IterationInfo
	 * @param out
	 *            the out list. may be null if is {@link SingleIterateState}.
	 * @return the result , may be null if is {@link MultipleIterateState}}.
	 */
	protected abstract T visitImpl(boolean hasExtra, VisitService<T>.GroupOperateInterceptor groupInterceptor, Object param,
			PredicateVisitor<? super T> predicate, Iterator<T> it, IterationInfo info, List<T> out);
}
