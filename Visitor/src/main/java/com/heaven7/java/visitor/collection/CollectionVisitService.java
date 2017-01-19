package com.heaven7.java.visitor.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import static com.heaven7.java.visitor.util.Throwables.*;

public class CollectionVisitService<T> {

	/**
	 * the visit rule: visit all.
	 */
	public static final int VISIT_RULE_ALL = 1;
	/**
	 * the visit rule: until null
	 */
	public static final int VISIT_RULE_UNTIL_NULL = 2;
	/**
	 * the visit rule: until not null.
	 */
	public static final int VISIT_RULE_UNTIL_NOT_NULL = 3;

	protected static final int OP_FILTER = 11;
	protected static final int OP_DELETE = 12;
	protected static final int OP_UPDATE = 13;
	protected static final int OP_INSERT = 14;
	// OP_QUERY ;
	// limitSize ,limit filter size. limit null size.

	protected Collection<T> mCollection;
	protected List<Operation<T>> mInserts;
	protected List<Operation<T>> mUpdates;
	private Operation<T> mFilterOp;
	private Operation<T> mDeleteOp;

	protected CollectionVisitService() {
		super();
	}

	public CollectionVisitService<T> filter(Object param, PredicateVisitor<T> filter) {
		checkNull(filter);
		if (mFilterOp != null) {
			throw new IllegalArgumentException("filter can only set once");
		}
		this.mFilterOp = Operation.create(OP_FILTER, param, filter);
		return this;
	}

	public CollectionVisitService<T> insert(T newT, Object param, InsertVisitor<T> visitor) {
		checkNull(newT);
		ensureInserts();
		mInserts.add(Operation.createInsert(newT, param, visitor));
		return this;
	}

	public CollectionVisitService<T> delete(Object param, PredicateVisitor<T> delete) {
		checkNull(delete);
		if (mDeleteOp != null) {
			throw new IllegalArgumentException("deleteOn can only set once");
		}
		mDeleteOp = Operation.create(OP_DELETE, param, delete);
		;
		return this;
	}

	public CollectionVisitService<T> update(T newT, Object param, PredicateVisitor<T> update) {
		checkNull(newT);
		checkNull(update);
		ensureUpdates();
		mUpdates.add(Operation.create(OP_UPDATE, newT, param, update));
		return this;
	}

	public boolean visit(int rule, Object param, PredicateVisitor<T> stopVisitor) {
		final int size = mCollection.size();
		if (size == 0) {
			return false;
		}
		final Iterator<T> it = mCollection.iterator();

		boolean result = false;
		Boolean visitResult;

		switch (rule) {
		case VISIT_RULE_UNTIL_NULL:
			for (; it.hasNext();) {
				T t = it.next();
				if (shouldFilter(t,param)) {
					continue;
				}
				if (shouldDelte(t,param)) {
					it.remove();
					continue;
				}
				if (shouldUpdate(t, param)){
					//TODO update
					continue;
				}
					visitResult = stopVisitor.visit(it.next(), param);
				if (visitResult == null || !visitResult) {
					result = true;
					break;
				}
			}
			break;

		case VISIT_RULE_UNTIL_NOT_NULL:

			break;

		case VISIT_RULE_ALL:

			break;

		default:
			System.err.println("unsupport rule = " + rule);
			break;
		}
		//check insert
		//shouldInsert
		return false;

	}

	private boolean shouldUpdate(T t, Object param) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean shouldDelte(T t, Object param) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean shouldFilter(T t, Object param) {
		// TODO Auto-generated method stub
		return false;
	}


	private void ensureInserts() {
		if (mInserts == null) {
			mInserts = new ArrayList<>();
		}
	}

	private void ensureUpdates() {
		if (mUpdates == null) {
			mUpdates = new ArrayList<>();
		}
	}

}
