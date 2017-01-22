package com.heaven7.java.visitor.collection;

import java.util.List;
/**
 * 
 * @author don
 *
 * @param <R> the return type for {@link #end()}}
 * @param <T> the parametric type of most method
 */
public abstract class OperareManager<R, T> {

	OperareManager() {
		super();
	}

	public abstract R end() ;

	public OperareManager<R, T> filter(PredicateVisitor<? super T> filter) {
		return filter(null, filter);
	}

	public OperareManager<R, T> insert(List<T> list, IterateVisitor<? super T> insert) {
		return insert(list, null, insert);
	}

	public OperareManager<R, T> insert(T newT, IterateVisitor<? super T> insert) {
		return insert(newT, null, insert);
	}

	public OperareManager<R, T> insertFinally(T newT, IterateVisitor<T> insert) {
		return insertFinally(newT, null, insert);
	}

	public OperareManager<R, T> insertFinally(List<T> list, IterateVisitor<? super T> insert) {
		return insertFinally(list, null, insert);
	}

	public OperareManager<R, T> delete(PredicateVisitor<? super T> delete) {
		return delete(null, delete);
	}

	public OperareManager<R, T> update(T newT, PredicateVisitor<? super T> update) {
		return update(newT, null, update);
	}

	// =============== public abstract method ======================

	public abstract OperareManager<R, T> filter(Object param, PredicateVisitor<? super T> filter);

	public abstract OperareManager<R, T> delete(Object param, PredicateVisitor<? super T> delete);

	public abstract OperareManager<R, T> update(T newT, Object param, PredicateVisitor<? super T> update);
	
	public abstract OperareManager<R, T> insert(List<T> list, Object param, IterateVisitor<? super T> insert);

	public abstract OperareManager<R, T> insert(T newT, Object param, IterateVisitor<? super T> insert);

	public abstract OperareManager<R, T> insertFinally(T newT, Object param, IterateVisitor<? super T> insert);

	public abstract OperareManager<R, T> insertFinally(List<T> list, Object param, IterateVisitor<? super T> insert);

}
