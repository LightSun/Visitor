package com.heaven7.java.visitor.util;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
/**
 * expand util class of Collections.
 * @author heaven7
 * @since 1.1.2
 */
public class Collections2 {

	public static <T> List<T> asList(Collection<T> coll){
		if(coll instanceof List){
			return (List<T>) coll;
		}
		throw new UnsupportedOperationException();
	}
	
	public static<T> SortedSet<T> asSortedSet(Collection<T> coll){
		if(coll instanceof SortedSet){
			return (SortedSet<T>) coll;
		}
		throw new UnsupportedOperationException();
	}
	
}
