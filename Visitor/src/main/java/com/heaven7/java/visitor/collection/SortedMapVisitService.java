package com.heaven7.java.visitor.collection;

import com.heaven7.java.visitor.util.Map;

/*public*/ class SortedMapVisitService<K,V> extends MapVisitServiceImpl<K, V> {

	public SortedMapVisitService(Map<K, V> mMap) {
		super(mMap);
	}
	
	@Override
	protected boolean isSorted() {
		return true;
	}

}
