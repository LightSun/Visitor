package com.heaven7.java.visitor.collection;

import java.util.Iterator;

public abstract class OperateInterceptor<T> {

	public abstract boolean intercept(Iterator<T> it, T t, Object param, IterationInfo info);
	
}
