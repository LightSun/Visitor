package com.heaven7.java.visitor.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static com.heaven7.java.visitor.util.Throwables.*;

/**
 * 
 * priority: delete > filter > update > insert.
 * 
 * @author heaven7
 *
 * @param <T>
 *            the type of visit
 */
public class VisitService<T> {

	/**
	 * the visit rule: visit all.
	 */
	public static final int VISIT_RULE_ALL = 1;
	/**
	 * the visit rule: until null
	 */
	public static final int VISIT_RULE_UNTIL_SUCCESS = 2;
	/**
	 * the visit rule: until not null.
	 */
	public static final int VISIT_RULE_UNTIL_FAILED = 3;

	// OP_QUERY ;
	// limitSize ,limit filter size. limit null size.

	protected Collection<T> mCollection;
	protected List<Operation<T>> mInserts;
	protected List<Operation<T>> mUpdates;
	private Operation<T> mFilterOp;
	private Operation<T> mDeleteOp;

	protected VisitService() {
		super();
	}

	public static <T> VisitService<T> newService() {
		return new VisitService<T>();
	}
	public static <T> VisitService<T> newService(Collection<T> collection) {
		return new VisitService<T>().view(collection);
	}

	public VisitService<T> view(Collection<T> collection) {
		checkNull(collection);
		reset();
		this.mCollection = collection;
		return this;
	}

	public VisitService<T> filter(PredicateVisitor<? super T> filter) {
		return filter(null, filter);
	}

	public VisitService<T> filter(Object param, PredicateVisitor<? super T> filter) {
		checkNull(filter);
		if (mFilterOp != null) {
			throw new IllegalArgumentException("filter can only set once");
		}
		this.mFilterOp = Operation.createFilter(param, filter);
		return this;
	}

	public VisitService<T> insert(T newT, Object param, IterateVisitor<? super T> insert) {
		checkNull(newT);
		ensureInserts();
		mInserts.add(Operation.createInsert(newT, param, insert));
		return this;
	}

	public VisitService<T> insert(T newT, IterateVisitor<T> insert) {
		return insert(newT, null, insert);
	}

	public VisitService<T> delete(Object param, PredicateVisitor<? super T> delete) {
		checkNull(delete);
		if (mDeleteOp != null) {
			throw new IllegalArgumentException("deleteOn can only set once");
		}
		mDeleteOp = Operation.createDelete(param, delete);
		return this;
	}

	public VisitService<T> delete(PredicateVisitor<? super T> delete) {
		return delete(null, delete);
	}

	public VisitService<T> update(T newT, PredicateVisitor<? super T> update) {
		return update(newT, null, update);
	}

	public VisitService<T> update(T newT, Object param, PredicateVisitor<? super T> update) {
		checkNull(newT);
		checkNull(update);
		ensureUpdates();
		mUpdates.add(Operation.createUpdate(newT, param, update));
		return this;
	}
	// =============================================================================//

	public boolean visitAll(Object param, IterateVisitor<? super T> breakVisitor) {
		return visit(VISIT_RULE_ALL, param, breakVisitor);
	}

	public boolean visitUntilSuccess(Object param, IterateVisitor<? super T> breakVisitor) {
		return visit(VISIT_RULE_UNTIL_SUCCESS, param, breakVisitor);
	}

	public boolean visitUntilFailed(Object param, IterateVisitor<? super T> breakVisitor) {
		return visit(VISIT_RULE_UNTIL_FAILED, param, breakVisitor);
	}

	private boolean visit(int rule, Object param, IterateVisitor<? super T> breakVisitor) {
		checkNull(breakVisitor);
		int size = mCollection.size();
		if (size == 0) {
			return false;
		}
		final IterationInfo info = new IterationInfo();
		info.setOriginSize(size);
		info.setCurrentSize(size);

		boolean result = false;
		Boolean visitResult;

		final Iterator<T> it = mCollection.iterator();

		switch (rule) {
		case VISIT_RULE_UNTIL_FAILED: {
			for (; it.hasNext();) {
				T t = it.next();
				if (shouldDelete(t, param)) {
					it.remove();
					info.incrementDelete();
					continue;
				}
				if (shouldFilter(t, param)) {
					info.incrementFilter();
					continue;
				}
				if (handleUpdate(it, t, param)) {
					info.incrementUpdate();
				}
				visitResult = breakVisitor.visit(t, param, info);
				if (visitResult == null || !visitResult) {
					result = true;
					break;
				}
			}
		}
			break;

		case VISIT_RULE_UNTIL_SUCCESS: {
			for (; it.hasNext();) {
				T t = it.next();
				if (shouldDelete(t, param)) {
					it.remove();
					info.incrementDelete();
					continue;
				}
				if (shouldFilter(t, param)) {
					info.incrementFilter();
					continue;
				}
				if (handleUpdate(it, t, param)) {
					info.incrementUpdate();
				}
				visitResult = breakVisitor.visit(t, param, info);
				if (visitResult != null && visitResult) {
					result = true;
					break;
				}
			}
		}
			break;

		case VISIT_RULE_ALL: {
			for (; it.hasNext();) {
				T t = it.next();
				if (shouldDelete(t, param)) {
					it.remove();
					info.incrementDelete();
					continue;
				}
				if (shouldFilter(t, param)) {
					info.incrementFilter();
					continue;
				}
				if (handleUpdate(it, t, param)) {
					info.incrementUpdate();
				}
				breakVisitor.visit(t, param, info);
			}
			result = true;
		}
			break;

		default:
			throw new RuntimeException("unsupport rule = " + rule);
		}
		handleInsert(param, info);
		return result;
	}

	public void reset() {
		mDeleteOp = null;
		mFilterOp = null;
		if (mInserts != null) {
			mInserts.clear();
		}
		if (mUpdates != null) {
			mUpdates.clear();
		}
	}

	private void handleInsert(Object param, final IterationInfo info) {
		info.setCurrentSize(mCollection.size());
		if (mInserts != null) {
			final List<Operation<T>> mInserts = this.mInserts;
			for (int i = 0, len = mInserts.size(); i < len; i++) {
				if (mInserts.get(i).insert(mCollection, param, info)) {
					info.incrementInsert();
					info.incrementCurrentSize();
				}
			}
		}
	}

	// private boolean handleInsert()

	private boolean handleUpdate(Iterator<T> lit, T t, Object param) {
		final List<Operation<T>> mUpdates = this.mUpdates;
		if (mUpdates == null || mUpdates.size() == 0) {
			return false;
		}
		final boolean isList = lit != null && (lit instanceof ListIterator);
		Operation<T> op;
		for (int i = 0, size = mUpdates.size(); i < size; i++) {
			op = mUpdates.get(i);
			if (isList && op.update((ListIterator<T>) lit, t, param)) {
				return true;
			}
			if (op.update(t, param)) {
				return true;
			}
		}
		return false;
	}

	private boolean shouldDelete(T t, Object param) {
		return mDeleteOp != null && mDeleteOp.shouldDelete(t, param);
	}

	private boolean shouldFilter(T t, Object param) {
		return mFilterOp != null && mFilterOp.shouldFilter(t, param);
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
