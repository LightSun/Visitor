package com.heaven7.java.visitor.util;

import com.heaven7.java.visitor.collection.KeyValuePair;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * like traditional map({@linkplain java.util.Map}). but add some useful
 * methods.
 * 
 * @author heaven7
 *
 * @param <K>
 *            the key type
 * @param <V>
 *            the value type.
 */
public interface Map<K, V> {

	int size();

	void put(K key, V value);

	void remove(K key);

	V get(K key);

	boolean containsKey(K key);

	void clear();

	void putAll(Map<? extends K, ? extends V> map);

	void putAll(java.util.Map<? extends K, ? extends V> map);

	/* @since 1.2.0 */
	void putPairs(Collection<KeyValuePair<K, V>> pairs);
	/* @since 1.2.0 */
	void putPairs2(Collection<KeyValuePair<V, K>> pairs);

	// =================================================//

	V replace(K key, V value);

	boolean replace(K key, V oldValue, V newValue);

	List<KeyValuePair<K, V>> getKeyValues();

	List<K> keys();

	List<V> values();

	/* @since 1.2.0 */
	void copyTo(Map<K, V> out);

	K getOneKey();

	// =================================================//
	/**
	 * to normal map
	 * 
	 * @return normal map
	 */
	java.util.Map<K, V> toNormalMap();

	boolean isEmpty();

	/**
	 * @return is sorted or not.
	 * @since 1.1.0
	 */
	boolean isSorted();
	
	/**
	 * {@inheritDoc}
	 */
	String toString();

	// ==================================================//
	/**
	 * start travel key-value pairs directly. this may cause {@linkplain ConcurrentModificationException}, so you should 
	 * care about it.
	 * @param travelCallback the travel callback
	 * @since 1.1.3
	 */
	void startTravel(MapTravelCallback<K,V>  travelCallback);

	/**
	 * a interface used for travel key-values of map.
	 * @author heaven7
	 *
	 * @param <K> the key type.
	 * @param <V> the value type.
	 * @since 1.1.3
	 */
	interface MapTravelCallback<K, V> {
		/**
		 * called when travel the target key-value.
		 * @param key the key
		 * @param value the value
		 */
		void onTravel(K key, V value);
	}
}
