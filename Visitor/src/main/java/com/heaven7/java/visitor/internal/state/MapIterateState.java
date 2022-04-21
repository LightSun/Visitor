package com.heaven7.java.visitor.internal.state;

import java.util.List;

import com.heaven7.java.visitor.MapPredicateVisitor;
import com.heaven7.java.visitor.MapResultVisitor;
import com.heaven7.java.visitor.anno.Nullable;
import com.heaven7.java.visitor.collection.IterationInfo;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.MapVisitService.MapOperateInterceptor;
import com.heaven7.java.visitor.util.Map;

public abstract class MapIterateState<K, V> {

	public static <K, V> MapIterateState<K, V> singleState() {
		return new SingleMapIterateState<K, V>();
	}

	public static <K, V> MapIterateState<K, V> multipleState() {
		return new MultipleMapIterateState<K, V>();
	}

	public <R> R visitForResult(Map<K, V> map, boolean hasExtra, MapOperateInterceptor<K, V> moip,
			IterationInfo info, Object param, 
			MapPredicateVisitor<K, V> predicate, MapResultVisitor<K, V, R> resultVisitor,
			@Nullable List<R> out) {
		if (hasExtra) {
			moip.begin();
			R result = visitForResultImpl(map, hasExtra, moip, info, param, predicate, resultVisitor, out);
			moip.end();
			return result;
		}
		return visitForResultImpl(map, hasExtra, moip, info, param, predicate, resultVisitor, out);
	}

	public final KeyValuePair<K, V> visit(Map<K, V> map, boolean hasExtra, MapOperateInterceptor<K, V> moip,
			IterationInfo info, Object param, MapPredicateVisitor<K, V> predicate,
			@Nullable List<KeyValuePair<K, V>> out) {
		if (hasExtra) {
			moip.begin();
			KeyValuePair<K, V> result = visitImpl(map, hasExtra, moip, info, param, predicate, out);
			moip.end();
			return result;
		}
		return visitImpl(map, hasExtra, moip, info, param, predicate, out);
	}
	
	protected abstract <R>  R visitForResultImpl(Map<K, V> map, boolean hasExtra, MapOperateInterceptor<K, V> moip, 
			IterationInfo info,  Object param, 
			MapPredicateVisitor<K, V> predicate, MapResultVisitor<K, V, R> resultVisitor, 
			List<R> out);

	protected abstract KeyValuePair<K, V> visitImpl(Map<K, V> map, boolean hasExtra, MapOperateInterceptor<K, V> moip,
			IterationInfo info, Object param, MapPredicateVisitor<K, V> predicate, List<KeyValuePair<K, V>> out);
}
