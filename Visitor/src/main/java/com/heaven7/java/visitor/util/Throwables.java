package com.heaven7.java.visitor.util;

import java.util.Collection;

public class Throwables {

	public static void checkNull(Object obj) {
		if (obj == null) {
			throw new NullPointerException();
		}
	}

	public static <T> void checkEmpty(Collection<T> collection) {
		if (collection == null) {
			throw new NullPointerException();
		}
		if (collection.size() == 0) {
			throw new IllegalArgumentException();
		}
	}
	public static <K,V> void checkEmpty(Map<K,V> map) {
		if (map == null) {
			throw new NullPointerException();
		}
		if (map.size() == 0) {
			throw new IllegalArgumentException();
		}
	}
	public static <K,V> void checkEmpty(java.util.Map<K,V> map) {
		if (map == null) {
			throw new NullPointerException();
		}
		if (map.size() == 0) {
			throw new IllegalArgumentException();
		}
	}
}
