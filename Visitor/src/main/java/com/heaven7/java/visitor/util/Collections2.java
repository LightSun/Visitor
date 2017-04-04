package com.heaven7.java.visitor.util;

import static com.heaven7.java.visitor.util.Predicates.isTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedSet;

import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.operator.Operator;
/**
 * expand util class of Collections.
 * @author heaven7
 * @since 1.1.2
 * @see Collections
 * @see ListIterator
 * @see Updatable
 */
public class Collections2 {
	
	/**
	 * update the all element if the result of ResultVisitor return not null. 
	 * @param <T> the element type
	 * @param <R> the result type
	 * @param src the collection
	 * @param param the extra parameter
	 * @param maxLeng the max length of update item count.
	 * @param visitor the result visitor.
	 * @param operator the operator
	 * @return true if update success(at least).
	 * @since 2.0.0
	 * @see ListIterator
     * @see Updatable
	 */
	@SuppressWarnings("unchecked")
	public static <T,R> boolean updateAll(Collection<T> src, Object param, int maxLeng, 
			ResultVisitor<? super T, T> visitor , Operator<T, R> operator) {
        Throwables.checkNull(src);
        Throwables.checkNull(visitor);
        
        boolean isList;
		Iterator<T> li;
		if(src instanceof List){
			isList = true;
			li = ((List<T>) src).listIterator();
		}else{
			isList = false;
			li = src.iterator();
		}
		// iterate
		T t = null;
		T result = null;
		int size = 0;
		for( ; li.hasNext();){
			t = li.next();
			operator.startVisitElement(t);
			result = visitor.visit(t, param);
			if(result != null){
				if(isList){
					((ListIterator<T>)li).set(result);
					if((size ++) > maxLeng){
						break;
					}
				}else if(t instanceof Updatable){
					((Updatable<T>) t).updateFrom(result);
					if((size ++) > maxLeng){
						break;
					}
				}
			}
		}
		return size > 0;
	}
	
	/**
	 * update the element which is assigned by target predicate visitor.
	 * @param <T> the element type
	 * @param <R> the result type
	 * @param src the src collection
	 * @param newT the new element
	 * @param param the param
	 * @param predicate the predicate visitor
	 * @param operator the operator
	 * @return true if update success.
	 * @since 2.0.0
	 * @see ListIterator
     * @see Updatable
	 */
	@SuppressWarnings("unchecked")
	public static <T,R> boolean update(Collection<T> src, T newT,
			Object param, PredicateVisitor<? super T> predicate, Operator<T, R> operator){
		boolean isList;
		Iterator<T> li;
		if(src instanceof List){
			isList = true;
			li = ((List<T>) src).listIterator();
		}else{
			isList = false;
			li = src.iterator();
		}
		// iterate
		T t = null;
		for( ; li.hasNext();){
			t = li.next();
			operator.startVisitElement(t);
			if(isTrue(predicate.visit(t, param))){
				//if list or element impl Updatable.
				if(isList){
					((ListIterator<T>)li).set(newT);
					return true;
				}
				if(t instanceof Updatable){
					((Updatable<T>) t).updateFrom(newT);
					return true;
				}
			}
		}
		return false;
	}
	
	public static <T> List<T> asList(Collection<T> coll){
		if(coll instanceof List){
			return (List<T>) coll;
		}
		throw new UnsupportedOperationException();
	}
	
	public static<T> SortedSet<T> asSortedSet(Collection<T> coll){
		if(coll instanceof SortedSet){
			return (SortedSet<T>) coll;
		}
		throw new UnsupportedOperationException();
	}
	
}
