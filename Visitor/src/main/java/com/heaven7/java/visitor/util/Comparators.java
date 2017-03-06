package com.heaven7.java.visitor.util;

import java.util.Comparator;

/**
 * some useful comparator.
 * @author heaven7
 * @since 1.1.5
 */
public final class Comparators {
	
	private Comparators(){}
	
	/**
	 * get the default comparator if the target class implements {@linkplain Comparable}.
	 * or else must cause {@linkplain IllegalStateException}
	 * @param <T> the class type who should implements {@linkplain Comparable}.
	 * @return a Comparator who's behaviour same as the {@linkplain Comparable}.
	 * @since 1.1.5
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Comparable<T>> Comparator<T> getDefaultComparator(){
		return (Comparator<T>) DEFAULT_COMPARATOR;
	}

	private static final Comparator<Object> DEFAULT_COMPARATOR = new Comparator<Object>(){
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public int compare(Object o1, Object o2) {
			if((o1 instanceof Comparable) && (o2 instanceof Comparable)){
				return ((Comparable)o1).compareTo(o2);
			}
			throw new IllegalStateException("must implements interface Comparable.");
		}
		
	};
}
