package com.heaven7.java.visitor.collection;

import java.util.List;

import com.heaven7.java.visitor.MapIterateVisitor;
import com.heaven7.java.visitor.MapPredicateVisitor;
import com.heaven7.java.visitor.MapResultVisitor;
import com.heaven7.java.visitor.TrimMapVisitor;
import com.heaven7.java.visitor.anno.Nullable;
import com.heaven7.java.visitor.collection.CollectionVisitService.OperateManager;
import com.heaven7.java.visitor.internal.OperateInterceptor;
import com.heaven7.java.visitor.util.Map;

public interface MapVisitService<K, V>{

	<R> List<R> visitForResultList(Object param, MapPredicateVisitor<K, V> predicate,
			MapResultVisitor<K, V, R> resultVisitor, @Nullable List<R> out);

	<R> List<R> visitForResultList(MapPredicateVisitor<K, V> predicate,
			MapResultVisitor<K, V, R> resultVisitor,@Nullable List<R> out);

	<R> R visitForResult(Object param, MapPredicateVisitor<K, V> predicate,
			MapResultVisitor<K, V, R> resultVisitor);

	<R> R visitForResult(MapPredicateVisitor<K, V> predicate,
			MapResultVisitor<K, V, R> resultVisitor);

	List<KeyValuePair<K, V>> visitForQueryList(Object param, MapPredicateVisitor<K, V> predicate, @Nullable List<KeyValuePair<K, V>> out);

	List<KeyValuePair<K, V>> visitForQueryList(MapPredicateVisitor<K, V> predicate,@Nullable List<KeyValuePair<K, V>> out);

	KeyValuePair<K, V> visitForQuery(Object param, MapPredicateVisitor<K, V> predicate);

	KeyValuePair<K, V> visitForQuery(MapPredicateVisitor<K, V> predicate);

	/**
	 * begin the iterate control , And then we can edit the order of operate in
	 * iteration. Finally you can call {@linkplain IterateControl#end()} to end
	 * the iterate control.
	 * 
	 * @return the iterate control.
	 */
	IterateControl<MapVisitService<K, V>> beginIterateControl();

	/**
	 * begin the operate manager , And then we can add some pending operation in
	 * or after iteration. Finally you can call
	 * {@linkplain OperateManager#end()} to end the iterate operate manager.
	 * 
	 * @return the iterate control.
	 */
	MapOperateManager<K, V> beginOperateManager();

	public static abstract class MapOperateInterceptor<K, V> extends OperateInterceptor{
		
		/**
		 * intercept the current iteration.
		 * 
		 * @param map
		 *            the map
		 * @param entry
		 *            the entry
		 * @param param
		 *            the parameter, may be null.
		 * @param info
		 *            the IterationInfo.
		 * @return true if intercept success, this means the loop of 'for' will
		 *         be 'continue'.
		 */
		public abstract boolean intercept(Map<K, V> map, KeyValuePair<K, V> pair, @Nullable Object param, IterationInfo info);
	}

	public static abstract class MapOperateManager<K, V> {

		public abstract MapVisitService<K, V> end();

		public abstract MapOperateManager<K, V> filter(@Nullable Object param,
				MapPredicateVisitor<K, V> visitor);

		public abstract MapOperateManager<K, V> delete(@Nullable Object param,
				MapPredicateVisitor<K, V> visitor);

		public abstract MapOperateManager<K, V> update(V value, @Nullable Object param,
				MapPredicateVisitor<K, V> visitor);

		public abstract MapOperateManager<K, V> insertFinally(KeyValuePair<K, V> newPair, @Nullable Object param,
				MapIterateVisitor<K, V> visitor);

		public abstract MapOperateManager<K, V> insertFinally(List<KeyValuePair<K, V>> newPairs, @Nullable Object param,
				MapIterateVisitor<K, V> visitor);

		public abstract MapOperateManager<K, V> trim(@Nullable Object param,
				TrimMapVisitor<K, V> visitor);

		// ==========================================================

		public final MapOperateManager<K, V> trim(TrimMapVisitor<K, V> visitor) {
			return trim(null, visitor);
		}

		public final MapOperateManager<K, V> insertFinally(List<KeyValuePair<K, V>> newPairs,
				MapIterateVisitor<K, V> visitor) {
			return insertFinally(newPairs, null, visitor);
		}

		public final MapOperateManager<K, V> insertFinally(KeyValuePair<K, V> pair,
				MapIterateVisitor<K, V> visitor) {
			return insertFinally(pair, null, visitor);
		}

		public final MapOperateManager<K, V> update(V value, MapPredicateVisitor<K, V> visitor) {
			return update(value, null, visitor);
		}

		public final MapOperateManager<K, V> filter(MapPredicateVisitor<K, V> visitor) {
			return filter(null, visitor);
		}

		public final MapOperateManager<K, V> delete(MapPredicateVisitor<K, V> visitor) {
			return delete(null, visitor);
		}

	}

}
