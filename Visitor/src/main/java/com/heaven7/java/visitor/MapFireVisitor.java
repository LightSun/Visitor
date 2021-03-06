package com.heaven7.java.visitor;

import com.heaven7.java.visitor.collection.KeyValuePair;

/**
 * fire/emit visitor
 * @author heaven7
 *
 * @param <K> the key type
 * @param <V> the value type
 * @since 1.1.1
 */
public interface MapFireVisitor<K, V> extends FireVisitor<KeyValuePair<K, V>>{

	/**
	 * fire the  key-value pair with target parameter .
	 * @param pair the key-value pair
	 * @param param the extra parameter.
	 */
	Boolean visit(KeyValuePair<K, V> pair, Object param);
}
