package com.heaven7.java.visitor.util;

public class Predicates {

	public static boolean isTrue(Boolean value) {
		return value != null && value.booleanValue();
	}
}
