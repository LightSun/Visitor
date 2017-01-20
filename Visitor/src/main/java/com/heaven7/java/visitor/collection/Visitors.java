package com.heaven7.java.visitor.collection;

public class Visitors {

	@SuppressWarnings("unchecked")
	public static <T>IterateVisitor<T> trueIterateVisitor(){
		return (IterateVisitor<T>) ITERATE_TRUE;
	}
	@SuppressWarnings("unchecked")
	public static <T>IterateVisitor<T> falseIterateVisitor(){
		return (IterateVisitor<T>) ITERATE_FALSE;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> PredicateVisitor<T> truePredicateVisitor() {
		return (PredicateVisitor<T>) PREDICARE_TRUE;
	}

	@SuppressWarnings("unchecked")
	public static <T> PredicateVisitor<T> falsePredicateVisitor() {
		return (PredicateVisitor<T>) PREDICARE_FALSE;
	}

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
