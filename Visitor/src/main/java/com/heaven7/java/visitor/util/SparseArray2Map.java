package com.heaven7.java.visitor.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.heaven7.java.visitor.collection.KeyValuePair;

public class SparseArray2Map<E> extends AbstractMap<Integer, E> {

	private final SparseArray<E> mMap;
	private List<KeyValuePair<Integer, E>> mList;

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
		if (mList == null) {
			mList = new ArrayList<>();
		}
		final List<KeyValuePair<Integer, E>> list = this.mList;
		final SparseArray<E> mMap = this.mMap;
		final int size = mMap.size();
		for (int i = 0; i < size; i++) {
			list.add(KeyValuePair.create(mMap.keyAt(i), mMap.valueAt(i)));
		}
		return list;
	}

	@Override
	public List<Integer> keys() {
		final List<Integer> list = new ArrayList<Integer>();
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
		final Map<Integer, E> map = new HashMap<Integer, E>();
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

}
