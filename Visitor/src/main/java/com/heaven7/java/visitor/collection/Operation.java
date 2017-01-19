package com.heaven7.java.visitor.collection;

public class Operation<T> {
	
	public int op;
	public T newT;
	
	public Object param;
	public PredicateVisitor<T> visitor;
	public InsertVisitor<T> insertVisitor;

	private Operation() {
	}

	public static <T> Operation<T> create(int op, Object param, PredicateVisitor<T> visitor) {
		Operation<T> operation = new Operation<T>();
		operation.op = op;
		operation.param = param;
		operation.visitor = visitor;
		return operation;
	}
	public static <T> Operation<T> create(int op, T newT, Object param, PredicateVisitor<T> visitor) {
		Operation<T> operation = new Operation<T>();
		operation.op = op;
		operation.newT = newT;
		operation.param = param;
		operation.visitor = visitor;
		return operation;
	}

	public static <T> Operation<T> createInsert(T newT, Object param, InsertVisitor<T> insertVisitor) {
		Operation<T> operation = new Operation<T>();
		operation.op = CollectionVisitService.OP_INSERT;
		operation.newT = newT;
		operation.param = param;
		operation.insertVisitor = insertVisitor;
		return operation;
	}

	public boolean shouldUpdate(T element, Object defaultParam) {
		return visitor != null && visitor.visit(element, 
				param != null ? param : defaultParam);
	}

	public boolean shouldInsert(T element, Object defaultParam, int size, int index) {
		return insertVisitor != null && insertVisitor.visit(element, 
				param != null ? param : defaultParam,
						size, index);
	}

}