package com.heaven7.java.visitor;

import java.util.Collection;

import com.heaven7.java.visitor.util.Map;

/**
 * save collection or map visitor.
 * @author heaven7
 *
 * @param <Out> the out type
 * @since 1.0.3
 */
public interface SaveVisitor<Out> {
	
	/**
	 * called on save current collection/map.
	 * @param o the out type.
	 */
	void onSave(Out o);
	
	/**
	 * the collection save visitor
	 * @author heaven7
	 *
	 * @param <T> the element type
	 */
	public static interface CollectionSaveVisitor<T> extends SaveVisitor<Collection<T>>{
	}
	/**
	 * the map save visitor
	 * @author heaven7
	 *
	 * @param <K> the key type
	 * @param <K> the value type
	 */
	public static interface MapSaveVisitor<K,V> extends SaveVisitor<Map<K,V>>{
		
	}
}
