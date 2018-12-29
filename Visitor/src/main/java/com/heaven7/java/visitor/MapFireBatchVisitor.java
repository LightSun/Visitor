package com.heaven7.java.visitor;

import java.util.Collection;

import com.heaven7.java.visitor.collection.KeyValuePair;

/**
 * fire/emit visitor
 * @author heaven7
 *
 * @param <K> the key type
 * @param <V> the value type
 * @since 1.1.1
 */
public interface MapFireBatchVisitor<K, V> extends FireBatchVisitor<KeyValuePair<K, V>>{

	/**
	 * fire/emit the pairs  with target parameter .
	 * @param collection the collection
	 * @param param the extra parameter.
	 */
	java.lang.Void visit(Collection<KeyValuePair<K, V>> collection, Object param);
}
