package com.heaven7.java.visitor.util;

import java.util.ArrayList;
import java.util.List;

import com.heaven7.java.visitor.collection.KeyValuePair;

public class SparseArray2Map<E> extends AbstractMap<Integer, E>{
	
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
		for(int i = size - 1  ; i >=0 ; i--){
			list.add(KeyValuePair.create(mMap.keyAt(i), mMap.valueAt(i)));
		}
		return list;
	}

}
