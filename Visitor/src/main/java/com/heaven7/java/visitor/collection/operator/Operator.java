package com.heaven7.java.visitor.collection.operator;

import java.util.Collection;

import com.heaven7.java.visitor.util.Observer;
import com.heaven7.java.visitor.util.Predicates;

/**
 * the operator of condition.
 * @author heaven7
 *
 * @param <T> the element type of collection
 * @param <R> the result type of {@linkplain Observer}
 * @since 2.0.0
 */
public abstract class Operator<T, R> {

	private T mCurrent;

	/**
	 * get the required arguments flags. which will used by {@linkplain OperateCondition} to check arguments.
	 * @return the required arguments flags. default is 0(means require nothing).
	 * @see OperateCondition#FLAG_COLLECTION
	 * @see OperateCondition#FLAG_SINGLE_ELEMENT
	 * @see OperateCondition#FLAG_RESULT
	 * @see OperateCondition#FLAG_PREDICATE
	 */
	public int getRequireArgsFlags() {
		return 0;
	}

	/**
	 * tag start visit the target element. this method will effect the
	 * {@linkplain Observer} when failed or throws exception.
	 * 
	 * @param element
	 *            the target element
	 */
	public void startVisitElement(T element) {
		this.mCurrent = element;
	}

	public final boolean apply(Collection<T> src, OperateCondition<T, R> condition) {
		final Observer<? super T, R> observer = condition.getObserver();
		final Object param = condition.getParam();
		try {
			// boolean = true or !=null only success.
			R r = executeOperator(src, condition);
			if (r instanceof Boolean) {
				if (Predicates.isTrue((Boolean) r)) {
					observer.onSuccess(param, r);
					return true;
				} else {
					observer.onFailed(param, mCurrent);
				}
			} else if (r != null) {
				observer.onSuccess(param, r);
				return true;
			} else {
				observer.onFailed(param, mCurrent);
			}
		} catch (Throwable e) {
			observer.onThrowable(param, mCurrent, e);
		}
		return false;
	}

	/**
	 * execute operate
	 * 
	 * @param src
	 *            the source collection
	 * @param condition
	 *            the condition
	 * @return the result. if (Boolean && true ) | not null. means success. or
	 *         else failed/exception.
	 */
	protected abstract R executeOperator(Collection<T> src, OperateCondition<T, R> condition);

}