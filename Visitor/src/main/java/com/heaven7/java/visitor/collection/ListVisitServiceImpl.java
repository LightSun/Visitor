package com.heaven7.java.visitor.collection;

import com.heaven7.java.visitor.*;
import com.heaven7.java.visitor.anno.Nullable;
import com.heaven7.java.visitor.internal.InternalUtil;
import com.heaven7.java.visitor.util.Collections2;
import com.heaven7.java.visitor.util.Predicates;
import com.heaven7.java.visitor.util.Throwables;

import java.lang.reflect.Array;
import java.util.*;

import static com.heaven7.java.visitor.internal.InternalUtil.processThrowable;
import static com.heaven7.java.visitor.util.Predicates.isTrue;
import static com.heaven7.java.visitor.util.Throwables.checkNull;

/**
 * list visit service
 * 
 * @author heaven7
 *
 * @param <T>
 *            the element type
 */
/*final*/ class ListVisitServiceImpl<T> extends CollectionVisitServiceImpl<T>
		implements CollectionVisitService<T>, ListVisitService<T> {
	
	private StringBuilder mSb;
	private List<String>  mTempStrs;

	protected ListVisitServiceImpl(List<T> collection) {
		super(collection);
	}

	@Override
	public <R> ListVisitService<R> filterMap(PredicateVisitor<T> predicate, ResultVisitor<T, R> mapVisitor) {
		return (ListVisitService<R>) super.filterMap(predicate, mapVisitor);
	}

	@Override
	public <R> ListVisitService<R> filterMap(PredicateVisitor<T> predicate, ResultVisitor<T, R> mapVisitor, List<T> dropOut) {
		return (ListVisitService<R>) super.filterMap(predicate, mapVisitor, dropOut);
	}

	@Override
	public <R> ListVisitService<R> filterMap(Object p, PredicateVisitor<T> predicate, ResultVisitor<T, R> mapVisitor, List<T> dropOut) {
		return (ListVisitService<R>) super.filterMap(p, predicate, mapVisitor, dropOut);
	}

	@Override
	public ListVisitService<List<T>> merge() {
		return (ListVisitService<List<T>>) super.merge();
	}
	public<R> ListVisitService<R> separate(){
		return (ListVisitService<R>) super.separate();
	}
	public <R> CollectionVisitService<R> fission(Object param, FissionVisitor<T, R> visitor){
		return (ListVisitService<R>) super.fission(param, visitor);
	}
	@Override
	public ListVisitService<Double> mapDouble() {
		return (ListVisitService<Double>) super.mapDouble();
	}
	@Override
	public ListVisitService<Byte> mapByte() {
		return (ListVisitService<Byte>) super.mapByte();
	}
	@Override
	public ListVisitService<Integer> mapInt() {
		return (ListVisitService<Integer>) super.mapInt();
	}
	@Override
	public ListVisitService<Float> mapFloat() {
		return (ListVisitService<Float>) super.mapFloat();
	}
	@Override
	public ListVisitService<String> mapString() {
		return (ListVisitService<String>) super.mapString();
	}
    @Override
    public ListVisitService<Long> mapLong() {
        return (ListVisitService<Long>) super.mapLong();
    }

    @Override
	public ListVisitService<T> addIfNotExist(T newT, com.heaven7.java.visitor.util.Observer<T, Void> observer) {
		return (ListVisitService<T>) super.addIfNotExist(newT, observer);
	}
	@Override
	public ListVisitService<T> addIfNotExist(T newT) {
		return (ListVisitService<T>) super.addIfNotExist(newT);
	}
	@Override
	public ListVisitService<T> removeIfExist(T newT, com.heaven7.java.visitor.util.Observer<T, Void> observer) {
		return (ListVisitService<T>) super.removeIfExist(newT, observer);
	}
	@Override
	public ListVisitService<T> removeIfExist(T newT) {
		return (ListVisitService<T>) super.removeIfExist(newT);
	}
	@Override
	public ListVisitService<T> reset(int flags) {
		return (ListVisitService<T>) super.reset(flags);
	}
	@Override
	public ListVisitService<T> resetAll() {
		return (ListVisitService<T>) super.resetAll();
	}

	@Override
	public ListVisitService<T> save(Collection<T> out, boolean clearBeforeSave) {
		return (ListVisitService<T>) super.save(out, clearBeforeSave);
	}
	@Override
	public ListVisitService<T> save(SaveVisitor<T> visitor) {
		return (ListVisitService<T>) super.save(visitor);
	}
	@Override
	public ListVisitService<T> save(Collection<T> out) {
		return (ListVisitService<T>) super.save(out);
	}
	@Override
	public ListVisitService<T> intersect(Collection<? super T> coll) {
		return (ListVisitService<T>) super.intersect(coll);
	}

	@Override
	public ListVisitService<T> zip(Object param, PredicateVisitor<T> visitor, com.heaven7.java.visitor.util.Observer<T, Void> observer) {
		return (ListVisitService<T>) super.zip(param, visitor, observer);
	}
	@Override
	public <R> ListVisitService<T> zipResult(Object param, ResultVisitor<T, R> visitor, com.heaven7.java.visitor.util.Observer<T, List<R>> observer) {
		return (ListVisitService<T>) super.zipResult(param, visitor, observer);
	}

	@Override
	public <T2> ListVisitService<T2> asAnother(Class<T2> clazz) {
		return (ListVisitService<T2>) super.asAnother(clazz);
	}
	@Override
	public <T2> ListVisitService<T2> asAnother() {
		return (ListVisitService<T2>) super.asAnother();
	}

	@Override
	public ListVisitService<T> removeRepeat(Object param, Comparator<? super T> com, WeightVisitor<T> weightVisitor) {
		return (ListVisitService<T>) super.removeRepeat(param, com, weightVisitor);
	}

	@Override
	public ListVisitService<T> removeRepeat(Object param, Comparator<? super T> com) {
		return (ListVisitService<T>) super.removeRepeat(param, com);
	}
	@Override
	public ListVisitService<T> removeRepeat(WeightVisitor<T> weightVisitor) {
		return (ListVisitService<T>) super.removeRepeat(weightVisitor);
	}
	@Override
	public ListVisitService<T> removeRepeat() {
		return (ListVisitService<T>) super.removeRepeat();
	}

	@Override
	public ListVisitService<T> filter(Object param, Comparator<? super T> com, PredicateVisitor<T> predicate, int maxCount, List<T> dropOut) {
		return (ListVisitService<T>) super.filter(param, com, predicate, maxCount, dropOut);
	}
	@Override
	public ListVisitService<T> filter(Object param, PredicateVisitor<T> predicate, int maxCount, List<T> dropOut) {
		return (ListVisitService<T>) super.filter(param, predicate, maxCount, dropOut);
	}
	@Override
	public ListVisitService<T> filter(Object param, PredicateVisitor<T> predicate, List<T> dropOut) {
		return (ListVisitService<T>) super.filter(param, predicate, dropOut);
	}
	@Override
	public ListVisitService<T> filter(PredicateVisitor<T> predicate) {
		return (ListVisitService<T>) super.filter(predicate);
	}

	@Override
	public ListVisitService<T> fireBatch(FireBatchVisitor<T> fireVisitor){
		return (ListVisitService<T>) super.fireBatch(fireVisitor);
	}

	@Override
	public ListVisitService<T> fireBatch(Object param, FireBatchVisitor<T> visitor, ThrowableVisitor tv) {
		return (ListVisitService<T>) super.fireBatch(param, visitor, tv);
	}
	@Override
	public ListVisitService<T> fireBatch(FireBatchVisitor<T> visitor, ThrowableVisitor tv) {
		return (ListVisitService<T>) super.fireBatch(visitor, tv);
	}
	@Override
	public ListVisitService<T> fire(Object param, FireVisitor<T> visitor, ThrowableVisitor tv) {
		return (ListVisitService<T>) super.fire(param, visitor, tv);
	}
	@Override
	public ListVisitService<T> fire(FireVisitor<T> visitor, ThrowableVisitor tv) {
		return (ListVisitService<T>) super.fire(visitor, tv);
	}
	@Override
	public ListVisitService<T> fire(FireVisitor<T> fireVisitor) {
		return (ListVisitService<T>) super.fire(fireVisitor);
	}

	@Override
	public ListVisitService<T> queryList(Object param, PredicateVisitor<? super T> predicate) {
		return (ListVisitService<T>) super.queryList(param, predicate);
	}

	@Override
	public ListVisitService<T> queryList(PredicateVisitor<? super T> predicate) {
		return (ListVisitService<T>) super.queryList(predicate);
	}
	@Override
	public <R> ListVisitService<R> map(Object param, Comparator<? super R> sort, ResultVisitor<? super T, R> resultVisitor) {
		return (ListVisitService<R>) super.map(param, sort, resultVisitor);
	}
	@Override
	public <R> ListVisitService<R> map(Object param, ResultVisitor<? super T, R> visitor) {
		return (ListVisitService<R>) super.map(param, visitor);
	}
	@Override
	public <R> ListVisitService<R> map(ResultVisitor<? super T, R> visitor) {
		return (ListVisitService<R>) super.map(visitor);
	}

	// ==============================================================
	@Override
	public <T2> ListVisitService<T2> mapIndexed(Object param, ResultIndexedVisitor<T, T2> result) {
		List<T2> results = new ArrayList<>();
		List<T> list = getAsList();
		for(int i = 0, size = list.size() ; i < size ; i++ ){
			T2 temp = result.visit(param, list.get(i), i, size);
			if(temp != null){
				results.add(temp);
			}
		}
		return VisitServices.from(results);
	}

	@Override
	public KeyValuePair<Integer, T> queryIndex(Object param, PredicateVisitor<T> visitor) {
		List<T> list = getAsList();
		for(int i = 0, size = list.size() ; i < size ; i ++){
			T t = list.get(i);
			if(Predicates.isTrue(visitor.visit(t, param))){
				return KeyValuePair.create(i, t);
			}
		}
		return null;
	}

	@Override
	public <R> R mapResult(Object param, ResultVisitor<T, R> visitor) {
		return mapResult(param, visitor, new PredicateVisitor<R>() {
			@Override
			public Boolean visit(R r, Object param) {
				return r != null;
			}
		});
	}

	@Override
	public <R> R mapResult(Object param, ResultVisitor<T, R> visitor, PredicateVisitor<R> predicate) {
		R result = null;
		for (T t : get()){
			result = visitor.visit(t, param);
			if(Predicates.isTrue(predicate.visit(result, param) )){
				break;
			}
		}
		return result;
	}

	@Override
	public ListVisitService<T> fireMulti(int count, Object param, FireMultiVisitor<T> visitor) {
		return fireMulti(count, count, param, visitor);
	}

	@Override
	public ListVisitService<T> fireMulti(int count, int step, Object param, final FireMultiVisitor<T> visitor) {
		return fireMulti2(count, step, param, new FireMultiVisitor2<T>() {
			@Override
			public boolean visit(Object param, int count, int step, List<T> ts) {
				visitor.visit(param, count, step, ts);
				return false;
			}
		});
	}

	@Override
	public ListVisitService<T> fireMulti2(int count, Object param, FireMultiVisitor2<T> visitor) {
		return fireMulti2(count, count, param, visitor);
	}

	@Override
	public ListVisitService<T> fireMulti2(int count, int step, Object param, FireMultiVisitor2<T> visitor) {
		Throwables.checkNull(visitor);
		final List<T> list = visitForQueryList(Visitors.truePredicateVisitor(), mCacheList);
		if(list.isEmpty()){
			throw new IllegalStateException();
		}
		final int[] indexes = new int[count];
		//init index
		for(int i = 0  ; i < count ; i ++){
			indexes[i] = i;
		}
		List<T> ns = new ArrayList<>();
		int size = list.size();
		do{
			for(int index : indexes){
				//the index >= size will be ignore
				if(index < size) {
					ns.add(list.get(index));
				}
			}
			//should stop
			if(visitor.visit(param, count, step, ns)){
                break;
			}
			ns.clear();
			//add by step
			for(int i = 0  ; i < count ; i ++){
				indexes[i] += step;
			}
		}while (indexes[0] < size);
		list.clear();
		return this;
	}

	@Override
	public ListVisitService<List<T>> group(int memberCount, boolean dropNotEnough) {
		int size = size();
		int left = size % memberCount;
		final int step = size / memberCount;
		if(dropNotEnough){
			left = step;
		}

		List<T> src = getAsList();
		List<List<T>> list = new ArrayList<List<T>>();
		int start = 0;
		for(int i = 0 ; i < memberCount ; i ++){
			List<T> ele;
			if(i == memberCount - 1){
				ele = src.subList(start, start + left);
			}else{
				ele = src.subList(start, start + step);
			}
			list.add(ele);
			start += step;
		}
		return VisitServices.from(list);
	}

	@Override
	public ListVisitService<T> asListService() throws UnsupportedOperationException {
		return this;
	}
	@Override
	protected Iterator<T> getIterator(Collection<T> coll) {
		return Collections2.asList(coll).listIterator();
	}

	@Override
	protected boolean onHandleInsert(List<CollectionOperation<T>> inserts, Iterator<T> it, T t, Object param,
			IterationInfo info) {
		final ListIterator<T> lit = (ListIterator<T>) it;
		info.setCurrentIndex(lit.previousIndex());

		// final boolean hasInfo = info != null;
		try {
			CollectionOperation<T> op;
			for (ListIterator<CollectionOperation<T>> olit = inserts.listIterator(); olit.hasNext();) {
				op = olit.next();
				if (op.insert(lit, t, param, info)) {
					// info update: impl move to internal
					return true;
				}
			}
		} catch (UnsupportedOperationException e) {
			System.err.println("insert failed. caused by the list is fixed. "
					+ "so can't modified. are your list comes from 'Arrays.asList(...)' ? ");
		}
		return false;
	}

	@Override
	public ListVisitService<T> subService(int start, int count) {
		if (count <= 0) {
			throw new IllegalArgumentException();
		}
		return VisitServices.from(asList().subList(start, start + count));
	}
	
	@Override
	public CollectionVisitService<T> subService(Object param, PredicateVisitor<T> visitor) {
		checkNull(visitor);
		final List<T> list = new ArrayList<T>();
		for(T t : asList()){
			if(isTrue(visitor.visit(t, param))){
				list.add(t);
			}
		}
		return VisitServices.from(list);
	}
	
	@Override
	public ListVisitService<T> headService(int count) {
		return subService(0, count);
	}

	@Override
	public ListVisitService<T> tailService(int count) {
		final List<T> list = asList();
		final int toIndex = list.size(); // 5 ,3 --> 2,5
		final int startIndex = toIndex - count;
		if (startIndex <= 0) {
			throw new IllegalArgumentException("count must smaller than list.size()");
		}
		return VisitServices.from(list.subList(startIndex, toIndex));
	}

	@Override
	public ListVisitService<T> reverseService(boolean reverseOriginList) {
		if (reverseOriginList) {
			Collections.reverse(asList());
			return this;
		} else {
			final List<T> list = new ArrayList<T>(asList());
			Collections.reverse(list);
			return VisitServices.from(list);
		}
	}

	@Override
	public ListVisitService<T> reverseService() {
		return reverseService(true);
	}

	@Override
	public ListVisitService<T> shuffleService() {
		return shuffleService(true);
	}

	@Override
	public ListVisitService<T> shuffleService(boolean shuffleOriginlist) {
		if (shuffleOriginlist) {
			Collections.shuffle(asList());
			return this;
		} else {
			final List<T> list = new ArrayList<T>(asList());
			Collections.shuffle(list);
			return VisitServices.from(list);
		}
	}

	@Override
	public ListVisitService<T> sortService(Comparator<? super T> c, boolean sortOriginList) {
		if (sortOriginList) {
			Collections.sort(asList(), c);
			return this;
		} else {
			final List<T> list = new ArrayList<T>(asList());
			Collections.sort(list, c);
			return VisitServices.from(list);
		}
	}

	@Override
	public ListVisitService<T> sortService(Comparator<? super T> comparator) {
		return sortService(comparator, true);
	}

	@Override
	public ListVisitService<String> joinToStringService(ResultVisitor<T, String> stringVisitor, String joinMark,
			int everyGroupCount) {
		return joinToStringService(null, stringVisitor, joinMark, everyGroupCount);
	}

	@Override
	public ListVisitService<String> joinToStringService(@Nullable Object param, ResultVisitor<T, String> stringVisitor,
			String joinMark, int everyGroupCount) {
		if (joinMark == null || everyGroupCount <= 0) {
			throw new IllegalArgumentException();
		}
		final List<T> list = asList();
		final List<String> strs = new ArrayList<String>();
		if (mSb == null) {
			mSb = new StringBuilder();
		}
		final StringBuilder sb = mSb;

		final int size = list.size();
		for (int i = 0; i < size; i++) {
			sb.append(stringVisitor.visit(list.get(i), param));
			if ((i + 1) % everyGroupCount == 0) {
				strs.add(sb.toString());
				sb.delete(0, sb.length());
			} else {
				sb.append(joinMark);
			}
		}
		if (size % everyGroupCount != 0) {
			int length = sb.length();
			sb.delete(length - joinMark.length(), length);
			strs.add(sb.toString());
		}
		sb.delete(0, sb.length());
		return VisitServices.from(strs);
	}

	@Override
	public ListVisitService<String> joinToStringService(String joinMark, int everyGroupCount) {
		return joinToStringService(null, Visitors.<T>toStringVisitor(), joinMark, everyGroupCount);
	}

	@Override
	public String joinToString(String joinMark) {
		if(mTempStrs == null){
			mTempStrs = new ArrayList<String>();
		}
		joinToStringService(joinMark, Integer.MAX_VALUE).save(mTempStrs, true);
		return mTempStrs.size() > 0 ? mTempStrs.get(0) : "";
	}
	
	@Override
	public <K> MapVisitService<K, List<T>> groupService(ResultVisitor<T, K> keyVisitor) {
		return groupService(null, keyVisitor);
	}
	
	@Override
	public <K> MapVisitService<K, List<T>> groupService(@Nullable Object param, 
			ResultVisitor<T, K> keyVisitor) {
		return groupService(param, keyVisitor, Visitors.<T,T>unchangeResultVisitor());
	}

	@Override
	public <K, V> MapVisitService<K, List<V>> groupService(Object param, ResultVisitor<T, K> keyVisitor,
			ResultVisitor<T, V> valueVisitor) {
		return groupService(param, null, keyVisitor, valueVisitor);
	}

	@Override
	public <K, V> MapVisitService<K, List<V>> groupService(Object param, Comparator<? super K> c,
														   ResultVisitor<T, K> keyVisitor, ResultVisitor<T, V> valueVisitor) {
		checkNull(keyVisitor);
		checkNull(valueVisitor);
		final List<T> list = asList();
		final Map<K, List<V>> map = InternalUtil.newMap(c);
		List<V> val;
		K key;
		for(T t : list){
			key = keyVisitor.visit(t, param);
			val = map.get(key);
			if(val == null){
				val = new ArrayList<V>();
				map.put(key, val);
			}
			V valueResult = valueVisitor.visit(t, param);
			if(valueResult != null) {
				val.add(valueResult);
			}
		}
		return VisitServices.from(map);
	}

	@Override
	public ListVisitService<List<T>> groupService(int everyGroupCount) {
		final List<List<T>> totalList = new ArrayList<List<T>>();
		final List<T> list = asList();
		
		final int size = list.size();
		List<T> temp = new ArrayList<T>();
		
		for (int i = 0; i < size; i++) {
			temp.add(list.get(i));
			if ((i + 1) % everyGroupCount == 0) {
				totalList.add(temp);
				temp = new ArrayList<T>();
			} 
		}
		if (size % everyGroupCount != 0) {
			totalList.add(temp);
		}
		return VisitServices.from(totalList);
	}

	@Override
	public ListVisitService<T> fireWithStartEnd(StartEndVisitor<T> fireVisitor) {
		return fireWithStartEnd(null, fireVisitor, null);
	}

	@Override
	public ListVisitService<T> fireWithStartEnd(Object param, StartEndVisitor<T> fireVisitor) {
		return fireWithStartEnd(param, fireVisitor, null);
	}

	@Override
	public ListVisitService<T> fireWithStartEnd(Object param, StartEndVisitor<T> fireVisitor, ThrowableVisitor tv) {
		Throwables.checkNull(fireVisitor);
		final List<T> list = visitForQueryList(Visitors.truePredicateVisitor(), mCacheList);
		try {
			for(int i = 0 , size = list.size() ; i < size ; i ++){
				fireVisitor.visit(param, list.get(i), i == 0, i == size - 1);
			}
		} catch (Throwable e) {
			processThrowable(e, tv);
		} finally {
			list.clear();
		}
		return this;
	}

	@Override
	public ListVisitService<T> fireWithIndex(Object param, FireIndexedVisitor<T> fireVisitor, ThrowableVisitor tv) {
		Throwables.checkNull(fireVisitor);
		final List<T> list = visitForQueryList(Visitors.truePredicateVisitor(), mCacheList);
		try {
			for(int i = 0 , size = list.size() ; i < size ; i ++){
				fireVisitor.visit(param, list.get(i), i, size);
			}
		} catch (Throwable e) {
			processThrowable(e, tv);
		} finally {
			list.clear();
		}
		return this;
	}

	@Override
	public ListVisitService<T> fireWithIndex(Object param, FireIndexedVisitor<T> fireVisitor) {
		return fireWithIndex(param, fireVisitor, null);
	}

	@Override
	public ListVisitService<T> fireWithIndex(FireIndexedVisitor<T> fireVisitor) {
		return fireWithIndex(null, fireVisitor, null);
	}
	//============
	
	@Override
	public OperateManager<T> beginOperateManager() {
		return new OperateManagerImpl(){
			@Override
			public ListVisitService<T> endAsList() throws UnsupportedOperationException {
				return ListVisitServiceImpl.this;
			}
		};
	}

	//for  OperateManagerImpl return the original OperateManager. even if you wapped.
	// so this must cause bug of exception(UnsupportedOperationException)
	/*
	 * @Override
	public OperateManager<T> beginOperateManager() {
		return new WrappedOperateManager<T>(super.beginOperateManager()) {
			@Override
			public ListVisitService<T> endAsList() throws UnsupportedOperationException {
				return ListVisitServiceImpl.this;
			}
		};
	}*/

}
