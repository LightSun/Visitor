package com.heaven7.java.visitor.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.heaven7.java.visitor.collection.KeyValuePair;

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
			mList = new ArrayList<>();
		}else{
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
		if(mKeys == null){
			mKeys = new ArrayList<>();
		}else{
			mKeys.clear();
		}
		final List<K> list = mKeys;
		list.addAll(mMap.keySet());
		return list;
	}

	@Override
	public List<V> values() {
		if(mValues == null){
			mValues = new ArrayList<V>();
		}else{
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

}
