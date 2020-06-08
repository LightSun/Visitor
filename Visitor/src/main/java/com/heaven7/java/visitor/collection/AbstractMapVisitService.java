package com.heaven7.java.visitor.collection;

import com.heaven7.java.visitor.*;
import com.heaven7.java.visitor.anno.Nullable;
import com.heaven7.java.visitor.collection.IterateControl.Callback;
import com.heaven7.java.visitor.internal.InternalUtil;
import com.heaven7.java.visitor.internal.OperationPools;
import com.heaven7.java.visitor.util.*;
import com.heaven7.java.visitor.util.Map.MapTravelCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.heaven7.java.visitor.collection.Operation.*;
import static com.heaven7.java.visitor.internal.InternalUtil.*;
import static com.heaven7.java.visitor.util.Throwables.checkEmpty;
import static com.heaven7.java.visitor.util.Throwables.checkNull;

/**
 * a base impl of {@linkplain MapVisitService}.
 * 
 * @author heaven7
 *
 * @param <K>
 *            the key type
 * @param <V>
 *            the value type
 */
@SuppressWarnings("deprecation")
public abstract class AbstractMapVisitService<K, V> implements MapVisitService<K, V> {

	private final List<KeyValuePair<K, V>> mCachePairs = new ArrayList<KeyValuePair<K, V>>();

	private final IterateControl<MapVisitService<K, V>> mIterateControl;
	private final MapOperateInterceptor<K, V> mOperateInterceptor;
	private MapVisitService.MapOperateManager<K, V> mOpManager;

	/** the all Operation/operate of update in iteration */
	private List<MapOperation<K, V>> mUpdateOps;
	/** the Operation/operate of filter */
	private MapOperation<K, V> mFilterOp;
	/** the Operation/operate of delete or remove. */
	private MapOperation<K, V> mDeleteOp;

	/** the all Operation/operate of final insert after iteration */
	private List<MapOperation<K, V>> mFinalInsertOps;
	/** the Operation/operate of trim */
	private MapOperation<K, V> mTrimOp;

	/** the ordered operate */
	private List<Integer> mOrderOps;
	/** the intercept operate */
	private List<Integer> mInterceptOps;

	/** the flags of clean up */
	private int mCleanUpFlags = FLAG_ALL;

	protected final Map<K, V> mMap;

	protected AbstractMapVisitService(Map<K, V> mMap) {
		this.mMap = mMap;
		mOperateInterceptor = new GroupMapOperateInterceptor();
		mIterateControl = IterateControl.<MapVisitService<K, V>>create(this, new IterateCallbackImpl());
		mIterateControl.begin().end();
		setUp();
	}

	protected MapOperateInterceptor<K, V> getOperateInterceptor() {
		return mOperateInterceptor;
	}

	private List<MapOperation<K, V>> getFinalInsertOperations() {
		return mFinalInsertOps;
	}

	private List<MapOperation<K, V>> getUpdateOperations() {
		return mUpdateOps;
	}

	private MapOperation<K, V> getFilterOperation() {
		return mFilterOp;
	}

	private MapOperation<K, V> getDeleteOperation() {
		return mDeleteOp;
	}

	public MapOperation<K, V> getTrimOperation() {
		return mTrimOp;
	}

	public boolean hasExtraOperationInIteration() {
		return mFilterOp != null || mDeleteOp != null || !Predicates.isEmpty(mUpdateOps);
	}

	private void ensureFinalInsertOperation() {
		if (mFinalInsertOps == null) {
			mFinalInsertOps = new ArrayList<MapOperation<K, V>>();
		}
	}

	private void ensureUpdateOperation() {
		if (mUpdateOps == null) {
			mUpdateOps = new ArrayList<MapOperation<K, V>>();
		}
	}
	// ==========================================================

	protected void setUp() {

	}

	/**
	 * handle the finally operations of function.
	 * 
	 * @param param
	 *            the parameter
	 * @param info
	 *            the Iteration Info
	 * @since 1.0.3
	 */
	protected void handleAtLast(Object param, IterationInfo info) {
		handleAtLast(mMap, param, info);
	}

	/**
	 * use {@linkplain #handleAtLast(Object, IterationInfo)} instead.
	 * 
	 * @param map
	 *            the map
	 * @param param
	 *            the parameter
	 * @param info
	 *            the IterationInfo
	 */
	@Deprecated
	protected void handleAtLast(Map<K, V> map, Object param, IterationInfo info) {
		info.setCurrentSize(map.size());
		final List<MapOperation<K, V>> ops = getFinalInsertOperations();
		if (ops != null && ops.size() > 0) {
			for (int i = 0, size = ops.size(); i < size; i++) {
				ops.get(i).insertFinally(map, param, info);
			}
		}
		if (mTrimOp != null) {
			mTrimOp.trim(map, param, info);
		}
		reset(mCleanUpFlags);
		//mCleanUpFlags = FLAG_ALL;
	}

	// ==================================================================//


	@Override
	public <V2> MapVisitService<K, V2> filterMapValue(MapPredicateVisitor<K, V> predicate, MapResultVisitor<K, V, V2> valueVisitor) {
		return filterMapValue(null, predicate, valueVisitor, null);
	}

	@Override
	public <V2> MapVisitService<K, V2> filterMapValue(MapPredicateVisitor<K, V> predicate, MapResultVisitor<K, V, V2> valueVisitor, Map<K, V> out) {
		return filterMapValue(null, predicate, valueVisitor, out);
	}

	@Override
	public <V2> MapVisitService<K, V2> filterMapValue(Object p, MapPredicateVisitor<K, V> predicate, MapResultVisitor<K, V, V2> valueVisitor, Map<K, V> out) {
		java.util.Map<K, V2> result = newMap(null);
		for (KeyValuePair<K, V> pair : get().getKeyValues()) {
			if(Predicates.isTrue(predicate.visit(pair, p))){
				V2 v = valueVisitor.visit(pair, p);
				result.put(pair.getKey(), v);
			}else{
				if(out != null){
					out.put(pair.getKey(), pair.getValue());
				}
			}
		}
		return VisitServices.from(result);
	}

	@Override
	public <K2> MapVisitService<K2, V> filterMapKey(MapPredicateVisitor<K, V> predicate, MapResultVisitor<K, V, K2> keyVisitor, Map<K, V> out) {
		return filterMapKey(null, predicate, keyVisitor, out);
	}

	@Override
	public <K2> MapVisitService<K2, V> filterMapKey(MapPredicateVisitor<K, V> predicate, MapResultVisitor<K, V, K2> keyVisitor) {
		return filterMapKey(null, predicate, keyVisitor, null);
	}

	@Override
	public <K2> MapVisitService<K2, V> filterMapKey(Object p, MapPredicateVisitor<K, V> predicate, MapResultVisitor<K, V, K2> keyVisitor, Map<K, V> out) {
		java.util.Map<K2, V> result = newMap(null);
		for (KeyValuePair<K, V> pair : get().getKeyValues()) {
			if(Predicates.isTrue(predicate.visit(pair, p))){
				K2 k = keyVisitor.visit(pair, p);
				if(k != null){
					result.put(k, pair.getValue());
				}
			}else{
				if(out != null){
					out.put(pair.getKey(), pair.getValue());
				}
			}
		}
		return VisitServices.from(result);
	}

	@Override
	public <K2, V2> MapVisitService<K2, V2> filterMap(MapPredicateVisitor<K, V> predicate, MapResultVisitor<K, V, K2> keyVisitor,
													  MapResultVisitor<K, V, V2> valueVisitor, Map<K, V> out) {
		return filterMap(null, predicate, keyVisitor, valueVisitor, out);
	}

	@Override
	public <K2, V2> MapVisitService<K2, V2> filterMap(MapPredicateVisitor<K, V> predicate, MapResultVisitor<K, V, K2> keyVisitor,
													  MapResultVisitor<K, V, V2> valueVisitor) {
		return filterMap(null, predicate, keyVisitor, valueVisitor, null);
	}

	@Override
	public <K2, V2> MapVisitService<K2, V2> filterMap(Object p, MapPredicateVisitor<K, V> predicate, MapResultVisitor<K, V, K2> keyVisitor,
													  MapResultVisitor<K, V, V2> valueVisitor, Map<K, V> out) {
		java.util.Map<K2, V2> result = newMap(null);
		for (KeyValuePair<K, V> pair : get().getKeyValues()) {
			if(Predicates.isTrue(predicate.visit(pair, p))){
				K2 k = keyVisitor.visit(pair, p);
				if(k != null){
					V2 v = valueVisitor.visit(pair, p);
					result.put(k, v);
				}
			}else{
				if(out != null){
					out.put(pair.getKey(), pair.getValue());
				}
			}
		}
		return VisitServices.from(result);
	}

	@Override
	public <V2> MapVisitService<K, V2> asAnotherValue(Class<V2> valueType) throws ClassCastException {
		return asAnother(null, valueType);
	}
	@Override
	public <K2> MapVisitService<K2, V> asAnotherKey(Class<K2> keyClass) throws ClassCastException {
		return asAnother(keyClass, null);
	}

	@Override
	public <K2, V2> MapVisitService<K2, V2> asAnother() {
		return asAnother(null, null);
	}

	@Override @SuppressWarnings("unchecked")
	public <K2, V2> MapVisitService<K2, V2> asAnother(Class<K2> keyClass, Class<V2> valueClass) throws ClassCastException {
		if(keyClass != null || valueClass != null){
			K key = mMap.getOneKey();
			if(key != null){
				if(keyClass != null){
					InternalUtil.checkCast(keyClass, key.getClass());
				}
				V v = mMap.get(key);
				if(v != null && valueClass != null){
					InternalUtil.checkCast(valueClass, v.getClass());
				}
			}
		}
		return (MapVisitService<K2, V2>) VisitServices.from(get());
	}

	@Override
	public MapVisitService<K, V> trimNullValue() {
		return filter(Visitors.<K, V>nonNullValueMapPredicateVisitor(), null);
	}

	@Override
	public <V1, R> MapVisitService<K, R> normalize(Object param, final MapVisitService<K, V1> service,final NormalizeVisitor<K, V, V1, Void, R> visitor) {
		Throwables.checkNull(visitor);
		return VisitServices.from(get().getKeyValues()).map2map(param, new ResultVisitor<KeyValuePair<K, V>, K>() {
			@Override
			public K visit(KeyValuePair<K, V> pair, Object param) {
				return pair.getKey();
			}
		}, new ResultVisitor<KeyValuePair<K,V>, R>() {
			@Override
			public R visit(KeyValuePair<K, V> pair, Object param) {
				K key = pair.getKey();
				V1 v1 = service.get().get(key);
				return visitor.visit(key, pair.getValue(), v1, null, param);
			}
		}).trimNullValue();
	}

	@Override
	public <V1, V2, R> MapVisitService<K, R> normalize(final Object param,final MapVisitService<K, V1> s1,final MapVisitService<K, V2> s2,
													   final NormalizeVisitor<K, V, V1, V2, R> visitor) {
		Throwables.checkNull(visitor);
		return VisitServices.from(get().getKeyValues()).map2map(param, new ResultVisitor<KeyValuePair<K, V>, K>() {
			@Override
			public K visit(KeyValuePair<K, V> pair, Object param) {
				return pair.getKey();
			}
		}, new ResultVisitor<KeyValuePair<K,V>, R>() {
			@Override
			public R visit(KeyValuePair<K, V> pair, Object param) {
				K key = pair.getKey();
				V1 v1 = s1.get().get(key);
				V2 v2 = s2.get().get(key);
				return visitor.visit(key, pair.getValue(), v1, v2, param);
			}
		}).trimNullValue();
	}

	@Override @SuppressWarnings("unchecked")
	public MapVisitService<K, V> sort(Comparator<? super K> c) {
		Throwables.checkNull(c);
		Map<K, V> map = new Map2Map<K,V>((java.util.Map<K, V>) newMap(c));
		get().copyTo(map);
		return VisitServices.from(map);
	}

	@Override
	public MapVisitService<K, V> fireBatch(MapFireBatchVisitor<K, V> fireVisitor) {
		return fireBatch(fireVisitor, null);
	}

	@Override
	public MapVisitService<K, V> fireBatch(MapFireBatchVisitor<K, V> fireVisitor, ThrowableVisitor tv) {
		return fireBatch(null, fireVisitor, tv);
	}

	@Override
	public MapVisitService<K, V> fireBatch(Object param, MapFireBatchVisitor<K, V> fireVisitor, ThrowableVisitor tv) {
		final List<KeyValuePair<K, V>> list = visitForQueryList(Visitors.<K, V>trueMapPredicateVisitor(), mCachePairs);
		try {
			fireVisitor.visit(list, param);
		} catch (Exception e) {
			processThrowable(e, tv);
		} finally {
			list.clear();
		}
		return this;
	}

	@Override
	public MapVisitService<K, V> fire(MapFireVisitor<K, V> fireVisitor) {
		return fire(fireVisitor, null);
	}

	@Override
	public MapVisitService<K, V> fire(MapFireVisitor<K, V> fireVisitor, ThrowableVisitor throwVisitor) {
		return fire(null, fireVisitor, throwVisitor);
	}

	@Override
	public MapVisitService<K, V> fire(Object param, MapFireVisitor<K, V> fireVisitor, ThrowableVisitor tv) {
		final List<KeyValuePair<K, V>> list = visitForQueryList(Visitors.<K, V>trueMapPredicateVisitor(), mCachePairs);
		try {
			for (KeyValuePair<K, V> pair : list) {
				fireVisitor.visit(pair, param);
			}
		} catch (Exception e) {
			processThrowable(e, tv);
		} finally {
			list.clear();
		}
		return this;
	}

	@Override
	public MapVisitService<K, V> reset(int flags) {
		if ((flags & FLAG_OPERATE_MANAGER) != 0) {
			OperationPools.recycle(mDeleteOp);
			mDeleteOp = null;
			OperationPools.recycle(mFilterOp);
			mFilterOp = null;
			OperationPools.recycle(mTrimOp);
			mTrimOp = null;
			if (mFinalInsertOps != null) {
				OperationPools.recycleAllMapOperation(mFinalInsertOps);
				mFinalInsertOps.clear();
			}
			if (mUpdateOps != null) {
				OperationPools.recycleAllMapOperation(mUpdateOps);
				mUpdateOps.clear();
			}
		}
		if ((flags & FLAG_OPERATE_ITERATE_CONTROL) != 0) {
			mOrderOps.clear();
			mInterceptOps.clear();
			mIterateControl.begin().end();
		}
		return this;
	}

	@Override
	public final MapVisitService<K, V> resetAll() {
		return reset(FLAG_ALL);
	}

	@Override
	public MapVisitService<K, V> save(Map<K, V> outMap) {
		return save(outMap, false);
	}

	@Override
	public MapVisitService<K, V> save(Map<K, V> outMap, boolean clearBeforeSave) {
		Throwables.checkNull(outMap);
		if (clearBeforeSave) {
			outMap.clear();
		}
		visitAll();
		outMap.putAll(mMap);
		return this;
	}

	@Override
	public MapVisitService<K, V> save(MapSaveVisitor<K, V> visitor) {
		Throwables.checkNull(visitor);
		visitAll();
		visitor.visit(new UnmodifiableMap<K, V>(mMap));
		return this;
	}

	@Override
	public CollectionVisitService<KeyValuePair<K, V>> transformToCollection2() {
		return transformToCollection2(null);
	}

	@Override
	public CollectionVisitService<KeyValuePair<K, V>> transformToCollectionByPairs() {
		return transformToCollectionByPairs(null);
	}

	@Override
	public CollectionVisitService<KeyValuePair<K, V>> transformToCollectionByPairs(Comparator<KeyValuePair<K, V>> c) {
		return transformToCollection2(c);
	}

	@Override
	public CollectionVisitService<KeyValuePair<K, V>> transformToCollection2(Comparator<KeyValuePair<K, V>> c) {
		final List<KeyValuePair<K, V>> list = visitForQueryList(Visitors.<K, V>trueMapPredicateVisitor(), null);
		return getVisitService(list, c, mMap.isSorted());
	}

	@Override
	public CollectionVisitService<K> transformToCollectionByKeys() {
		return transformToCollectionByKeys(null);
	}

	@Override
	public CollectionVisitService<K> transformToCollectionByKeys(Comparator<? super K> comparator) {
		final List<KeyValuePair<K, V>> list = visitForQueryList(Visitors.<K, V>trueMapPredicateVisitor(), mCachePairs);
		final List<K> results = new ArrayList<K>();
		for (KeyValuePair<K, V> pair : list) {
			results.add(pair.getKey());
		}
		// clear cache
		list.clear();
		return getVisitService(results, comparator, mMap.isSorted());
	}

	@Override
	public CollectionVisitService<V> transformToCollectionByValues(Comparator<? super V> comparator) {
		final List<KeyValuePair<K, V>> list = visitForQueryList(Visitors.<K, V>trueMapPredicateVisitor(), mCachePairs);
		final List<V> results = new ArrayList<V>();
		for (KeyValuePair<K, V> pair : list) {
			results.add(pair.getValue());
		}
		// clear cache
		list.clear();
		return getVisitService(results, comparator, mMap.isSorted());
	}

	@Override
	public CollectionVisitService<V> transformToCollectionByValues() {
		return transformToCollectionByValues(null);
	}

	@Override
	public <R> CollectionVisitService<R> transformToCollection(MapResultVisitor<K, V, R> resultVisitor) {
		return transformToCollection(null, resultVisitor);
	}

	@Override
	public <R> CollectionVisitService<R> transformToCollection(Object param, MapResultVisitor<K, V, R> resultVisitor) {
		return transformToCollection(param, null, resultVisitor);
	}

	@Override
	public <R> CollectionVisitService<R> transformToCollection(Object param, Comparator<? super R> comparator,
			MapResultVisitor<K, V, R> resultVisitor) {
		Throwables.checkNull(resultVisitor);

		final List<KeyValuePair<K, V>> list = visitForQueryList(Visitors.<K, V>trueMapPredicateVisitor(), mCachePairs);
		final List<R> results = new ArrayList<R>();
		for (KeyValuePair<K, V> pair : list) {
			results.add(resultVisitor.visit(pair, param));
		}
		// clear cache
		list.clear();
		return getVisitService(results, comparator, mMap.isSorted());
	}

	@Override
	public <K2> MapVisitService<K2, V> transformToMapAsValues(MapResultVisitor<K, V, K2> keyVisitor) {
		return transformToMapAsValues(null, keyVisitor);
	}

	@Override
	public <K2> MapVisitService<K2, V> transformToMapAsValues(Object param, MapResultVisitor<K, V, K2> keyVisitor) {
		return transformToMapAsValues(param, null, keyVisitor);
	}

	@Override
	public <K2> MapVisitService<K2, V> transformToMapAsValues(Object param, Comparator<? super K2> comparator,
			MapResultVisitor<K, V, K2> keyVisitor) {
		Throwables.checkNull(keyVisitor);

		final List<KeyValuePair<K, V>> list = visitForQueryList(Visitors.<K, V>trueMapPredicateVisitor(), null);
		final java.util.Map<K2, V> map = newMap(comparator);
		for (KeyValuePair<K, V> pair : list) {
			map.put(keyVisitor.visit(pair, param), pair.getValue());
		}
		// clear cache
		list.clear();
		return VisitServices.from(map);
	}

	@Override
	public <V2> MapVisitService<K, V2> transformToMapAsKeys(MapResultVisitor<K, V, V2> valueVisitor) {
		return transformToMapAsKeys(null, valueVisitor);
	}

	@Override
	public <V2> MapVisitService<K, V2> transformToMapAsKeys(Object param, MapResultVisitor<K, V, V2> valueVisitor) {
		return transformToMapAsKeys(param, null, valueVisitor);
	}

	@Override
	public <V2> MapVisitService<K, V2> transformToMapAsKeys(Object param, Comparator<? super K> comparator,
			MapResultVisitor<K, V, V2> valueVisitor) {
		Throwables.checkNull(valueVisitor);

		final List<KeyValuePair<K, V>> list = visitForQueryList(Visitors.<K, V>trueMapPredicateVisitor(), null);
		final java.util.Map<K, V2> map = newMap(comparator);
		for (KeyValuePair<K, V> pair : list) {
			map.put(pair.getKey(), valueVisitor.visit(pair, param));
		}
		// clear cache
		list.clear();
		return VisitServices.from(map);
	}

	@Override
	public MapVisitService<V, K> transformToMapBySwap() {
		return transformToMapBySwap(null);
	}

	@Override
	public MapVisitService<V, K> transformToMapBySwap(Comparator<? super V> comparator) {
		final List<KeyValuePair<K, V>> list = visitForQueryList(Visitors.<K, V>trueMapPredicateVisitor(), null);
		final java.util.Map<V, K> map = newMap(comparator);
		for (KeyValuePair<K, V> pair : list) {
			map.put(pair.getValue(), pair.getKey());
		}
		// clear cache
		list.clear();
		return VisitServices.from(map);
	}

	@Override
	public <K2, V2> MapVisitService<K2, V2> transformToMap(MapResultVisitor<K, V, K2> keyVisitor,
			MapResultVisitor<K, V, V2> valueVisitor) {
		return transformToMap(null, keyVisitor, valueVisitor);
	}

	@Override
	public <K2, V2> MapVisitService<K2, V2> transformToMap(Object param, MapResultVisitor<K, V, K2> keyVisitor,
			MapResultVisitor<K, V, V2> valueVisitor) {
		return transformToMap(param, null, keyVisitor, valueVisitor);
	}

	@Override
	public <K2, V2> MapVisitService<K2, V2> transformToMap(Object param, Comparator<? super K2> comparator,
			MapResultVisitor<K, V, K2> keyVisitor, MapResultVisitor<K, V, V2> valueVisitor) {
		Throwables.checkNull(keyVisitor);
		Throwables.checkNull(valueVisitor);

		final List<KeyValuePair<K, V>> list = visitForQueryList(Visitors.<K, V>trueMapPredicateVisitor(), null);
		final java.util.Map<K2, V2> map = newMap(comparator);
		for (KeyValuePair<K, V> pair : list) {
			map.put(keyVisitor.visit(pair, param), valueVisitor.visit(pair, param));
		}
		// clear cache
		list.clear();
		return VisitServices.from(map);
	}

	// ========================================================================================================

	@Override
	public <R> List<R> visitForResultList(Object param, MapResultVisitor<K, V, R> resultVisitor, List<R> out) {
		return visitForResultList(param, Visitors.<K, V>trueMapPredicateVisitor(), resultVisitor, out);
	}

	@Override
	public <R> List<R> visitForResultList(MapResultVisitor<K, V, R> resultVisitor, List<R> out) {
		return visitForResultList(null, Visitors.<K, V>trueMapPredicateVisitor(), resultVisitor, out);
	}

	@Override
	public final <R> List<R> visitForResultList(MapPredicateVisitor<K, V> predicate,
			MapResultVisitor<K, V, R> resultVisitor, @Nullable List<R> out) {
		return visitForResultList(null, predicate, resultVisitor, out);
	}

	@Override
	public final <R> R visitForResult(MapPredicateVisitor<K, V> predicate, MapResultVisitor<K, V, R> resultVisitor) {
		return visitForResult(null, predicate, resultVisitor);
	}

	@Override
	public final List<KeyValuePair<K, V>> visitForQueryList(MapPredicateVisitor<K, V> predicate,
			@Nullable List<KeyValuePair<K, V>> out) {
		return visitForQueryList(null, predicate, out);
	}

	@Override
	public final KeyValuePair<K, V> visitForQuery(MapPredicateVisitor<K, V> predicate) {
		return visitForQuery(null, predicate);
	}

	@Override
	public IterateControl<MapVisitService<K, V>> beginIterateControl() {
		return mIterateControl.begin();
	}

	@Override
	public MapVisitService.MapOperateManager<K, V> beginOperateManager() {
		return mOpManager == null ? (mOpManager = new MapOperateManagerImpl()) : mOpManager;
	}

	@Override
	public int size() {
		return mMap.size();
	}

	@Override
	public MapVisitService<K, V> clear() {
		mMap.clear();
		return this;
	}

	@Override
	public MapVisitService<K, V> subService(MapPredicateVisitor<K, V> predicate) {
		return subService(null, predicate);
	}

	@Override
	public MapVisitService<K, V> subService(Object param, MapPredicateVisitor<K, V> predicate) {
		return subService(param, predicate, null);
	}

	@Override
	public MapVisitService<K, V> subService(final Object param, final MapPredicateVisitor<K, V> predicate,
			Comparator<? super K> c) {
		checkNull(predicate);
		
		final java.util.Map<K, V> map = newMap(c);
		final KeyValuePair<K, V> pair = KeyValuePair.create(null, null);
		
		mMap.startTravel(new MapTravelCallback<K, V>() {
			@Override
			public void onTravel(K key, V value) {
				pair.setKeyValue(key, value);
				if (predicate.visit(pair, param)) {
					map.put(key, value);
				}
			}
		});
		return VisitServices.from(map);
	}

	@Override
	public MapVisitService<K, V> copyService(@Nullable Comparator<? super K> com) {
		return VisitServices.from(copy(com));
	}

	@Override
	public MapVisitService<K, V> copyService() {
		return copyService(null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<K, V> copy(Comparator<? super K> com) {
		Map2Map<K, V> map = new Map2Map<K, V>((java.util.Map<K, V>) newMap(com));
		get().copyTo(map);
		return map;
	}
	@Override
	public Map<K, V> copy() {
		return copy(null);
	}

	@Override
	public <R> CollectionVisitService<R> map(Object param, Comparator<? super R> com, MapResultVisitor<K, V, R> visitor) {
		return transformToCollection(param, com, visitor);
	}

	@Override
	public <R> CollectionVisitService<R> map(Object param, MapResultVisitor<K, V, R> visitor) {
		return map(param, null, visitor);
	}
	@Override
	public <R> CollectionVisitService<R> map(MapResultVisitor<K, V, R> visitor) {
		return map(null, null, visitor);
	}

	@Override
	public CollectionVisitService<K> mapKey(Comparator<? super K> com) {
		return transformToCollectionByKeys(com);
	}

	@Override
	public CollectionVisitService<K> mapKey() {
		return mapKey(null);
	}

	@Override
	public CollectionVisitService<V> mapValue(Comparator<? super V> com) {
		return transformToCollectionByValues(com);
	}
	@Override
	public CollectionVisitService<V> mapValue() {
		return mapValue(null);
	}

	@Override
	public KeyValueListService<K, V>  mapPair(Comparator<KeyValuePair<K, V>> com) {
		final List<KeyValuePair<K, V>> list = visitForQueryList(Visitors.<K, V>trueMapPredicateVisitor(), null);
		if(com != null){
			Collections.sort(list, com);
		}
		return new KeyValueListService<K, V>(list);
	}

	@Override
	public KeyValueListService<K, V>  mapPair() {
		return mapPair(null);
	}

	@Override
	public KeyValueListService<V, K> mapWithSwap(Comparator<KeyValuePair<V, K>> com) {
		List<KeyValuePair<V, K>> pairs = transformToMapBySwap().visitForQueryList(Visitors.<V,K>trueMapPredicateVisitor(), null);
		if(com != null){
			Collections.sort(pairs, com);
		}
		return new KeyValueListService<V, K>(pairs);
	}
	@Override
	public KeyValueListService<V, K>  mapWithSwap() {
		return mapWithSwap(null);
	}

	@Override
	public <V2> MapVisitService<K, V2> map2MapKey(Object param, Comparator<? super K> com, MapResultVisitor<K, V, V2> visitor) {
		return transformToMapAsKeys(param, com, visitor);
	}
	@Override
	public <V2> MapVisitService<K, V2> map2MapKey(Object param, MapResultVisitor<K, V, V2> visitor) {
		return map2MapKey(param, null, visitor);
	}
	@Override
	public <V2> MapVisitService<K, V2> map2MapKey(MapResultVisitor<K, V, V2> visitor) {
		return map2MapKey(null, null, visitor);
	}

	@Override
	public <K2> MapVisitService<K2, V> map2MapValue(Object param, Comparator<? super K2> com, MapResultVisitor<K, V, K2> visitor) {
		return transformToMapAsValues(param, com, visitor);
	}

	@Override
	public <K2> MapVisitService<K2, V> map2MapValue(Object param, MapResultVisitor<K, V, K2> visitor) {
		return map2MapValue(param, null, visitor);
	}
	@Override
	public <K2> MapVisitService<K2, V> map2MapValue(MapResultVisitor<K, V, K2> visitor) {
		return map2MapValue(null, null, visitor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public MapVisitService<V, K> map2MapWithSwap(Comparator<? super V> com) {
		List<KeyValuePair<K, V>> pairs = visitForQueryList(Visitors.<K, V>trueMapPredicateVisitor(), mCachePairs);
		Map2Map<V, K> map = new Map2Map<V, K>((java.util.Map<V, K>) newMap(com));
		map.putPairs2(pairs);
		pairs.clear();
		return VisitServices.from(map);
	}
	@Override
	public MapVisitService<V, K> map2MapWithSwap() {
		return map2MapWithSwap(null);
	}

	@Override
	public <K2, V2> MapVisitService<K2, V2> map2Map(MapResultVisitor<K, V, K2> key, MapResultVisitor<K, V, V2> value) {
		return map2Map(null, null, key, value);
	}

	@Override
	public <K2, V2> MapVisitService<K2, V2> map2Map(Object param, MapResultVisitor<K, V, K2> key, MapResultVisitor<K, V, V2> value) {
		return map2Map(param, null, key, value);
	}

	@Override
	public <K2, V2> MapVisitService<K2, V2> map2Map(Object param, Comparator<? super K2> com, MapResultVisitor<K, V, K2> key, MapResultVisitor<K, V, V2> value) {
		return transformToMap(param, com, key, value);
	}

	@Override
	public MapVisitService<K, V> filter(final Object param, Comparator<? super K> com, final MapPredicateVisitor<K, V> predicate, final Map<K, V> dropOut) {
		final java.util.Map<K, V> map = newMap(com);
		final KeyValuePair<K, V> pair = KeyValuePair.create(null, null);
		get().startTravel(new MapTravelCallback<K, V>() {
			@Override
			public void onTravel(K key, V value) {
				pair.setKeyValue(key, value);
				if(Predicates.isTrue(predicate.visit(pair, param))){
					map.put(key, value);
				}else if(dropOut != null){
					dropOut.put(key, value);
				}
			}
		});
		return VisitServices.from(map);
	}
	@Override
	public MapVisitService<K, V> filter(Object param, MapPredicateVisitor<K, V> predicate, Map<K, V> dropOut) {
		return filter(param, null, predicate, dropOut);
	}
	@Override
	public MapVisitService<K, V> filter(MapPredicateVisitor<K, V> predicate, Map<K, V> dropOut) {
		return filter(null, null, predicate, dropOut);
	}
	// ======================================================

	protected boolean handleDelete(Map<K, V> map, KeyValuePair<K, V> pair, Object param, IterationInfo info) {
		final MapOperation<K, V> op = getDeleteOperation();
		if (op != null && op.delete(map, pair, param, info)) {
			return true;
		}
		return false;
	}

	protected boolean handleFilter(Map<K, V> map, KeyValuePair<K, V> pair, Object param, IterationInfo info) {
		final MapOperation<K, V> operation = getFilterOperation();
		if (operation != null && operation.filter(pair, param, info)) {
			return true;
		}
		return false;
	}

	protected boolean handleUpdate(Map<K, V> map, KeyValuePair<K, V> pair, Object param, IterationInfo info) {
		final List<MapOperation<K, V>> ops = getUpdateOperations();
		if (ops != null) {
			for (int i = 0, size = ops.size(); i < size; i++) {
				if (ops.get(i).update(map, pair, param, info)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * visit the all key-values by deal with the all operations.
	 */
	protected void visitAll() {
		visitForQueryList(Visitors.<K, V>trueMapPredicateVisitor(), mCachePairs);
		mCachePairs.clear();
	}

	// ========================================================

	private class GroupMapOperateInterceptor extends MapOperateInterceptor<K, V> {

		final SparseArray<MapOperateInterceptor<K, V>> mInterceptorMap;
		final ArrayList<MapOperateInterceptor<K, V>> mOrderInerceptors;
		boolean mIntercept_Delete;
		boolean mIntercept_Filter;
		boolean mIntercept_Update;

		public GroupMapOperateInterceptor() {
			super();
			mOrderInerceptors = new ArrayList<MapOperateInterceptor<K, V>>(6);
			mInterceptorMap = new SparseArray<MapOperateInterceptor<K, V>>(6);
			addMemberInterceptors();
		}

		private void addMemberInterceptors() {
			mInterceptorMap.put(OP_DELETE, new MapOperateInterceptor<K, V>() {
				@Override
				public boolean intercept(Map<K, V> map, KeyValuePair<K, V> pair, Object param, IterationInfo info) {
					return handleDelete(map, pair, param, info) && mIntercept_Delete;
				}
			});
			mInterceptorMap.put(OP_FILTER, new MapOperateInterceptor<K, V>() {
				@Override
				public boolean intercept(Map<K, V> map, KeyValuePair<K, V> pair, Object param, IterationInfo info) {
					return handleFilter(map, pair, param, info) && mIntercept_Filter;
				}
			});
			mInterceptorMap.put(OP_UPDATE, new MapOperateInterceptor<K, V>() {
				@Override
				public boolean intercept(Map<K, V> map, KeyValuePair<K, V> pair, Object param, IterationInfo info) {
					return handleUpdate(map, pair, param, info) && mIntercept_Update;
				}
			});
		}

		@Override
		public boolean intercept(Map<K, V> map, KeyValuePair<K, V> pair, Object param, IterationInfo info) {
			final ArrayList<MapOperateInterceptor<K, V>> mOrderInerceptors = this.mOrderInerceptors;
			for (int i = 0, size = mOrderInerceptors.size(); i < size; i++) {
				if (mOrderInerceptors.get(i).intercept(map, pair, param, info)) {
					return true;
				}
			}
			return false;
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

				default:
					System.err.println("unsupport operate = " + op);
					break;
				}
			}

			final SparseArray<MapOperateInterceptor<K, V>> mInterceptorMap = this.mInterceptorMap;
			final List<Integer> mOrderOps = AbstractMapVisitService.this.mOrderOps;
			for (int i = 0, size = mOrderOps.size(); i < size; i++) {
				mOrderInerceptors.add(mInterceptorMap.get(mOrderOps.get(i)));
			}
		}

		@Override
		public void end() {
			mIntercept_Delete = false;
			mIntercept_Filter = false;
			mIntercept_Update = false;
			mOrderInerceptors.clear();
		}

	}

	private class IterateCallbackImpl extends Callback {

		@Override
		public void applyCache() {
			mCleanUpFlags &= ~FLAG_OPERATE_ITERATE_CONTROL;
		}

		@Override
		public void applyNoCache() {
			mCleanUpFlags |=  FLAG_OPERATE_ITERATE_CONTROL;
		}

		@Override
		public void saveState(List<Integer> orderOps, List<Integer> interceptOps) {
			mInterceptOps = interceptOps;
			mOrderOps = orderOps;
		}

		@Override
		public void onStart(List<Integer> orderOps, List<Integer> interceptOps) {
			// by default only delete and fiter op will intercept iteration.
			// that means if success the iterator will skip and continue next
			// iteration.
			interceptOps.add(OP_DELETE);
			interceptOps.add(OP_FILTER);

			/**
			 * default order is delete -> filter -> update -> insert
			 */
			orderOps.add(OP_DELETE);
			orderOps.add(OP_FILTER);
			orderOps.add(OP_UPDATE);
			// orderOps.add(OP_INSERT); //insert in map is a finally op
		}

		@Override
		public void checkOperation(int op) {
			if (op < OP_DELETE || op > OP_UPDATE) {
				throw new IllegalArgumentException("unsupport opertion");
			}
		}
	}

	private class MapOperateManagerImpl extends MapOperateManager<K, V> {

		@Override
		public MapOperateManager<K, V> cache() {
			mCleanUpFlags &= ~FLAG_OPERATE_MANAGER;
			return this;
		}

		@Override
		public MapOperateManager<K, V> noCache() {
			mCleanUpFlags |= FLAG_OPERATE_MANAGER;
			return this;
		}

		@Override
		public MapVisitService<K, V> end() {
			return AbstractMapVisitService.this;
		}

		@Override
		public MapOperateManager<K, V> filter(Object param, MapPredicateVisitor<K, V> visitor) {
			checkNull(visitor);
			if (mFilterOp != null) {
				throw new IllegalArgumentException("filter operation can only set once");
			}
			mFilterOp = MapOperation.createFilter(param, visitor);
			return this;
		}

		@Override
		public MapOperateManager<K, V> delete(Object param, MapPredicateVisitor<K, V> visitor) {
			checkNull(visitor);
			if (mDeleteOp != null) {
				throw new IllegalArgumentException("delete operation can only set once");
			}
			mDeleteOp = MapOperation.createDelete(param, visitor);
			return this;
		}

		@Override
		public MapOperateManager<K, V> update(V value, Object param, MapPredicateVisitor<K, V> visitor) {
			checkNull(value);
			checkNull(visitor);
			ensureUpdateOperation();
			mUpdateOps.add(MapOperation.createUpdate(value, param, visitor));
			return this;
		}

		@Override
		public MapOperateManager<K, V> insertFinally(KeyValuePair<K, V> newPair, Object param,
				MapIterateVisitor<K, V> visitor) {
			checkNull(newPair);
			checkNull(visitor);
			ensureFinalInsertOperation();
			mFinalInsertOps.add(MapOperation.createInsert(newPair, param, visitor));
			return this;
		}

		@Override
		public MapOperateManager<K, V> insertFinally(List<KeyValuePair<K, V>> newPairs, Object param,
				MapIterateVisitor<K, V> visitor) {
			checkEmpty(newPairs);
			checkNull(visitor);
			ensureFinalInsertOperation();
			mFinalInsertOps.add(MapOperation.createInsert(newPairs, param, visitor));
			return this;
		}

		@Override
		public MapOperateManager<K, V> trim(Object param, TrimMapVisitor<K, V> visitor) {
			checkNull(visitor);
			if (mTrimOp != null) {
				throw new IllegalArgumentException("trim operation can only set once");
			}
			mTrimOp = MapOperation.createTrim(param, visitor);
			return this;
		}

	}

}
