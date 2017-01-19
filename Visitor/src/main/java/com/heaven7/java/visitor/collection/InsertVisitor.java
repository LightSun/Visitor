package com.heaven7.java.visitor.collection;

import com.heaven7.java.visitor.Visitor;

public interface InsertVisitor<T> extends Visitor{

	//true 代表需要在当前index后面插入
	boolean visit(T t, Object param, int size, int index);
}
