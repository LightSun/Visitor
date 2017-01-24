package com.heaven7.java.visitor.collection;

import java.util.Map;
import java.util.Map.Entry;

/**
 * an entry wrapper of Map.Entry , this class just avoid access
 * method({@linkplain Map.Entry#setValue(Object)}) directly for user.
 * 
 * @author heaven7
 *
 * @param <K>
 *            the key type
 * @param <V>
 *            the value type
 */
public final class KeyValuePair<K, V> {

	private final K key;
	private final V value;

	/* public */ KeyValuePair(Entry<K, V> entry) {
		super();
		this.key = entry.getKey();
		this.value = entry.getValue();
	}

	KeyValuePair(K key, V value) {
		super();
		this.key = key;
		this.value = value;
	}

	/**
	 * set the value.
	 * 
	 * @param value
	 *            the target value
	 * @return the old value
	 */
	// avoid access directly for user.
	/*V setValue(V value) {
		// AccessController.
		return entry.setValue(value);
	}*/

	/**
	 * get the key
	 * 
	 * @return the key
	 * @see {@linkplain Map.Entry#getKey()}
	 */
	public K getKey() {
		return key;
	}

	/**
	 * get the value
	 * 
	 * @return the value
	 * @see {@linkplain Map.Entry#getValue()}
	 */
	public V getValue() {
		return value;
	}

	@Override
	public String toString() {
		return key + " = " + value;
	}

}
