package com.heaven7.java.visitor.collection;

import java.util.Set;

import com.heaven7.java.visitor.MapPredicateVisitor;
import com.heaven7.java.visitor.MapResultVisitor;

public abstract class AbstractMapVisitService<K, V> implements MapVisitService<K, V> {

	@Override
	public final <R> Set<R> visitForResultSet(MapPredicateVisitor<? super K, ? super V> predicate,
			MapResultVisitor<? super K, ? super V, R> resultVisitor) {
		return visitForResultSet(null, predicate, resultVisitor);
	}

	@Override
	public final <R> R visitForResult(MapPredicateVisitor<? super K, ? super V> predicate,
			MapResultVisitor<? super K, ? super V, R> resultVisitor) {
		return visitForResult(null, predicate, resultVisitor);
	}

	@Override
	public final Set<KeyValuePair<K, V>> visitForQuerySet(MapPredicateVisitor<? super K, ? super V> predicate) {
		return visitForQuerySet(null, predicate);
	}

	@Override
	public final KeyValuePair<K, V> visitForQuery(MapPredicateVisitor<? super K, ? super V> predicate) {
		return visitForQuery(null, predicate);
	}

	
}
