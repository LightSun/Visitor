package com.heaven7.java.visitor.util;

import java.util.List;

import com.heaven7.java.visitor.collection.KeyValuePair;

/**
 * like traditional map({@linkplain java.util.Map}). but add some useful methods.
 * @author heaven7
 *
 * @param <K> the key type
 * @param <V> the value type.
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
	
	//=================================================//
	
	V replace(K key, V value);
	
	boolean replace(K key, V oldValue, V newValue);

	List<KeyValuePair<K, V>> getKeyValues();
	
	List<K> keys();
	
	List<V> values();
	
	//=================================================//
	java.util.Map<K, V> toNormalMap();
	
	boolean isEmpty();
	
}
