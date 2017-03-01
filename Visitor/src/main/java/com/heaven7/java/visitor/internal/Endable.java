package com.heaven7.java.visitor.internal;

/**
 *  end able
 * @author heaven7
 *
 * @param <T> the original type
 * @since 1.1.2
 */
public interface Endable<T> {

	/**
	 * end current and return to the original object
	 * @return the original object
	 * @since 1.1.2
	 */
	T end();
}
