package com.heaven7.java.visitor.collection;

import com.heaven7.java.visitor.util.Map2Map;
import com.heaven7.java.visitor.util.SparseArray;
import com.heaven7.java.visitor.util.SparseArray2Map;

import java.util.*;

/**
 * like executors. we use all APIs start in here.
 * 
 * @author heaven7
 * @see CollectionVisitService
 * @see ListVisitService
 * @see MapVisitService
 */
public final class VisitServices {

	/*
	 * create an obserable collection service.
	 * @param <T> the element type of collection.
	 * @return ObservableCollectionService.
	 * @see ObservableCollectionService
	 * @since 2.0.0
	 */
	/*public static <T> ObservableCollectionService<T> newObserableService() {
		return newObserableService(new ArrayList<T>());
	}*/
	/*
	 * create an obserable collection service.
	 * @param <T> the element type of collection.
	 * @param co the source collection
	 * @return ObservableCollectionService.
	 * @see ObservableCollectionService
	 * @since 2.0.0
	 */
	/*public static <T> ObservableCollectionService<T> newObserableService(Collection<T> co) {
		return new ObservableCollectionService<T>(co);
	}*/
	/**
	 * create MapVisitService with empty capacity.
	 * @param <K> the key type
	 * @param <V> the value type
	 * @return an instance of {@linkplain MapVisitService}
	 * @since  1.1.8
	 */
	public static <K,V> MapVisitService<K,V> newMapService() {
		return from(new HashMap<K,V>());
	}

	/**
	 * create ListVisitService with empty capacity.
	 * @param <T> the element type
	 * @return an instance of {@linkplain ListVisitService}
	 * @since  1.1.8
	 */
	public static <T> ListVisitService<T> newListService() {
          return from(new ArrayList<T>());
	}

	/**
	 * create {@linkplain CollectionVisitService} from the target
	 * collection. <br>
	 * <b>Note: don't use {@linkplain Arrays#asList(Object...)} in here. </b>
	 * 
	 * @param <T>
	 *            the type
	 * @param collection
	 *            the target collection, such as set.
	 * @return an instance of {@linkplain CollectionVisitServiceImpl}
	 */
	public static <T> CollectionVisitService<T> from(Collection<T> collection) {
		return new CollectionVisitServiceImpl<T>(collection);
	}

	/**
	 * create {@linkplain ListVisitService} from the target list.<br>
	 * <b>Note: don't use {@linkplain Arrays#asList(Object...)} in here. </b>
	 * 
	 * @param <T>
	 *            the type
	 * @param list
	 *            the target list.
	 * @return an instance of {@linkplain CollectionVisitServiceImpl}
	 */
	public static <T> ListVisitService<T> from(List<T> list) {
		return new ListVisitServiceImpl<T>(list);
	}

	/**
	 * create {@linkplain ListVisitService} from the target array.<br>
	 * @param <T>
	 *            the type
	 * @param ts
	 *            the target array.
	 * @return an instance of {@linkplain CollectionVisitServiceImpl}
	 * @since 1.3.1
	 */
	public static <T> ListVisitService<T> from(T... ts) {
		return new ListVisitServiceImpl<T>(new ArrayList<T>(Arrays.asList(ts)));
	}

	/**
	 * create key value list service from pairs.
	 * @param list the pairs
	 * @param <K> the key type of pair
	 * @param <V> the value type of pair
	 * @return the key value list service
	 * @since 1.2.71
	 */
	public static <K,V> KeyValueListService<K,V> fromPairs(List<KeyValuePair<K,V>> list) {
		return new KeyValueListService<K,V>(list);
	}

	/**
	 * create an instance of {@linkplain MapVisitService} from target map.
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param map the map
	 * @return an instance of {@linkplain MapVisitService}
	 * @see SortedMap
	 * @see #from(com.heaven7.java.visitor.util.Map)
	 * @see #from(SparseArray)
	 */
	public static <K, V> MapVisitService<K, V> from(Map<K, V> map) {
		return new MapVisitServiceImpl<K, V>(new Map2Map<K, V>(map));
	}
	
	
	/**
	 * create an instance of {@linkplain MapVisitService} from target {@linkplain SparseArray}.
	 * @param <V> the value type or the element type
	 * @param array the map (key is Integer, value is V)
	 * @return an instance of {@linkplain MapVisitService}
	 */
	public static <V> MapVisitService<Integer, V> from(SparseArray<V> array) {
		return new MapVisitServiceImpl<Integer,V>(new SparseArray2Map<V>(array));
	}
	
	/**
	 * create an instance of {@linkplain MapVisitService} from target {@linkplain com.heaven7.java.visitor.util.Map}.
	 * @param <K> the key type 
	 * @param <V> the value type 
	 * @param map the map
	 * @return an instance of {@linkplain MapVisitService}
	 * @since 1.0.1
	 */
	public static <K, V> MapVisitService<K, V> from(com.heaven7.java.visitor.util.Map<K, V> map) {
		return new MapVisitServiceImpl<K,V>(map);
	}

}
