package com.heaven7.java.visitor;

import com.heaven7.java.visitor.collection.IterationInfo;

/**
 * helper class help we fast get certain visitor.
 * @author heaven7
 *
 */
public final class Visitors {

	/**
	 * @param <T> the element type
	 * @param <R> the result type of {@linkplain ResultVisitor}
	 * @return a result visitor that always return the object which is transported by {@linkplain ResultVisitor#visit(Object, Object)}.
	 */
	@SuppressWarnings("unchecked")
	public static <T, R> ResultVisitor<T, R> unchangeResultVisitor() {
		return (ResultVisitor<T, R>) RESULT_UNCHANGE;
	}

	/**
	 * @param <T> the type
	 * @return a iterate visitor that always return true. 
	 */
	@SuppressWarnings("unchecked")
	public static <T> IterateVisitor<T> trueIterateVisitor() {
		return (IterateVisitor<T>) ITERATE_TRUE;
	}

	/**
	 * @param <T> the type
	 * @return a iterate visitor that always return false. 
	 */
	@SuppressWarnings("unchecked")
	public static <T> IterateVisitor<T> falseIterateVisitor() {
		return (IterateVisitor<T>) ITERATE_FALSE;
	}

	/**
	 * @param <T> the type
	 * @return a predicate visitor that always return true. 
	 */
	@SuppressWarnings("unchecked")
	public static <T> PredicateVisitor<T> truePredicateVisitor() {
		return (PredicateVisitor<T>) PREDICARE_TRUE;
	}

	/**
	 * @param <T> the type
	 * @return a predicate visitor that always return false. 
	 */
	@SuppressWarnings("unchecked")
	public static <T> PredicateVisitor<T> falsePredicateVisitor() {
		return (PredicateVisitor<T>) PREDICARE_FALSE;
	}

	private static final ResultVisitor<Object, Object> RESULT_UNCHANGE = new ResultVisitor<Object, Object>() {
		@Override
		public Object visit(Object t, Object param) {
			return t;
		}
	};
	private static final PredicateVisitor<Object> PREDICARE_TRUE = new PredicateVisitor<Object>() {
		@Override
		public Boolean visit(Object t, Object param) {
			return true;
		}
	};
	private static final PredicateVisitor<Object> PREDICARE_FALSE = new PredicateVisitor<Object>() {
		@Override
		public Boolean visit(Object t, Object param) {
			return false;
		}
	};

	private static final IterateVisitor<Object> ITERATE_TRUE = new IterateVisitor<Object>() {
		@Override
		public Boolean visit(Object t, Object param, IterationInfo info) {
			return true;
		}
	};

	private static final IterateVisitor<Object> ITERATE_FALSE = new IterateVisitor<Object>() {

		@Override
		public Boolean visit(Object t, Object param, IterationInfo info) {
			return false;
		}
	};

}
