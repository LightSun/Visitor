package com.heaven7.java.visitor.util;

import java.util.List;
import java.util.SortedMap;
/**
 * some useful methods of predicate.
 * @author heaven7
 *
 */
public final class Predicates {
	
	private Predicates(){}
	
	public static boolean equals(Object a, Object b) {
		return (a == b) || (a != null && a.equals(b));
	}
	public static boolean isTrue(Boolean value) {
		return value != null && value;
	}
	public static <E> boolean isEmpty(List<E> list) {
		return list == null || list.size() == 0;
	}
	public static <K,V> boolean isEmpty(Map<K,V> map) {
		return map == null || map.size() == 0;
	}
	
	public static <K,V> boolean isSortedMap(java.util.Map<K, V> map){
		return map instanceof SortedMap;
	}
}
