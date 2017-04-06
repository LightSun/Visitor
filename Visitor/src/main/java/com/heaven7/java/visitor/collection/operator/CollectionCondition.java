package com.heaven7.java.visitor.collection.operator;

import java.util.Collection;
/**
 * the operate condition of collection.
 * @author heaven7
 *
 * @param <T> the element type of collection
 * @since 2.0.0
 */
public interface CollectionCondition<T> {

	/**
	 * apply the condition to target collection.
	 * @param src the target collection
	 * @return true if apply success.
	 */
	boolean apply(Collection<T> src);

}