package com.heaven7.java.visitor.util;

import com.heaven7.java.visitor.collection.KeyValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * an instance of {@linkplain com.heaven7.java.visitor.util.Map}. convert
 * {@linkplain Map} to {@linkplain com.heaven7.java.visitor.util.Map}
 * 
 * @author heaven7
 *
 * @param <K>
 *            the key type
 * @param <V>
 *            the value type
 */
public class Map2Map<K, V> extends AbstractMap<K, V> {

	private final Map<K, V> mMap;
	private List<KeyValuePair<K, V>> mList;
	private List<K> mKeys;
	private List<V> mValues;

	public Map2Map(Map<K, V> mMap) {
		super();
		this.mMap = mMap;
	}

	@Override
	public int size() {
		return mMap.size();
	}

	@Override
	public void put(K key, V value) {
		mMap.put(key, value);
	}

	@Override
	public void remove(K key) {
		mMap.remove(key);
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
	public List<KeyValuePair<K, V>> getKeyValues() {
		if (mList == null) {
			mList = new ArrayList<KeyValuePair<K, V>>();
		} else {
			mList.clear();
		}
		final List<KeyValuePair<K, V>> list = this.mList;
		for (Entry<K, V> en : mMap.entrySet()) {
			list.add(KeyValuePair.create(en.getKey(), en.getValue()));
		}
		return list;
	}

	@Override
	public List<K> keys() {
		if (mKeys == null) {
			mKeys = new ArrayList<K>();
		} else {
			mKeys.clear();
		}
		final List<K> list = mKeys;
		list.addAll(mMap.keySet());
		return list;
	}

	@Override
	public List<V> values() {
		if (mValues == null) {
			mValues = new ArrayList<V>();
		} else {
			mValues.clear();
		}
		final List<V> list = mValues;
		list.addAll(mMap.values());
		return list;
	}

	@Override
	public Map<K, V> toNormalMap() {
		return mMap;
	}

	@Override
	public void clear() {
		mMap.clear();
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		mMap.putAll(map);
	}

	@Override
	public boolean isSorted() {
		return Predicates.isSortedMap(mMap);
	}

	@Override
	public void startTravel(com.heaven7.java.visitor.util.Map.MapTravelCallback<K, V> travelCallback) {
		for (Entry<K, V> en : mMap.entrySet()) {
			travelCallback.onTravel(en.getKey(), en.getValue());
		}
	}

	@Override
	public K getOneKey() {
		Set<K> set = mMap.keySet();
		if(set.isEmpty()){
			return null;
		}
		return new ArrayList<>(set).get(0);
	}
}
