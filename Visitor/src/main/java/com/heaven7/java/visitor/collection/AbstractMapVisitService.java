package com.heaven7.java.visitor.collection;

import static com.heaven7.java.visitor.collection.Operation.OP_DELETE;
import static com.heaven7.java.visitor.collection.Operation.OP_FILTER;
import static com.heaven7.java.visitor.collection.Operation.OP_UPDATE;
import static com.heaven7.java.visitor.util.Throwables.checkEmpty;
import static com.heaven7.java.visitor.util.Throwables.checkNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.heaven7.java.visitor.MapIterateVisitor;
import com.heaven7.java.visitor.MapPredicateVisitor;
import com.heaven7.java.visitor.MapResultVisitor;
import com.heaven7.java.visitor.SaveVisitor.MapSaveVisitor;
import com.heaven7.java.visitor.TrimMapVisitor;
import com.heaven7.java.visitor.Visitors;
import com.heaven7.java.visitor.anno.Nullable;
import com.heaven7.java.visitor.collection.IterateControl.Callback;
import com.heaven7.java.visitor.util.Map;
import com.heaven7.java.visitor.util.Predicates;
import com.heaven7.java.visitor.util.SparseArray;
import com.heaven7.java.visitor.util.Throwables;

public abstract class AbstractMapVisitService<K, V> implements MapVisitService<K, V> {

	private final IterateControl<MapVisitService<K, V>> mIterateControl;
	private final MapOperateInterceptor<K, V> mOperateInterceptor;

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
	
	protected final Map<K, V> mMap;

	protected AbstractMapVisitService(Map<K, V> mMap) {
		this.mMap = mMap;
		mOperateInterceptor = new GroupMapOperateInterceptor();
		mIterateControl = IterateControl.<MapVisitService<K, V>>create(this, new IterateCallbackImpl());
		mIterateControl.begin().end();
		setUp();
	}

	public MapOperateInterceptor<K, V> getOperateInterceptor() {
		return mOperateInterceptor;
	}

	public List<MapOperation<K, V>> getFinalInsertOperations() {
		return mFinalInsertOps;
	}

	public List<MapOperation<K, V>> getUpdateOperations() {
		return mUpdateOps;
	}

	public MapOperation<K, V> getFilterOperation() {
		return mFilterOp;
	}

	public MapOperation<K, V> getDeleteOperation() {
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
			mFinalInsertOps = new ArrayList<>();
		}
	}

	private void ensureUpdateOperation() {
		if (mUpdateOps == null) {
			mUpdateOps = new ArrayList<>();
		}
	}
	// ==========================================================

	protected void setUp() {

	}

	public void reset() {
		mDeleteOp = null;
		mFilterOp = null;
		mTrimOp = null;
		if (mFinalInsertOps != null) {
			mFinalInsertOps.clear();
		}
		if (mUpdateOps != null) {
			mUpdateOps.clear();
		}
		mOrderOps.clear();
		mInterceptOps.clear();
	}

	protected void handleAtLast(Map<K, V> map, Object param, IterationInfo info) {
		info.setCurrentSize(map.size());
		final List<MapOperation<K, V>> ops = getFinalInsertOperations();
		if(ops != null && ops.size() > 0){
			for (int i = 0, size = ops.size(); i < size; i++) {
				ops.get(i).insertFinally(map, param, info);
			}
		}
		if (mTrimOp != null) {
			mTrimOp.trim(map, param, info);
		}
	}
	
	// ==================================================================//
	
	@Override
	public MapVisitService<K, V> save(Map<K, V> outMap) {
		return save(outMap, false);
	}
	@Override
	public MapVisitService<K, V> save(Map<K, V> outMap, boolean clearBeforeSave) {
		Throwables.checkEmpty(outMap);
		if(clearBeforeSave){
			outMap.clear();
		}
		outMap.putAll(mMap);
		return this;
	}
	@Override
	public MapVisitService<K, V> save(MapSaveVisitor<K, V> visitor) {
		Throwables.checkNull(visitor);
		visitor.onSave(mMap);
		return this;
	}
	
	@Override
	public CollectionVisitService<K> transformToCollectionByKeys() {
		final List<KeyValuePair<K, V>> list = visitForQueryList(Visitors.trueMapPredicateVisitor(), null);
		final List<K> results = new ArrayList<K>();
		for(KeyValuePair<K, V> pair : list){
			results.add(pair.getKey());
		}
		return VisitServices.from(results);
	}
	@Override
	public CollectionVisitService<V> transformToCollectionByValues() {
		final List<KeyValuePair<K, V>> list = visitForQueryList(Visitors.trueMapPredicateVisitor(), null);
		final List<V> results = new ArrayList<V>();
		for(KeyValuePair<K, V> pair : list){
			results.add(pair.getValue());
		}
		return VisitServices.from(results);
	}
	
	@Override
	public <R> CollectionVisitService<R> transformToCollection(MapResultVisitor<K, V, R> resultVisitor) {
		return transformToCollection(null, resultVisitor);
	}
	
	@Override
	public <R> CollectionVisitService<R> transformToCollection(Object param, MapResultVisitor<K, V, R> resultVisitor) {
		Throwables.checkNull(resultVisitor);
		final List<KeyValuePair<K,V>> list = visitForQueryList(Visitors.trueMapPredicateVisitor(), null);
		final List<R> results = new ArrayList<R>();
		for(KeyValuePair<K, V> pair : list){
			results.add(resultVisitor.visit(pair, param));
		}
		return VisitServices.from(results);
	}
	
	@Override
	public <K2> MapVisitService<K2, V> transformToMapAsValues(MapResultVisitor<K, V, K2> keyVisitor) {
		return transformToMapAsValues(null, keyVisitor);
	}
	
	@Override
	public <K2> MapVisitService<K2, V> transformToMapAsValues(Object param, MapResultVisitor<K, V, K2> keyVisitor) {
		Throwables.checkNull(keyVisitor);
		final List<KeyValuePair<K,V>> list = visitForQueryList(Visitors.trueMapPredicateVisitor(), null);
		final HashMap<K2, V> map = new HashMap<K2, V>();
		for(KeyValuePair<K, V> pair : list){
			map.put(keyVisitor.visit(pair, param), pair.getValue());
		}
		return VisitServices.from(map);
	}
	
	@Override
	public <V2> MapVisitService<K, V2> transformToMapAsKeys(MapResultVisitor<K, V, V2> valueVisitor) {
		return transformToMapAsKeys(null, valueVisitor);
	}
	@Override
	public <V2> MapVisitService<K, V2> transformToMapAsKeys(Object param, MapResultVisitor<K, V, V2> valueVisitor) {
		Throwables.checkNull(valueVisitor);
		final List<KeyValuePair<K,V>> list = visitForQueryList(Visitors.trueMapPredicateVisitor(), null);
		final HashMap<K, V2> map = new HashMap<K, V2>();
		for(KeyValuePair<K, V> pair : list){
			map.put(pair.getKey(), valueVisitor.visit(pair, param));
		}
		return VisitServices.from(map);
	}
	
	@Override
	public MapVisitService<V, K> transformToMapBySwap() {
		final List<KeyValuePair<K,V>> list = visitForQueryList(Visitors.trueMapPredicateVisitor(), null);
		final HashMap<V, K> map = new HashMap<V, K>();
		for(KeyValuePair<K, V> pair : list){
			map.put(pair.getValue(), pair.getKey());
		}
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
		Throwables.checkNull(keyVisitor);
		Throwables.checkNull(valueVisitor);
		
		final List<KeyValuePair<K,V>> list = visitForQueryList(Visitors.trueMapPredicateVisitor(), null);
		final HashMap<K2,V2> map = new HashMap<K2, V2>();
		for(KeyValuePair<K,V> pair : list){
			map.put(keyVisitor.visit(pair, param), valueVisitor.visit(pair, param));
		}
		return VisitServices.from(map);
	}
	
	@Override
	public <R> List<R> visitForResultList(Object param, MapResultVisitor<K, V, R> resultVisitor, List<R> out) {
		return visitForResultList(param, Visitors.trueMapPredicateVisitor(), resultVisitor, out);
	}
	
	@Override
	public <R> List<R> visitForResultList(MapResultVisitor<K, V, R> resultVisitor, List<R> out) {
		return visitForResultList(null, Visitors.trueMapPredicateVisitor(), resultVisitor, out);
	}

	@Override
	public final <R> List<R> visitForResultList(MapPredicateVisitor<K, V> predicate,
			MapResultVisitor<K, V, R> resultVisitor,@Nullable List<R> out) {
		return visitForResultList(null, predicate, resultVisitor,out);
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
		return new MapOperateManagerImpl();
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
