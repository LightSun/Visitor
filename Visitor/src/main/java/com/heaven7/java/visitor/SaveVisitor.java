package com.heaven7.java.visitor;

import java.util.Collection;

/**
 * the collection save visitor
 * 
 * @author heaven7
 *
 * @param <T>
 *            the element type
 * @since 1.0.3
 */
public interface SaveVisitor<T> {
	/**
	 * visit to save collection
	 * 
	 * @param collection
	 *            the collection to save. But this collection is read-only.
	 */
	void visit(Collection<T> collection);
}