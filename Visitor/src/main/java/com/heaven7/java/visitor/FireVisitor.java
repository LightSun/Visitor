package com.heaven7.java.visitor;

/**
 * fire/emit visitor
 * @author heaven7
 *
 * @param <T> the object to fire
 * @since 1.1.1
 */
public interface FireVisitor<T> extends Visitor1<T, Object, Boolean>{

	/**
	 * fire the object t with target parameter .
	 * @param t the object
	 * @param param the extra parameter.
	 */
	Boolean visit(T t, Object param);
}
