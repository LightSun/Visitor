package com.heaven7.java.visitor.util;

public class Throwables {

	public static void checkNull(Object obj){
		if(obj == null){
			throw new NullPointerException();
		}
	}
}
