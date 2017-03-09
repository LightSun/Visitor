package com.heaven7.java.visitor.util;

import java.util.Collection;
/**
 * some useful methods for check arguments.
 * @author heaven7
 *
 */
public final class Throwables {
	private Throwables(){}
	
	/**
	 * throw exception if fatal.
	 * @param e the throwable
	 * @since 1.1.7
	 */
	public static void throwIfFatal(Throwable e) {
		if (e instanceof Error) {
			throw (Error) e;
		} else if (e instanceof VisitException) {
			throw (VisitException) e;
		} else if (e instanceof RuntimeException) {
			throw (RuntimeException) e;
		} else {
			throw new RuntimeException(e);
		}
	}

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
