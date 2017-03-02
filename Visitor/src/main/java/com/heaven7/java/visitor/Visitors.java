package com.heaven7.java.visitor;

import com.heaven7.java.visitor.collection.IterationInfo;
import com.heaven7.java.visitor.collection.KeyValuePair;

/**
 * helper class help we fast get certain visitor.
 * 
 * @author heaven7
 *
 */
public final class Visitors {

	private Visitors(){}
	
	@SuppressWarnings("unchecked")
	public static <T> ResultVisitor<T, String> toStringVisitor(){
		return (ResultVisitor<T, String>) sDefaultStringVisitor;
	}
	/**
	 * @param <T>
	 *            the element type
	 * @param <R>
	 *            the result type of {@linkplain ResultVisitor}
	 * @return a result visitor that always return the object which is
	 *         transported by {@linkplain ResultVisitor#visit(Object, Object)}.
	 */
	@SuppressWarnings("unchecked")
	public static <T, R> ResultVisitor<T, R> unchangeResultVisitor() {
		return (ResultVisitor<T, R>) RESULT_UNCHANGE;
	}

	/**
	 * @param <T>
	 *            the type
	 * @return a iterate visitor that always return true.
	 */
	@SuppressWarnings("unchecked")
	public static <T> IterateVisitor<T> trueIterateVisitor() {
		return (IterateVisitor<T>) ITERATE_TRUE;
	}

	/**
	 * @param <T>
	 *            the type
	 * @return a iterate visitor that always return false.
	 */
	@SuppressWarnings("unchecked")
	public static <T> IterateVisitor<T> falseIterateVisitor() {
		return (IterateVisitor<T>) ITERATE_FALSE;
	}

	/**
	 * @param <T>
	 *            the type
	 * @return a predicate visitor that always return true.
	 */
	@SuppressWarnings("unchecked")
	public static <T> PredicateVisitor<T> truePredicateVisitor() {
		return (PredicateVisitor<T>) PREDICARE_TRUE;
	}
	
	/**
	 * @param <T>
	 *            the type
	 * @return a predicate visitor that always return false.
	 */
	@SuppressWarnings("unchecked")
	public static <T> PredicateVisitor<T> falsePredicateVisitor() {
		return (PredicateVisitor<T>) PREDICARE_FALSE;
	}
	
	/**
	 * @param <K> the key type
	 * @param <V> the value type
	 * @return a MapPredicateVisitor that always return true.
	 */
	@SuppressWarnings("unchecked")
	public static <K,V> MapPredicateVisitor<K,V> trueMapPredicateVisitor() {
		return (MapPredicateVisitor<K,V>) MAP_PREDICARE_TRUE;
	}

	/**
	 * @param <K> the key type
	 * @param <V> the value type
	 * @return a MapPredicateVisitor that always return false.
	 */
	@SuppressWarnings("unchecked")
	public static <K,V> MapPredicateVisitor<K,V> falseMapPredicateVisitor() {
		return (MapPredicateVisitor<K,V>) MAP_PREDICARE_FALSE;
	}
	
	/**
	 * @param <K> the key type
	 * @param <V> the value type
	 * @return a MapIterateVisitor that always return true.
	 */
	@SuppressWarnings("unchecked")
	public static <K,V> MapIterateVisitor<K, V> trueMapIterateVisitor(){
		return (MapIterateVisitor<K, V>) MAP_ITERATE_TRUE;
	}
	/**
	 * @param <K> the key type
	 * @param <V> the value type
	 * @return a MapIterateVisitor that always return false.
	 */
	@SuppressWarnings("unchecked")
	public static <K,V> MapIterateVisitor<K, V> falseMapIterateVisitor(){
		return (MapIterateVisitor<K, V>) MAP_ITERATE_FALSE;
	}
	/**
	 * @param <K> the key type
	 * @param <V> the value type
	 * @return a MapResultVisitor that always return the key which comes from map.
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> MapResultVisitor<K, V, K> keyMapResultVisitor(){
		return (MapResultVisitor<K, V, K>) MAP_RESULT_KEY;
	}
	/**
	 * @param <K> the key type
	 * @param <V> the value type
	 * @return a MapResultVisitor that always return the value which comes from map.
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> MapResultVisitor<K, V, V> valueMapResultVisitor(){
		return (MapResultVisitor<K, V, V>) MAP_RESULT_VALUE;
	}
	
	private static final MapResultVisitor<Object, Object, Object> MAP_RESULT_KEY = new MapResultVisitor<Object, Object, Object>() {
		@Override
		public Object visit(KeyValuePair<Object, Object> t, Object param) {
			return t.getKey();
		}
	};
	private static final MapResultVisitor<Object, Object, Object> MAP_RESULT_VALUE = new MapResultVisitor<Object, Object, Object>() {
		@Override
		public Object visit(KeyValuePair<Object, Object> t, Object param) {
			return t.getValue();
		}
	};

	private static final ResultVisitor<Object, Object> RESULT_UNCHANGE = new ResultVisitor<Object, Object>() {
		@Override
		public Object visit(Object t, Object param) {
			return t;
		}
	};
	private static final PredicateVisitor<Object> PREDICARE_TRUE = new PredicateVisitor<Object>() {
		@Override
		public Boolean visit(Object t, Object param) {
			return Boolean.TRUE;
		}
	};
	private static final MapPredicateVisitor<Object, Object> MAP_PREDICARE_TRUE = new MapPredicateVisitor<Object, Object>() {
		@Override
		public Boolean visit(KeyValuePair<Object, Object> pair, Object param) {
			return Boolean.TRUE;
		}
	};
	private static final MapPredicateVisitor<Object, Object> MAP_PREDICARE_FALSE = new MapPredicateVisitor<Object, Object>() {
		@Override
		public Boolean visit(KeyValuePair<Object, Object> pair, Object param) {
			return Boolean.FALSE;
		}
	};
	private static final PredicateVisitor<Object> PREDICARE_FALSE = new PredicateVisitor<Object>() {
		@Override
		public Boolean visit(Object t, Object param) {
			return Boolean.FALSE;
		}
	};
	
	private static final MapIterateVisitor<Object, Object> MAP_ITERATE_TRUE = 
			new MapIterateVisitor<Object, Object>() {
		@Override
		public Boolean visit(KeyValuePair<Object, Object> pair, Object param, IterationInfo info) {
			return Boolean.TRUE;
		}
	};
	private static final MapIterateVisitor<Object, Object> MAP_ITERATE_FALSE = 
			new MapIterateVisitor<Object, Object>() {
		@Override
		public Boolean visit(KeyValuePair<Object, Object> pair, Object param, IterationInfo info) {
			return Boolean.FALSE;
		}
	};

	private static final IterateVisitor<Object> ITERATE_TRUE = new IterateVisitor<Object>() {
		@Override
		public Boolean visit(Object t, Object param, IterationInfo info) {
			return Boolean.TRUE;
		}
	};

	private static final IterateVisitor<Object> ITERATE_FALSE = new IterateVisitor<Object>() {

		@Override
		public Boolean visit(Object t, Object param, IterationInfo info) {
			return Boolean.FALSE;
		}
	};
	private static final ResultVisitor<Object, String> sDefaultStringVisitor = 
			new ResultVisitor<Object, String>() {
		@Override
		public String visit(Object t, Object param) {
			return t.toString();
		}
	};

}
