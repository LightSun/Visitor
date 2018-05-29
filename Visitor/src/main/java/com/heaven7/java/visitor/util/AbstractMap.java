package com.heaven7.java.visitor.util;

import com.heaven7.java.visitor.collection.KeyValuePair;

import java.util.Collection;
import java.util.List; /**
 * a base impl of {@linkplain com.heaven7.java.visitor.util.Map}
 * @author heaven7
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public abstract class AbstractMap<K,V> implements Map<K,V> {
	
	@Override
	public V replace(K key, V value) {
	    V curValue;
        if (((curValue = get(key)) != null) || containsKey(key)) {
            put(key, value);
        }
        return curValue;
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue) {
	    Object curValue = get(key);
        if (!Predicates.equals(curValue, oldValue) ||
            (curValue == null && !containsKey(key))) {
            return false;
        }
        put(key, newValue);
		return true;
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		putAll(map.toNormalMap());
	}
	@Override
	public void putPairs(Collection<KeyValuePair<K, V>> pairs) {
        for(KeyValuePair<K, V> pair : pairs){
        	put(pair.getKey(), pair.getValue());
		}
	}
	@Override
	public void putPairs2(Collection<KeyValuePair<V, K>> pairs) {
		for(KeyValuePair<V, K> pair : pairs){
			put(pair.getValue(), pair.getKey());
		}
	}

	@Override
	public void copyTo(final Map<K, V> out) {
		startTravel(new MapTravelCallback<K, V>() {
			@Override
			public void onTravel(K key, V value) {
				out.put(key, value);
			}
		});
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}
	
	@Override
	public String toString() {
		return toNormalMap().toString();
	}

}
