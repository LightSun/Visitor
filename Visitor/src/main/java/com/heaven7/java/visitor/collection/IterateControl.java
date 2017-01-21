package com.heaven7.java.visitor.collection;

public abstract class IterateControl<T> {

	private final T src;

	 IterateControl(T src) {
		super();
		this.src = src;
	}
	
	public T end() {
		return src;
	}

	public abstract IterateControl<T> interceptIfSuccess(int operate);

	public abstract IterateControl<T> first(int operate);

	public abstract IterateControl<T> second(int operate);

	public abstract IterateControl<T> then(int operate);

	public abstract IterateControl<T> last(int operate);

}