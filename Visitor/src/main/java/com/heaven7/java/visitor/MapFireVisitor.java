package com.heaven7.java.visitor;

import com.heaven7.java.visitor.collection.KeyValuePair;

/**
 * fire/emit visitor
 * @author heaven7
 *
 * @param <T> the object to fire
 * @since 1.1.1
 */
public interface MapFireVisitor<K, V> extends FireVisitor<KeyValuePair<K, V>>{

	/**
	 * fire the  key-value pair with target parameter .
	 * @param t the key-value pair
	 * @param param the extra parameter.
	 */
	Void visit(KeyValuePair<K, V> pair, Object param);
}
