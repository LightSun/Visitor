package com.heaven7.java.visitor;

import com.heaven7.java.visitor.collection.IterationInfo;
import com.heaven7.java.visitor.collection.KeyValuePair;

/**
 * the map iterate visitor
 * @author heaven7
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public interface MapIterateVisitor<K,V> extends IterateVisitor<KeyValuePair<K, V>> {

	/**
	 * visit for Boolean result
	 * @param pair the key-value 
	 * @param param the extra parameter
	 * @param info the IterationInfo of current iterate
	 * @return the Boolean result of this visit
	 */
	@Override
	Boolean visit(KeyValuePair<K, V> pair, Object param, IterationInfo info);
}
