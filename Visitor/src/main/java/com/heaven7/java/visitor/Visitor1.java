package com.heaven7.java.visitor;

/**
 * a visitor carry an extra parameter.
 * @author heaven7
 *
 * @param <T> the type
 * @param <Param> the parameter type
 * @param <Result> the result type
 */
public interface Visitor1<T, Param, Result> {

	/**
	 * visit for result
	 * @param t the element
	 * @param param the parameter
	 * @return the result
	 */
	Result visit(T t, Param param);

}