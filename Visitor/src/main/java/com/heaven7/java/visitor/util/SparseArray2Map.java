package com.heaven7.java.visitor.util;

import com.heaven7.java.visitor.collection.KeyValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SparseArray2Map<E> extends AbstractMap<Integer, E> {

	private final SparseArray<E> mMap;
	private List<KeyValuePair<Integer, E>> mTempPairs;
	private List<Integer> mKeys;
	private Map<Integer, E> mNormalMap;

	public SparseArray2Map(SparseArray<E> mMap) {
		super();
		this.mMap = mMap;
	}

	@Override
	public int size() {
		return mMap.size();
	}

	@Override
	public void put(Integer key, E value) {
		mMap.put(key, value);
	}

	@Override
	public void remove(Integer key) {
		mMap.remove(key);
	}

	@Override
	public E get(Integer key) {
		return mMap.get(key);
	}

	@Override
	public boolean containsKey(Integer key) {
		return mMap.indexOfKey(key) >= 0;
	}

	@Override
	public List<KeyValuePair<Integer, E>> getKeyValues() {
		if (mTempPairs == null) {
			mTempPairs = new ArrayList<KeyValuePair<Integer, E>>();
		} else {
			mTempPairs.clear();
		}
		final List<KeyValuePair<Integer, E>> list = this.mTempPairs;
		final SparseArray<E> mMap = this.mMap;
		final int size = mMap.size();
		for (int i = 0; i < size; i++) {
			list.add(KeyValuePair.create(mMap.keyAt(i), mMap.valueAt(i)));
		}
		return list;
	}

	@Override
	public List<Integer> keys() {
		if (mKeys == null) {
			mKeys = new ArrayList<Integer>();
		} else {
			mKeys.clear();
		}
		final List<Integer> list = mKeys;
		final SparseArray<E> mMap = this.mMap;
		final int size = mMap.size();
		for (int i = 0; i < size; i++) {
			list.add(mMap.keyAt(i));
		}
		return list;
	}

	@Override
	public List<E> values() {
		return mMap.getValues();
	}

	@Override
	public Map<Integer, E> toNormalMap() {
		if (mNormalMap == null) {
			mNormalMap = new HashMap<Integer, E>();
		} else {
			mNormalMap.clear();
		}
		final Map<Integer, E> map = mNormalMap;
		final SparseArray<E> mMap = this.mMap;
		final int size = mMap.size();
		for (int i = size - 1; i >= 0; i--) {
			map.put(mMap.keyAt(i), mMap.valueAt(i));
		}
		return map;
	}

	@Override
	public void clear() {
		mMap.clear();
	}

	@Override
	public void putAll(Map<? extends Integer, ? extends E> map) {
		final SparseArray<E> mMap = this.mMap;
		for (Entry<? extends Integer, ? extends E> en : map.entrySet()) {
			mMap.put(en.getKey(), en.getValue());
		}
	}

	@Override
	public boolean isSorted() {
		return true;
	}

	@Override
	public void startTravel(com.heaven7.java.visitor.util.Map.MapTravelCallback<Integer, E> travelCallback) {
		final SparseArray<E> mMap = this.mMap;
		final int size = mMap.size();
		for (int i = size - 1; i >= 0; i--) {
			travelCallback.onTravel(mMap.keyAt(i), mMap.valueAt(i));
		}
	}

}
