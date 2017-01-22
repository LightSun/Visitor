package com.heaven7.java.visitor.collection;

public abstract class IterateControl<T> {
	
	IterateControl(){}

	public abstract T end();

	public abstract IterateControl<T> interceptIfSuccess(int operate);

	public abstract IterateControl<T> first(int operate);

	public abstract IterateControl<T> second(int operate);

	public abstract IterateControl<T> then(int operate);

	public abstract IterateControl<T> last(int operate);

}