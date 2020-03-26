package com.heaven7.java.visitor.collection;

import com.heaven7.java.visitor.IterateVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.anno.Nullable;
import com.heaven7.java.visitor.collection.IterateControl.Callback;
import com.heaven7.java.visitor.internal.InternalUtil;
import com.heaven7.java.visitor.internal.OperationPools;
import com.heaven7.java.visitor.internal.state.IterateState;
import com.heaven7.java.visitor.item.*;
import com.heaven7.java.visitor.util.Collections2;
import com.heaven7.java.visitor.util.Observer;
import com.heaven7.java.visitor.util.SparseArray;
import com.heaven7.java.visitor.util.Throwables;

import java.util.*;

import static com.heaven7.java.visitor.collection.Operation.*;
import static com.heaven7.java.visitor.util.Predicates.isTrue;
import static com.heaven7.java.visitor.util.Throwables.checkEmpty;
import static com.heaven7.java.visitor.util.Throwables.checkNull;

/**
 *
 * priority: delete > filter > update > insert.
 *
 * @author heaven7
 *
 * @param <T>
 *            the type of element
 * @see ListVisitServiceImpl
 */
public class CollectionVisitServiceImpl<T> extends AbstractCollectionVisitService<T>
		implements CollectionVisitService<T> {

	//protected static final boolean DEBUG = true;

	private final Collection<T> mCollection;

	/** the ordered operate */
	private List<Integer> mOrderOps;
	/** the intercept operate */
	private List<Integer> mInterceptOps;

	private final IterateControl<CollectionVisitService<T>> mControl;
	/** the operate interceptor */
	private final GroupOperateInterceptor mGroupInterceptor = new GroupOperateInterceptor();

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
	/** the operate of insert if not exist. */
	private CollectionOperation<T> mInsertIfNotExistOp;
	/** the operate of delete if exist. */
	private CollectionOperation<T> mDeleteIfExistOp;

	private IterationInfo mIterationInfo;

	/** the flags of clean up, default is {@linkplain VisitService#FLAG_ALL} */
	private int mCleanUpFlags = FLAG_ALL;

	/* protected */ CollectionVisitServiceImpl(Collection<T> collection) {
		super();
		checkNull(collection);
		this.mCollection = collection;
		mControl = IterateControl.<CollectionVisitService<T>>create(this, new ControlCallbackImpl());
		// init default
		mControl.begin().end();
	}

	// ===============================================================================


	@Override
	public CollectionVisitService<Byte> mapByte() {
		return map(new ResultVisitor<T, Byte>() {
			@Override
			public Byte visit(T t, Object param) {
				if(t instanceof IByteItem){
					return ((IByteItem) t).getByte();
				}
				return null;
			}
		});
	}

	@Override
	public CollectionVisitService<Integer> mapInt() {
		return map(new ResultVisitor<T, Integer>() {
			@Override
			public Integer visit(T t, Object param) {
				if(t instanceof IIntItem){
					return ((IIntItem) t).getInt();
				}
				return null;
			}
		});
	}

	@Override
	public CollectionVisitService<Float> mapFloat() {
		return map(new ResultVisitor<T, Float>() {
			@Override
			public Float visit(T t, Object param) {
				if(t instanceof IFloatItem){
					return ((IFloatItem) t).getFloat();
				}
				return null;
			}
		});
	}

	@Override
	public CollectionVisitService<Double> mapDouble() {
		return map(new ResultVisitor<T, Double>() {
			@Override
			public Double visit(T t, Object param) {
				if(t instanceof IDoubleItem){
					return ((IDoubleItem) t).getDouble();
				}
				return null;
			}
		});
	}
	@Override
	public CollectionVisitService<String> mapString() {
		return map(new ResultVisitor<T, String>() {
			@Override
			public String visit(T t, Object param) {
				if(t instanceof IStringItem){
					return ((IStringItem) t).getString();
				}
				return null;
			}
		});
	}
	@Override
	public CollectionVisitService<List<T>> merge() {
		ArrayList<List<T>> result = new ArrayList<>();
		result.add(new ArrayList<>(mCollection));
		return VisitServices.from(result);
	}
	@SuppressWarnings("unchecked")
	public<R> CollectionVisitService<R> separate(){
		if(mCollection.isEmpty()){
			return VisitServices.from(new ArrayList<R>());
		}
		ArrayList<T> ts = new ArrayList<>(mCollection);
		if(ts.get(0) instanceof Collection){
			List<R> list = new ArrayList<>();
			int size = ts.size();
			try{
				for (int i = 0; i < size; i++) {
					Collection<R> t = (Collection<R>) ts.get(0);
					list.addAll(t);
				}
			}catch (ClassCastException e){
				throw new IllegalStateException("for separate. element must impl Collection, And also type mus be target type.");
			}
			return VisitServices.from(list);
		}else {
			throw new IllegalStateException("for separate. element must impl Collection.");
		}
	}

	@Override
	public int size() {
		return mCollection.size();
	}

	@Override
	public CollectionVisitService<T> clear() {
		mCollection.clear();
		return this;
	}

	@Override
	public CollectionVisitService<T> addIfNotExist(T newT, Observer<T, Void> observer) {
		try {
			if (!mCollection.contains(newT) && mCollection.add(newT)) {
				observer.onSuccess(null, null);
			} else {
				observer.onFailed(null, newT);
			}
		} catch (Exception e) {
			observer.onThrowable(null, newT, e);
		}
		return this;
	}

	@Override
	public CollectionVisitService<T> addIfNotExist(T newT) {
		if (!mCollection.contains(newT)) {
			mCollection.add(newT);
		}
		return this;
	}

	@Override
	public CollectionVisitService<T> removeIfExist(T newT) {
		mCollection.remove(newT);
		return this;
	}

	@Override
	public CollectionVisitService<T> removeIfExist(T newT, Observer<T, Void> observer) {
		try {
			if (mCollection.remove(newT)) {
				observer.onSuccess(null, null);
			} else {
				observer.onFailed(null, newT);
			}
		} catch (Exception e) {
			observer.onThrowable(null, newT, e);
		}
		return this;
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
		doLast(param);
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
		doLast(param);
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
		IterateState.<T>multipleIterateState().visit(mCollection, hasExtraOperateInIteration(), mGroupInterceptor, info,
				param, predicate, out);
		doLast(param);
		return out;
	}

	@Override
	public T visitForQuery(@Nullable Object param, PredicateVisitor<? super T> predicate) {
		checkNull(predicate);

		final IterationInfo info = initAndGetIterationInfo();
		T result = IterateState.<T>singleIterateState().visit(mCollection, hasExtraOperateInIteration(),
				mGroupInterceptor, info, param, predicate, null);
		doLast(param);
		return result;
	}

	/**
	 * get the iterator, list visit service should override this.
	 *
	 * @param coll
	 *            the collection.
	 * @return the {@linkplain Iterator}
	 */
	protected Iterator<T> getIterator(Collection<T> coll) {
		return coll.iterator();
	}

	@Override
	protected boolean visit(int rule, @Nullable Object param, IterateVisitor<? super T> breakVisitor) {
		final IterationInfo info = initAndGetIterationInfo();
		if (mCollection.size() == 0) {
			doLast(param);
			return false;
		}
		mGroupInterceptor.begin();
		boolean result = visitImpl(mCollection, rule, param, mGroupInterceptor, breakVisitor, info);
		mGroupInterceptor.end();
		doLast(param);
		return result;
	}

	protected void doLast(Object param) {
		handleFinal(param, mIterationInfo);
		reset(mCleanUpFlags);
		//next op will clear all. fix?
		//mCleanUpFlags = FLAG_ALL;
	}

	protected List<T> asList() {
		return Collections2.asList(mCollection);
	}

	@Override
	public CollectionVisitService<T> reset(int flags) {
		if ((flags & FLAG_OPERATE_MANAGER) != 0) {
			OperationPools.recycle(mDeleteOp);
			mDeleteOp = null;
			OperationPools.recycle(mFilterOp);
			mFilterOp = null;
			OperationPools.recycle(mInsertIfNotExistOp);
			mInsertIfNotExistOp = null;
			OperationPools.recycle(mDeleteIfExistOp);
			mDeleteIfExistOp = null;
			if (mInsertOps != null) {
				OperationPools.recycleAllCollectionOperation(mInsertOps);
				mInsertOps.clear();
			}
			if (mUpdateOps != null) {
				OperationPools.recycleAllCollectionOperation(mUpdateOps);
				mUpdateOps.clear();
			}
			if (mFinalInsertOps != null) {
				OperationPools.recycleAllCollectionOperation(mFinalInsertOps);
				mFinalInsertOps.clear();
			}
		}
		if ((flags & FLAG_OPERATE_ITERATE_CONTROL) != 0) {
			mOrderOps.clear();
			mInterceptOps.clear();
			mControl.begin().end();
		}
		return this;
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

	/**
	 * this is different with {@linkplain #hasExtraOperateInIteration} .
	 *
	 * @return true if has operation .
	 */
	protected boolean hasOperation() {
		if (mFinalInsertOps != null && mFinalInsertOps.size() > 0) {
			return true;
		}
		if (mInsertIfNotExistOp != null || mDeleteIfExistOp != null) {
			return true;
		}
		return hasExtraOperateInIteration();
	}

	private boolean handleInsert(Iterator<T> it, T t, Object param, final IterationInfo info) {
		return mInsertOps != null && onHandleInsert(mInsertOps, it, t, param, info);
	}

	private void handleFinal(Object param, final IterationInfo info) {
		info.setCurrentSize(mCollection.size());
		if (mFinalInsertOps != null) {
			// final boolean hasInfo = info != null;
			final List<CollectionOperation<T>> mInserts = this.mFinalInsertOps;
			try {
				for (int i = 0, len = mInserts.size(); i < len; i++) {
					if (mInserts.get(i).insertLast(mCollection, param, info)) {
						// info update :change to internal of
						// CollectionOperation
					}
				}
			} catch (UnsupportedOperationException e) {
				System.err.println("insert failed. caused by the list is fixed. "
						+ "so can't modified. are your list comes from 'Arrays.asList(...)' ? ");
				return;
			}
		}
		if (mInsertIfNotExistOp != null) {
			mInsertIfNotExistOp.insertIfNotExist(mCollection);
		}
		if (mDeleteIfExistOp != null) {
			mDeleteIfExistOp.deleteIfExist(mCollection);
		}
	}

	private boolean handleUpdate(Iterator<T> lit, T t, Object param, IterationInfo info) {
		final List<CollectionOperation<T>> mUpdates = this.mUpdateOps;
		if (mUpdates == null || mUpdates.size() == 0) {
			return false;
		}
		final ListIterator<T> listIt = (lit != null && (lit instanceof ListIterator)) ? (ListIterator<T>) lit : null;
		CollectionOperation<T> op;
		try {
			for (int i = 0, size = mUpdates.size(); i < size; i++) {
				op = mUpdates.get(i);
				if (op.update(listIt, t, param, info)) {
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
				// it.remove(); // 之前必须调用next().
				// info.incrementDelete();
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
			mInsertOps = new ArrayList<CollectionOperation<T>>();
		}
	}

	private void ensureFinalInserts() {
		if (mFinalInsertOps == null) {
			mFinalInsertOps = new ArrayList<CollectionOperation<T>>();
		}
	}

	private void ensureUpdates() {
		if (mUpdateOps == null) {
			mUpdateOps = new ArrayList<CollectionOperation<T>>();
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

	@Override
	public CollectionVisitService<T> subService(Object param, PredicateVisitor<T> visitor) {
		checkNull(visitor);
		final List<T> list = new ArrayList<T>();
		for (T t : asList()) {
			if (isTrue(visitor.visit(t, param))) {
				list.add(t);
			}
		}
		return (this instanceof ListVisitService) ? VisitServices.from(list) : VisitServices.from((Collection<T>) list);
	}

	@Override
	public <R> CollectionVisitService<R> zipService(@Nullable Object param, ResultVisitor<T, R> resultVisitor, Observer<T, List<R>> observer) {
		Throwables.checkNull(resultVisitor);
		Throwables.checkNull(observer);
		final List<R> results = new ArrayList<R>();
		CollectionVisitService<R> service =  InternalUtil.getVisitService(results, null, this instanceof ListVisitService);
		R r;
		T failedT = null;
		T curr = null ;
		try {
			for (T t : mCollection) {
				r = resultVisitor.visit((curr = t), param);
				if (r == null) {
					failedT = t;
					break;
				}
				results.add(r);
			}
		}catch (Throwable e){
			observer.onThrowable(param, curr, e);
			return service;
		}
		if(failedT != null){
			observer.onFailed(param, failedT);
		}else{
			observer.onSuccess(param, results);
		}
		return service;
	}
	//==================================== 1.2.0 =============================================

	@Override
	public Collection<T> get() {
		return mCollection;
	}
	//==========================================================================================

	// ============================== start--> private and static
	// ===========================

	private boolean visitImpl(Collection<T> collection, int rule, @Nullable Object param,
			CollectionOperateInterceptor<T> interceptor, IterateVisitor<? super T> breakVisitor,
			final IterationInfo info) {
		return executeVisit(rule, param, interceptor, breakVisitor, info, hasExtraOperateInIteration(),
				getIterator(collection));
	}

	protected static <T> boolean executeVisit(int rule, Object param, CollectionOperateInterceptor<T> interceptor,
			IterateVisitor<? super T> breakVisitor, IterationInfo info, boolean hasExtra, Iterator<T> it) {
		T t;
		boolean result = true;
		switch (rule) {
		case VISIT_RULE_ALL:
			if (hasExtra) {
				for (; it.hasNext();) {
					t = it.next();
					if (interceptor.intercept(it, t, param, info)) {
						continue;
					}
					// visit all
					if (breakVisitor != null) {
						breakVisitor.visit(t, param, info);
					}
				}
			} else {
				if (breakVisitor != null) {
					for (; it.hasNext();) {
						// visit all
						breakVisitor.visit(it.next(), param, info);
					}
				}
			}
			break;

		case VISIT_RULE_UNTIL_FAILED:
			if (hasExtra) {
				for (; it.hasNext();) {
					t = it.next();
					if (interceptor.intercept(it, t, param, info)) {
						continue;
					}
					// check failed
					if (!isTrue(breakVisitor.visit(t, param, info))) {
						result = false;
						break;
					}
				}
			} else {
				for (; it.hasNext();) {
					// check failed
					if (!isTrue(breakVisitor.visit(it.next(), param, info))) {
						result = false;
						break;
					}
				}
			}
			break;

		case VISIT_RULE_UNTIL_SUCCESS:
			if (hasExtra) {
				for (; it.hasNext();) {
					t = it.next();
					if (interceptor.intercept(it, t, param, info)) {
						continue;
					}
					// check success
					if (isTrue(breakVisitor.visit(t, param, info))) {
						result = false;
						break;
					}
				}
			} else {
				for (; it.hasNext();) {
					// check success
					if (isTrue(breakVisitor.visit(it.next(), param, info))) {
						result = false;
						break;
					}
				}
			}

			break;

		default:
			throw new RuntimeException("unsupport rule = " + rule);
		}
		return result;
	}

	// ============================== end --> private or static
	// ===========================

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

	protected class OperateManagerImpl extends OperateManager<T> {

		@Override
		public CollectionVisitService<T> end() {
			return CollectionVisitServiceImpl.this;
		}

		@Override
		public OperateManager<T> filter(@Nullable Object param, PredicateVisitor<? super T> filter) {
			checkNull(filter);
			mFilterOp = CollectionOperation.createFilter(param, filter);
			return this;
		}

		@Override
		public OperateManager<T> delete(@Nullable Object param, PredicateVisitor<? super T> delete) {
			checkNull(delete);
			mDeleteOp = CollectionOperation.createDelete(param, delete);
			return this;
		}

		@Override
		public OperateManager<T> update(T newT, @Nullable Object param, PredicateVisitor<? super T> update) {
			checkNull(newT);
			checkNull(update);
			ensureUpdates();
			mUpdateOps.add(CollectionOperation.createUpdate(newT, param, update));
			return this;
		}

		@Override
		public OperateManager<T> insert(List<T> list, @Nullable Object param, IterateVisitor<? super T> insert) {
			checkEmpty(list);
			ensureInserts();
			mInsertOps.add(CollectionOperation.createInsert(list, param, insert));
			return this;
		}

		@Override
		public OperateManager<T> insert(T newT, @Nullable Object param, IterateVisitor<? super T> insert) {
			checkNull(newT);
			ensureInserts();
			mInsertOps.add(CollectionOperation.createInsert(newT, param, insert));
			return this;
		}

		@Override
		public OperateManager<T> insertFinally(T newT, @Nullable Object param, IterateVisitor<? super T> insert) {
			checkNull(newT);
			ensureFinalInserts();
			mFinalInsertOps.add(CollectionOperation.createInsert(newT, param, insert));
			return this;
		}

		@Override
		public OperateManager<T> insertFinally(List<T> list, @Nullable Object param, IterateVisitor<? super T> insert) {
			checkEmpty(list);
			ensureFinalInserts();
			mFinalInsertOps.add(CollectionOperation.createInsert(list, param, insert));
			return this;
		}

		@Override
		public OperateManager<T> insertFinallyIfNotExist(T newT) {
			mInsertIfNotExistOp = CollectionOperation.createInsertIfNotExist(newT);
			return this;
		}

		@Override
		public OperateManager<T> deleteFinallyIfExist(T t) {
			mDeleteIfExistOp = CollectionOperation.createDeleteIfExist(t);
			return this;
		}

		@Override
		public OperateManager<T> cache() {
			mCleanUpFlags &= ~FLAG_OPERATE_MANAGER;
			return this;
		}
		@Override
		public OperateManager<T> noCache() {
			mCleanUpFlags |= FLAG_OPERATE_MANAGER;
			return this;
		}
	}

	private class ControlCallbackImpl extends Callback {

		@Override
		public void saveState(List<Integer> orderOps, List<Integer> interceptOps) {
			mInterceptOps = interceptOps;
			mOrderOps = orderOps;
		}

		@Override
		public void checkOperation(int op) {
			if (op < OP_DELETE || op > OP_INSERT) {
				throw new IllegalArgumentException("unsupport opertion");
			}
		}

		@Override
		public void applyCache() {
			mCleanUpFlags &= ~FLAG_OPERATE_ITERATE_CONTROL;
		}

		@Override
		public void applyNoCache() {
			mCleanUpFlags |=  FLAG_OPERATE_ITERATE_CONTROL;
		}
	}

}
