package com.heaven7.java.visitor.collection;


import java.util.List;

import com.heaven7.java.visitor.MapIterateVisitor;
import com.heaven7.java.visitor.MapPredicateVisitor;
import com.heaven7.java.visitor.TrimMapVisitor;
import com.heaven7.java.visitor.util.Map;
import com.heaven7.java.visitor.util.Updatable;
/**
 * the operation of operate map.
 * @author heaven7
 *
 * @param <K> the key type
 * @param <V> the value type
 */
@SuppressWarnings("deprecation")
public class MapOperation<K, V> extends Operation {

	private V mValue;
	private KeyValuePair<K, V> mPair;
	private List<KeyValuePair<K, V>> mPairs;
	private MapPredicateVisitor<K, V> mPredicateVisitor;
	private MapIterateVisitor<K, V> mIterateVisitor;
	private TrimMapVisitor<K, V> mTrimVisitor;

	public static <K, V> MapOperation<K, V> createFilter(Object param, MapPredicateVisitor<K, V> predicate) {
		MapOperation<K, V> operation = new MapOperation<K, V>();
		operation.mOp = OP_FILTER;
		operation.mParam = param;
		operation.mPredicateVisitor = predicate;
		return operation;
	}

	public static <K, V> MapOperation<K, V> createUpdate(V value, Object param, MapPredicateVisitor<K, V> predicate) {
		MapOperation<K, V> operation = new MapOperation<K, V>();
		operation.mOp = OP_UPDATE;
		operation.mValue = value;
		operation.mParam = param;
		operation.mPredicateVisitor = predicate;
		return operation;
	}

	public static <K, V> MapOperation<K, V> createDelete(Object param, MapPredicateVisitor<K, V> predicate) {
		MapOperation<K, V> operation = new MapOperation<K, V>();
		operation.mOp = OP_DELETE;
		operation.mParam = param;
		operation.mPredicateVisitor = predicate;
		return operation;
	}

	public static <K, V> MapOperation<K, V> createInsert(List<KeyValuePair<K, V>> mPairs, Object param,
			MapIterateVisitor<K, V> iterateVisitor) {
		MapOperation<K, V> operation = new MapOperation<K, V>();
		operation.mOp = OP_INSERT;
		operation.mPairs = mPairs;
		operation.mParam = param;
		operation.mIterateVisitor = iterateVisitor;
		return operation;
	}

	public static <K, V> MapOperation<K, V> createInsert(KeyValuePair<K, V> pair, Object param,
			MapIterateVisitor<K, V> iterateVisitor) {
		MapOperation<K, V> operation = new MapOperation<K, V>();
		operation.mOp = OP_INSERT;
		operation.mPair = pair;
		operation.mParam = param;
		operation.mIterateVisitor = iterateVisitor;
		return operation;
	}
	public static <K, V> MapOperation<K, V> createTrim(Object param,  TrimMapVisitor<K, V> trim) {
		MapOperation<K, V> operation = new MapOperation<K, V>();
		operation.mOp = OP_TRIM;
		operation.mParam = param;
		operation.mTrimVisitor = trim;
		return operation;
	}

	@Override
	public void reset() {
		super.reset();
		this.mPredicateVisitor = null;
		this.mIterateVisitor = null;
		this.mTrimVisitor = null;
		this.mValue = null;
		this.mPairs = null;
		this.mPair = null;
	}

	private boolean shouldFilter(KeyValuePair<K, V> pair, Object param) {
		if (mOp == OP_FILTER && mPredicateVisitor != null) {
			Boolean result = mPredicateVisitor.visit(pair, mParam != null ? mParam : param);
			return result != null && result;
		}
		return false;
	}

	private boolean shouldDelete(KeyValuePair<K, V> pair, Object param) {
		if (mOp == OP_DELETE && mPredicateVisitor != null) {
			Boolean result = mPredicateVisitor.visit(pair, mParam != null ? mParam : param);
			return result != null && result;
		}
		return false;
	}

	private boolean shouldUpdate(KeyValuePair<K, V> pair, Object param) {
		if (mOp == OP_UPDATE && mPredicateVisitor != null) {
			Boolean result = mPredicateVisitor.visit(pair, mParam != null ? mParam : param);
			return result != null && result;
		}
		return false;
	}

	private boolean shouldInsert(KeyValuePair<K, V> pair, Object param, IterationInfo info) {
		if (mOp == OP_INSERT && mIterateVisitor != null) {
			Boolean result = mIterateVisitor.visit(pair, mParam != null ? mParam : param, info);
			return result != null && result;
		}
		return false;
	}
	//=================================================================================
	
	public boolean delete(Map<K, V> map, KeyValuePair<K, V> pair, Object param,IterationInfo info){
		if(shouldDelete(pair, param)){
			map.remove(pair.getKey());
			info.incrementDelete();
			return true;
		}
		return false;
	}
	
	public boolean filter(KeyValuePair<K, V> pair, Object param,IterationInfo info){
		if(shouldFilter(pair, param)){
			info.incrementFilter();
			return true;
		}
		return false;
	}

	public boolean insertFinally(Map<K, V> map, Object param, IterationInfo info) {
		if (shouldInsert(null, param, info)) {
			if (mPair != null) {
				map.put(mPair.getKey(), mPair.getValue());
				info.incrementInsert();
				return true;
			} else {
				if (mPairs != null && mPairs.size() > 0) {
					final int size = mPairs.size();
					for (KeyValuePair<K, V> pair : mPairs) {
						map.put(pair.getKey(), pair.getValue());
					}
					info.addInsert(size);
					return true;
				}
			}
			return false;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean update(Map<K, V> map, KeyValuePair<K, V> pair, Object param, IterationInfo info) {
		if (shouldUpdate(pair, param)) {
			final V value = pair.getValue();
			if (value instanceof Updatable) {
				((Updatable<V>) value).updateFrom(mValue);
				info.incrementUpdate();
				return true;
			}
			if(map.replace(pair.getKey(), value, mValue)){
			   info.incrementUpdate();
			   return true;
			}
			return false;
		}
		return false;
	}
	
	public boolean trim(Map<K, V> map, Object param, IterationInfo info){
		if (mOp == OP_TRIM && mTrimVisitor != null) {
			Boolean result = mTrimVisitor.visit(map, mParam != null ? mParam : param, info);
			info.setCurrentSize(map.size());
			return result != null && result;
		}
		return false;
	}
	
}
