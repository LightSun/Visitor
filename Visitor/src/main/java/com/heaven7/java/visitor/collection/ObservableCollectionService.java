package com.heaven7.java.visitor.collection;

import java.util.Collection;
import java.util.List;

import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.anno.Nullable;
import com.heaven7.java.visitor.collection.operator.CollectionCondition;
import com.heaven7.java.visitor.collection.operator.OperateConditions;
import com.heaven7.java.visitor.collection.operator.Operator;
import com.heaven7.java.visitor.util.Observer;
import com.heaven7.java.visitor.util.Throwables;

/**
 * this class indicate the observable collection service that can observe the all operations.
 * such as: contains. clear.add , remove and son on.
 * @param <T> the element type of collection.
 * 
 * @author heaven7
 * @since 2.0.0
 * @see Observer
 * @see CollectionCondition
 * @see Operator
 */
/*public*/ final class ObservableCollectionService<T> {

	private final Collection<T> mCollection;

	/*public*/ ObservableCollectionService(Collection<T> mCollection) {
		super();
		this.mCollection = mCollection;
	}
	
	public ObservableCollectionService<T> fire(ResultVisitor<? super T, Boolean>  visitor,
			Observer<T, Boolean> observer){
		return fire(null, visitor, observer);
	}
	
	public ObservableCollectionService<T> fire(Object param, ResultVisitor<? super T, Boolean>  visitor,
			Observer<T, Boolean> observer){
		return apply(OperateConditions.ofFire(param, visitor, observer));
	}
	
	public ObservableCollectionService<T> size(Observer<T, Integer> observer){
		return apply(OperateConditions.ofSize(observer));
	}
	
	public ObservableCollectionService<T> sizeEquals(int expectSize, Observer<T, Boolean> observer) {
		return apply(OperateConditions.ofSizeEquals(expectSize, observer));
	}
	
	public ObservableCollectionService<T> sizeMin(int minSize, Observer<T, Boolean> observer) {
		return apply(OperateConditions.ofSizeMin(minSize, observer));
	}
	
	public ObservableCollectionService<T> sizeMax(int maxSize, Observer<T, Boolean> observer) {
		return apply(OperateConditions.ofSizeMax(maxSize, observer));
	}
	
	/**
	 * filter current collection by target predicate visitor.
	 * @param param the parameter which can used by predicate visitor. 
	 * @param predicate the predicate visitor.
	 * @param observer the list result observer, the result order determined by the source collection is ordered or not.
	 * @return this
	 */
	public ObservableCollectionService<T> filter(Object param,
			PredicateVisitor<? super T> predicate, Observer<T, List<T>> observer) {
		return apply(OperateConditions.ofFilter(Integer.MAX_VALUE, param, predicate, observer));
	}
	
	/**
	 * filter current collection by target predicate visitor.
	 * @param maxLength the max length of list result.
	 * @param param the parameter which can used by predicate visitor. 
	 * @param predicate the predicate visitor.
	 * @param observer the list result observer, the result order determined by the source collection is ordered or not.
	 * @return this
	 */
	public ObservableCollectionService<T> filter(int maxLength , Object param,
			PredicateVisitor<? super T> predicate, Observer<T, List<T>> observer) {
		return apply(OperateConditions.ofFilter(maxLength, param, predicate, observer));
	}
	
	/**
	 * judge if not contains target element.
	 * @param t the target element
	 * @param observer the result observer
	 * @return this.
	 */
	public ObservableCollectionService<T> containsReverse(T t,Observer<T, Boolean> observer){
		return apply(OperateConditions.ofContainsReverse(t, observer));
	}
	public ObservableCollectionService<T> contains(T t, Observer<T, Boolean> observer){
		return apply(OperateConditions.ofContains(t, observer));
	}
	public ObservableCollectionService<T> addAll(Collection<? extends T> coll){
		return addAll(coll, null);
	}
	public ObservableCollectionService<T> addAll(Collection<? extends T> coll, Observer<T, Boolean> observer){
		return apply(OperateConditions.ofAddAll(coll, observer));
	}
	
	public ObservableCollectionService<T> addIfNotExist(T t){
		return addIfNotExist(t, null);
	}
	public ObservableCollectionService<T> addIfNotExist(T t, Observer<T, Boolean> observer){
		return apply(OperateConditions.ofAddIfNotExist(t, observer));
	}
	public ObservableCollectionService<T> add(T t){
		return add(t, null);
	}
	public ObservableCollectionService<T> add(T t, Observer<T, Boolean> observer){
		return apply(OperateConditions.ofAdd(t, observer));
	}
	
	public ObservableCollectionService<T> clear(){
		return clear(null);
	}
	public ObservableCollectionService<T> clear(Observer<T, Boolean> observer){
		return apply(OperateConditions.ofClear(observer));
	}
	
	public ObservableCollectionService<T> replaceAll(ResultVisitor<? super T, T> result) {
		return replaceAll(null, result, null);
	}

	public ObservableCollectionService<T> replaceAll(@Nullable Object param, ResultVisitor<? super T, T> result) {
		return replaceAll(param, result, null);
	}

	public ObservableCollectionService<T> replaceAll(@Nullable Object param, ResultVisitor<? super T, T> result,
			Observer<T, Boolean> observer) {
		return apply(OperateConditions.ofReplaceAll(param, result, observer));
	}

	public ObservableCollectionService<T> removeIf(PredicateVisitor<T> predicate) {
		return removeIf(null, predicate, null);
	}

	public ObservableCollectionService<T> removeIf(Object param, PredicateVisitor<T> predicate) {
		return removeIf(param, predicate, null);
	}

	public ObservableCollectionService<T> removeIf(Object param, PredicateVisitor<T> predicate,
			Observer<T, Boolean> observer) {
		return apply(OperateConditions.ofRemoveIf(param, predicate, observer));
	}

	public ObservableCollectionService<T> retainAll(Collection<? extends T> c) {
		return retainAll(c, null);
	}

	public ObservableCollectionService<T> retainAll(Collection<? extends T> c, Observer<T, Boolean> observer) {
		return apply(OperateConditions.ofRetainAll(c, observer));
	}

	public ObservableCollectionService<T> removeAll(Collection<? extends T> c) {
		return removeAll(c, null);
	}

	public ObservableCollectionService<T> removeAll(Collection<? extends T> c, Observer<T, Boolean> observer) {
		return apply(OperateConditions.ofRemoveAll(c, observer));
	}

	public ObservableCollectionService<T> apply(CollectionCondition<T> condition) {
		Throwables.checkNull(condition);
		condition.apply(mCollection);
		return this;
	}

	/*
	 * CollectionDirectService<T> removeAll(Collection<?> c);
	 * 
	 * CollectionDirectService<T> retainAll(Collection<?> c);
	 * 
	 * CollectionDirectService<T> removeIf(@Nullable Object param,
	 * PredicateVisitor<? super T> filter);
	 * 
	 * CollectionDirectService<T> replaceAll(@Nullable Object param,
	 * ResultVisitor<? super T, T> filter);
	 * 
	 * CollectionDirectService<T> clear();
	 * 
	 * CollectionDirectService<T> contains();
	 * 
	 * CollectionDirectService<T> size();
	 * 
	 * CollectionDirectService<T> addIfNotExist(T newT);
	 * 
	 * CollectionDirectService<T> removeIfExist(T newT);
	 * 
	 * CollectionDirectService<T> queryAll(@Nullable Object param,
	 * PredicateVisitor<? super T> filter);
	 */

	// zip/ zipService(@Nullable Object param, ResultVisitor<T, R> resultVisitor

}
