package com.heaven7.java.visitor.collection;

import com.heaven7.java.visitor.*;
import com.heaven7.java.visitor.anno.DependOn;
import com.heaven7.java.visitor.anno.Independence;
import com.heaven7.java.visitor.anno.Nullable;
import com.heaven7.java.visitor.util.Observer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * list visit service 
 * @author heaven7
 *
 * @param <T> the element type.
 * @since 1.1.2
 */
public interface ListVisitService<T> extends CollectionVisitService<T>{

	@Override
	ListVisitService<String> mapString();
	@Override
	ListVisitService<Float> mapFloat();
	@Override
	ListVisitService<Double> mapDouble();
	@Override
	ListVisitService<Integer> mapInt();
	@Override
	ListVisitService<Byte> mapByte();

	@Override
	ListVisitService<List<T>> merge();

	@Override
	<R> ListVisitService<R> separate();

	@Override
	<T2> ListVisitService<T2> asAnother();
	@Override
	<T2> ListVisitService<T2> asAnother(Class<T2> clazz) throws ClassCastException;

	@Override
	<R> ListVisitService<R> map(ResultVisitor<? super T, R> visitor);
	@Override
	<R> ListVisitService<R> map(Object param, ResultVisitor<? super T, R> visitor);

	<R> ListVisitService<R> map(Object param, Comparator<? super R> sort, ResultVisitor<? super T, R> resultVisitor);

	@Override
	ListVisitService<T> queryList(PredicateVisitor<? super T> predicate);
	@Override
	ListVisitService<T> queryList(Object param, PredicateVisitor<? super T> predicate);
	@Override
	ListVisitService<T> intersect(Collection<? super T> coll);

	@Override
	ListVisitService<T> filter(PredicateVisitor<T> predicate);
	@Override
	ListVisitService<T> filter(Object param, PredicateVisitor<T> predicate, List<T> dropOut);
	@Override
	ListVisitService<T> filter(Object param, PredicateVisitor<T> predicate, int maxCount, List<T> dropOut);
	@Override
	ListVisitService<T> filter(Object param, Comparator<? super T> com, PredicateVisitor<T> predicate, int maxCount, List<T> dropOut);

	@Override
	ListVisitService<T> fire(FireVisitor<T> fireVisitor);

	@Override
	ListVisitService<T> fire(FireVisitor<T> fireVisitor, ThrowableVisitor throwVisitor);

	@Override
	ListVisitService<T> fire(Object param, FireVisitor<T> fireVisitor, ThrowableVisitor throwVisitor);

	@Override
	ListVisitService<T> fireBatch(FireBatchVisitor<T> fireVisitor);
	@Override
	ListVisitService<T> fireBatch(FireBatchVisitor<T> fireVisitor, ThrowableVisitor throwVisitor);
	@Override
	ListVisitService<T> fireBatch(Object param, FireBatchVisitor<T> fireVisitor, ThrowableVisitor throwVisitor);

	@Override
	ListVisitService<T> save(Collection<T> out);
	@Override
	ListVisitService<T> save(SaveVisitor<T> visitor);
	@Override
	ListVisitService<T> save(Collection<T> out, boolean clearBeforeSave);

	@Override
	ListVisitService<T> addIfNotExist(T newT);
	@Override
	ListVisitService<T> addIfNotExist(T newT, Observer<T, Void> observer);

	@Override
	ListVisitService<T> removeIfExist(T newT);

	@Override
	ListVisitService<T> removeIfExist(T newT, Observer<T, Void> observer);

	@Override
	ListVisitService<T> removeRepeat();

	@Override
	ListVisitService<T> removeRepeat(WeightVisitor<T> weightVisitor);

	@Override
	ListVisitService<T> removeRepeat(Object param, Comparator<? super T> com);

	@Override
	ListVisitService<T> removeRepeat(Object param, Comparator<? super T> com, WeightVisitor<T> weightVisitor);

	@Override
	ListVisitService<T> reset(int flags);

	@Override
	ListVisitService<T> resetAll();

	@Override
	<R> ListVisitService<T> zipResult(Object param, ResultVisitor<T, R> visitor, Observer<T, List<R>> observer);
	@Override
	ListVisitService<T> zip(Object param, PredicateVisitor<T> visitor, Observer<T, Void> observer);

	/**
	 * map to another type with index.
	 * @param param the param
	 * @param result the result index visitor.
	 * @param <T2> the target type to map
	 * @return the mapped list visit service
	 * @since 1.3.3
	 */
	@Independence
	<T2> ListVisitService<T2> mapIndexed(Object param, ResultIndexedVisitor<T, T2> result);

	/**
	 * query item with index, then pack the item and index as a pair.
	 * @param param the param
	 * @param visitor the predicate visitor.
	 * @return the pair which contains index and item.
	 * @since 1.3.0.1
	 */
	@Independence
	KeyValuePair<Integer, T> queryIndex(Object param, PredicateVisitor<T> visitor);

	/**
	 * map a result by a element. if any one map success. the iterate will stop. if all map failed. return null.
	 * @param param the param
	 * @param visitor the result visitor
	 * @param <R> the result type
	 * @return the result
	 * @since 1.3.0
	 */
	@Independence
	<R> R mapResult(Object param, ResultVisitor<T, R> visitor);

	/**
	 * map a result by a element. if any one map success. the iterate will stop. if all map failed. return null.
	 * @param param the param
	 * @param visitor the result visitor
	 * @param predicate the predicate visitor to decide the result is success or not.
	 * @param <R> the result type
	 * @return the result
	 * @since 1.3.0
	 */
	@Independence
	<R> R mapResult(Object param, ResultVisitor<T, R> visitor, PredicateVisitor<R> predicate);

	//--------------------------------- 1.2.0--------------------------------------------

	/**
	 * fire elements with multi elements at the same time. and 'step = count'.
	 * @param count the count of every fire
	 * @param param the param
	 * @param visitor the fire multi visitor
	 * @return this
	 * @see #fireMulti(int, int, Object, FireMultiVisitor)
	 * @since 1.2.2
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	ListVisitService<T> fireMulti(int count, @Nullable Object param, FireMultiVisitor<T> visitor);
	/**
	 * fire elements with multi elements at the same time.
	 * @param count the count of every fire
	 * @param step  the step of fire
	 * @param param the param
	 * @param visitor the fire multi visitor
	 * @return this
	 * @since 1.2.2
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	ListVisitService<T> fireMulti(int count, int step, @Nullable Object param, FireMultiVisitor<T> visitor);

	/**
	 * fire elements with multi elements at the same time. and 'step = count'.
	 * @param count the count of every fire
	 * @param param the param
	 * @param visitor the fire multi visitor , which can stop next visit.
	 * @return this
	 * @see #fireMulti(int, int, Object, FireMultiVisitor)
	 * @since 1.2.9.1
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	ListVisitService<T> fireMulti2(int count, @Nullable Object param, FireMultiVisitor2<T> visitor);
	/**
	 * fire elements with multi elements at the same time.
	 * @param count the count of every fire
	 * @param step  the step of fire
	 * @param param the param
	 * @param visitor the fire multi visitor, which can stop next visit.
	 * @return this
	 * @since 1.2.9.1
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	ListVisitService<T> fireMulti2(int count, int step, @Nullable Object param, FireMultiVisitor2<T> visitor);

	/**
	 * group a collection to lists service
	 * @param memberCount the member count of a group
	 * @param dropNotEnough true if not enough drop the elements
	 * @return the new service
	 * @since 1.2.0
	 */
	ListVisitService<List<T>> group(int memberCount, boolean dropNotEnough);
	//--------------------------------- end 1.2.0--------------------------------------------

	/**
	 * fire the all element with index by target {@linkplain FireIndexedVisitor} and etc.
	 * @param fireVisitor fire index visitor
	 * @return this
	 * @since 1.2.0
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	ListVisitService<T> fireWithIndex(FireIndexedVisitor<T> fireVisitor);
	/**
	 * fire the all element with index by target {@linkplain FireIndexedVisitor} and etc.
	 * @param param the parameter , can be null
	 * @param fireVisitor fire index visitor
	 * @return this
	 * @since 1.2.0
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	ListVisitService<T> fireWithIndex(@Nullable Object param, FireIndexedVisitor<T> fireVisitor);

	/**
	 * fire the all element with index by target {@linkplain FireIndexedVisitor} and etc.
	 * @param param the parameter , can be null
	 * @param fireVisitor fire index visitor
	 * @param throwVisitor the throwable visitor, can be null.
	 * @return this
	 * @since 1.2.0
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	ListVisitService<T> fireWithIndex(@Nullable Object param, FireIndexedVisitor<T> fireVisitor, @Nullable ThrowableVisitor throwVisitor);


	/**
	 * fire the all element with start or end by target {@linkplain StartEndVisitor} and etc.
	 * @param fireVisitor fire start/end visitor
	 * @return this
	 * @since 1.2.0
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	ListVisitService<T> fireWithStartEnd(StartEndVisitor<T> fireVisitor);

	/**
	 * fire the all element with start or end by target {@linkplain StartEndVisitor} and etc.
	 * @param param the parameter , can be null
	 * @param fireVisitor fire start/end visitor
	 * @return this
	 * @since 1.2.0
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	ListVisitService<T> fireWithStartEnd(@Nullable Object param, StartEndVisitor<T> fireVisitor);

	/**
	 * fire the all element with start or end by target {@linkplain StartEndVisitor} and etc.
	 * @param param the parameter , can be null
	 * @param fireVisitor fire start/end visitor
	 * @param throwVisitor the throwable visitor, can be null.
	 * @return this
	 * @since 1.2.0
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	ListVisitService<T> fireWithStartEnd(@Nullable Object param, StartEndVisitor<T> fireVisitor, @Nullable ThrowableVisitor throwVisitor);
	
	/**
	 * get a sub visit service by the target start index and count.
	 * @param start the start index of list
	 * @param count the count of you want 
	 * @return a instance of {@linkplain ListVisitService}
	 * @since 1.1.2
	 */
	@Independence
	ListVisitService<T> subService(int start, int count);

	/**
	 * get a sub visit service safely by the target start index and count.
	 * relative
	 * @param start the start index of list
	 * @param count the count of you want
	 * @return a instance of {@linkplain ListVisitService}
	 * @since 1.3.6
	 */
	@Independence
	default ListVisitService<T> subServiceSafely(int start, int count){
		int val = Math.min(start + count, size());
		//empty
		if(val == 0){
			return VisitServices.from(new ArrayList<>());
		}
		return subService(start, val);
	}
	
	/**
	 * return a sub visit service .from current ListVisitService.
	 * @param count the count from the first element of list
	 * @return {@linkplain ListVisitService}
	 * @since 1.1.2
	 */
	@Independence
	ListVisitService<T> headService(int count);

	/**
	 * return a sub visit service safely from current ListVisitService.
	 * @param count the count from the first element of list
	 * @return {@linkplain ListVisitService}
	 * @since 1.3.6
	 */
	@Independence
	default ListVisitService<T> headServiceSafely(int count){
		int val = Math.min(count, size());
		//empty
		if(val == 0){
			return VisitServices.from(new ArrayList<>());
		}
		return headService(val);
	}
	
	/**
	 * return a sub visit service. from current ListVisitService. 
	 * <ul>
	 * <li>from index is list.size - count.
	 *  <li>to index is list.size.
	 *  </ul>
	 * @param count the count from the last element of list
	 * @return {@linkplain ListVisitService}
	 * @since 1.1.2
	 */
	@Independence
	ListVisitService<T> tailService(int count);

	/**
	 * return a sub visit service safely from current ListVisitService.
	 * <ul>
	 * <li>from index is list.size - count.
	 *  <li>to index is list.size.
	 *  </ul>
	 * @param count the count from the last element of list
	 * @return {@linkplain ListVisitService}
	 * @since 1.3.6
	 */
	@Independence
	default ListVisitService<T> tailServiceSafely(int count){
		final int totalSize = size();
		count = Math.min(count, totalSize);
		//empty
		if(count == 0){
			return VisitServices.from(new ArrayList<>());
		}
		return subService(totalSize - count, totalSize);
	}
	
	/**
	 * reverse the order of list. this is equal to 
	 * <p>reverseService(true)
	 * @return {@linkplain ListVisitService}
	 * @since 1.1.2
	 */
	@Independence
	ListVisitService<T> reverseService();
	
	/**
	 * reverse the order of list
	 * @param reverseOriginList true to reverse the origin list.false otherwise.
	 * @return {@linkplain ListVisitService}
	 * @since 1.1.2
	 */
	@Independence
	ListVisitService<T> reverseService(boolean reverseOriginList);
	
	/**
	 * shuffle the collection.
	 * @param shuffleOriginlist shuffle the origin list or not.
	 * @return this or new {@linkplain ListVisitService}
	 * @since 1.1.2
	 */
	@Independence
	ListVisitService<T> shuffleService(boolean shuffleOriginlist);
	
	/**
	 * shuffle the collection.
	 * @return this
	 * @since 1.1.2
	 */
	@Independence
	ListVisitService<T> shuffleService();
	
	/**
	 * sort the collection.
	 * @param c the comparator used to sort.
	 * @return this
	 * @see #sortService(Comparator, boolean)
	 * @since 1.1.2
	 */
	@Independence
	ListVisitService<T> sortService(Comparator<? super T> c);
	
	/**
	 * sort the collection.
	 * @param c the comparator used to sort.
	 * @param sortOriginList sort the origin list or not.
	 * @return this or new {@linkplain ListVisitService}
	 * @see #sortService(Comparator)
	 * @since 1.1.2
	 */
	@Independence
	ListVisitService<T> sortService(Comparator<? super T> c, boolean sortOriginList);
	
	/**
	 * group the collection to {@linkplain MapVisitService}.
	 * @param <K> the key type
	 * @param keyVisitor the key visitor.
	 * @return {@linkplain MapVisitService}
	 * @see #groupService(Object, ResultVisitor, ResultVisitor)
	 * @see #groupService(ResultVisitor)
	 * @since 1.1.2
	 */
	@Independence
	<K> MapVisitService<K, List<T>> groupService(ResultVisitor<T, K> keyVisitor);
	
	/**
	 * group the collection to {@linkplain MapVisitService}.
	 * @param <K> the key type
	 * @param param the parameter which is used to visitor.
	 * @param keyVisitor the key visitor.
	 * @return {@linkplain MapVisitService}
	 * @see #groupService(Object, ResultVisitor, ResultVisitor)
	 * @see #groupService(ResultVisitor)
	 * @since 1.1.2
	 */
	@Independence
	<K> MapVisitService<K, List<T>> groupService(@Nullable Object param, ResultVisitor<T, K> keyVisitor);
	
	/**
	 * group the collection to {@linkplain MapVisitService}.
	 * @param <K> the key type
	 * @param <V> the type of list value
	 * @param param the parameter which is used to visitor.
	 * @param keyVisitor the key visitor.
	 * @param valueVisitor the value visitor.
	 * @return {@linkplain MapVisitService}
	 * @see #groupService(Object, ResultVisitor, ResultVisitor)
	 * @see #groupService(ResultVisitor)
	 * @since 1.1.2
	 */
	@Independence
	<K, V> MapVisitService<K, List<V>> groupService(@Nullable Object param, ResultVisitor<T, K> keyVisitor,
			ResultVisitor<T, V>  valueVisitor);

	/**
	 * group the collection to {@linkplain MapVisitService}.
	 * @param <K> the key type
	 * @param <V> the type of list value
	 * @param param the parameter which is used to visitor.
	 * @param keyVisitor the key visitor.
	 * @param valueVisitor the value visitor.
	 * @return {@linkplain MapVisitService}
	 * @see #groupService(Object, ResultVisitor, ResultVisitor)
	 * @see #groupService(ResultVisitor)
	 * @since 1.2.7
	 */
	@Independence
	<K, V> MapVisitService<K, List<V>> groupService(@Nullable Object param, Comparator<? super K> c,
													ResultVisitor<T, K> keyVisitor, ResultVisitor<T, V>  valueVisitor);
	
	/**
	 * group the list to scrap ListVisitService.
	 * @param memberCount the member count of group, But may last group's count smaller than it. 
	 * @return ListVisitService
	 * @since 1.1.2
	 */
	@Independence
	ListVisitService<List<T>> groupService(int memberCount);
	
	//==================== String ===========================
	/**
	 * join or concat the group elements to string list.
	 * @param joinMark the join/concat mark
	 * @param everyGroupCount the count of every group.may last group's smaller than this. 
	 * @return String ListVisitService
	 * @since 1.1.2
	 */
	@Independence
	ListVisitService<String> joinToStringService(String joinMark, int everyGroupCount);
	
	/**
	 * join or concat the group elements to string list.
	 * @param param the parameter which is used by visitor.
	 * @param stringVisitor the result visitor.
	 * @param joinMark the join/concat mark
	 * @param everyGroupCount the count of every group.may last group's smaller than this. 
	 * @return String ListVisitService
	 * @since 1.1.2
	 */
	@Independence
	ListVisitService<String> joinToStringService(@Nullable Object param, 
			ResultVisitor<T, String> stringVisitor, String joinMark, int everyGroupCount);
	
	/**
	 * join or concat the group elements to string list.
	 * @param stringVisitor the result visitor.
	 * @param joinMark the join/concat mark
	 * @param everyGroupCount the expect count of every group. But may last group's smaller than it. 
	 * @return String ListVisitService
	 * @since 1.1.2
	 */
	@Independence
	ListVisitService<String> joinToStringService(ResultVisitor<T, String> stringVisitor,
			String joinMark, int everyGroupCount);
	
	/**
	 * join or concat the all elements to a string.
	 * @param joinMark the join/concat mark
	 * @return a join string result
	 * @since 1.1.2
	 */
	@Independence
	String joinToString(String joinMark);
	//========================= end string ========================
}
