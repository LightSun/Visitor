package com.heaven7.java.visitor.collection;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.heaven7.java.visitor.IterateVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.anno.Nullable;
/**
 * the pending operate manager used to support filter/delete/update/insert in iteration. and support insert after iteration (before method return).
 * <ul>
 * <li>filter method (in iteration): <br>  {@linkplain OperateManager#filter(Object, PredicateVisitor)}} <br> {@linkplain OperateManager#filter(PredicateVisitor)}}
 * <li>delete method (in iteration): <br> {@linkplain OperateManager#delete(Object, PredicateVisitor)}} <br> {@linkplain OperateManager#delete(PredicateVisitor)}}
 * <li>update method (in iteration): <br> {@linkplain OperateManager#update(Object, PredicateVisitor) <br> {@linkplain OperateManager#update(Object, Object, PredicateVisitor)}
 * <li> insert method (in iteration, and only support for List): <br>
 *   {@linkplain OperateManager#insert(List, IterateVisitor)}  <br>
 *   {@linkplain OperateManager#insert(List, Object, IterateVisitor)}  <br>
 *   {@linkplain OperateManager#insert(Object, IterateVisitor)}  <br>
 *   {@linkplain OperateManager#insert(Object, Object, IterateVisitor)} <br>
 * <li>insert finally method (after iteration ): <br>
 *   {@linkplain OperateManager#insertFinally(List, IterateVisitor)}  <br>
 *   {@linkplain OperateManager#insertFinally(List, Object, IterateVisitor)}  <br>
 *   {@linkplain OperateManager#insertFinally(Object, IterateVisitor)}  <br>
 *   {@linkplain OperateManager#insertFinally(Object, Object, IterateVisitor)} <br>  
 *  </ul>
 * @author heaven7
 *
 * @param <R> the return type for {@link #end()}}
 * @param <T> the parametric type of most method
 */
public abstract class OperateManager<R, T> {

	OperateManager() {
		super();
	}
    
	/**
	 * end the operate and return the target object
	 * @return the original object
	 */
	public abstract R end() ;


	/**
	 * pending filter element in the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
	 * @param filter the predicate visitor for filter.
	 * @return this.
	 * @see {@linkplain OperateManager#filter(Object, PredicateVisitor)}
	 */
	public OperateManager<R, T> filter(PredicateVisitor<? super T> filter) {
		return filter(null, filter);
	}
	
	/**
	 * pending remove/delete element in the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
	 * @param delete the predicate visitor for delete/remove.
	 * @return this.
	 * @see {@linkplain #delete(Object, PredicateVisitor)}.
	 */
	public OperateManager<R, T> delete( PredicateVisitor<? super T> delete) {
		return delete(null, delete);
	}

	/**
	 * pending update element in the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
	 * @param newT the new element to update/replace.
	 * @param update the predicate visitor for update
	 * @return this.
	 * @see {@linkplain #update(Object, Object, PredicateVisitor)}
	 */
	public OperateManager<R, T> update(T newT, PredicateVisitor<? super T> update) {
		return update(newT, null, update);
	}

	/**
	 * pending insert the list in the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
	 * @param list the list to insert
	 * @param insert the insert iterate visitor. see {@linkplain IterateVisitor}.
	 * @return this.
	 * @see {@linkplain #insert(List, Object, IterateVisitor)}
	 */
	public OperateManager<R, T> insert(List<T> list,  IterateVisitor<? super T> insert) {
		return insert(list, null, insert);
	}

	/**
	 * pending insert the element in the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
	 * @param newT the element to insert
	 * @param insert the insert iterate visitor. see {@linkplain IterateVisitor}.
	 * @return this.
	 * @see {@linkplain #insert(Object, Object, IterateVisitor)}
	 */
	public OperateManager<R, T> insert(T newT, IterateVisitor<? super T> insert) {
		return insert(newT, null, insert);
	}

	/**
	 * pending insert the element after the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
	 * @param newT the element to insert
	 * @param insert the insert iterate visitor. see {@linkplain IterateVisitor}.
	 * @return this.
	 * @see {@linkplain #insertFinally(Object, Object, IterateVisitor)}
	 */
	public OperateManager<R, T> insertFinally( T newT, IterateVisitor<T> insert) {
		return insertFinally(newT, null, insert);
	}

	/**
	 * pending insert the list after the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
	 * @param list the list to insert
	 * @param insert the insert iterate visitor. see {@linkplain IterateVisitor}.
	 * @return this.
	 * @see {@linkplain #insertFinally(List, Object, IterateVisitor)}
	 */
	public OperateManager<R, T> insertFinally(List<T> list,IterateVisitor<? super T> insert) {
		return insertFinally(list, null, insert);
	}
	

	// =============== public abstract method ======================
	
	/**
	 * pending filter element in the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
	 * @param param the extra parameter
	 * @param filter the predicate visitor for filter.
	 * @return this.
	 */
	public abstract OperateManager<R, T> filter(@Nullable Object param, PredicateVisitor<? super T> filter);

	/**
	 * pending remove/delete element in the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
	 * @param param the extra parameter
	 * @param delete the predicate visitor for delete/remove.
	 * @return this.
	 */
	public abstract OperateManager<R, T> delete(@Nullable Object param, PredicateVisitor<? super T> delete);

	/**
	 * pending update element in the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
	 * @param newT the new element to update/replace.
	 * @param param the extra parameter
	 * @param update the predicate visitor for update
	 * @return this.
	 */
	public abstract OperateManager<R, T> update(T newT,@Nullable Object param, PredicateVisitor<? super T> update);
	
	/**
	 * pending insert the list in the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
	 * @param list the list to insert
	 * @param param the extra parameter
	 * @param insert the insert iterate visitor. see {@linkplain IterateVisitor}.
	 * @return this.
	 */
	public abstract OperateManager<R, T> insert(List<T> list,@Nullable Object param, IterateVisitor<? super T> insert);

	/**
	 * pending insert the element in the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
	 * @param newT the element to insert
	 * @param param the extra parameter
	 * @param insert the insert iterate visitor. see {@linkplain IterateVisitor}.
	 * @return this.
	 */
	public abstract OperateManager<R, T> insert(T newT,@Nullable Object param, IterateVisitor<? super T> insert);

	/**
	 * pending insert the element after the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
	 * @param newT the element to insert
	 * @param param the extra parameter
	 * @param insert the insert iterate visitor. see {@linkplain IterateVisitor}.
	 * @return this.
	 */
	public abstract OperateManager<R, T> insertFinally(T newT,@Nullable Object param, IterateVisitor<? super T> insert);

	/**
	 * pending insert the list after the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
	 * @param list the list to insert
	 * @param param the extra parameter
	 * @param insert the insert iterate visitor. see {@linkplain IterateVisitor}.
	 * @return this.
	 */
	public abstract OperateManager<R, T> insertFinally(List<T> list,@Nullable Object param, IterateVisitor<? super T> insert);

}
