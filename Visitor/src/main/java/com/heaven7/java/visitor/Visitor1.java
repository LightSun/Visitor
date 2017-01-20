package com.heaven7.java.visitor;

public interface Visitor1<T, Param, Result> {

	Result visit(T t, Param param);

}