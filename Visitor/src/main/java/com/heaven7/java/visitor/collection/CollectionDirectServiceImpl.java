package com.heaven7.java.visitor.collection;

import java.util.Collection;

import com.heaven7.java.visitor.collection.operator.CollectionCondition;
import com.heaven7.java.visitor.util.Throwables;

//TODO
public class CollectionDirectServiceImpl<T> {

	private final Collection<T> mCollection;

	public CollectionDirectServiceImpl(Collection<T> mCollection) {
		super();
		this.mCollection = mCollection;
	}

	public void apply(CollectionCondition<T> condition) {
		Throwables.checkNull(condition);
		condition.apply(mCollection);
	}

	/*
	 * CollectionDirectService<T> removeAll(Collection<?> c);
	 * 
	 * CollectionDirectService<T> retainAll(Collection<?> c);
	 * 
	 * CollectionDirectService<T> removeIf(@Nullable Object param,
	 * PredicateVisitor<? super T> filter);
	 * 
	 * CollectionDirectService<T> replaceAll(@Nullable Object param,
	 * ResultVisitor<? super T, T> filter);
	 * 
	 * CollectionDirectService<T> clear();
	 * 
	 * CollectionDirectService<T> contains();
	 * 
	 * CollectionDirectService<T> size();
	 * 
	 * CollectionDirectService<T> addIfNotExist(T newT);
	 * 
	 * CollectionDirectService<T> removeIfExist(T newT);
	 * 
	 * CollectionDirectService<T> queryAll(@Nullable Object param,
	 * PredicateVisitor<? super T> filter);
	 */

	// zip/ zipService(@Nullable Object param, ResultVisitor<T, R> resultVisitor

}
