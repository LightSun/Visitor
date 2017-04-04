package com.heaven7.java.visitor.collection.operator;

import java.util.Collection;

public interface CollectionCondition<T> {

	boolean apply(Collection<T> src);

}