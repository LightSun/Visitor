package com.heaven7.java.visitor.collection;

import java.util.Collection;
import java.util.ListIterator;

public class Operation<T> {

	private static final int OP_DELETE = 1;
	private static final int OP_FILTER = 2;
	private static final int OP_UPDATE = 3;
	private static final int OP_INSERT = 4;

	private int mOp;
	private T mNewT;

	private Object mParam;
	private PredicateVisitor<? super T> mVisitor;
	private IterateVisitor<? super T> mIteratorVisitor;

	private Operation() {
	}

	public static <T> Operation<T> createFilter(Object param, PredicateVisitor<? super T> visitor) {
		Operation<T> operation = new Operation<T>();
		operation.mOp = OP_FILTER;
		operation.mParam = param;
		operation.mVisitor = visitor;
		return operation;
	}

	public static <T> Operation<T> createUpdate(T newT, Object param, PredicateVisitor<? super T> visitor) {
		Operation<T> operation = new Operation<T>();
		operation.mOp = OP_UPDATE;
		operation.mNewT = newT;
		operation.mParam = param;
		operation.mVisitor = visitor;
		return operation;
	}

	public static <T> Operation<T> createDelete(Object param, PredicateVisitor<? super T> visitor) {
		Operation<T> operation = new Operation<T>();
		operation.mOp = OP_DELETE;
		operation.mParam = param;
		operation.mVisitor = visitor;
		return operation;
	}

	public static <T> Operation<T> createInsert(T newT, Object param, IterateVisitor<? super T> insertVisitor) {
		Operation<T> operation = new Operation<T>();
		operation.mOp = OP_INSERT;
		operation.mNewT = newT;
		operation.mParam = param;
		operation.mIteratorVisitor = insertVisitor;
		return operation;
	}

	@SuppressWarnings({ "unchecked" })
	public boolean update(T t, Object defaultParam) {
		if (shouldUpdate(t, defaultParam)) {
			if (t instanceof Updatable) {
				((Updatable<T>) t).updateFrom(mNewT);
				return true;
			}
		}
		return false;
	}

	public boolean update(ListIterator<T> lit, T t, Object defaultParam) {
		if (shouldUpdate(t, defaultParam)) {
			lit.set(mNewT);
			return true;
		}
		return false;
	}

	public boolean shouldFilter(T t, Object defaultParam) {
		if (mOp == OP_FILTER && mVisitor != null) {
			Boolean result = mVisitor.visit(t, mParam != null ? mParam : defaultParam);
			return result != null && result;
		}
		return false;
	}

	public boolean shouldUpdate(T t, Object defaultParam) {
		if (mOp == OP_UPDATE && mVisitor != null) {
			Boolean result = mVisitor.visit(t, mParam != null ? mParam : defaultParam);
			return result != null && result;
		}
		return false;
	}

	public boolean shouldDelete(T t, Object defaultParam) {
		if (mOp == OP_DELETE && mVisitor != null) {
			Boolean result = mVisitor.visit(t, mParam != null ? mParam : defaultParam);
			return result != null && result;
		}
		return false;
	}

	public boolean shouldInsert(T t, Object defaultParam, IterationInfo info) {
		if (mOp == OP_INSERT && mIteratorVisitor != null) {
			Boolean result = mIteratorVisitor.visit(t, mParam != null ? mParam : defaultParam, info);
			return result != null && result;
		}

		return false;
	}

	public boolean insert(Collection<? super T> collection, Object param, IterationInfo info) {
		if (shouldInsert(null, param, info)) {
			return collection.add(mNewT);
		}
		return false;
	}

	public boolean insert(T t, Object param, IterationInfo info, Collection<? super T> collection) {
		if (shouldInsert(t, param, info)) {
			return collection.add(mNewT);
		}
		return false;
	}

}