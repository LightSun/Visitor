package com.heaven7.java.visitor;

import com.heaven7.java.visitor.Visitor2;
import com.heaven7.java.visitor.collection.IterationInfo;

/**
 * the iterate visitor
 * @author heaven7
 *
 * @param <T> the element type
 */
public interface IterateVisitor<T> extends Visitor2<T, Object, IterationInfo, Boolean> {
	
	/**
	 * visit for Boolean result
	 * @param t the element 
	 * @param p1 the extra parameter
	 * @param p2 the IterationInfo of current iterate
	 * @return the Boolean result of this visit
	 */
	Boolean visit(T t, Object param, IterationInfo info);
}
