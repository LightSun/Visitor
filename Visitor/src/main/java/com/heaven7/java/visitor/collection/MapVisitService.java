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
/**
 * visit service of common map . <br>here is a demo used to query a key-value .
 * <pre>
    public void testQuery() {
		  KeyValuePair<String, Integer> pair = service.beginOperateManager()
			.delete(new MapPredicateVisitor<String, Integer>() {
				@Override
				public Boolean visit(KeyValuePair<String, Integer> pair, Object param) {
					assertEquals(param, "123");
					return pair.getValue() == 2;
				}
			}).end().visitForQuery("123", new MapPredicateVisitor<String, Integer>() {
				@Override
				public Boolean visit(KeyValuePair<String, Integer> pair, Object param) {
					assertEquals(param, "123");
					return pair.getValue() == 5;
				}
			});
	
		  assertEquals(pair.getValue().intValue(), 5);
		  assertEquals(map.size(), size - 1);
	}
  </pre> <br>the more to see MapVisitServiceTest <br>
 * @author heaven7
 *
 * @param <K> the key type
 * @param <V> the value type.
 * @see {@linkplain AbstractMapVisitService}
 * @see {@linkplain MapVisitServiceImpl}
 */
public interface MapVisitService<K, V>{
	
	
	/**
	 * transform to {@linkplain CollectionVisitService} by keys .
	 * @return a {@linkplain CollectionVisitService}.
	 * @since 1.0.2
	 */
	CollectionVisitService<K> transformToCollectionByKeys();
	
	/**
	 * transform to {@linkplain CollectionVisitService} by values .
	 * @return a {@linkplain CollectionVisitService}.
	 * @since 1.0.2
	 */
	CollectionVisitService<V> transformToCollectionByValues();
	
	/**
	 * transform to {@linkplain CollectionVisitService} by target result visitor.
	 * @param <R> the result type
	 * @param resultVisitor  the result visitor
	 * @return a {@linkplain CollectionVisitService}.
	 * @since 1.0.2
	 * @see {@linkplain #transformToCollection(Object, MapResultVisitor)}
	 */
	<R> CollectionVisitService<R> transformToCollection(MapResultVisitor<K, V, R> resultVisitor);
	
	/**
	 * transform to {@linkplain CollectionVisitService} by target result visitor.
	 * @param <R> the result type
	 * @param param  the parameter,can be null.
	 * @param resultVisitor  the result visitor
	 * @return a {@linkplain CollectionVisitService}.
	 * @since 1.0.2
	 */
	<R> CollectionVisitService<R> transformToCollection(@Nullable Object param , MapResultVisitor<K, V, R> resultVisitor);
	
	/**
	 * transform to new {@linkplain MapVisitService} as values.
	 * @param <K2> the new key type
	 * @param param  the parameter,can be null.
	 * @param keyVisitor  the key visitor
	 * @return a {@linkplain MapVisitService}.
	 * @since 1.0.2
	 */
	<K2> MapVisitService<K2, V> transformToMapAsValues(@Nullable Object param, MapResultVisitor<K, V, K2> keyVisitor);
	
	/**
	 * transform to new {@linkplain MapVisitService} as values.
	 * @param <K2> the new key type
	 * @param keyVisitor  the key visitor
	 * @return a {@linkplain MapVisitService}.
	 * @since 1.0.2
	 * @see {@linkplain #transformToMapAsValues(Object, MapResultVisitor)}
	 */
	<K2> MapVisitService<K2, V> transformToMapAsValues(MapResultVisitor<K, V, K2> keyVisitor);
	
	/**
	 * transform to new {@linkplain MapVisitService} as keys.
	 * @param <V2> the new value type
	 * @param param  the parameter,can be null.
	 * @param valueVisitor  the value visitor
	 * @return a {@linkplain MapVisitService}.
	 * @since 1.0.2
	 */
	<V2> MapVisitService<K, V2> transformToMapAsKeys(@Nullable Object param, MapResultVisitor<K, V, V2> valueVisitor);
	
	/**
	 * transform to new {@linkplain MapVisitService} as keys.
	 * @param <V2> the new value type
	 * @param valueVisitor  the value visitor
	 * @return a {@linkplain MapVisitService}.
	 * @since 1.0.2
	 * @see {@linkplain #transformToMapAsKeys(Object, MapResultVisitor)}
	 */
	<V2> MapVisitService<K, V2> transformToMapAsKeys(MapResultVisitor<K, V, V2> valueVisitor);
	
	/**
	 * transform to new {@linkplain MapVisitService} by swap the key with value.
	 * @return a new {@linkplain MapVisitService}.
	 * @since 1.0.2
	 */
	MapVisitService<V, K> transformToMapBySwap();
	
	/**
	 * transform to new {@linkplain MapVisitService}.
	 * @param <K2> the new key type
	 * @param <V2> the new value type
	 * @param keyVisitor  the key visitor
	 * @param valueVisitor  the value visitor
	 * @return a {@linkplain MapVisitService}.
	 * @see {@linkplain #transformToMap(Object, MapResultVisitor, MapResultVisitor)}
	 * @since 1.0.2
	 */
	<K2,V2> MapVisitService<K2, V2> transformToMap(MapResultVisitor<K, V, K2> keyVisitor, MapResultVisitor<K, V, V2> valueVisitor);
	
	/**
	 * transform to new {@linkplain MapVisitService}.
	 * @param <K2> the new key type
	 * @param <V2> the new value type
	 * @param param  the parameter
	 * @param keyVisitor  the key visitor
	 * @param valueVisitor  the value visitor
	 * @return a {@linkplain MapVisitService}.
	 * @since 1.0.2
	 */
	<K2,V2> MapVisitService<K2, V2> transformToMap(@Nullable Object param, MapResultVisitor<K, V, K2> keyVisitor, MapResultVisitor<K, V, V2> valueVisitor);

	/**
	 * visit for result list and carry extra parameter
	 * @param <R> the result type
	 * @param param the extra parameter to carry to visitor.
	 * @param predicate the predicate visitor
	 * @param resultVisitor the result visitor.
	 * @param out the out list of all result, can be null.
	 * @return result list
	 */
	<R> List<R> visitForResultList(@Nullable Object param, MapPredicateVisitor<K, V> predicate,
			MapResultVisitor<K, V, R> resultVisitor, @Nullable List<R> out);
	
	/**
	 * visit for result list and carry extra parameter .With default predicate visitor which always return true.
	 * @param <R> the result type
	 * @param param the extra parameter to carry to visitor.
	 * @param resultVisitor the result visitor.
	 * @param out the out list of all result, can be null.
	 * @return result list
	 * @see {@linkplain #visitForResultList(Object, MapPredicateVisitor, MapResultVisitor, List)}
	 * @since 1.0.1
	 */
	<R> List<R> visitForResultList(@Nullable Object param, MapResultVisitor<K, V, R> resultVisitor, @Nullable List<R> out);
	
	/**
	 * visit for result list With default predicate visitor which always return true.
	 * @param <R> the result type
	 * @param resultVisitor the result visitor.
	 * @param out the out list of all result, can be null.
	 * @return result list
	 * @see {@linkplain #visitForResultList(Object, MapPredicateVisitor, MapResultVisitor, List)}
	 * @since 1.0.1
	 */
	<R> List<R> visitForResultList(MapResultVisitor<K, V, R> resultVisitor, @Nullable List<R> out);

	/**
	 * visit for result list but carry no extra parameter.
	 * @param <R> the result type
	 * @param predicate the predicate visitor
	 * @param resultVisitor the result visitor.
	 * @param out the out list of all result, can be null.
	 * @return the result list
	 * @see {@linkplain #visitForResultList(Object, MapPredicateVisitor, MapResultVisitor, List)}
	 */
	<R> List<R> visitForResultList(MapPredicateVisitor<K, V> predicate,
			MapResultVisitor<K, V, R> resultVisitor,@Nullable List<R> out);

	/**
	 * visit for result and carry extra parameter.
	 * @param <R> the result type
	 * @param param the extra parameter to carry to visitor.
	 * @param predicate the predicate visitor
	 * @param resultVisitor the result visitor.
	 * @return the result 
	 */
	<R> R visitForResult(@Nullable Object param, MapPredicateVisitor<K, V> predicate,
			MapResultVisitor<K, V, R> resultVisitor);

	/**
	 * visit for result , but carry no extra parameter.
	 * @param <R> the result type
	 * @param predicate the predicate visitor
	 * @param resultVisitor the result visitor.
	 * @return the result 
	 * @see {@linkplain #visitForResult(MapPredicateVisitor, MapResultVisitor)}
	 */
	<R> R visitForResult(MapPredicateVisitor<K, V> predicate,
			MapResultVisitor<K, V, R> resultVisitor);
	
	/**
	 * visit for query list key-value and carry extra parameter.
	 * @param param the extra parameter to carry to visitor.
	 * @param predicate the predicate visitor
	 * @param out the out list. can be null.
	 * @return the query result list
	 */
	List<KeyValuePair<K, V>> visitForQueryList(@Nullable Object param, MapPredicateVisitor<K, V> predicate, @Nullable List<KeyValuePair<K, V>> out);

	/**
	 * visit for query list key-value but don't carry extra parameter.
	 * @param predicate the predicate visitor
	 * @param out the out list. can be null.
	 * @return the query result list
	 * @see {@linkplain MapVisitService#visitForQueryList(Object, MapPredicateVisitor, List)}
	 */
	List<KeyValuePair<K, V>> visitForQueryList(MapPredicateVisitor<K, V> predicate,@Nullable List<KeyValuePair<K, V>> out);

	/**
	 * visit for query a key-value and carry extra parameter.
	 * @param param the extra parameter to carry to visitor.
	 * @param predicate the predicate visitor
	 * @return the query result 
	 */
	KeyValuePair<K, V> visitForQuery(@Nullable Object param, MapPredicateVisitor<K, V> predicate);

	/**
	 * visit for query a key-value ,but carry no extra parameter.
	 * @param predicate the predicate visitor
	 * @return the query result 
	 * @see {@linkplain #visitForQuery(Object, MapPredicateVisitor)}
	 */
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

	/**
	 * an {@linkplain OperateInterceptor} which used for map.
	 * @author heaven7
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 */
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

	/**
	 * the  operate manager of map.
	 * @author heaven7
	 *
	 * @param <K> the key type
	 * @param <V> the value type.
	 */
	public static abstract class MapOperateManager<K, V> {

		/**
		 * end the operate manager and return to {@linkplain MapVisitService}.
		 * @return {@linkplain MapVisitService}.
		 */
		public abstract MapVisitService<K, V> end();

		/**
		 * add a filter operation. but this can only set once.
		 * @param param the extra parameter
		 * @param visitor the predicate visitor
		 * @return this.
		 */
		public abstract MapOperateManager<K, V> filter(@Nullable Object param,
				MapPredicateVisitor<K, V> visitor);

		/**
		 * add a delete operation. but this can only set once.
		 * @param param the extra parameter
		 * @param visitor the predicate visitor
		 * @return this.
		 */
		public abstract MapOperateManager<K, V> delete(@Nullable Object param,
				MapPredicateVisitor<K, V> visitor);

		/**
		 * add a update operation.
		 * @param value a new value to update.
		 * @param param the extra parameter
		 * @param visitor the predicate visitor
		 * @return this.
		 */
		public abstract MapOperateManager<K, V> update(V value, @Nullable Object param,
				MapPredicateVisitor<K, V> visitor);

		/**
		 * add a insert-finally operation , it means that this operate will execute after iterate.
		 * @param newPair a new key-value to insert.
		 * @param param the extra parameter
		 * @param visitor the iterate visitor
		 * @return this.
		 */
		public abstract MapOperateManager<K, V> insertFinally(KeyValuePair<K, V> newPair, @Nullable Object param,
				MapIterateVisitor<K, V> visitor);

		/**
		 * add a insert-finally operation , it means that this operate will execute after iterate.
		 * @param newPairs a list of key-value to insert.
		 * @param param the extra parameter
		 * @param visitor the iterate visitor
		 * @return this.
		 */
		public abstract MapOperateManager<K, V> insertFinally(List<KeyValuePair<K, V>> newPairs, @Nullable Object param,
				MapIterateVisitor<K, V> visitor);

		/**
		 * add a trim operation , it will execute after iterate.can only set once.
		 * @param param the extra parameter
		 * @param visitor the trim visitor
		 * @return this.
		 */
		public abstract MapOperateManager<K, V> trim(@Nullable Object param,
				TrimMapVisitor<K, V> visitor);

		// ==========================================================

		/**
		 * add a trim operation , it will execute after iterate.can only set once.
		 * @param visitor the trim visitor
		 * @return this.
		 * @see {@linkplain #trim(Object, TrimMapVisitor)}.
		 */
		public final MapOperateManager<K, V> trim(TrimMapVisitor<K, V> visitor) {
			return trim(null, visitor);
		}

		/**
		 * add a insert-finally operation , it means that this operate will execute after iterate.
		 * @param newPairs a list of key-value to insert.
		 * @param visitor the iterate visitor
		 * @return this.
		 * @see {@linkplain #insertFinally(List, Object, MapIterateVisitor)}
		 */
		public final MapOperateManager<K, V> insertFinally(List<KeyValuePair<K, V>> newPairs,
				MapIterateVisitor<K, V> visitor) {
			return insertFinally(newPairs, null, visitor);
		}

		/**
		 * add a insert-finally operation , it means that this operate will execute after iterate.
		 * @param newPair a new key-value to insert.
		 * @param visitor the iterate visitor
		 * @return this.
		 * @see {@linkplain #insertFinally(KeyValuePair, Object, MapIterateVisitor)}
		 */
		public final MapOperateManager<K, V> insertFinally(KeyValuePair<K, V> pair,
				MapIterateVisitor<K, V> visitor) {
			return insertFinally(pair, null, visitor);
		}

		/**
		 * add a update operation.
		 * @param value a new value to update.
		 * @param visitor the predicate visitor
		 * @return this.
		 * @see {@linkplain #update(Object, Object, MapPredicateVisitor)}
		 */
		public final MapOperateManager<K, V> update(V value, MapPredicateVisitor<K, V> visitor) {
			return update(value, null, visitor);
		}

		/**
		 * add a filter operation. but this can only set once.
		 * @param visitor the predicate visitor
		 * @return this.
		 * {@linkplain #filter(Object, MapPredicateVisitor)}
		 */
		public final MapOperateManager<K, V> filter(MapPredicateVisitor<K, V> visitor) {
			return filter(null, visitor);
		}

		/**
		 * add a delete operation. but this can only set once.
		 * @param visitor the predicate visitor
		 * @return this.
		 * @see {@linkplain #delete(Object, MapPredicateVisitor)}
		 */
		public final MapOperateManager<K, V> delete(MapPredicateVisitor<K, V> visitor) {
			return delete(null, visitor);
		}
      }

}
