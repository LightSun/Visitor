package com.heaven7.java.visitor.collection;

import java.util.Iterator;
import java.util.ListIterator;

import com.heaven7.java.visitor.anno.Nullable;

/**
 * the operate interceptor in iteration({@linkplain Iterator} or [{@linkplain ListIterator}).
 * @author heaven7
 *
 * @param <T> the type
 */
public abstract class OperateInterceptor<T> {

	/**
	 * intercept the current iteration.
	 * @param it the Iterator
	 * @param t the element
	 * @param param the parameter, may be null.
	 * @param info the IterationInfo.
	 * @return true if intercept success, this means the loop of 'for' will be 'continue'. 
	 */
	public abstract boolean intercept(Iterator<T> it, T t,@Nullable Object param, IterationInfo info);

}
