package com.heaven7.java.visitor;

import com.heaven7.java.visitor.util.Map;

/**
 * the map save visitor
 * 
 * @author heaven7
 *
 * @param <K>
 *            the key type
 * @param <K>
 *            the value type
 */
public interface MapSaveVisitor<K, V>{
	/**
	 * visit to save map.
	 * 
	 * @param o
	 *            the map to save, And this map is read-only.
	 */
	void visit(Map<K, V> o);
}