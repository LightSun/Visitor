package com.heaven7.java.visitor.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
/**
 * like executors. we use all api start in here.
 * @author heaven7
 *
 */
public final class VisitServices {

	/**
	 * create {@linkplain CollectionVisitServiceImpl} from the target collection. <br>
	 * <b>Note: don't use {@linkplain Arrays#asList(Object...)} in here. </b>
	 * 
	 * @param <T>
	 *            the type
	 * @param collection
	 *            the target collection, such as set.
	 * @return an instance of {@linkplain CollectionVisitServiceImpl}
	 */
	public static <T> CollectionVisitService<T> from(Collection<T> collection) {
		return new CollectionVisitServiceImpl<T>(collection);
	}

	/**
	 * create {@linkplain CollectionVisitServiceImpl} from the target list.<br>
	 * <b>Note: don't use {@linkplain Arrays#asList(Object...)} in here. </b>
	 * 
	 * @param <T>
	 *            the type
	 * @param list
	 *            the target list.
	 * @return an instance of {@linkplain CollectionVisitServiceImpl}
	 */
	public static <T> CollectionVisitService<T> from(List<T> list) {
		return new ListVisitService<T>(list);
	}

}
