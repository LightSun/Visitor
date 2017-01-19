package com.heaven7.java.visitor.collection;

import java.util.List;

public interface ListVisitable<T> extends CollectionVisitable<T> {

	<R> R acceptFirst(Object param, ResultVisitor<T, R> visitor);

	<R> R acceptLast(Object param, ResultVisitor<T, R> visitor);
	
	<R> List<R> accept(Object param, PredicateVisitor<T> predicate,
			ResultVisitor<T, R> visitor, int count, List<R> out);

}
