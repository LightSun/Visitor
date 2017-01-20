package com.heaven7.java.visitor.collection;

import com.heaven7.java.visitor.Visitor2;

public interface IterateVisitor<T> extends Visitor2<T, Object, IterationInfo, Boolean> {
	
	Boolean visit(T t, Object param, IterationInfo info);
}
