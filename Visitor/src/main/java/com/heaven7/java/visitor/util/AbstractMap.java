package com.heaven7.java.visitor.util;

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
	public boolean isEmpty() {
		return size() == 0;
	}
	

}
