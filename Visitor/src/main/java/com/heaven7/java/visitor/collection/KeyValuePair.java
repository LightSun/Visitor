package com.heaven7.java.visitor.collection;

import com.heaven7.java.visitor.util.Throwables;

/**
 * an entry like Map.Entry .
 * 
 * @author heaven7
 *
 * @param <K>
 *            the key type
 * @param <V>
 *            the value type
 */
public final class KeyValuePair<K, V> {

	private K key;
	private V value;

	KeyValuePair(K key, V value) {
		super();
		this.key = key;
		this.value = value;
	}

	public static <K, V> KeyValuePair<K, V> create(K key, V value) {
		Throwables.checkNull(key);
		return new KeyValuePair<K, V>(key, value);
	}

	void setKeyValue(K key, V value) {
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
	/*
	 * V setValue(V value) { // AccessController. return entry.setValue(value);
	 * }
	 */

	/**
	 * get the key
	 * 
	 * @return the key
	 */
	public K getKey() {
		return key;
	}

	/**
	 * get the value
	 * 
	 * @return the value
	 */
	public V getValue() {
		return value;
	}

	@Override
	public String toString() {
		return key + " = " + value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof KeyValuePair)) {
			return false;
		}
		final KeyValuePair<K, V> e2;
		try {
			e2 = (KeyValuePair<K, V>) obj;
		} catch (ClassCastException e) {
			return false;
		}
		final KeyValuePair<K, V> e1 = this;
		return (e1.getKey() == null ? e2.getKey() == null : e1.getKey().equals(e2.getKey()))
				&& (e1.getValue() == null ? e2.getValue() == null : e1.getValue().equals(e2.getValue()));

	}

}
