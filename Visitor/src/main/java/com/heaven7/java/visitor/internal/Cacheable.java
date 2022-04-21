package com.heaven7.java.visitor.internal;

/**
 * cache able
 * @author heaven7
 *
 * @param <T> the type
 * @since 1.1.2
 */
public interface Cacheable<T> {
    
	/**
	 * do cache it.
	 * @return the original object
	 * @since 1.1.2
	 */
	T cache();

	/**
	 * no cache means clear cache.
	 * @return the original object
	 * @since  1.1.8
	 */
	T noCache();
}
