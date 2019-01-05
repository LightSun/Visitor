package com.heaven7.java.visitor.internal;

import static com.heaven7.java.visitor.collection.Operation.OP_DELETE;
import static com.heaven7.java.visitor.collection.Operation.OP_FILTER;
import static com.heaven7.java.visitor.collection.Operation.OP_INSERT;
import static com.heaven7.java.visitor.collection.Operation.OP_TRIM;
import static com.heaven7.java.visitor.collection.Operation.OP_UPDATE;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import com.heaven7.java.visitor.ThrowableVisitor;
import com.heaven7.java.visitor.collection.CollectionVisitService;
import com.heaven7.java.visitor.collection.MapVisitService;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.java.visitor.util.VisitException;

/**
 * this class only used internal.
 * 
 * @author heaven7
 */
public final class InternalUtil {

	public static void checkCast(Class<?> parent, Class<?> child){
		if(!parent.isAssignableFrom(child)){
			throw new ClassCastException("type '"+ child.getName() + "' can't cast to '"
					+ parent.getName() + "'");
		}
	}

	public static void processThrowable(Throwable e, ThrowableVisitor tv) throws VisitException {
		if (tv != null) {
			tv.visit(e);
		} else {
			throw new VisitException(e);
		}
	}

	/**
	 * get the right {@linkplain CollectionVisitService}. often called by
	 * {@linkplain MapVisitService}.
	 * 
	 * @param <T>
	 *            the element type
	 * @param list
	 *            the list data
	 * @param c
	 *            the sort Comparator, can be null.
	 * @param sort
	 *            represent should regard as sorted
	 *            {@linkplain CollectionVisitService}. if false
	 *            {@linkplain #getVisitService(List, Comparator)} will be called
	 *            or else sort and return {@linkplain VisitServices#from(List)}
	 * @return an instance of {@linkplain CollectionVisitService}.
	 */
	public static <T> CollectionVisitService<T> getVisitService(List<T> list, Comparator<? super T> c, boolean sort) {
		// if should not sort return default CollectionVisitService .
		if (!sort) {
			return getVisitService(list, c);
		} else {
			if (c != null) {
				Collections.sort(list, c);
			}
			return VisitServices.from(list);
		}
	}

	/**
	 * get the collection visit service. if the Comparator is null return
	 * 'VisitServices.from((Collection) list)' or else return
	 * 'VisitServices.from(list)';
	 * 
	 * @param <T>
	 *            the element type.
	 * @param c
	 *            sort comparator , can be null.
	 * @param list
	 *            the collection
	 * @return CollectionVisitService
	 */
	public static <T> CollectionVisitService<T> getVisitService(List<T> list, Comparator<? super T> c) {
		if (c != null) {
			Collections.sort(list, c);
			return VisitServices.from(list);
		} else {
			return VisitServices.from((Collection<T>) list);
		}
	}

	/**
	 * create a map instance.
	 * 
	 * @param <K>
	 *            the key type.
	 * @param <V>
	 *            the value type.
	 * @param comparator
	 *            the key comparator , can be null.
	 * @return TreeMap or HashMap determined by target comparator.
	 */
	public static <K, V> Map<K, V> newMap(Comparator<? super K> comparator) {
		return comparator != null ? new TreeMap<K, V>(comparator) : new HashMap<K, V>();
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
		}
		/*
		 * else if (collection instanceof NavigableSet) {
		 * if(JDKVersion.isJdK18()){ //jdk1.8 return
		 * Collections.unmodifiableNavigableSet((NavigableSet<T>) collection); }
		 * }
		 */
		else if (collection instanceof SortedSet) {
			return Collections.unmodifiableSortedSet((SortedSet<T>) collection);
		} else if (collection instanceof Set) {
			return Collections.unmodifiableSet((Set<? extends T>) collection);
		}
		return Collections.unmodifiableCollection(collection);
	}

	public static <K, V> Map<K, V> unmodifiable(Map<K, V> map) {
		// jdk1.8
		/*
		 * if (map instanceof NavigableMap) { return
		 * Collections.unmodifiableNavigableMap((NavigableMap<K, ? extends V>)
		 * map); } else
		 */
		if (map instanceof SortedMap) {
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
