package com.heaven7.java.visitor.collection;

import java.util.ArrayList;
import java.util.List;

import com.heaven7.java.visitor.MapPredicateVisitor;
import com.heaven7.java.visitor.MapResultVisitor;
import com.heaven7.java.visitor.anno.Nullable;
import com.heaven7.java.visitor.internal.state.MapIterateState;
import com.heaven7.java.visitor.util.Map;

public class MapVisitServiceImpl<K, V> extends AbstractMapVisitService<K, V> {
	
	private final IterationInfo mIterationInfo = new IterationInfo();
	private final Map<K, V> mMap;

	public MapVisitServiceImpl(Map<K, V> mMap) {
		super();
		this.mMap = mMap;
	}
	
	@Override
	public <R> List<R> visitForResultList(Object param, MapPredicateVisitor<K, V> predicate,
			MapResultVisitor<K, V, R> resultVisitor,@Nullable List<R> out) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <R> R visitForResult(Object param, MapPredicateVisitor<K, V> predicate,
			MapResultVisitor<K, V, R> resultVisitor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KeyValuePair<K, V>> visitForQueryList(Object param, MapPredicateVisitor<K, V> predicate, 
			@Nullable List<KeyValuePair<K, V>> out) {
		if( out == null ){
			out = new ArrayList<>();
		}
		final IterationInfo info = initAndGetIterationInfo();
		MapIterateState.<K,V>multipleState().visit(mMap, hasExtraOperations(), getOperateInterceptor(), 
				info, param, predicate, out);
		handleAtLast(mMap, param, info);
		return out;
	}

	@Override
	public KeyValuePair<K, V> visitForQuery(Object param, MapPredicateVisitor<K, V> predicate) {
		final IterationInfo info = initAndGetIterationInfo();
		KeyValuePair<K, V> result = MapIterateState.<K,V>singleState().visit(mMap, hasExtraOperations(), getOperateInterceptor(), 
				info, param, predicate, null);
		handleAtLast(mMap, param, info);
		return result;
	}

	private IterationInfo initAndGetIterationInfo() {
		int size = mMap.size();
		mIterationInfo.setOriginSize(size);
		mIterationInfo.setCurrentSize(size);
		return mIterationInfo;
	}

}
