package com.heaven7.java.visitor.collection;

import java.util.Comparator;
import java.util.List;

import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.anno.Independence;
import com.heaven7.java.visitor.anno.Nullable;

/**
 * list visit service 
 * @author heaven7
 *
 * @param <T> the element type.
 * @since 1.1.2
 */
@Independence("all methods is independent in here.")
public interface ListVisitService<T> extends CollectionVisitService<T>{

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
	 * @param shuffleOriginlist shuffle the origin list or not.
	 * @return this
	 * @since 1.1.2
	 */
	@Independence
	ListVisitService<T> shuffleService();
	
	/**
	 * sort the collection.
	 * @param c the comparator used to sort.
	 * @return this
	 * @see {@linkplain #sortService(Comparator, boolean)}
	 * @since 1.1.2
	 */
	@Independence
	ListVisitService<T> sortService(Comparator<? super T> c);
	
	/**
	 * sort the collection.
	 * @param c the comparator used to sort.
	 * @param sortOriginList sort the origin list or not.
	 * @return this or new {@linkplain ListVisitService}
	 * @see {@linkplain #sortService(Comparator)}
	 * @since 1.1.2
	 */
	@Independence
	ListVisitService<T> sortService(Comparator<? super T> c, boolean sortOriginList);
	
	/**
	 * group the collection to {@linkplain MapVisitService}.
	 * @param <K> the key type
	 * @param keyVisitor the key visitor.
	 * @return {@linkplain MapVisitService}
	 * @see {@linkplain #groupService(Object, ResultVisitor, ResultVisitor)}
	 * @see {@linkplain #groupService(ResultVisitor, ResultVisitor)}
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
	 * @see {@linkplain #groupService(Object, ResultVisitor, ResultVisitor)}
	 * @see {@linkplain #groupService(ResultVisitor)}
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
	 * @see {@linkplain #groupService(Object, ResultVisitor, ResultVisitor)}
	 * @see {@linkplain #groupService(ResultVisitor)}
	 * @since 1.1.2
	 */
	@Independence
	<K, V> MapVisitService<K, List<V>> groupService(@Nullable Object param, ResultVisitor<T, K> keyVisitor,
			ResultVisitor<T, V>  valueVisitor);
	
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
