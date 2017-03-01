package com.heaven7.java.visitor.collection;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.heaven7.java.visitor.IterateVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.util.Predicates;
import com.heaven7.java.visitor.util.Updatable;
/**
 * the collection operation
 * @author heaven7
 *
 * @param <T> the element type
 */
public class CollectionOperation<T> extends Operation{

	/** used to update or insert */
	private T mNewElement;
	/** only used to insert */
	private List<T> mNewElements;

	/** used to common operate, eg : filter, update, delete */
	private PredicateVisitor<? super T> mVisitor;
	/**
	 * only used to insert/finalInsert (because we care about the capacity of collection)
	 */
	private IterateVisitor<? super T> mIteratorVisitor;

	protected CollectionOperation() {
	}

	public static <T> CollectionOperation<T> createFilter(Object param, PredicateVisitor<? super T> visitor) {
		CollectionOperation<T> operation = new CollectionOperation<T>();
		operation.mOp = OP_FILTER;
		operation.mParam = param;
		operation.mVisitor = visitor;
		return operation;
	}

	public static <T> CollectionOperation<T> createUpdate(T newT, Object param, PredicateVisitor<? super T> visitor) {
		CollectionOperation<T> operation = new CollectionOperation<T>();
		operation.mOp = OP_UPDATE;
		operation.mNewElement = newT;
		operation.mParam = param;
		operation.mVisitor = visitor;
		return operation;
	}

	public static <T> CollectionOperation<T> createDelete(Object param, PredicateVisitor<? super T> visitor) {
		CollectionOperation<T> operation = new CollectionOperation<T>();
		operation.mOp = OP_DELETE;
		operation.mParam = param;
		operation.mVisitor = visitor;
		return operation;
	}

	public static <T> CollectionOperation<T> createInsert(T newT, Object param, IterateVisitor<? super T> insertVisitor) {
		CollectionOperation<T> operation = new CollectionOperation<T>();
		operation.mOp = OP_INSERT;
		operation.mNewElement = newT;
		operation.mParam = param;
		operation.mIteratorVisitor = insertVisitor;
		return operation;
	}

	public static <T> CollectionOperation<T> createInsert(List<T> list, Object param, IterateVisitor<? super T> insertVisitor) {
		CollectionOperation<T> operation = new CollectionOperation<T>();
		operation.mOp = OP_INSERT;
		operation.mNewElements = list;
		operation.mParam = param;
		operation.mIteratorVisitor = insertVisitor;
		return operation;
	}
	
	@Override
	public void reset() {
		super.reset();
		this.mNewElement = null;
		this.mNewElements = null;
		this.mIteratorVisitor = null;
		this.mVisitor = null;
	}

	@SuppressWarnings("unchecked")
	public boolean update(ListIterator<T> it, T t, Object defaultParam, IterationInfo info) {
		if (shouldUpdate(t, defaultParam)) {
			//System.out.println("update success: old element = " + t + " ,new element =" + mNewElement);
			if(t instanceof Updatable) {
				((Updatable<T>) t).updateFrom(mNewElement);
				info.incrementUpdate();
				return true;
			}else if(it != null){
				it.set(mNewElement);
				info.incrementUpdate();
				return true;
			}
			return false;
		}
		return false;
	}
	
	public boolean filter(T t, Object defaultParam, IterationInfo info){
		if(shouldFilter(t, defaultParam)){
			info.incrementFilter();
			return true;
		}
		return false;
	}
	
	public boolean delete(Iterator<T> it, T t, Object param, IterationInfo info){
		if(shouldDelete(t, param)){
			it.remove(); // 之前必须调用next().
			info.incrementDelete();
			return true;
		}
		return false;
	}

	private boolean shouldFilter(T t, Object defaultParam) {
		if (mOp == OP_FILTER && mVisitor != null) {
			Boolean result = mVisitor.visit(t, mParam != null ? mParam : defaultParam);
			return Predicates.isTrue(result);
		}
		return false;
	}
	
	private boolean shouldUpdate(T t, Object defaultParam) {
		if (mOp == OP_UPDATE && mVisitor != null) {
			Boolean result = mVisitor.visit(t, mParam != null ? mParam : defaultParam);
			return Predicates.isTrue(result);
		}
		return false;
	}

	private boolean shouldDelete(T t, Object defaultParam) {
		if (mOp == OP_DELETE && mVisitor != null) {
			Boolean result = mVisitor.visit(t, mParam != null ? mParam : defaultParam);
			return Predicates.isTrue(result);
		}
		return false;
	}

	/**
	 * indicate if should insert/insert finally.
	 * @param t may be null , if use as finally insert.
	 * @param defaultParam the default param
	 * @param info the IterationInfo
	 * @return true if should insert.
	 */
	private boolean shouldInsert(T t, Object defaultParam, IterationInfo info) {
		if (mOp == OP_INSERT && mIteratorVisitor != null) {
			Boolean result = mIteratorVisitor.visit(t, mParam != null ? mParam : defaultParam, info);
			return Predicates.isTrue(result);
		}

		return false;
	}

	public boolean insertLast(Collection<? super T> collection, Object param, IterationInfo info) {
		if (shouldInsert(null, param, info)) {
			if (mNewElements != null && mNewElements.size() > 0) {
				info.addInsert(mNewElements.size());
				return collection.addAll(mNewElements);
			}
			info.incrementInsert();
			return collection.add(mNewElement);
		}
		return false;
	}

	public boolean insert(ListIterator<T> lit, T t, Object param, IterationInfo info) {
		if (shouldInsert(t, param, info)) {
			if (mNewElements != null && mNewElements.size() > 0) {
				final List<T> mNewElements = this.mNewElements;
				final int size = mNewElements.size();
				for (int i = 0; i < size; i++) {
					lit.add(mNewElements.get(i));
				}
				info.addInsert(size);
				return true;
			}
			lit.add(mNewElement);
			info.incrementInsert();
			return true;
		}
		return false;
	}

}