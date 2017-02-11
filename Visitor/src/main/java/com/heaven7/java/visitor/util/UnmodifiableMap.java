package com.heaven7.java.visitor.util;

import java.util.List;

import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.internal.InternalUtil;
/**
 * a read-only map that wrap a {@link Map}, if you use some write methods must cause 
 * {@linkplain UnsupportedOperationException}.
 * @author heaven7
 *
 * @param <K> the key type
 * @param <V> the value type
 * @since 1.0.3
 */
public class UnmodifiableMap<K,V> implements Map<K, V> {
	
	private final Map<K, V> mMap;
	
	public UnmodifiableMap(Map<K, V> mMap) {
		super();
		this.mMap = mMap;
	}

	@Override
	public int size() {
		return mMap.size();
	}

	@Override
	public void put(K key, V value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove(K key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public V get(K key) {
		return mMap.get(key);
	}

	@Override
	public boolean containsKey(K key) {
		return mMap.containsKey(key);
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void putAll(java.util.Map<? extends K, ? extends V> map) {
		throw new UnsupportedOperationException();
	}

	@Override
	public V replace(K key, V value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<KeyValuePair<K, V>> getKeyValues() {
		return mMap.getKeyValues();
	}

	@Override
	public List<K> keys() {
		return mMap.keys();
	}

	@Override
	public List<V> values() {
		return mMap.values();
	}

	@Override
	public java.util.Map<K, V> toNormalMap() {
		return InternalUtil.unmodifiable(mMap.toNormalMap());
	}

	@Override
	public boolean isEmpty() {
		return mMap.isEmpty();
	}

}
