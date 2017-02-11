package com.heaven7.java.visitor.internal;

import static com.heaven7.java.visitor.collection.Operation.OP_DELETE;
import static com.heaven7.java.visitor.collection.Operation.OP_FILTER;
import static com.heaven7.java.visitor.collection.Operation.OP_INSERT;
import static com.heaven7.java.visitor.collection.Operation.OP_TRIM;
import static com.heaven7.java.visitor.collection.Operation.OP_UPDATE;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import com.heaven7.java.visitor.collection.CollectionVisitService;
import com.heaven7.java.visitor.collection.MapVisitService;
import com.heaven7.java.visitor.collection.VisitServices;
/**
 * this class only used internal.
 * @author heaven7
 *
 */
public final class InternalUtil {

	/**
	 * get the collection visit service.
	 * @param <T> the element type.
	 * @param list indicate is list or not.
	 * @param collection the collection
	 * @return CollectionVisitService
	 */
	public static <T> CollectionVisitService<T> getVisitService(boolean list,
			Collection<T> collection) {
		return list ? VisitServices.from((List<T>) collection) 
				: VisitServices.from(collection) ;
	}
	
	/**
	 * get the map visit service.
	 * @param <K> the key type.
	 * @param <V> the value type.
	 * @param sorted indicate is sorted or not.
	 * @param map the map
	 * @return CollectionVisitService
	 */
	public static <K, V> MapVisitService<K, V> getMapVisitService(boolean sorted,
			Map<K,V> map) {
		return sorted ? VisitServices.from((SortedMap<K, V>) map) 
				: VisitServices.from(map) ;
	}

	public static <K, V> Map<K, V> newMap(boolean sorted) {
		return sorted ? new TreeMap<K, V>() : new HashMap<K, V>();
	}

	/**
	 * auto Adaptation List, NavigableSet, SortedSet, Set and Collection.
	 * 
	 * @param <T>
	 *            the element type
	 * @param collection
	 *            the target collection
	 * @return a unmodifiable collection
	 */
	public static <T> Collection<T> unmodifiable(Collection<T> collection) {
		if (collection instanceof List) {
			return Collections.unmodifiableList((List<? extends T>) collection);
		} else if (collection instanceof NavigableSet) {
			return Collections.unmodifiableNavigableSet((NavigableSet<T>) collection);
		} else if (collection instanceof SortedSet) {
			return Collections.unmodifiableSortedSet((SortedSet<T>) collection);
		} else if (collection instanceof Set) {
			return Collections.unmodifiableSet((Set<T>) collection);
		}
		return Collections.unmodifiableCollection(collection);
	}

	public static <K, V> Map<K, V> unmodifiable(Map<K, V> map) {
		if (map instanceof NavigableMap) {
			return Collections.unmodifiableNavigableMap((NavigableMap<K, ? extends V>) map);
		} else if (map instanceof SortedMap) {
			return Collections.unmodifiableSortedMap((SortedMap<K, ? extends V>) map);
		}
		return Collections.unmodifiableMap(map);
	}

	public static String op2String(int op) {
		switch (op) {
		case OP_DELETE:
			return "OP_DELETE";

		case OP_FILTER:
			return "OP_FILTER";

		case OP_UPDATE:
			return "OP_UPDATE";

		case OP_INSERT:
			return "OP_INSERT";

		case OP_TRIM:
			return "OP_TRIM";
		default:
			return null;
		}
	}

}
