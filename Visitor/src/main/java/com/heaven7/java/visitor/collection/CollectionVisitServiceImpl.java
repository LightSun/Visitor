package com.heaven7.java.visitor.collection;

import static com.heaven7.java.visitor.collection.Operation.OP_DELETE;
import static com.heaven7.java.visitor.collection.Operation.OP_FILTER;
import static com.heaven7.java.visitor.collection.Operation.OP_INSERT;
import static com.heaven7.java.visitor.collection.Operation.OP_UPDATE;
import static com.heaven7.java.visitor.internal.InternalUtil.unmodifiable;
import static com.heaven7.java.visitor.util.Throwables.checkEmpty;
import static com.heaven7.java.visitor.util.Throwables.checkNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.heaven7.java.visitor.IterateVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.SaveVisitor;
import com.heaven7.java.visitor.anno.Nullable;
import com.heaven7.java.visitor.collection.IterateControl.Callback;
import com.heaven7.java.visitor.internal.state.IterateState;
import com.heaven7.java.visitor.util.SparseArray;
import com.heaven7.java.visitor.util.Throwables;

/**
 * 
 * priority: delete > filter > update > insert.
 * 
 * @author heaven7
 *
 * @param <T>
 *            the type of element
 * @see ListVisitService
 */
public class CollectionVisitServiceImpl<T> extends AbstractCollectionVisitService<T> implements CollectionVisitService<T> {

	protected static final boolean DEBUG = true;

	/** the ordered operate */
	private List<Integer> mOrderOps;
	/** the intercept operate */
	private List<Integer> mInterceptOps;
	
	private final IterateControl<CollectionVisitService<T>> mControl;
	/** the operate interceptor */
	private final GroupOperateInterceptor mGroupInterceptor = new GroupOperateInterceptor();

	// OP_QUERY ;
	// limitSize ,limit filter size. limit null size.

	private final Collection<T> mCollection;
	
	/** the all Operation/operate of insert in iteration */
	private List<CollectionOperation<T>> mInsertOps;
	/** the all Operation/operate of final insert after iteration */
	private List<CollectionOperation<T>> mFinalInsertOps;
	/** the all Operation/operate of update in iteration */
	private List<CollectionOperation<T>> mUpdateOps;
	/** the Operation/operate of filter */
	private CollectionOperation<T> mFilterOp;
	/** the Operation/operate of delete or remove. */
	private CollectionOperation<T> mDeleteOp;

	private IterationInfo mIterationInfo;

	/*protected*/ CollectionVisitServiceImpl(Collection<T> collection) {
		super();
		checkNull(collection);
		this.mCollection = collection;
		mControl = IterateControl.<CollectionVisitService<T>>create(this, 
				new ControlCallbackImpl());
		//init default
		mControl.begin().end();
	}

	// =============================================================================//
	@Override
	public <R> R visitForResult(@Nullable Object param, PredicateVisitor<? super T> predicate,
			ResultVisitor<? super T, R> resultVisitor) {

		checkNull(predicate);
		checkNull(resultVisitor);
		final IterationInfo info = initAndGetIterationInfo();
		R r = IterateState.<T>singleIterateState().visitForResult(mCollection, hasExtraOperateInIteration(),
				mGroupInterceptor, info, param, predicate, resultVisitor, null);
		handleFinalInsert(param, info);
		return r;
	}

	@Override
	public <R> List<R> visitForResultList(@Nullable Object param, PredicateVisitor<? super T> predicate,
			ResultVisitor<? super T, R> resultVisitor, @Nullable List<R> out) {

		checkNull(predicate);
		checkNull(resultVisitor);
		if (out == null) {
			out = new ArrayList<R>();
		}
		final IterationInfo info = initAndGetIterationInfo();
		IterateState.<T>multipleIterateState().visitForResult(mCollection, hasExtraOperateInIteration(),
				mGroupInterceptor, info, param, predicate, resultVisitor, out);
		handleFinalInsert(param, info);
		return out;
	}

	@Override
	public List<T> visitForQueryList(@Nullable Object param, PredicateVisitor<? super T> predicate,
			@Nullable List<T> out) {
		checkNull(predicate);
		if (out == null) {
			out = new ArrayList<T>();
		}
		final IterationInfo info = initAndGetIterationInfo();
		IterateState.<T>multipleIterateState().visit(mCollection, hasExtraOperateInIteration(), mGroupInterceptor,
				info, param, predicate, out);
		handleFinalInsert(param, info);
		return out;
	}

	@Override
	public T visitForQuery(@Nullable Object param, PredicateVisitor<? super T> predicate) {
		checkNull(predicate);

		final IterationInfo info = initAndGetIterationInfo();
		T result = IterateState.<T>singleIterateState().visit(mCollection, hasExtraOperateInIteration(),
				mGroupInterceptor, info, param, predicate, null);
		handleFinalInsert(param, info);
		return result;
	}

	@Override
	protected boolean visit(int rule, @Nullable Object param, IterateVisitor<? super T> breakVisitor) {
		if (mCollection.size() == 0) {
			return false;
		}
		final IterationInfo info = initAndGetIterationInfo();

		mGroupInterceptor.begin();
		boolean result = visitImpl(mCollection, rule, param, mGroupInterceptor, breakVisitor, info);
		mGroupInterceptor.end();

		handleFinalInsert(param, info);
		return result;
	}

	protected boolean visitImpl(Collection<T> collection, int rule, @Nullable Object param,
			CollectionOperateInterceptor<T> interceptor, IterateVisitor<? super T> breakVisitor, final IterationInfo info) {

		final boolean hasExtra = hasExtraOperateInIteration();

		if (hasExtra) {
			final Iterator<T> it = collection.iterator();
			T t;
			for (; it.hasNext();) {
				t = it.next();
				if (interceptor.intercept(it, t, param, info)) {
					continue;
				}
				// ignore break , there is no need to break. just for test.
				/*
				 * if(DEBUG){ breakVisitor.visit(t, param, info); }
				 */
			}
		}
		return true;
	}

	public void reset() {
		mDeleteOp = null;
		mFilterOp = null;
		if (mInsertOps != null) {
			mInsertOps.clear();
		}
		if (mUpdateOps != null) {
			mUpdateOps.clear();
		}
		if (mFinalInsertOps != null) {
			mFinalInsertOps.clear();
		}
		mOrderOps.clear();
		mInterceptOps.clear();
		mControl.begin().end();
	}

	/**
	 * handle the all operations of insert.
	 * 
	 * @param inserts
	 *            the insert operations.
	 * @param it
	 *            the Iterator
	 * @param t
	 *            the element for current visit
	 * @param param
	 *            the extra parameter
	 * @param info
	 *            the IterationInfo
	 * @return true if handle success.
	 */
	protected boolean onHandleInsert(List<CollectionOperation<T>> inserts, Iterator<T> it, T t, Object param,
			IterationInfo info) {

		return false;
	}

	private boolean handleInsert(Iterator<T> it, T t, Object param, final IterationInfo info) {
		return mInsertOps != null && onHandleInsert(mInsertOps, it, t, param, info);
	}

	private void handleFinalInsert(Object param, final IterationInfo info) {
		info.setCurrentSize(mCollection.size());
		if (mFinalInsertOps != null) {
			// final boolean hasInfo = info != null;
			final List<CollectionOperation<T>> mInserts = this.mFinalInsertOps;
			try {
				for (int i = 0, len = mInserts.size(); i < len; i++) {
					if (mInserts.get(i).insertLast(mCollection, param, info)) {
						//info update :change to internal of CollectionOperation
					}
				}
			} catch (UnsupportedOperationException e) {
				System.err.println("insert failed. caused by the list is fixed. "
						+ "so can't modified. are your list comes from 'Arrays.asList(...)' ? ");
				return;
			}
		}
	}

	private boolean handleUpdate(Iterator<T> lit, T t, Object param, IterationInfo info) {
		final List<CollectionOperation<T>> mUpdates = this.mUpdateOps;
		if (mUpdates == null || mUpdates.size() == 0) {
			return false;
		}
		final boolean isList = lit != null && (lit instanceof ListIterator);
		CollectionOperation<T> op;
		try {
			for (int i = 0, size = mUpdates.size(); i < size; i++) {
				op = mUpdates.get(i);
				if (isList && op.update((ListIterator<T>) lit, t, param, info)) {
					return true;
				} else if (op.update(t, param,info)) {
					return true;
				}
			}
		} catch (UnsupportedOperationException e) {
			System.err.println("update failed. caused by the list is fixed. "
					+ "so can't modified. are your list comes from 'Arrays.asList(...)'? ");
			return false;
		}
		return false;
	}

	private boolean handleDelete(Iterator<T> it, T t, Object param, IterationInfo info) {
		if (mDeleteOp != null) {
			try {
				return mDeleteOp.delete(it, t, param, info);
				//it.remove(); // 之前必须调用next().
				//info.incrementDelete();
			} catch (UnsupportedOperationException e) {
				System.err.println("update failed. caused by the list is fixed. "
						+ "so can't modified. are your list comes from 'Arrays.asList(...)' ? ");
				return false;
			}
		}
		return false;
	}

	private boolean handleFilter(Iterator<T> it, T t, Object param, IterationInfo info) {
		if (mFilterOp != null && mFilterOp.filter(t, param, info)) {
			return true;
		}
		return false;
	}

	private IterationInfo initAndGetIterationInfo() {
		if (mIterationInfo != null) {
			mIterationInfo.reset();
		} else {
			mIterationInfo = new IterationInfo();
		}
		int size = mCollection.size();
		mIterationInfo.setOriginSize(size);
		mIterationInfo.setCurrentSize(size);
		return mIterationInfo;
	}

	/** indicate if has extra operate in iteration. */
	public boolean hasExtraOperateInIteration() {
		return mDeleteOp != null || mFilterOp != null || (mInsertOps != null && mInsertOps.size() > 0)
				|| (mUpdateOps != null && mUpdateOps.size() > 0);
	}

	private void ensureInserts() {
		if (mInsertOps == null) {
			mInsertOps = new ArrayList<>();
		}
	}

	private void ensureFinalInserts() {
		if (mFinalInsertOps == null) {
			mFinalInsertOps = new ArrayList<>();
		}
	}

	private void ensureUpdates() {
		if (mUpdateOps == null) {
			mUpdateOps = new ArrayList<>();
		}
	}

	@Override
	public IterateControl<CollectionVisitService<T>> beginIterateControl() {
		return mControl.begin();
	}

	@Override
	public OperateManager<T> beginOperateManager() {
		return new OperateManagerImpl();
	}

	private class GroupOperateInterceptor extends CollectionOperateInterceptor<T> {

		final SparseArray<CollectionOperateInterceptor<T>> mInterceptorMap;
		final List<CollectionOperateInterceptor<T>> mOrderInerceptors;
		boolean mIntercept_Delete;
		boolean mIntercept_Filter;
		boolean mIntercept_Update;
		boolean mIntercept_Insert;

		public GroupOperateInterceptor() {
			super();
			mOrderInerceptors = new ArrayList<CollectionOperateInterceptor<T>>(6);
			mInterceptorMap = new SparseArray<CollectionOperateInterceptor<T>>(6);
			addMemberInterceptors();
		}

		private void addMemberInterceptors() {
			mInterceptorMap.put(OP_DELETE, new CollectionOperateInterceptor<T>() {
				@Override
				public boolean intercept(Iterator<T> it, T t, Object param, IterationInfo info) {
					return handleDelete(it, t, param, info) && mIntercept_Delete;
				}
			});
			mInterceptorMap.put(OP_FILTER, new CollectionOperateInterceptor<T>() {
				@Override
				public boolean intercept(Iterator<T> it, T t, Object param, IterationInfo info) {
					return handleFilter(it, t, param, info) && mIntercept_Filter;
				}
			});
			mInterceptorMap.put(OP_UPDATE, new CollectionOperateInterceptor<T>() {
				@Override
				public boolean intercept(Iterator<T> it, T t, Object param, IterationInfo info) {
					return handleUpdate(it, t, param, info) && mIntercept_Update;
				}
			});
			mInterceptorMap.put(OP_INSERT, new CollectionOperateInterceptor<T>() {
				@Override
				public boolean intercept(Iterator<T> it, T t, Object param, IterationInfo info) {
					return handleInsert(it, t, param, info) && mIntercept_Insert;
				}
			});
		}

		@Override
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

			final SparseArray<CollectionOperateInterceptor<T>> mInterceptorMap = this.mInterceptorMap;
			final List<Integer> mOrderOps = CollectionVisitServiceImpl.this.mOrderOps;
			for (int i = 0, size = mOrderOps.size(); i < size; i++) {
				mOrderInerceptors.add(mInterceptorMap.get(mOrderOps.get(i)));
			}
		}

		@Override
		public void end() {
			mIntercept_Delete = false;
			mIntercept_Filter = false;
			mIntercept_Update = false;
			mIntercept_Insert = false;
			mOrderInerceptors.clear();
		}

		@Override
		public boolean intercept(Iterator<T> it, T t, Object param, IterationInfo info) {
			final List<CollectionOperateInterceptor<T>> mOrderInerceptors = this.mOrderInerceptors;
			for (int i = 0, size = mOrderInerceptors.size(); i < size; i++) {
				if (mOrderInerceptors.get(i).intercept(it, t, param, info)) {
					return true;
				}
			}
			return false;
		}

	}

	private class OperateManagerImpl extends OperateManager<T> {

		@Override
		public CollectionVisitServiceImpl<T> end() {
			return CollectionVisitServiceImpl.this;
		}

		@Override
		public OperateManager<T> filter(@Nullable Object param, PredicateVisitor<? super T> filter) {
			checkNull(filter);
			if (mFilterOp != null) {
				throw new IllegalArgumentException("filter can only set once");
			}
			mFilterOp = CollectionOperation.createFilter(param, filter);
			return this;
		}

		@Override
		public OperateManager<T> delete(@Nullable Object param, PredicateVisitor<? super T> delete) {
			checkNull(delete);
			if (mDeleteOp != null) {
				throw new IllegalArgumentException("deleteOn can only set once");
			}
			mDeleteOp = CollectionOperation.createDelete(param, delete);
			return this;
		}

		@Override
		public OperateManager<T> update(T newT, @Nullable Object param,
				PredicateVisitor<? super T> update) {
			checkNull(newT);
			checkNull(update);
			ensureUpdates();
			mUpdateOps.add(CollectionOperation.createUpdate(newT, param, update));
			return this;
		}

		@Override
		public OperateManager<T> insert(List<T> list, @Nullable Object param,
				IterateVisitor<? super T> insert) {
			checkEmpty(list);
			ensureInserts();
			mInsertOps.add(CollectionOperation.createInsert(list, param, insert));
			return this;
		}

		@Override
		public OperateManager<T> insert(T newT, @Nullable Object param,
				IterateVisitor<? super T> insert) {
			checkNull(newT);
			ensureInserts();
			mInsertOps.add(CollectionOperation.createInsert(newT, param, insert));
			return this;
		}

		@Override
		public OperateManager<T> insertFinally(T newT, @Nullable Object param,
				IterateVisitor<? super T> insert) {
			checkNull(newT);
			ensureFinalInserts();
			mFinalInsertOps.add(CollectionOperation.createInsert(newT, param, insert));
			return this;
		}

		@Override
		public OperateManager<T> insertFinally(List<T> list, @Nullable Object param,
				IterateVisitor<? super T> insert) {
			checkEmpty(list);
			ensureFinalInserts();
			mFinalInsertOps.add(CollectionOperation.createInsert(list, param, insert));
			return this;
		}

	}

	private class ControlCallbackImpl extends Callback{

		@Override
		public void saveState(List<Integer> orderOps, List<Integer> interceptOps) {
			mInterceptOps = interceptOps;
			mOrderOps = orderOps;
		}
		@Override
		public void checkOperation(int op) {
			if(op < OP_DELETE || op > OP_INSERT){
				throw new IllegalArgumentException("unsupport opertion");
			}
		}
		
	}

	@Override
	public CollectionVisitService<T> save(SaveVisitor<T> visitor) {
		Throwables.checkNull(visitor);
		visitAll();
		visitor.visit(unmodifiable(mCollection));
		return this;
	}
	
	@Override
	public CollectionVisitService<T> save(Collection<T> out, boolean clearBeforeSave) {
		Throwables.checkNull(out);
		visitAll();
		if(clearBeforeSave){
			out.clear();
		}
		out.addAll(mCollection);
		return this;
	}


}
