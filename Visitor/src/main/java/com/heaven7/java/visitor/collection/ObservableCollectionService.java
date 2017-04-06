package com.heaven7.java.visitor.collection;

import java.util.Collection;

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
public class ObservableCollectionService<T> {

	private final Collection<T> mCollection;

	public ObservableCollectionService(Collection<T> mCollection) {
		super();
		this.mCollection = mCollection;
	}
	
	public ObservableCollectionService<T> clear(){
		return apply(OperateConditions.ofClear(null));
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
