package com.heaven7.java.visitor;

import java.util.Collection;

/**
 * the collection save visitor
 * 
 * @author heaven7
 *
 * @param <T>
 *            the element type
 */
public interface SaveVisitor<T> {
	/**
	 * visit to save collection
	 * 
	 * @param o
	 *            the collection to save. And this collection is read-only.
	 */
	void visit(Collection<T> o);
}