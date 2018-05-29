package com.heaven7.java.visitor.util;

import java.util.Collection;
import java.util.List;

import com.heaven7.java.visitor.collection.KeyValuePair;
/**
 * synchronized map.
 * @author heaven7
 *
 * @param <K> the key type
 * @param <V> the value type
 * @since 1.1.6
 */
public class SynchronizedMap<K, V> implements Map<K, V> {

	private final Map<K, V> mBase;
	private final Object mLock;
	
	public SynchronizedMap(Map<K, V> mBase){
		this(mBase, new Object());
	}
	public SynchronizedMap(Map<K, V> mBase, Object lock) {
		super();
		this.mBase = mBase;
		this.mLock = lock;
	}

	@Override
	public int size() {
		synchronized (mLock) {
			return mBase.size();
		}
	}

	@Override
	public void put(K key, V value) {
		synchronized (mLock) {
			mBase.put(key, value);
		}
	}

	@Override
	public void remove(K key) {
		synchronized (mLock) {
			mBase.remove(key);
		}
	}

	@Override
	public V get(K key) {
		synchronized (mLock) {
			return mBase.get(key);
		}
	}

	@Override
	public boolean containsKey(K key) {
		synchronized (mLock) {
			return mBase.containsKey(key);
		}
	}

	@Override
	public void clear() {
		synchronized (mLock) {
			mBase.clear();
		}
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		synchronized (mLock) {
			mBase.putAll(map);
		}
	}

	@Override
	public void putAll(java.util.Map<? extends K, ? extends V> map) {
		synchronized (mLock) {
			mBase.putAll(map);
		}
	}

	@Override
	public void putPairs(Collection<KeyValuePair<K, V>> pairs) {
		synchronized (mLock) {
			mBase.putPairs(pairs);
		}
	}

	@Override
	public void putPairs2(Collection<KeyValuePair<V, K>> pairs) {
		synchronized (mLock) {
			mBase.putPairs2(pairs);
		}
	}

	@Override
	public V replace(K key, V value) {
		synchronized (mLock) {
			return mBase.replace(key, value);
		}
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue) {
		synchronized (mLock) {
			return mBase.replace(key, oldValue, newValue);
		}
	}

	@Override
	public List<KeyValuePair<K, V>> getKeyValues() {
		synchronized (mLock) {
			return mBase.getKeyValues();
		}
	}

	@Override
	public List<K> keys() {
		synchronized (mLock) {
			return mBase.keys();
		}
	}

	@Override
	public List<V> values() {
		synchronized (mLock) {
			return mBase.values();
		}
	}
	@Override
	public void copyTo(Map<K, V> out) {
		synchronized (mLock) {
			mBase.copyTo(out);
		}
	}

	@Override
	public java.util.Map<K, V> toNormalMap() {
		synchronized (mLock) {
			return mBase.toNormalMap();
		}
	}

	@Override
	public boolean isEmpty() {
		synchronized (mLock) {
			return mBase.isEmpty();
		}
	}

	@Override
	public boolean isSorted() {
		synchronized (mLock) {
			return mBase.isSorted();
		}
	}

	@Override
	public void startTravel(com.heaven7.java.visitor.util.Map.MapTravelCallback<K, V> travelCallback) {
		synchronized (mLock) {
			mBase.startTravel(travelCallback);
		}
	}
	
	@Override
	public String toString() {
		return mBase.toString();
	}

}
