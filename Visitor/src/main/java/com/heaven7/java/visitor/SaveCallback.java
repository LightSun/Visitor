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
public interface SaveCallback<Out> {
	
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
	interface CollectionSaveCallback<T> extends SaveCallback<Collection<T>>{
		/**
		 * called on save collection
		 * @param o the collection to save.
		 */
		void onSave(Collection<T> o);
	}
	/**
	 * the map save visitor
	 * @author heaven7
	 *
	 * @param <K> the key type
	 * @param <K> the value type
	 */
     interface MapSaveCallback<K,V> extends SaveCallback<Map<K,V>>{
    	 /**
    	  * called on save map.
    	  * @param o the map to save
    	  */
		 void onSave(Map<K, V> o);
	}
}
