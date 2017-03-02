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
	ListVisitService<T> subService(int start, int count);
	
	/**
	 * return a sub visit service .from current ListVisitService.
	 * @param count the count from the first element of list
	 * @return {@linkplain ListVisitService}
	 * @since 1.1.2
	 */
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
	ListVisitService<T> tailService(int count);
	
	/**
	 * reverse the order of list. this is equal to 
	 * <p>reverseService(true)
	 * @return {@linkplain ListVisitService}
	 * @since 1.1.2
	 */
	ListVisitService<T> reverseService();
	
	/**
	 * reverse the order of list
	 * @param reverseOriginList true to reverse the origin list.false otherwise.
	 * @return {@linkplain ListVisitService}
	 * @since 1.1.2
	 */
	ListVisitService<T> reverseService(boolean reverseOriginList);
	
	ListVisitService<T> shuffleService(boolean shuffleOriginlist);
	
	ListVisitService<T> shuffleService();
	
	ListVisitService<T> sortService(Comparator<? super T> c);
	
	ListVisitService<T> sortService(Comparator<? super T> c, boolean sortOriginList);
	
	//TODO <K, V> MapVisitService<K, List<T>> listGroupMapService(ResultVisitor<T, K> keyVisitor);
	
	//==================== String ===========================
	ListVisitService<String> joinToStringService(String joinMark, int everyGroupCount);
	
	ListVisitService<String> joinToStringService(@Nullable Object param, 
			ResultVisitor<T, String> stringVisitor, String joinMark, int everyGroupCount);
	
	ListVisitService<String> joinToStringService(ResultVisitor<T, String> stringVisitor,
			String joinMark, int everyGroupCount);
	
	String joinToString(String joinMark);
	//========================= end string ========================
}
