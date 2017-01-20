package com.heaven7.java.visitor;

public interface Visitor3<T, P1, P2, P3, Result> {

	Result visit(T t, P1 p1, P2 p2, P3 p3);
}
