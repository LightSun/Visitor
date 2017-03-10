package com.heaven7.java.visitor.collection;

import com.heaven7.java.visitor.MapPredicateVisitor;
import com.heaven7.java.visitor.MapResultVisitor;
import com.heaven7.java.visitor.anno.Nullable;
import com.heaven7.java.visitor.internal.state.MapIterateState;
import com.heaven7.java.visitor.util.Map;

import java.util.ArrayList;
import java.util.List;
/**
 * an impl of MapVisitService.
 * @author heaven7
 *
 * @param <K> the key type
 * @param <V> the value type
 * @see {@linkplain MapVisitService}
 * @see {@linkplain AbstractMapVisitService}
 */
/*public*/ class MapVisitServiceImpl<K, V> extends AbstractMapVisitService<K, V> {

	private final IterationInfo mIterationInfo = new IterationInfo();

	public MapVisitServiceImpl(Map<K, V> mMap) {
		super(mMap);
	}

	@Override
	public <R> List<R> visitForResultList(Object param, MapPredicateVisitor<K, V> predicate,
			MapResultVisitor<K, V, R> resultVisitor, @Nullable List<R> out) {
		if (out == null) {
			out = new ArrayList<R>();
		}
		final IterationInfo info = initAndGetIterationInfo();
		MapIterateState.<K, V>multipleState().visitForResult(mMap, hasExtraOperationInIteration(), 
				getOperateInterceptor(), info, param, predicate, resultVisitor, out);
		handleAtLast(param, info);
		return out;
	}

	@Override
	public <R> R visitForResult(Object param, MapPredicateVisitor<K, V> predicate,
			MapResultVisitor<K, V, R> resultVisitor) {

		final IterationInfo info = initAndGetIterationInfo();
		R result = MapIterateState.<K, V>singleState().visitForResult(mMap, hasExtraOperationInIteration(),
				getOperateInterceptor(), info, param, predicate, resultVisitor, null);
		handleAtLast(param, info);
		return result;
	}

	@Override
	public List<KeyValuePair<K, V>> visitForQueryList(Object param, MapPredicateVisitor<K, V> predicate,
			@Nullable List<KeyValuePair<K, V>> out) {
		if (out == null) {
			out = new ArrayList<KeyValuePair<K, V>>();
		}
		final IterationInfo info = initAndGetIterationInfo();
		MapIterateState.<K, V>multipleState().visit(mMap, hasExtraOperationInIteration(), getOperateInterceptor(), info, param,
				predicate, out);
		handleAtLast(param, info);
		return out;
	}

	@Override
	public KeyValuePair<K, V> visitForQuery(Object param, MapPredicateVisitor<K, V> predicate) {
		final IterationInfo info = initAndGetIterationInfo();
		KeyValuePair<K, V> result = MapIterateState.<K, V>singleState().visit(mMap, hasExtraOperationInIteration(),
				getOperateInterceptor(), info, param, predicate, null);
		handleAtLast(param, info);
		return result;
	}

	private IterationInfo initAndGetIterationInfo() {
		int size = mMap.size();
		mIterationInfo.setOriginSize(size);
		mIterationInfo.setCurrentSize(size);
		return mIterationInfo;
	}

}
