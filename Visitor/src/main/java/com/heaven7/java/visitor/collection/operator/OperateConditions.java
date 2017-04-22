package com.heaven7.java.visitor.collection.operator;

import java.util.Collection;
import java.util.List;

import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.util.Observer;

/**
 * this class help we fast get the all common operate conditions.
 * @author heaven7
 * @since 2.0.0
 */
public final class OperateConditions {

	//clear
	public static <T> OperateCondition<T, Boolean> ofClear(Observer<T, Boolean> observer) {
		return new OperateCondition<T, Boolean>()
				.observer(observer)
				.operator(Operators.ofClear());
	}
	// removeAll(Collection<?> c);
	public static <T> OperateCondition<T, Boolean> ofRemoveAll(Collection<? extends T> c,
			Observer<T, Boolean> observer) {
		return new OperateCondition<T, Boolean>()
				.focus(c)
				.observer(observer)
				.operator(Operators.ofRemoveAll());
	}

	//retainAll
	public static <T> OperateCondition<T, Boolean> ofRetainAll(Collection<? extends T> c,
			Observer<T, Boolean> observer) {
		return ofRemoveAll(c, observer)
				.operator(Operators.ofRetainAll());
	}

	//removeIf
	public static <T> OperateCondition<T, Boolean> ofRemoveIf(Object param, PredicateVisitor<? super T> predicate,
			Observer<T, Boolean> observer) {
		return new OperateCondition<T, Boolean>()
				.predicate(predicate)
				.extra(param)
				.observer(observer)
				.operator(Operators.ofRemoveIf());
	}

	// replaceAll(@Nullable Object param, ResultVisitor<? super T, T> filter);
	public static <T> OperateCondition<T, Boolean> ofReplaceAll(int maxLength, Object param, 
			ResultVisitor<? super T, T> result, Observer<T, Boolean> observer) {
		return new OperateCondition<T, Boolean>()
				.result(result)
				.extra(param)
				.observer(observer)
				.operator(Operators.ofUpdateAll(maxLength));
	}
	// replaceAll(@Nullable Object param, ResultVisitor<? super T, T> filter);
		public static <T> OperateCondition<T, Boolean> ofReplaceAll(Object param, 
				ResultVisitor<? super T, T> result, Observer<T, Boolean> observer) {
			return ofReplaceAll(Integer.MAX_VALUE, param , result, observer);
		}

	// contains();
	public static <T> OperateCondition<T, Boolean> ofContains(T t, Observer<T, Boolean> observer) {
		return new OperateCondition<T, Boolean>()
				.focus(t)
				.observer(observer)
				.operator(Operators.ofContains());
	}
	
	public static <T> OperateCondition<T, Boolean> ofContainsReverse(T t, Observer<T, Boolean> observer) {
		return new OperateCondition<T, Boolean>()
				.focus(t)
				.observer(observer)
				.operator(Operators.ofContains(true));
	}

	// size();
	public static <T> OperateCondition<T, Integer> ofSize(Observer<T, Integer> observer) {
		return new OperateCondition<T, Integer>()
				.observer(observer)
				.operator(Operators.ofSize());
	}
	// if size >= min
	public static <T> OperateCondition<T, Boolean> ofSizeMin(int minSize, Observer<T, Boolean> observer) {
		return new OperateCondition<T, Boolean>()
				.observer(observer)
				.operator(Operators.ofSizeMin(minSize));
	}
	// if size <= max
	public static <T> OperateCondition<T, Boolean> ofSizeMax(int maxSize, Observer<T, Boolean> observer) {
		return new OperateCondition<T, Boolean>()
				.observer(observer)
				.operator(Operators.ofSizeMax(maxSize));
	}
	//if size == expectSize
	public static <T> OperateCondition<T, Boolean> ofSizeEquals(int expectSize, Observer<T, Boolean> observer) {
		return new OperateCondition<T, Boolean>()
				.observer(observer)
				.operator(Operators.ofSizeEquals(expectSize));
	}

	// add
	public static <T> OperateCondition<T, Boolean> ofAdd(T t, Observer<T, Boolean> observer) {
		return new OperateCondition<T, Boolean>()
				.focus(t)
				.observer(observer)
				.operator(Operators.ofAdd());
	}

	// addIfNotExist
	public static <T> OperateCondition<T, Boolean> ofAddIfNotExist(T t, Observer<T, Boolean> observer) {
		return new OperateCondition<T, Boolean>()
				.focus(t)
				.observer(observer)
				.operator(Operators.ofAddIfNotExist());
	}

	// add -> list
	public static <T> OperateCondition<T, Boolean> ofAddAll(Collection<? extends T> list,
			Observer<T, Boolean> observer) {
		return new OperateCondition<T, Boolean>()
				.focus(list)
				.observer(observer)
				.operator(Operators.ofAddList());
	}

	// remove t
	public static <T> OperateCondition<T, Boolean> ofRemove(T t, Observer<T, Boolean> observer) {
		return new OperateCondition<T, Boolean>()
				.focus(t)
				.observer(observer)
				.operator(Operators.ofRemove());
	}

	// removeIfExist(T newT);
	public static <T> OperateCondition<T, Boolean> ofRemoveIfExist(T t, Observer<T, Boolean> observer) {
		return ofRemove(t, observer)
				.operator(Operators.ofRemoveIfExist());
	}

	// queryAll/filter
	public static <T> OperateCondition<T, List<T>> ofFilter(int maxLength , Object param, PredicateVisitor<? super T> predicate,
			Observer<T, List<T>> observer) {
		return new OperateCondition<T, List<T>>()
				.predicate(predicate)
				.extra(param)
				.observer(observer)
				.operator(Operators.ofFilter(maxLength));
	}
	// queryAll/filter
	public static <T> OperateCondition<T, List<T>> ofFilter(Object param, PredicateVisitor<? super T> predicate,
			Observer<T, List<T>> observer) {
		return ofFilter(Integer.MAX_VALUE, param, predicate, observer);
	}
	
	public static <T> CollectionCondition<T> ofFire(Object param, 
			ResultVisitor<? super T, Boolean>  visitor,  
			Observer<T, Boolean> observer     ) {
		return new OperateCondition<T,Boolean>()
				.result(visitor)
				.extra(param)
				.observer(observer)
				.operator(Operators.ofFire());
	}
}
