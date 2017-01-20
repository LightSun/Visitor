package com.heaven7.java.visitor;

public interface Visitor2<T, P1, P2, Result> {

	Result visit(T t, P1 p1, P2 p2);
}
