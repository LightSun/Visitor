package com.heaven7.java.visitor;

import com.heaven7.java.visitor.collection.KeyValuePair;

/**
 * the map result visitor
 * @author heaven7
 *
 * @param <K> the key type
 * @param <V> the value type
 * @param <R> the result type
 */
public interface MapResultVisitor<K, V, R> extends ResultVisitor<KeyValuePair<K, V>, R> {

	/**
	 * visit a key-value for result.
	 * @param t a key-value pair
	 * @param param the parameter. 
	 * @return the result.
	 */
    @Override
    R visit(KeyValuePair<K, V> t, Object param);
}