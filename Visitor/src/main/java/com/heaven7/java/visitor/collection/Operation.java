package com.heaven7.java.visitor.collection;

import static com.heaven7.java.visitor.collection.VisitService.OP_DELETE;
import static com.heaven7.java.visitor.collection.VisitService.OP_FILTER;
import static com.heaven7.java.visitor.collection.VisitService.OP_INSERT;
import static com.heaven7.java.visitor.collection.VisitService.OP_UPDATE;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import com.heaven7.java.visitor.IterateVisitor;
import com.heaven7.java.visitor.PredicateVisitor;

public class Operation<T> {

	private int mOp;
	/** used to update or insert */
	private T mNewElement;
	/** only used to insert */
	private List<T> mNewElements;

	/** ths extra param for current operation. */
	private Object mParam;
	/** used to common operate, eg : filter, update, delete */
	private PredicateVisitor<? super T> mVisitor;
	/**
	 * only used to insert/finalInsert (because we care about the capacity of collection)
	 */
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
		operation.mNewElement = newT;
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
		operation.mNewElement = newT;
		operation.mParam = param;
		operation.mIteratorVisitor = insertVisitor;
		return operation;
	}

	public static <T> Operation<T> createInsert(List<T> list, Object param, IterateVisitor<? super T> insertVisitor) {
		Operation<T> operation = new Operation<T>();
		operation.mOp = OP_INSERT;
		operation.mNewElements = list;
		operation.mParam = param;
		operation.mIteratorVisitor = insertVisitor;
		return operation;
	}

	@SuppressWarnings({ "unchecked" })
	public boolean update(T t, Object defaultParam) {
		if (shouldUpdate(t, defaultParam)) {
			if (t instanceof Updatable) {
				((Updatable<T>) t).updateFrom(mNewElement);
				return true;
			}
		}
		return false;
	}

	public boolean update(ListIterator<T> lit, T t, Object defaultParam) {
		if (shouldUpdate(t, defaultParam)) {
			lit.set(mNewElement);
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

	public boolean insertLast(Collection<? super T> collection, Object param, IterationInfo info) {
		if (shouldInsert(null, param, info)) {
			if (mNewElements != null && mNewElements.size() > 0) {
				return collection.addAll(mNewElements);
			}
			return collection.add(mNewElement);
		}
		return false;
	}

	public boolean insert(ListIterator<T> lit, T t, Object param, IterationInfo info) {
		if (shouldInsert(t, param, info)) {
			if (mNewElements != null && mNewElements.size() > 0) {
				for (int i = 0, size = mNewElements.size(); i < size; i++) {
					lit.add(mNewElements.get(i));
				}
				return true;
			}
			lit.add(mNewElement);
			return true;
		}
		return false;
	}

}