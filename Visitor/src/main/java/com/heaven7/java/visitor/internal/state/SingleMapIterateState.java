package com.heaven7.java.visitor.internal.state;

import java.util.List;
import java.util.ListIterator;

import com.heaven7.java.visitor.MapPredicateVisitor;
import com.heaven7.java.visitor.MapResultVisitor;
import com.heaven7.java.visitor.collection.IterationInfo;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.MapVisitService.MapOperateInterceptor;
import com.heaven7.java.visitor.util.Map;
import com.heaven7.java.visitor.util.Predicates;

/*public*/ class SingleMapIterateState<K, V> extends MapIterateState<K, V> {

	@Override
	protected KeyValuePair<K, V> visitImpl(Map<K, V> map, boolean hasExtra, MapOperateInterceptor<K, V> moip,
			IterationInfo info, Object param, MapPredicateVisitor<K, V> predicate, List<KeyValuePair<K, V>> out) {
		final List<KeyValuePair<K, V>> keyValues = map.getKeyValues();
		final ListIterator<KeyValuePair<K, V>> it = keyValues.listIterator();
		
		KeyValuePair<K, V> result = null;
		KeyValuePair<K, V> pair;
		if(hasExtra){
			for (; it.hasNext();) {
				pair = it.next();
				if(moip.intercept(map, pair, param, info)){
					continue;
				}
				if(result == null && Predicates.isTrue(predicate.visit(pair, param))){
					result = pair;
				}
			}
		}else{
			//no extra ops
			for (; it.hasNext();) {
				pair = it.next();
				if(Predicates.isTrue(predicate.visit(pair, param))){
					result = pair;
					break;
				}
			}
		}
		return result;
	}

	@Override
	protected <R> R visitForResultImpl(Map<K, V> map, boolean hasExtra, MapOperateInterceptor<K, V> moip,
			IterationInfo info, Object param, MapPredicateVisitor<K, V> predicate,
			MapResultVisitor<K, V, R> resultVisitor, List<R> out) {
		
		final List<KeyValuePair<K, V>> keyValues = map.getKeyValues();
		final ListIterator<KeyValuePair<K, V>> it = keyValues.listIterator();
		
		R result = null;
		KeyValuePair<K, V> pair;
		if(hasExtra){
			for (; it.hasNext();) {
				pair = it.next();
				if(moip.intercept(map, pair, param, info)){
					continue;
				}
				if(result == null && Predicates.isTrue(predicate.visit(pair, param))){
					result = resultVisitor.visit(pair, param);
				}
			}
		}else{
			//no extra ops
			for (; it.hasNext();) {
				pair = it.next();
				if(Predicates.isTrue(predicate.visit(pair, param))){
					result = resultVisitor.visit(pair, param);
					break;
				}
			}
		}
		return result;
	}

}
