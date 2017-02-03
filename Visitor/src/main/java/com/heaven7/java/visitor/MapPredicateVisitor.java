package com.heaven7.java.visitor;

import com.heaven7.java.visitor.collection.KeyValuePair;

/**
 * the map predicate visitor
 * @author heaven7
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public interface MapPredicateVisitor<K, V> extends PredicateVisitor<KeyValuePair<K, V>> {

	@Override
	Boolean visit(KeyValuePair<K, V> pair, Object param);



}