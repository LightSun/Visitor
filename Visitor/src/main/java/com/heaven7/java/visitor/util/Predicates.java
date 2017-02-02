package com.heaven7.java.visitor.util;

import java.util.List;

public class Predicates {
	
	public static boolean equals(Object a, Object b) {
		return (a == b) || (a != null && a.equals(b));
	}
	public static boolean isTrue(Boolean value) {
		return value != null && value.booleanValue();
	}
	public static <E> boolean isEmpty(List<E> list) {
		return list == null || list.size() == 0;
	}
	public static <K,V> boolean isEmpty(Map<K,V> map) {
		return map == null || map.size() == 0;
	}
}
