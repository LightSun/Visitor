package com.heaven7.java.visitor.collection;

import static com.heaven7.java.visitor.internal.InternalUtil.getVisitService;
import static com.heaven7.java.visitor.internal.InternalUtil.newMap;
import static com.heaven7.java.visitor.internal.InternalUtil.processThrowable;
import static com.heaven7.java.visitor.internal.InternalUtil.unmodifiable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.heaven7.java.visitor.FireBatchVisitor;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.IterateVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.SaveVisitor;
import com.heaven7.java.visitor.ThrowableVisitor;
import com.heaven7.java.visitor.Visitors;
import com.heaven7.java.visitor.anno.Nullable;
import com.heaven7.java.visitor.internal.InternalUtil;
import com.heaven7.java.visitor.util.Observer;
import com.heaven7.java.visitor.util.Throwables;

/**
 * the abstract visit service
 * 
 * @author heaven7
 *
 * @param <T>
 *            the type
 * @see CollectionVisitServiceImpl
 * @see ListVisitServiceImpl
 */
public abstract class AbstractCollectionVisitService<T> implements CollectionVisitService<T> {

	private final List<T> mCacheList = new ArrayList<T>();

	protected AbstractCollectionVisitService() {
		super();
	}

	@Override
	public CollectionVisitService<T> zip(@Nullable Object param, PredicateVisitor<T> visitor, 
			Observer<T> observer) {
		Throwables.checkNull(visitor);
		Throwables.checkNull(observer);
		WrappedObserveVisitor<T> observeVisitor = new WrappedObserveVisitor<T>(param, 
				visitor, observer);
		try {
			if (visitUntilFailed(param, observeVisitor)) {
				observeVisitor.onSuccess();
			} else {
				observeVisitor.onFailed();
			}
		} catch (Throwable e) {
			observeVisitor.onThrowable(e);
		}
		return this;
	}

	@Override
	public final CollectionVisitService<T> fireBatch(FireBatchVisitor<T> visitor) {
		return fireBatch(visitor, null);
	}

	@Override
	public final CollectionVisitService<T> fireBatch(FireBatchVisitor<T> visitor, ThrowableVisitor tv) {
		return fireBatch(null, visitor, tv);
	}

	@Override
	public final CollectionVisitService<T> fireBatch(Object param, FireBatchVisitor<T> visitor, ThrowableVisitor tv) {
		Throwables.checkNull(visitor);
		final List<T> list = visitForQueryList(Visitors.truePredicateVisitor(), mCacheList);
		try {
			visitor.visit(list, param);
		} catch (Throwable e) {
			processThrowable(e, tv);
		} finally {
			list.clear();
		}
		return this;
	}

	@Override
	public final CollectionVisitService<T> fire(FireVisitor<T> fireVisitor) {
		return fire(fireVisitor, null);
	}

	@Override
	public final CollectionVisitService<T> fire(FireVisitor<T> visitor, ThrowableVisitor tv) {
		return fire(null, visitor, tv);
	}

	@Override
	public final CollectionVisitService<T> fire(Object param, FireVisitor<T> visitor, ThrowableVisitor tv) {
		Throwables.checkNull(visitor);
		final List<T> list = visitForQueryList(Visitors.truePredicateVisitor(), mCacheList);
		try {
			for (T t : list) {
				visitor.visit(t, param);
			}
		} catch (Throwable e) {
			processThrowable(e, tv);
		} finally {
			list.clear();
		}
		return this;
	}

	@Override
	public final CollectionVisitService<T> resetAll() {
		return reset(FLAG_ALL);
	}

	@Override
	public final CollectionVisitService<T> save(Collection<T> out) {
		return save(out, false);
	}

	@Override
	public final CollectionVisitService<T> save(SaveVisitor<T> visitor) {
		Throwables.checkNull(visitor);
		final List<T> results = visitForQueryList(Visitors.truePredicateVisitor(), mCacheList);
		try {
			visitor.visit(unmodifiable(results));
		} finally {
			results.clear();
		}
		return this;
	}

	@Override
	public final CollectionVisitService<T> save(Collection<T> out, boolean clearBeforeSave) {
		Throwables.checkNull(out);
		if (clearBeforeSave) {
			out.clear();
		}
		final List<T> results = visitForQueryList(Visitors.truePredicateVisitor(), mCacheList);
		out.addAll(results);
		results.clear();
		return this;
	}

	@Override
	public final <K> MapVisitService<K, List<T>> transformToMapByGroup(ResultVisitor<T, K> keyVisitor) {
		return transformToMapByGroup(null, keyVisitor);
	}

	@Override
	public final <K> MapVisitService<K, List<T>> transformToMapByGroup(Comparator<? super K> comparator,
			ResultVisitor<T, K> keyVisitor) {
		return transformToMapByGroup(null, comparator, keyVisitor);
	}

	@Override
	public final <K> MapVisitService<K, List<T>> transformToMapByGroup(Object param, Comparator<? super K> comparator,
			ResultVisitor<T, K> keyVisitor) {
		Throwables.checkNull(keyVisitor);
		final List<T> list = visitForQueryList(Visitors.truePredicateVisitor(), mCacheList);
		try {
			final Map<K, List<T>> map = InternalUtil.newMap(comparator);
			K key;
			List<T> value;
			for (T t : list) {
				key = keyVisitor.visit(t, param);
				value = map.get(key);
				if (value == null) {
					value = new ArrayList<T>();
					map.put(key, value);
				}
				value.add(t);
			}
			return VisitServices.from(map);
		} finally {
			list.clear();
		}
	}

	@Override
	public final <K, V> MapVisitService<K, List<V>> transformToMapByGroupValue(ResultVisitor<T, K> keyVisitor,
			ResultVisitor<T, V> valueVisitor) {
		return transformToMapByGroupValue(null, keyVisitor, valueVisitor);
	}

	@Override
	public final <K, V> MapVisitService<K, List<V>> transformToMapByGroupValue(Comparator<? super K> comparator,
			ResultVisitor<T, K> keyVisitor, ResultVisitor<T, V> valueVisitor) {
		return transformToMapByGroupValue(null, comparator, keyVisitor, valueVisitor);
	}

	@Override
	public final <K, V> MapVisitService<K, List<V>> transformToMapByGroupValue(Object param,
			Comparator<? super K> comparator, ResultVisitor<T, K> keyVisitor, ResultVisitor<T, V> valueVisitor) {
		Throwables.checkNull(keyVisitor);
		Throwables.checkNull(valueVisitor);
		final List<T> list = visitForQueryList(Visitors.truePredicateVisitor(), mCacheList);
		try {
			final Map<K, List<V>> map = InternalUtil.newMap(comparator);
			K key;
			List<V> value;
			for (T t : list) {
				key = keyVisitor.visit(t, param);
				value = map.get(key);
				if (value == null) {
					value = new ArrayList<V>();
					map.put(key, value);
				}
				value.add(valueVisitor.visit(t, param));
			}
			return VisitServices.from(map);
		} finally {
			list.clear();
		}
	}

	@Override
	public final <K> MapVisitService<K, T> transformToMapAsValues(ResultVisitor<? super T, K> keyVisitor) {
		return transformToMapAsValues(null, keyVisitor);
	}

	@Override
	public final <K> MapVisitService<K, T> transformToMapAsValues(Object param,
			ResultVisitor<? super T, K> keyVisitor) {
		return transformToMapAsValues(param, null, keyVisitor);
	}

	@Override
	public final <K> MapVisitService<K, T> transformToMapAsValues(Object param, Comparator<? super K> comparator,
			ResultVisitor<? super T, K> keyVisitor) {
		Throwables.checkNull(keyVisitor);
		final List<T> list = visitForQueryList(Visitors.truePredicateVisitor(), mCacheList);
		final Map<K, T> map = newMap(comparator);
		for (T t : list) {
			map.put(keyVisitor.visit(t, param), t);
		}
		list.clear();
		return VisitServices.from(map);
	}

	@Override
	public final <V> MapVisitService<T, V> transformToMapAsKeys(ResultVisitor<? super T, V> valueVisitor) {
		return transformToMapAsKeys(null, valueVisitor);
	}

	@Override
	public final <V> MapVisitService<T, V> transformToMapAsKeys(Object param,
			ResultVisitor<? super T, V> valueVisitor) {
		return transformToMapAsKeys(param, null, valueVisitor);
	}

	@Override
	public final <V> MapVisitService<T, V> transformToMapAsKeys(Object param, Comparator<? super T> comparator,
			ResultVisitor<? super T, V> valueVisitor) {
		Throwables.checkNull(valueVisitor);

		final List<T> list = visitForQueryList(Visitors.truePredicateVisitor(), mCacheList);
		final Map<T, V> map = newMap(comparator);
		for (T t : list) {
			map.put(t, valueVisitor.visit(t, param));
		}
		list.clear();
		return VisitServices.from(map);
	}

	@Override
	public final <K, V> MapVisitService<K, V> transformToMap(ResultVisitor<? super T, K> keyVisitor,
			ResultVisitor<? super T, V> valueVisitor) {
		return transformToMap(null, keyVisitor, valueVisitor);
	}

	@Override
	public final <K, V> MapVisitService<K, V> transformToMap(Object param, ResultVisitor<? super T, K> keyVisitor,
			ResultVisitor<? super T, V> valueVisitor) {
		return transformToMap(param, null, keyVisitor, valueVisitor);
	}

	@Override
	public final <K, V> MapVisitService<K, V> transformToMap(Object param, Comparator<? super K> comparator,
			ResultVisitor<? super T, K> keyVisitor, ResultVisitor<? super T, V> valueVisitor) {
		Throwables.checkNull(keyVisitor);
		Throwables.checkNull(valueVisitor);

		final List<T> list = visitForQueryList(param, Visitors.truePredicateVisitor(), mCacheList);
		final Map<K, V> map = newMap(comparator);
		for (T t : list) {
			map.put(keyVisitor.visit(t, param), valueVisitor.visit(t, param));
		}
		list.clear();
		return VisitServices.from(map);
	}

	@Override
	public final <R> CollectionVisitService<R> transformToCollection(Object param, Comparator<? super R> sort,
			ResultVisitor<? super T, R> resultVisitor) {
		Throwables.checkNull(resultVisitor);
		return getVisitService(visitForResultList(param, resultVisitor, null), sort,
				(this instanceof ListVisitService));
	}

	@Override
	public final <R> CollectionVisitService<R> transformToCollection(ResultVisitor<? super T, R> resultVisitor) {
		return transformToCollection(null, resultVisitor);
	}

	@Override
	public final <R> CollectionVisitService<R> transformToCollection(Object param,
			ResultVisitor<? super T, R> resultVisitor) {
		return transformToCollection(param, null, resultVisitor);
	}

	// =======================================================================

	@Override
	public final <R> List<R> visitForResultList(PredicateVisitor<? super T> predicate,
			ResultVisitor<? super T, R> resultVisitor, List<R> out) {
		return visitForResultList(null, predicate, resultVisitor, out);
	}

	@Override
	public final <R> List<R> visitForResultList(Object param, ResultVisitor<? super T, R> resultVisitor, List<R> out) {
		return visitForResultList(param, Visitors.truePredicateVisitor(), resultVisitor, out);
	}

	@Override
	public final <R> List<R> visitForResultList(ResultVisitor<? super T, R> resultVisitor, List<R> out) {
		return visitForResultList(null, Visitors.truePredicateVisitor(), resultVisitor, out);
	}

	@Override
	public final <R> R visitForResult(PredicateVisitor<? super T> predicate,
			ResultVisitor<? super T, R> resultVisitor) {
		return visitForResult(null, predicate, resultVisitor);
	}

	@Override
	public final List<T> visitForQueryList(PredicateVisitor<? super T> predicate, List<T> out) {
		return visitForQueryList(null, predicate, out);
	}

	@Override
	public final T visitForQuery(PredicateVisitor<? super T> predicate) {
		return visitForQuery(null, predicate);
	}

	@Override
	public final boolean visitAll() {
		return visitAll(null);
	}

	@Override
	public final boolean visitUntilSuccess(IterateVisitor<? super T> breakVisitor) {
		return visitUntilSuccess(null, breakVisitor);
	}

	@Override
	public final boolean visitUntilFailed(IterateVisitor<? super T> breakVisitor) {
		return visitUntilFailed(null, breakVisitor);
	}

	@Override
	public final boolean visitAll(Object param) {
		return visit(VISIT_RULE_ALL, param, null);
	}

	@Override
	public final boolean visitUntilSuccess(Object param, IterateVisitor<? super T> breakVisitor) {
		Throwables.checkNull(breakVisitor);
		return visit(VISIT_RULE_UNTIL_SUCCESS, param, breakVisitor);
	}

	@Override
	public final boolean visitUntilFailed(Object param, IterateVisitor<? super T> breakVisitor) {
		Throwables.checkNull(breakVisitor);
		return visit(VISIT_RULE_UNTIL_FAILED, param, breakVisitor);
	}

	/**
	 * do visit service. if current collection is empty, the parameter and break
	 * visitor will be ignored. instead it will directly execute the
	 * insert-finally operation and return true.
	 * 
	 * @param rule
	 *            the rule . VISIT_RULE_ALL, VISIT_RULE_UNTIL_SUCCESS or
	 *            VISIT_RULE_UNTIL_FAILED.
	 * @param param
	 *            the extra parameter
	 * @param breakVisitor
	 *            the break visitor
	 * @return true if operate success.
	 */
	protected abstract boolean visit(int rule, Object param, IterateVisitor<? super T> breakVisitor);

	// =======================================

	@Override
	public ListVisitService<T> asListService() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public final CollectionVisitService<T> subService(PredicateVisitor<T> visitor) {
		return subService(null, visitor);
	}

	// ===================================================

	//===================== start inner classes ====================================
	
	private static class WrappedObserveVisitor<T> implements IterateVisitor<T> {

		private final PredicateVisitor<T> mPredicate;
		private final Observer<T> mObserver;
		private final Object mParam;
		private T mLastT;

		public WrappedObserveVisitor(Object param, PredicateVisitor<T> mPredicate, Observer<T> mObserver) {
			super();
			this.mParam = param;
			this.mPredicate = mPredicate;
			this.mObserver = mObserver;
		}

		@Override
		public Boolean visit(T t, Object param, IterationInfo info) {
			mLastT = t;
			return mPredicate.visit(t, param);
		}

		public void onFailed() {
			mObserver.onFailed(mParam, mLastT);
		}

		public void onSuccess() {
			mObserver.onSucess(mParam);
		}

		public void onThrowable(Throwable e) {
			mObserver.onThrowable(mParam, mLastT, e);
		}
	}
	//====================== end inner classes ============================
}
