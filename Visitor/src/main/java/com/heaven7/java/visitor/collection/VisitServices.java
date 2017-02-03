package com.heaven7.java.visitor.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.heaven7.java.visitor.util.Map2Map;
import com.heaven7.java.visitor.util.SparseArray;
import com.heaven7.java.visitor.util.SparseArray2Map;

/**
 * like executors. we use all api start in here.
 * 
 * @author heaven7
 *
 */
public final class VisitServices {

	/**
	 * create {@linkplain CollectionVisitServiceImpl} from the target
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
	 * create {@linkplain CollectionVisitServiceImpl} from the target list.<br>
	 * <b>Note: don't use {@linkplain Arrays#asList(Object...)} in here. </b>
	 * 
	 * @param <T>
	 *            the type
	 * @param list
	 *            the target list.
	 * @return an instance of {@linkplain CollectionVisitServiceImpl}
	 */
	public static <T> CollectionVisitService<T> from(List<T> list) {
		return new ListVisitService<T>(list);
	}

	/**
	 * create an instance of {@linkplain MapVisitService} from target map.
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param map the map
	 * @return an instance of {@linkplain MapVisitService}
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

}
