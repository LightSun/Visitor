package com.heaven7.java.visitor.util;

import java.util.List;

import com.heaven7.java.visitor.collection.KeyValuePair;

public interface Map<K, V> {

	int size();
	
	void put(K key, V value);
	
	V replace(K key, V value);
	
	boolean replace(K key, V oldValue, V newValue);

	void remove(K key);
	
	V get(K key);
	
	boolean containsKey(K key);
	
	List<KeyValuePair<K, V>> getKeyValues();
	
}
