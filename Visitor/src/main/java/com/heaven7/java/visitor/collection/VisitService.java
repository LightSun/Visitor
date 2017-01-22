package com.heaven7.java.visitor.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import com.heaven7.java.visitor.util.SparseArray;

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
	
	protected static final boolean DEBUG = true;

	/**
	 * the visit rule: visit all.
	 */
	public static final int VISIT_RULE_ALL = 11;
	/**
	 * the visit rule: until success. this is useless for the 'Set' of
	 * 'Collection'
	 */
	public static final int VISIT_RULE_UNTIL_SUCCESS = 12;
	/**
	 * the visit rule: until failed. this is useless for the 'Set' of
	 * 'Collection'
	 */
	public static final int VISIT_RULE_UNTIL_FAILED = 13;

	public static final int OP_DELETE = 1;
	public static final int OP_FILTER = 2;
	public static final int OP_UPDATE = 3;
	public static final int OP_INSERT = 4;

	private final ArrayList<Integer> mOrderOps = new ArrayList<Integer>(6);
	private final ArrayList<Integer> mInterceptOps = new ArrayList<Integer>(6);
	private final GroupOperateInterceptor mGroupInterceptor = new GroupOperateInterceptor();

	// OP_QUERY ;
	// limitSize ,limit filter size. limit null size.

	private final Collection<T> mCollection;
	private List<Operation<T>> mInserts;
	private List<Operation<T>> mFinalInserts;
	private List<Operation<T>> mUpdates;
	private Operation<T> mFilterOp;
	private Operation<T> mDeleteOp;

	protected VisitService(Collection<T> collection) {
		super();
		checkNull(collection);
		this.mCollection = collection;

		initDefault();
	}

	private void initDefault() {
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

	public static <T> VisitService<T> from(List<T> list) {
		return new ListVisitService<T>(list);
	}

	// =============================================================================//

	public boolean visitAll(Object param, IterateVisitor<? super T> breakVisitor) {
		return visit(VISIT_RULE_ALL, param, breakVisitor);
	}
	
	public boolean visitAll(Object param) {
		return visit(VISIT_RULE_ALL, param, null);
	}

	public boolean visitUntilSuccess(Object param, IterateVisitor<? super T> breakVisitor) {
		checkNull(breakVisitor);
		return visit(VISIT_RULE_UNTIL_SUCCESS, param, breakVisitor);
	}

	public boolean visitUntilFailed(Object param, IterateVisitor<? super T> breakVisitor) {
		checkNull(breakVisitor);
		return visit(VISIT_RULE_UNTIL_FAILED, param, breakVisitor);
	}

	private boolean visit(int rule, Object param, IterateVisitor<? super T> breakVisitor) {
		int size = mCollection.size();
		if (size == 0) {
			return false;
		}
		final IterationInfo info = new IterationInfo();
		info.setOriginSize(size);
		info.setCurrentSize(size);

		mGroupInterceptor.begin();
		boolean result = visitImpl(mCollection, rule, param, mGroupInterceptor, breakVisitor, info);
		mGroupInterceptor.end();

		handleFinalInsert(param, info);
		return result;
	}

	protected boolean visitImpl(Collection<T> collection, int rule, Object param, OperateInterceptor<T> interceptor,
			IterateVisitor<? super T> breakVisitor, final IterationInfo info) {

		final Iterator<T> it = collection.iterator();
		T t;
		for (; it.hasNext();) {
			t = it.next();
			if (interceptor.intercept(it, t, param, info)) {
				continue;
			}
			// ignore break , there is no need to break. just for test.
			/*if(DEBUG){
				breakVisitor.visit(t, param, info);
			}*/
		}
		return true;
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
		if (mFinalInserts != null) {
			mFinalInserts.clear();
		}
		mOrderOps.clear();
		mInterceptOps.clear();
		initDefault();
	}

	protected boolean onHandleInsert(List<Operation<T>> inserts, Iterator<T> it, T t, Object param,
			IterationInfo info) {

		return false;
	}

	private boolean handleInsert(Iterator<T> it, T t, Object param, final IterationInfo info) {
		return mInserts != null && onHandleInsert(mInserts, it, t, param, info);
	}

	private void handleFinalInsert(Object param, final IterationInfo info) {
		info.setCurrentSize(mCollection.size());
		if (mFinalInserts != null) {
			final List<Operation<T>> mInserts = this.mFinalInserts;
			try {
				for (int i = 0, len = mInserts.size(); i < len; i++) {
					if (mInserts.get(i).insertLast(mCollection, param, info)) {
						info.incrementInsert();
						info.incrementCurrentSize();
					}
				}
			} catch (UnsupportedOperationException e) {
				System.err.println("insert failed. caused by the list is fixed. "
						+ "so can't modified. are your list comes from 'Arrays.asList(...)'");
				return;
			}
		}
	}

	private boolean handleUpdate(Iterator<T> lit, T t, Object param, IterationInfo info) {
		final List<Operation<T>> mUpdates = this.mUpdates;
		if (mUpdates == null || mUpdates.size() == 0) {
			return false;
		}
		final boolean isList = lit != null && (lit instanceof ListIterator);
		boolean modified = false;
		Operation<T> op;
		try {
			for (int i = 0, size = mUpdates.size(); i < size; i++) {
				op = mUpdates.get(i);
				if (isList && op.update((ListIterator<T>) lit, t, param)) {
					modified = true;
					break;
				} else if (op.update(t, param)) {
					modified = true;
					break;
				}
			}
		} catch (UnsupportedOperationException e) {
			System.err.println("update failed. caused by the list is fixed. "
					+ "so can't modified. are your list comes from 'Arrays.asList(...)'");
			return false;
		}
		if (modified) {
			info.incrementUpdate();
		}
		return modified;
	}

	private boolean handleDelete(Iterator<T> it, T t, Object param, IterationInfo info) {
		if (mDeleteOp != null && mDeleteOp.shouldDelete(t, param)) {
			try {
				it.remove(); // 之前必须调用next().
				info.incrementDelete();
				info.decrementCurrentSize();
			} catch (UnsupportedOperationException e) {
				System.err.println("update failed. caused by the list is fixed. "
						+ "so can't modified. are your list comes from 'Arrays.asList(...)'");
				return false;
			}
			return true;
		}
		return false;
	}

	private boolean handleFilter(Iterator<T> it, T t, Object param, IterationInfo info) {
		if (mFilterOp != null && mFilterOp.shouldFilter(t, param)) {
			info.incrementFilter();
			return true;
		}
		return false;
	}

	private void ensureInserts() {
		if (mInserts == null) {
			mInserts = new ArrayList<>();
		}
	}

	private void ensureFinalInserts() {
		if (mFinalInserts == null) {
			mFinalInserts = new ArrayList<>();
		}
	}

	private void ensureUpdates() {
		if (mUpdates == null) {
			mUpdates = new ArrayList<>();
		}
	}

	public IterateControl<VisitService<T>> beginIterateControl() {
		return new ProcessController();
	}
	public OperareManager<VisitService<T>, T> beginOperateManager() {
		return new OperateManagerImpl();
	}

	private class GroupOperateInterceptor extends OperateInterceptor<T> {

		final SparseArray<OperateInterceptor<T>> mInterceptorMap;
		final List<OperateInterceptor<T>> mOrderInerceptors;
		boolean mIntercept_Delete;
		boolean mIntercept_Filter;
		boolean mIntercept_Update;
		boolean mIntercept_Insert;

		public GroupOperateInterceptor() {
			super();
			mOrderInerceptors = new ArrayList<OperateInterceptor<T>>();
			mInterceptorMap = new SparseArray<OperateInterceptor<T>>();
			addMemberInterceptors();
		}

		private void addMemberInterceptors() {
			mInterceptorMap.put(OP_DELETE, new OperateInterceptor<T>() {
				@Override
				public boolean intercept(Iterator<T> it, T t, Object param, IterationInfo info) {
					return handleDelete(it, t, param, info) && mIntercept_Delete;
				}
			});
			mInterceptorMap.put(OP_FILTER, new OperateInterceptor<T>() {
				@Override
				public boolean intercept(Iterator<T> it, T t, Object param, IterationInfo info) {
					return handleFilter(it, t, param, info) && mIntercept_Filter;
				}
			});
			mInterceptorMap.put(OP_UPDATE, new OperateInterceptor<T>() {
				@Override
				public boolean intercept(Iterator<T> it, T t, Object param, IterationInfo info) {
					return handleUpdate(it, t, param, info) && mIntercept_Update;
				}
			});
			mInterceptorMap.put(OP_INSERT, new OperateInterceptor<T>() {
				@Override
				public boolean intercept(Iterator<T> it, T t, Object param, IterationInfo info) {
					return handleInsert(it, t, param, info) && mIntercept_Insert;
				}
			});
		}

		public void begin() {
			Integer op;
			for (int size = mInterceptOps.size(), i = size - 1; i >= 0; i--) {
				switch (op = mInterceptOps.get(i)) {
				case OP_DELETE:
					mIntercept_Delete = true;
					break;

				case OP_FILTER:
					mIntercept_Filter = true;
					break;

				case OP_UPDATE:
					mIntercept_Update = true;
					break;

				case OP_INSERT:
					mIntercept_Insert = true;
					break;

				default:
					System.err.println("unsupport operate = " + op);
					break;
				}
			}

			final SparseArray<OperateInterceptor<T>> mInterceptorMap = this.mInterceptorMap;
			final List<Integer> mOrderOps = VisitService.this.mOrderOps;
			for (int i = 0, size = mOrderOps.size(); i < size; i++) {
				mOrderInerceptors.add(mInterceptorMap.get(mOrderOps.get(i)));
			}
		}

		public void end() {
			mIntercept_Delete = false;
			mIntercept_Filter = false;
			mIntercept_Update = false;
			mIntercept_Insert = false;
			mOrderInerceptors.clear();
		}

		@Override
		public boolean intercept(Iterator<T> it, T t, Object param, IterationInfo info) {
			final List<OperateInterceptor<T>> mOrderInerceptors = this.mOrderInerceptors;
			for (int i = 0, size = mOrderInerceptors.size(); i < size; i++) {
				if (mOrderInerceptors.get(i).intercept(it, t, param, info)) {
					return true;
				}
			}
			return false;
		}

	}
	
	private class OperateManagerImpl extends OperareManager<VisitService<T>, T>{
		
		@Override
		public VisitService<T> end() {
			return VisitService.this;
		}

		@Override
		public OperareManager<VisitService<T>, T> filter(Object param, PredicateVisitor<? super T> filter) {
			checkNull(filter);
			if (mFilterOp != null) {
				throw new IllegalArgumentException("filter can only set once");
			}
			mFilterOp = Operation.createFilter(param, filter);
			return this;
		}

		@Override
		public OperareManager<VisitService<T>, T> delete(Object param, PredicateVisitor<? super T> delete) {
			checkNull(delete);
			if (mDeleteOp != null) {
				throw new IllegalArgumentException("deleteOn can only set once");
			}
			mDeleteOp = Operation.createDelete(param, delete);
			return this;
		}

		@Override
		public OperareManager<VisitService<T>, T> update(T newT, Object param, PredicateVisitor<? super T> update) {
			checkNull(newT);
			checkNull(update);
			ensureUpdates();
			mUpdates.add(Operation.createUpdate(newT, param, update));
			return this;
		}

		@Override
		public OperareManager<VisitService<T>, T> insert(List<T> list, Object param, IterateVisitor<? super T> insert) {
			checkEmpty(list);
			ensureInserts();
			mInserts.add(Operation.createInsert(list, param, insert));
			return this;
		}

		@Override
		public OperareManager<VisitService<T>, T> insert(T newT, Object param, IterateVisitor<? super T> insert) {
			checkNull(newT);
			ensureInserts();
			mInserts.add(Operation.createInsert(newT, param, insert));
			return this;
		}

		@Override
		public OperareManager<VisitService<T>, T> insertFinally(T newT, Object param, IterateVisitor<? super T> insert) {
			checkNull(newT);
			ensureFinalInserts();
			mFinalInserts.add(Operation.createInsert(newT, param, insert));
			return this;
		}

		@Override
		public OperareManager<VisitService<T>, T> insertFinally(List<T> list, Object param,
				IterateVisitor<? super T> insert) {
			checkEmpty(list);
			ensureFinalInserts();
			mFinalInserts.add(Operation.createInsert(list, param, insert));
			return this;
		}
		
	}

	private final class ProcessController extends IterateControl<VisitService<T>> {

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
			/*if(DEBUG){
				System.err.println();
			}*/
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
			if(size >= 3){
				mOrderOps.add(2, operate);
			}else{
				mOrderOps.add(operate);
			}
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

		@Override
		public VisitService<T> end() {
			if(DEBUG){
				System.out.println("the operate order is: ");
				for(int op : mOrderOps){
					System.out.println(op2String(op));
				}
				System.out.println();
			}
			return VisitService.this;
		}
	}
	
	private static String op2String(int op){
		switch (op) {
		case OP_DELETE:
			return "OP_DELETE";
			
		case OP_FILTER:
			return "OP_FILTER";
			
		case OP_UPDATE:
			return "OP_UPDATE";
			
		case OP_INSERT:
			return "OP_INSERT";
		default:
			return null;
		}
	}

}
