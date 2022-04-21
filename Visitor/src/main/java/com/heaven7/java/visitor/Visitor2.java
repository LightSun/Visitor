package com.heaven7.java.visitor;

/**
 * a visitor carry double extra parameter.
 * @author heaven7
 *
 * @param <T> the type
 * @param <P1> the first parameter type 
 * @param <P2> the second parameter type
 * @param <Result> the result type
 */
public interface Visitor2<T, P1, P2, Result> {

	/**
	 * visit for result
	 * @param t the element 
	 * @param p1 the first parameter
	 * @param p2 the second parameter
	 * @return the result of this visit
	 */
	Result visit(T t, P1 p1, P2 p2);
}
