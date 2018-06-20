package com.heaven7.java.visitor.collection;

import com.heaven7.java.visitor.*;
import com.heaven7.java.visitor.anno.DependOn;
import com.heaven7.java.visitor.anno.Independence;
import com.heaven7.java.visitor.anno.Nullable;

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
	 * return a sub visit service .from current ListVisitService.
	 * @param count the count from the first element of list
	 * @return {@linkplain ListVisitService}
	 * @since 1.1.2
	 */
	@Independence
	ListVisitService<T> headService(int count);
	
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
