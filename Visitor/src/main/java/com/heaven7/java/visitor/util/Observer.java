package com.heaven7.java.visitor.util;

public interface Observer<T> {

	void onSucess(Object param);

	void onFailed(Object param, T t);
	
	void onThrowable(Object param, T t, Throwable e);

}