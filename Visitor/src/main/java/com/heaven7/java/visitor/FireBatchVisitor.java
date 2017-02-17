package com.heaven7.java.visitor;

import java.util.Collection;

/**
 * fire/emit visitor
 * @author heaven7
 *
 * @param <T> the object to fire
 * @since 1.1.1
 */
public interface FireBatchVisitor<T> extends Visitor1<Collection<T>, Object, Void>{

	/**
	 * fire/emit the collection data with target parameter .
	 * @param collection the collection
	 * @param param the extra parameter.
	 */
	Void visit(Collection<T> collection, Object param);
}
