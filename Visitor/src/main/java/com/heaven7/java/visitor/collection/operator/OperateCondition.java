package com.heaven7.java.visitor.collection.operator;

import java.util.Collection;
import java.util.Comparator;

import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.util.Observer;
import com.heaven7.java.visitor.util.Observers;

/**
 * the common operate condition.
 * 
 * @author heaven7
 *
 * @param <T>
 *            the element type.
 * @param <R>
 *            the result type.
 * @since 2.0.0
 */
public class OperateCondition<T, R> implements CollectionCondition<T> {

	/**
	 * the require flag:which indicate the {@linkplain ResultVisitor}.
	 * @see #result(ResultVisitor)
	 */
	public static final int FLAG_RESULT         = 0x0001;
	/**
	 * the require flag:which indicate the {@linkplain PredicateVisitor}.
	 * @see #predicate(PredicateVisitor)
	 */
	public static final int FLAG_PREDICATE      = 0x0002;
	/**
	 * the require flag:which indicate the single element.
	 * @see #focus(Object)
	 */
	public static final int FLAG_SINGLE_ELEMENT = 0x0004;
	/**
	 * the require flag:which indicate another collection.
	 * @see #focus(Collection)
	 */
	public static final int FLAG_COLLECTION     = 0x0008;

	/** the flags which is already set. */
	private int mSettingFlags;

	private Observer<? super T, R> mObserver = Observers.defaultObserver();

	private Collection<? extends T> mOther;
	private Object mParam;
	private T mElement;

	private ResultVisitor<? super T, ?> mResult;
	private PredicateVisitor<? super T> mPredicate;
	private Comparator<?> mComparator;

	private Operator<T, R> mOperator;

	//private Collection<T> mTempTarget;

	protected OperateCondition() {
	}

	// =============================== static method =========================

	/*public OperateCondition<T, R> after(final CollectionCondition<T> post) {
		Observer<? super T, R> observer = getObserver();
		observer(new Observers.WrappedObserver<T, R>(observer){
			@Override
			public void onSuccess(Object param, R r) {
				super.onSuccess(param, r);
				post.apply(mTempTarget);
			}
		});
		return this;
	}*/
	// ============================ start dynamic method

	public OperateCondition<T, R> observer(Observer<? super T, R> observer) {
		this.mObserver = (Observer<? super T, R>) (observer != null ? observer : Observers.<T, R>defaultObserver());
		return this;
	}

	public OperateCondition<T, R> focus(T t) {
		this.mElement = t;
		if (t != null) {
			mSettingFlags |= FLAG_SINGLE_ELEMENT;
		} else {
			mSettingFlags &= ~FLAG_SINGLE_ELEMENT;
		}
		return this;
	}

	public OperateCondition<T, R> focus(Collection<? extends T> collection) {
		this.mOther = collection;
		if (collection != null) {
			mSettingFlags |= FLAG_COLLECTION;
		} else {
			mSettingFlags &= ~FLAG_COLLECTION;
		}
		return this;
	}

	public OperateCondition<T, R> extra(Object parameter) {
		this.mParam = parameter;
		return this;
	}

	public OperateCondition<T, R> predicate(PredicateVisitor<? super T> predicate) {
		this.mPredicate = predicate;
		if (predicate != null) {
			mSettingFlags |= FLAG_PREDICATE;
		} else {
			mSettingFlags &= ~FLAG_PREDICATE;
		}
		return this;
	}

	public OperateCondition<T, R> result(ResultVisitor<? super T, ?> result) {
		this.mResult = result;
		if (result != null) {
			mSettingFlags |= FLAG_RESULT;
		} else {
			mSettingFlags &= ~FLAG_RESULT;
		}
		return this;
	}

	public OperateCondition<T, R> comparator(Comparator<?> c) {
		this.mComparator = c;
		return this;
	}

	public OperateCondition<T, R> operator(Operator<T, R> op) {
		this.mOperator = op;
		return this;
	}

	private void checkArguments() {
		// check operator(must)
		if (mOperator == null) {
			throw new NullPointerException("require Operator, but didn't set.");
		}
		final int requireArgsFlags = mOperator.getRequireArgsFlags();
		if (requireArgsFlags > 0) {
			int share = requireArgsFlags & mSettingFlags;
			// real require
			int lostFlags = requireArgsFlags & ~share;
			if (lostFlags != 0) {
				if ((lostFlags & FLAG_SINGLE_ELEMENT) != 0) {
					throw new IllegalArgumentException("require a element, but didn't set.");
				}
				if ((lostFlags & FLAG_COLLECTION) != 0) {
					throw new IllegalArgumentException("require another collection, but didn't set.");
				}
				if ((lostFlags & FLAG_PREDICATE) != 0) {
					throw new IllegalArgumentException("require PredicateVisitor, but didn't set.");
				}
				if ((lostFlags & FLAG_RESULT) != 0) {
					throw new IllegalArgumentException("require ResultVisitor, but didn't set.");
				}
			}
		}
	}

	public void reset() {
		mOperator = null;
		mObserver = Observers.<T, R>defaultObserver();
		mElement = null;
		mOther = null;
		mParam = null;

		mComparator = null;
		mPredicate = null;
		mResult = null;
		mSettingFlags = 0;
		// reset pre condition
	}

	// ==============================================================

	// ============================ end dynamic method ===================

	public Observer<? super T, R> getObserver() {
		return mObserver;
	}

	public Collection<? extends T> getOtherCollection() {
		return mOther;
	}

	public Object getParam() {
		return mParam;
	}

	public T getElement() {
		return mElement;
	}

	public ResultVisitor<? super T, ?> getResultVisitor() {
		return mResult;
	}

	public PredicateVisitor<? super T> getPredicateVisitor() {
		return mPredicate;
	}

	public Comparator<?> getComparator() {
		return mComparator;
	}

	public Operator<T, R> getOperator() {
		return mOperator;
	}
	// ============================================

	@Override
	public boolean apply(Collection<T> src) {
		checkArguments();
		/*mTempTarget = src;
		boolean result = mOperator.apply(src, this);
		mTempTarget = null;
		return result;*/
		final Operator<T, R> operator = getOperator();
		mOperator = null;
		return operator.apply(src, this);
	}

}