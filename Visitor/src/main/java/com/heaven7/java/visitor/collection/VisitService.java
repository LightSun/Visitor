package com.heaven7.java.visitor.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static com.heaven7.java.visitor.collection.VisitService.OP_DELETE;
import static com.heaven7.java.visitor.collection.VisitService.OP_FILTER;
import static com.heaven7.java.visitor.collection.VisitService.OP_INSERT;
import static com.heaven7.java.visitor.collection.VisitService.OP_UPDATE;
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
public abstract class VisitService<T> {

	/**
	 * the visit rule: visit all.
	 */
	public static final int VISIT_RULE_ALL = 11;
	/**
	 * the visit rule: until success
	 */
	public static final int VISIT_RULE_UNTIL_SUCCESS = 12;
	/**
	 * the visit rule: until failed.
	 */
	public static final int VISIT_RULE_UNTIL_FAILED = 13;

	public static final int OP_DELETE = 1;
	public static final int OP_FILTER = 2;
	public static final int OP_UPDATE = 3;
	public static final int OP_INSERT = 4;

	private final ArrayList<Integer> mOrderOps = new ArrayList<Integer>(6);
	private final ArrayList<Integer> mInterceptOps = new ArrayList<Integer>(6);
	// OP_QUERY ;
	// limitSize ,limit filter size. limit null size.

	protected final Collection<T> mCollection;
	protected List<Operation<T>> mInserts;
	protected List<Operation<T>> mUpdates;
	private Operation<T> mFilterOp;
	private Operation<T> mDeleteOp;

	protected VisitService(Collection<T> collection) {
		super();
		checkNull(collection);
		this.mCollection = collection;

		// by default only delete and fiter op will intercept iteration.
		// that means if success the iterator will skip and continue next
		// iteration.
		mInterceptOps.add(OP_DELETE);
		mInterceptOps.add(OP_FILTER);

		/**
		 * default order is delete -> filter -> update -> insert
		 */
		mOrderOps.add(OP_DELETE);
		mOrderOps.add(OP_FILTER);
		mOrderOps.add(OP_UPDATE);
		mOrderOps.add(OP_INSERT);
	}

	public static <T> VisitService<T> from(Collection<T> collection) {
		return new VisitService<T>(collection) {
		};
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

		return visitImpl(rule, param, breakVisitor, info);
	}

	protected boolean onInterceptIteration(Iterator<T> it, T t, Object param, IterationInfo info) {
		if (shouldDelete(t, param)) {
			it.remove(); // 之前必须调用next().
			info.incrementDelete();
			return true;
		}
		if (shouldFilter(t, param)) {
			info.incrementFilter();
			return true;
		}
		if (handleUpdate(it, t, param)) {
			info.incrementUpdate();
		}
		return false;
	}

	protected boolean visitImpl(int rule, Object param, IterateVisitor<? super T> breakVisitor,
			final IterationInfo info) {
		boolean result = false;
		Boolean visitResult;

		final Iterator<T> it = mCollection.iterator();

		switch (rule) {
		case VISIT_RULE_UNTIL_FAILED: {
			for (; it.hasNext();) {
				T t = it.next();
				if (onInterceptIteration(it, t, param, info)) {
					continue;
				}
				// insert, update(之后 是否continye ? ) . breakVisitor 访问顺序？
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
				if (onInterceptIteration(it, t, param, info)) {
					continue;
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
				if (onInterceptIteration(it, t, param, info)) {
					continue;
				}
				breakVisitor.visit(t, param, info);
			}
			result = true;
		}
			break;

		default:
			throw new RuntimeException("unsupport rule = " + rule);
		}
		if (result) {
			handleInsert(param, info);
		}
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

	public IterateControl<VisitService<T>> startIterateControl() {
		return new ProcessController(this);
	}

	private final class ProcessController extends IterateControl<VisitService<T>> {

		protected ProcessController(VisitService<T> src) {
			super(src);
		}

		@Override
		public ProcessController interceptIfSuccess(int operate) {
			int index = mInterceptOps.indexOf(operate);
			if (index == -1) {
				mInterceptOps.add(operate);
			}
			return this;
		}

		@Override
		public ProcessController first(int operate) {
			int index = mOrderOps.indexOf(operate);
			if (index != -1) {
				mOrderOps.remove(index);
			} else {
				throw new IllegalArgumentException("unsupport operate.");
			}
			mOrderOps.add(0, operate);
			return this;
		}

		@Override
		public ProcessController second(int operate) {
			int index = mOrderOps.indexOf(operate);
			if (index != -1) {
				mOrderOps.remove(index);
			} else {
				throw new IllegalArgumentException("unsupport operate.");
			}
			mOrderOps.add(mOrderOps.size() > 0 ? 1 : 0, operate);
			return this;
		}

		@Override
		public ProcessController then(int operate) {
			int index = mOrderOps.indexOf(operate);
			if (index != -1) {
				mOrderOps.remove(index);
			} else {
				throw new IllegalArgumentException("unsupport operate.");
			}
			final int size = mOrderOps.size();
			switch (size) {
			case 0:
				index = 0;
				break;

			case 1:
				index = 1;
				break;

			default:
				index = 2;
				break;
			}
			mOrderOps.add(index, operate);
			return this;
		}

		@Override
		public ProcessController last(int operate) {
			int index = mOrderOps.indexOf(operate);
			if (index != -1) {
				mOrderOps.remove(index);
			} else {
				throw new IllegalArgumentException("unsupport operate.");
			}
			mOrderOps.add(mOrderOps.size(), operate);
			return this;
		}

	}

}
