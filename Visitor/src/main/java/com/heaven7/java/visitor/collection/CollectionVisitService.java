package com.heaven7.java.visitor.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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

	private static final int OP_INSERT = 11;
	private static final int OP_DELETE = 13;
	private static final int OP_UPDATE = 13;
	private static final int OP_QUERY  = 14;
	private static final int OP_FILTER = 15;

	protected final Collection<T> mCollection;
	protected final List<Operation<T>> mOpList;

	protected CollectionVisitService(Collection<T> mCollection) {
		super();
		this.mCollection = mCollection;
		this.mOpList = new ArrayList<>();
	}

	public static <T> CollectionVisitService<T> from(Collection<T> Collection) {
		return new CollectionVisitService<>(Collection);
	}

	public CollectionVisitService<T> filter(PredicateVisitor<T> filter) {
		return this;
	}

	public CollectionVisitService<T> insertOnSuccess(T t) {
		return this;
	}

	public boolean visit(int rule, Object param, PredicateVisitor<T> predicate) {
		final Iterator<T> it = mCollection.iterator();

		boolean result = false;
		Boolean visitResult;

		switch (rule) {
		case VISIT_RULE_UNTIL_NULL:
			for (; it.hasNext();) {
				T t = it.next();
				/*if (mFilter.visit(t, param)) {
					continue;
				}*/
				visitResult = predicate.visit(it.next(), param);
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
		return false;

	}

	protected static class Operation<T> implements Comparable<Operation<T>> {
		int op;
		T otherT;
		PredicateVisitor<T> conditionVisitor;

		public Operation(int op) {
			super();
			this.op = op;
		}

		public Operation(int op, PredicateVisitor<T> conditionVisitor) {
			super();
			this.op = op;
			this.conditionVisitor = conditionVisitor;
		}

		public Operation(int op, T otherT, PredicateVisitor<T> conditionVisitor) {
			super();
			this.op = op;
			this.otherT = otherT;
			this.conditionVisitor = conditionVisitor;
		}

		@Override
		public int compareTo(CollectionVisitService.Operation<T> o) {
			return Integer.compare(this.op, o.op);
		}

	}

}
