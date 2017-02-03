package com.heaven7.java.visitor;


import com.heaven7.java.visitor.collection.IterationInfo;
import com.heaven7.java.visitor.util.Map;

/**
 * trim map visitor
 * @author heaven7
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public interface TrimMapVisitor<K, V> extends IterateVisitor<Map<K, V>> {
	
	/**
	 * visit for trim operation. called when we want to trim this map.
	 * @param t the map
	 * @param param the extra parameter
	 * @param info the IterationInfo
	 * @return a boolean result which indicate trim success or failed.
	 */
	@Override
	Boolean visit(Map<K, V> t, Object param, IterationInfo info);

}
