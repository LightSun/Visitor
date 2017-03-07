package com.heaven7.java.visitor.collection;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.heaven7.java.visitor.FireBatchVisitor;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.IterateVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.SaveVisitor;
import com.heaven7.java.visitor.ThrowableVisitor;
import com.heaven7.java.visitor.anno.DependOn;
import com.heaven7.java.visitor.anno.Independence;
import com.heaven7.java.visitor.anno.Nullable;
import com.heaven7.java.visitor.internal.Cacheable;
import com.heaven7.java.visitor.internal.Endable;
import com.heaven7.java.visitor.internal.OperateInterceptor;
import com.heaven7.java.visitor.util.Observer;

/**
 * the super interface of collection 'Visit-Service'
 * @author heaven7
 *
 * @param <T> the type of element
 * @see AbstractCollectionVisitService
 * @see CollectionVisitServiceImpl
 * @see ListVisitServiceImpl
 */
//TODO need visitFirst/ visitLast / ?
public interface CollectionVisitService<T> extends VisitService<CollectionVisitService<T>>{
	
	/**
	 * the visit rule: visit all.
	 */
	public static final int VISIT_RULE_ALL = 11;
	/**
	 * the visit rule: until success. this is useless for the 'Set' of
	 * 'Collection'
	 */
	public static final int VISIT_RULE_UNTIL_SUCCESS = 12;
	/**
	 * the visit rule: until failed. this is useless for the 'Set' of
	 * 'Collection'
	 */
	public static final int VISIT_RULE_UNTIL_FAILED = 13;
	
	/**
	 * zip the all elements with target visitor .
	 * <p>that is if 'case' then 'result'</p>
	 * <ul> <h2>here is the left case with right result</h2>
	 *    <li> if predicate visitor always visit success(true) ==>cause call {@linkplain Observer#onSucess(Object)}
	 *    <li> if predicate visitor sometime visit failed(false) ==>cause call {@linkplain Observer#onFailed(Object, Object)}
	 *    <li> if occurs {@linkplain Throwable} during visit ==>cause call {@linkplain Observer#onThrowable(Object, Object, Throwable)}
	 * </ul>
	 * 
	 * @param param the extra parameter
	 * @param visitor the predicate visitor
	 * @param observer the result observer
	 * @return this.
	 * @since 1.1.6
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	CollectionVisitService<T> zip(@Nullable Object param, PredicateVisitor<T> visitor,
		  Observer<T> observer);
	
	/**
	 * fire the all element by target {@linkplain FireBatchVisitor} and etc.
	 * @param param the parameter , can be null
	 * @param fireVisitor fire batch visitor 
	 * @param throwVisitor the throwable visitor, can be null.
	 * @return this
	 * @since 1.1.1
	 * @see {@linkplain #fireBatch(FireBatchVisitor, ThrowableVisitor)}
	 * @see {@linkplain #fireBatch(Object,FireBatchVisitor, ThrowableVisitor)}
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	CollectionVisitService<T> fireBatch(FireBatchVisitor<T> fireVisitor);
	/**
	 * fire the all element by target {@linkplain FireBatchVisitor} and etc.
	 * @param param the parameter , can be null
	 * @param fireVisitor fire batch visitor 
	 * @param throwVisitor the throwable visitor, can be null.
	 * @return this
	 * @since 1.1.1
	 * @see {@linkplain #fireBatch(Object,FireBatchVisitor, ThrowableVisitor)}
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	CollectionVisitService<T> fireBatch(FireBatchVisitor<T> fireVisitor, @Nullable ThrowableVisitor throwVisitor);
	/**
	 * fire the all element by target {@linkplain FireBatchVisitor} and etc.
	 * @param param the parameter , can be null
	 * @param fireVisitor fire batch visitor 
	 * @param throwVisitor the throwable visitor, can be null.
	 * @return this
	 * @since 1.1.1
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	CollectionVisitService<T> fireBatch(@Nullable Object param, FireBatchVisitor<T> fireVisitor,@Nullable ThrowableVisitor throwVisitor);
	
	/**
	 * fire the all element by target {@linkplain FireVisitor}.
	 * @param param the parameter , can be null
	 * @param fireVisitor fire visitor 
	 * @return this
	 * @since 1.1.1
	 * @see [{@linkplain #fire(FireVisitor, ThrowableVisitor)}
	 * @see [{@linkplain #fire(Object, FireVisitor, ThrowableVisitor)}
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	CollectionVisitService<T> fire(FireVisitor<T> fireVisitor);
	/**
	 * fire the all element by target {@linkplain FireVisitor} and etc.
	 * @param fireVisitor fire visitor 
	 * @param throwVisitor the throwable visitor, can be null.
	 * @return this
	 * @since 1.1.1
	 * @see [{@linkplain #fire(Object, FireVisitor, ThrowableVisitor)}
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	CollectionVisitService<T> fire(FireVisitor<T> fireVisitor,@Nullable ThrowableVisitor throwVisitor);
	/**
	 * fire the all element by target {@linkplain FireVisitor} and etc.
	 * @param param the parameter , can be null
	 * @param fireVisitor fire visitor 
	 * @param throwVisitor the throwable visitor, can be null.
	 * @return this
	 * @since 1.1.1
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	CollectionVisitService<T> fire(@Nullable Object param, FireVisitor<T> fireVisitor, @Nullable ThrowableVisitor throwVisitor);
	
	
	/**
	 * save the current elements by target {@linkplain SaveVisitor}.
	 * @param visitor the save visitor.
	 * @return this.
	 * @since 1.0.3
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	CollectionVisitService<T> save(SaveVisitor<T> visitor);
	/**
	 * save the current elements to the target out collection.
	 * @param out the out collection.
	 * @param clearBeforeSave if you want to clear the out collection before save.
	 * @return this.
	 * @since 1.0.3
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	CollectionVisitService<T> save(Collection<T> out, boolean clearBeforeSave);
	
	/**
	 * save the current elements to the target out collection.
	 * @param out the out collection.
	 * @return this.
	 * @since 1.0.3
	 * @see {@linkplain #save(Collection, boolean)}
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	CollectionVisitService<T> save(Collection<T> out);
	
	/**
	 * transform to map visit service by group.
	 * @param <K> the key type 
	 * @param param the parameter
	 * @param comparator the comparator to sort key if you want to transform to sorted map.
	 * @param keyVisitor the key visitor
	 * @return a grouped map visit service.
	 * @see {@linkplain #transformToMapByGroup(Comparator, ResultVisitor)}
	 * @see {@linkplain #transformToMapByGroup(ResultVisitor)}
	 * @since 1.1.2
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<K> MapVisitService<K, List<T>> transformToMapByGroup(@Nullable Object param, 
			@Nullable Comparator<? super K> comparator,  ResultVisitor<T, K> keyVisitor);
	
	/**
	 * transform to map visit service by group. and the comparator is used to sort key if you
	 *  want to transform to sorted map.
	 * @param <K> the key type 
	 * @param comparator the comparator to sort key if you want to transform to sorted map.
	 * @param keyVisitor the key visitor
	 * @return a grouped map visit service.
	 * @see {@linkplain #transformToMapByGroup(ResultVisitor)}
	 * @see {@linkplain #transformToMapByGroup(Object,Comparator, ResultVisitor)}
	 * @since 1.1.2
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<K> MapVisitService<K, List<T>> transformToMapByGroup( @Nullable Comparator<? super K> comparator,
			ResultVisitor<T, K> keyVisitor);
	
	/**
	 * transform to simple map visit service by group.and the comparator is used to sort key if you
	 *  want to transform to sorted map.
	 * @param <K> the key type 
	 * @param keyVisitor the key visitor
	 * @return a grouped map visit service.
	 * @see {@linkplain #transformToMapByGroup(Comparator, ResultVisitor)}
	 * @see {@linkplain #transformToMapByGroup(Object,Comparator, ResultVisitor)}
	 * @since 1.1.2
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<K> MapVisitService<K, List<T>> transformToMapByGroup(ResultVisitor<T, K> keyVisitor);
	
	/**
	 * transform to map visit service by group value which is indicate by valueVisitor.
	 * and the comparator is used to sort key if you
	 *  want to transform to sorted map.
	 * @param <K> the key type 
	 * @param <V> the type of List value
	 * @param param the parameter
	 * @param comparator the comparator to sort key if you want to transform to sorted map.
	 * @param keyVisitor the key visitor
	 * @param valueVisitor the value visitor.
	 * @return a grouped map visit service.
	 * @see {@linkplain #transformToMapByGroupValue(Comparator, ResultVisitor, ResultVisitor)}
	 * @see {@linkplain #transformToMapByGroupValue(ResultVisitor, ResultVisitor)}
	 * @since 1.1.2
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<K, V> MapVisitService<K, List<V>> transformToMapByGroupValue(@Nullable Object param, Comparator<? super K> comparator, 
			ResultVisitor<T, K> keyVisitor, ResultVisitor<T, V>  valueVisitor);
	
	/**
	 * transform to map visit service by group value which is indicate by valueVisitor.
	 * and the comparator is used to sort key if you
	 *  want to transform to sorted map.
	 * @param <K> the key type 
	 * @param <V> the type of List value
	 * @param comparator the comparator to sort key if you want to transform to sorted map.
	 * @param keyVisitor the key visitor
	 * @param valueVisitor the value visitor.
	 * @return a grouped map visit service.
	 * @see {@linkplain #transformToMapByGroupValue(Object,Comparator, ResultVisitor, ResultVisitor)}
	 * @see {@linkplain #transformToMapByGroupValue(ResultVisitor, ResultVisitor)}
	 * @since 1.1.2
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<K, V> MapVisitService<K, List<V>> transformToMapByGroupValue(Comparator<? super K> comparator, 
			ResultVisitor<T, K> keyVisitor, ResultVisitor<T, V>  valueVisitor);
	
	/**
	 * transform to simple map visit service by group value which is indicate by valueVisitor.
	 * and the comparator is used to sort key if you
	 *  want to transform to sorted map.
	 * @param <K> the key type 
	 * @param <V> the type of List value
	 * @param keyVisitor the key visitor
	 * @param valueVisitor the value visitor.
	 * @return a grouped map visit service.
	 * @see {@linkplain #transformToMapByGroupValue(Object,Comparator, ResultVisitor, ResultVisitor)}
	 * @see {@linkplain #transformToMapByGroupValue(ResultVisitor, ResultVisitor)}
	 * @since 1.1.2
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<K, V> MapVisitService<K, List<V>> transformToMapByGroupValue(ResultVisitor<T, K> keyVisitor,
			ResultVisitor<T, V>  valueVisitor);
	
	/**
	 * transform this service to map service. And the value type is T.
	 * @param <K> the key type
	 * @param param the extra parameter
	 * @param comparator the key comparator of map, can be null.
	 * @param keyVisitor the key visitor.
	 * @return the {@linkplain MapVisitService}
	 * @since 1.1.0
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<K> MapVisitService<K,T> transformToMapAsValues(@Nullable Object param, 
			@Nullable Comparator<? super K> comparator, ResultVisitor<? super T, K> keyVisitor);
	
	/**
	 * transform this service to map service. And the value type is T .
	 * @param <K> the key type
	 * @param param the extra parameter
	 * @param keyVisitor the key visitor.
	 * @return the {@linkplain MapVisitService}
	 * @since 1.0.2
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<K> MapVisitService<K,T> transformToMapAsValues(@Nullable Object param, ResultVisitor<? super T, K> keyVisitor);
	
	/**
	 * transform this service to map service. And the value type is T .
	 * @param <K> the key type
	 * @param keyVisitor the key visitor.
	 * @return the {@linkplain MapVisitService}
	 * @see {@linkplain #transformToMapAsValues(Object, ResultVisitor)}
	 * @since 1.0.2
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<K> MapVisitService<K,T> transformToMapAsValues(ResultVisitor<? super T, K> keyVisitor);
	
	/**
	 * transform this service to map service. And the key type is T .
	 * @param <V> the value type
	 * @param param the extra parameter
	 * @param comparator the key comparator, can be null.
	 * @param valueVisitor the value visitor.
	 * @return the {@linkplain MapVisitService}
	 * @since 1.1.0
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<V> MapVisitService<T,V> transformToMapAsKeys(@Nullable Object param, 
			@Nullable Comparator<? super T> comparator, ResultVisitor<? super T, V> valueVisitor);
	
	/**
	 * transform this service to map service. And the key type is T 
	 * @param <V> the value type
	 * @param param the extra parameter
	 * @param valueVisitor the value visitor.
	 * @return the {@linkplain MapVisitService}
	 * @since 1.0.2
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<V> MapVisitService<T,V> transformToMapAsKeys(@Nullable Object param, ResultVisitor<? super T, V> valueVisitor);
	
	/**
	 * transform this service to map service. And the key type is T 
	 * @param <V> the value type
	 * @param valueVisitor the value visitor.
	 * @return the {@linkplain MapVisitService}
	 * @see {@linkplain #transformToMapAsKeys(Object, ResultVisitor)}
	 * @since 1.0.2
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<V> MapVisitService<T,V> transformToMapAsKeys(ResultVisitor<? super T, V> valueVisitor);
	
	/**
	 * transform this service to map service.
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param param the extra parameter
	 * @param comparator the key comparator
	 * @param keyVisitor the key visitor.
	 * @param valueVisitor the value visitor.
	 * @return the {@linkplain MapVisitService}
	 * @since 1.1.0
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<K,V> MapVisitService<K, V> transformToMap(@Nullable Object param,@Nullable Comparator<? super K> comparator,
			ResultVisitor<? super T, K> keyVisitor, ResultVisitor<? super T, V> valueVisitor);
	
	/**
	 * transform this service to map service.
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param param the extra parameter
	 * @param keyVisitor the key visitor.
	 * @param valueVisitor the value visitor.
	 * @return the {@linkplain MapVisitService}
	 * @since 1.0.2
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<K,V> MapVisitService<K, V> transformToMap(@Nullable Object param, ResultVisitor<? super T, K> keyVisitor, 
			ResultVisitor<? super T, V> valueVisitor);
	
	/**
	 * transform this service to map service.
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param keyVisitor the key visitor.
	 * @param valueVisitor the value visitor.
	 * @return the {@linkplain MapVisitService}
	 * @see {@linkplain #transformToMap(Object, ResultVisitor, ResultVisitor)}
	 * @since 1.0.2
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<K,V> MapVisitService<K, V> transformToMap(ResultVisitor<? super T, K> keyVisitor, ResultVisitor<? super T, V> valueVisitor);

	/**
	 * transform current CollectionVisitService to another.
	 * @param <R> the result type.
	 * @param param 
	 *           the extra parameter, which is used for the callback of visitors.
	 * @param sort 
	 *           the result sort Comparator.
	 * @param resultVisitor
	 *           the  result visitor       
	 * @return the new {@linkplain CollectionVisitService}.
	 * @since 1.1.0
	 * 
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<R> CollectionVisitService<R> transformToCollection(@Nullable Object param,
			@Nullable Comparator<? super R> sort, ResultVisitor<? super T, R> resultVisitor);
	
	/**
	 * transform current CollectionVisitService to another, but don't sort.
	 * @param <R> the result type.
	 * @param param 
	 *           the extra parameter, which is used for the callback of visitors.
	 * @param resultVisitor
	 *           the  result visitor       
	 * @return the new {@linkplain CollectionVisitService}.
	 * @see {@linkplain #transformToCollection(Object, Comparator, ResultVisitor)}
	 * @since 1.0.2
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<R> CollectionVisitService<R> transformToCollection(@Nullable Object param, ResultVisitor<? super T, R> resultVisitor);
	
	/**
	 * transform current CollectionVisitService to another. but don't sort
	 * @param <R> the result type.
	 * @param resultVisitor
	 *           the  result visitor       
	 * @return the new {@linkplain CollectionVisitService}.
	 * @see {@linkplain #transformToCollection(Object, ResultVisitor)}
	 * @see {@linkplain #transformToCollection(Object, Comparator, ResultVisitor)}
	 * @since 1.0.2
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<R> CollectionVisitService<R> transformToCollection(ResultVisitor<? super T, R> resultVisitor);
	
	//============================================================================================================
	
	/**
	 * visit the collection for result . if predicate visit true, the iterate will be breaked.
	 *
	 * @param param
	 *            the extra parameter, which is used for the callback of visitors.
	 * @param predicate
	 *            the predicate visitor
	 * @param resultVisitor
	 *            the result visitor
	 * @param <R>
	 *            the result type of visit.
	 * @return the result of this visit
	 * @see {@linkplain #visitForResultList(Object, PredicateVisitor, ResultVisitor, List)}
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<R> R visitForResult(@Nullable Object param, PredicateVisitor<? super T> predicate, ResultVisitor<? super T, R> resultVisitor);
	
	/**
	 * visit the collection for result , but carry no extra data.
	 *
	 * @param predicate
	 *            the predicate visitor
	 * @param resultVisitor
	 *            the result visitor
	 * @param <R>
	 *            the result type of visit.
	 * @return the result of this visit
	 * @see {@linkplain #visitForResultList(Object, PredicateVisitor, ResultVisitor, List)}
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<R> R visitForResult(PredicateVisitor<? super T> predicate, ResultVisitor<? super T, R> resultVisitor);

	/**
	 * visit the all elements for result. which is matched by the target predicate visitor.
	 *
	 * @param param
	 *            the extra parameter for transport to the all visitors.
	 * @param predicate
	 *            the predicate visitor ,indicate whether the element is match this predicate or not. if predicate visit true, it will be ignored.
	 * @param resultVisitor
	 *            the result visitor, if visit result is null , it will be ignored.
	 * @param out
	 *            the out list. can be null.
	 * @param <R>
	 *            the result type of visit.
	 * @return the list of visit result
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<R> List<R> visitForResultList(@Nullable Object param, PredicateVisitor<? super T> predicate,
			ResultVisitor<? super T, R> resultVisitor, @Nullable List<R> out);
	
	/**
	 * visit the all elements for result, With the default predicate visitor which  always return true.
	 *
	 * @param param
	 *            the extra parameter for transport to the all visitors.
	 * @param resultVisitor
	 *            the result visitor, if visit result is null , it will be ignored.
	 * @param out
	 *            the out list. can be null.
	 * @param <R>
	 *            the result type of visit.
	 * @return the list of visit result
	 * @see {@linkplain #visitForResultList(Object, PredicateVisitor, ResultVisitor, List)}
	 * @since 1.0.1
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<R> List<R> visitForResultList(@Nullable Object param, ResultVisitor<? super T, R> resultVisitor, @Nullable List<R> out);
	
	/**
	 * visit the all elements for result. which is matched by the target predicate visitor, but carry no extra data.
	 *
	 * @param predicate
	 *            the predicate visitor
	 * @param resultVisitor
	 *            the result visitor
	 * @param out
	 *            the out list. can be null.
	 * @param <R>
	 *            the result type of visit.
	 * @return the collection of visit result
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<R> List<R> visitForResultList(PredicateVisitor<? super T> predicate, ResultVisitor<? super T, R> resultVisitor, @Nullable List<R> out);
	
	/**
	 * visit the all elements for result. which is matched by the target predicate visitor, but carry no extra data.
	 * With the default predicate visitor which  always return true.
	 *
	 * @param resultVisitor
	 *            the result visitor
	 * @param out
	 *            the out list. can be null.
	 * @param <R>
	 *            the result type of visit.
	 * @return the collection of visit result
	 * @since 1.0.1
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	<R> List<R> visitForResultList(ResultVisitor<? super T, R> resultVisitor, @Nullable List<R> out);

	/**
	 * visit for query the all element which is match the PredicateVisitor.
	 * 
	 * @param param
	 *            the extra parameter for transport to the all visitors.
	 * @param predicate
	 *            the predicate visitor
	 * @param out
	 *            the out list, can be null.
	 * @return the list 
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	List<T> visitForQueryList(@Nullable Object param, PredicateVisitor<? super T> predicate,@Nullable List<T> out);
	
	/**
	 * visit for query the all element which is match the PredicateVisitor, but carry no extra data.
	 * 
	 * @param predicate
	 *            the predicate visitor
	 * @param out
	 *            the out list, can be null.
	 * @return
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	List<T> visitForQueryList(PredicateVisitor<? super T> predicate,@Nullable List<T> out);
	

	/**
	 * visit for query a element by the target parameter and predicate.
	 *
	 * @param param
	 *            the extra parameter for transport to the all visitors.
	 * @param predicate
	 *            the predicate visitor
	 * @return the target element by find in array.
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	T visitForQuery(@Nullable Object param, PredicateVisitor<? super T> predicate);
	
	/**
	 * visit for query a element by the target parameter and predicate, but carry no extra data.
	 *
	 * @param predicate
	 *            the predicate visitor
	 * @return the target element by find in array.
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	T visitForQuery(PredicateVisitor<? super T> predicate);

	/**
	 * visit the all elements if possible.
	 * @param param the extra parameter.
	 * @return true if operate success.
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	boolean visitAll(@Nullable Object param);
	
	/**
	 * visit the all elements if possible, but carry no extra data.
	 * equal to <pre>visitAll(null). </pre>
	 * @return true if operate success.
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	boolean visitAll();

	/**
	 * visit the all elements until someone success.
	 * @param param the extra parameter.
	 * @param breakVisitor the break visitor
	 * @return true if operate success(no break).
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	boolean visitUntilSuccess(@Nullable Object param, IterateVisitor<? super T> breakVisitor);
	
	/**
	 * visit the all elements until someone success, but carry no extra data. 
	 * @param breakVisitor the break visitor
	 * @return true if operate success(no break).
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	boolean visitUntilSuccess(IterateVisitor<? super T> breakVisitor);

	/**
	 * visit the all elements until  someone failed.
	 * @param param the extra parameter.
	 * @param breakVisitor the break visitor
	 * @return true if operate success(no break).
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	boolean visitUntilFailed(@Nullable Object param, IterateVisitor<? super T> breakVisitor);
	/**
	 * visit the all elements until someone  failed, but carry no extra data.
	 * @param breakVisitor the break visitor
	 * @return true if operate success(no break).
	 */
	@DependOn(classes ={OperateManager.class, IterateControl.class })
	boolean visitUntilFailed(IterateVisitor<? super T> breakVisitor);
	
	/**
	 * begin the iterate control , And then we can edit the order of operate in iteration.
	 * Finally you can call {@linkplain IterateControl#end()} to end the iterate control.  
	 * @return the iterate control.
	 */
	IterateControl<CollectionVisitService<T>> beginIterateControl(); 
	
	/**
	 * begin the operate manager , And then we can add some pending operation in or after iteration.
	 * Finally you can call {@linkplain OperateManager#end()} to end the iterate operate manager.  
	 * @return the iterate control.
	 */
	OperateManager<T> beginOperateManager();
	
	//=======================================================================================
	
	/**
	 * cast to {@linkplain ListVisitService} if can.
	 * @return {@linkplain ListVisitService}.
	 * @throws UnsupportedOperationException if the collection isn't list.
	 * @since 1.1.2
	 */
	ListVisitService<T> asListService() throws UnsupportedOperationException;
	
	/**
	 * get a sub visit service by target visitor and parameter.
	 * @param param the extra parameter which will be visit by visitor.
	 * @param visitor the predicate visitor, if visit success means contains the element.
	 * @return a sub {@linkplain CollectionVisitService}
	 * @since 1.1.5
	 */
	@Independence
	CollectionVisitService<T> subService(@Nullable Object param, PredicateVisitor<T> visitor);
	
	/**
	 * get a sub visit service by target visitor.
	 * @param visitor the predicate visitor , if visit success means contains the element.
	 * @return a sub {@linkplain CollectionVisitService}
	 * @since 1.1.5
	 */
	@Independence
	CollectionVisitService<T> subService(PredicateVisitor<T> visitor);
	
	
	/**
	 * the size of collection
	 * @return the size
	 * @since 1.1.2
	 */
	@Independence
	int size();
	
	//=======================================================================================
	
	/**
	 * the operate interceptor in iteration({@linkplain Iterator} or [{@linkplain ListIterator}) for {@linkplain Collection}.
	 * @author heaven7
	 *
	 * @param <T> the type
	 */
	public static abstract class CollectionOperateInterceptor<T> extends OperateInterceptor {

		/**
		 * intercept the current iteration.
		 * @param it the Iterator
		 * @param t the element
		 * @param param the parameter, may be null.
		 * @param info the IterationInfo.
		 * @return true if intercept success, this means the loop of 'for' will be 'continue'. 
		 */
		public abstract boolean intercept(Iterator<T> it, T t,@Nullable Object param, IterationInfo info);

	}
	
	
	/**
	 * The pending operate manager used to support filter/delete/update/insert in iteration for all {@linkplain Collection}. 
	 * and support insert after iteration (before method return).
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
	 * @param <R> the return type for {@link #end()}
	 * @param <T> the parametric type of most method
	 */
	public static abstract class OperateManager<T> implements Cacheable<OperateManager<T>>,
	         Endable<CollectionVisitService<T>>{

		OperateManager() {
			super();
		}
		
		/**
		 * cache the all operate which is set by the {@linkplain OperateManager}.
		 * @return the original object
		 * @since 1.1.2
		 */
		public abstract OperateManager<T> cache() ;
		
		/**
		 * end the operate and return the target object
		 * @return the original object
		 */
		public abstract CollectionVisitService<T> end() ;


		/**
		 * add a filter operation. This means: 
		 * pending filter element in the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
		 * that means if filter operate visit true, the element of collection will skip next visitors' visit.
		 * @param filter the predicate visitor for filter.
		 * @return this.
		 * @see {@linkplain OperateManager#filter(Object, PredicateVisitor)}
		 */
		public OperateManager<T> filter(PredicateVisitor<? super T> filter) {
			return filter(null, filter);
		}
		
		/**
		 * add a delete operation. This means: 
		 * pending remove/delete element in the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
		 * @param delete the predicate visitor for delete/remove.
		 * @return this.
		 * @see {@linkplain #delete(Object, PredicateVisitor)}.
		 */
		public OperateManager<T> delete(PredicateVisitor<? super T> delete) {
			return delete(null, delete);
		}

		/**
		 * add a update operation. This means: 
		 * pending update element in the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
		 * @param newT the new element to update/replace.
		 * @param update the predicate visitor for update
		 * @return this.
		 * @see {@linkplain #update(Object, Object, PredicateVisitor)}
		 */
		public OperateManager<T> update(T newT, PredicateVisitor<? super T> update) {
			return update(newT, null, update);
		}

		/**
		 * add a insert operation. This means: 
		 * pending insert the list in the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
		 * But this only support for List, or else has nothing effect.
		 * @param list the list to insert
		 * @param insert the insert iterate visitor. see {@linkplain IterateVisitor}.
		 * @return this.
		 * @see {@linkplain #insert(List, Object, IterateVisitor)}
		 */
		public OperateManager<T> insert(List<T> list,  IterateVisitor<? super T> insert) {
			return insert(list, null, insert);
		}

		/**
		 * add a insert operation. This means: 
		 * pending insert the element in the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
		 *  But this only support for List, or else has nothing effect.
		 * @param newT the element to insert
		 * @param insert the insert iterate visitor. see {@linkplain IterateVisitor}.
		 * @return this.
		 * @see {@linkplain #insert(Object, Object, IterateVisitor)}
		 */
		public OperateManager<T> insert(T newT, IterateVisitor<? super T> insert) {
			return insert(newT, null, insert);
		}

		/**
		 * add a final insert operation. This means: 
		 * pending insert the element after the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
		 * @param newT the element to insert
		 * @param insert the insert iterate visitor. see {@linkplain IterateVisitor}.
		 * @return this.
		 * @see {@linkplain #insertFinally(Object, Object, IterateVisitor)}
		 */
		public OperateManager<T> insertFinally( T newT, IterateVisitor<? super T> insert) {
			return insertFinally(newT, null, insert);
		}

		/** add a final insert operation. This means: 
		 * pending insert the list after the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
		 * @param list the list to insert
		 * @param insert the insert iterate visitor. see {@linkplain IterateVisitor}.
		 * @return this.
		 * @see {@linkplain #insertFinally(List, Object, IterateVisitor)}
		 */
		public OperateManager<T> insertFinally(List<T> list,IterateVisitor<? super T> insert) {
			return insertFinally(list, null, insert);
		}
		

		// =============== public abstract method ======================
		
		/**
		 * add a filter operation. This means: 
		 * pending filter element in the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
		 * @param param the extra parameter
		 * @param filter the predicate visitor for filter.
		 * @return this.
		 */
		public abstract OperateManager<T> filter(@Nullable Object param, PredicateVisitor<? super T> filter);

		/**
		 * add a delete operation. This means: 
		 * pending remove/delete element in the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
		 * @param param the extra parameter
		 * @param delete the predicate visitor for delete/remove.
		 * @return this.
		 */
		public abstract OperateManager<T> delete(@Nullable Object param, PredicateVisitor<? super T> delete);

		/**
		 * add a update operation. This means: 
		 * pending update element in the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
		 * @param newT the new element to update/replace.
		 * @param param the extra parameter
		 * @param update the predicate visitor for update
		 * @return this.
		 */
		public abstract OperateManager<T> update(T newT,@Nullable Object param, PredicateVisitor<? super T> update);
		
		/**
		 * add a insert operation. This means: 
		 * pending insert the list in the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
		 * But this only support for List, or else has nothing effect.
		 * @param list the list to insert
		 * @param param the extra parameter
		 * @param insert the insert iterate visitor. see {@linkplain IterateVisitor}.
		 * @return this.
		 */
		public abstract OperateManager<T> insert(List<T> list,@Nullable Object param, IterateVisitor<? super T> insert);

		/**
		 * add a insert operation. This means: 
		 * pending insert the element in the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
		 * But this only support for List, or else has nothing effect.
		 * @param newT the new element to insert
		 * @param param the extra parameter
		 * @param insert the insert iterate visitor. see {@linkplain IterateVisitor}.
		 * @return this.
		 */
		public abstract OperateManager<T> insert(T newT,@Nullable Object param, IterateVisitor<? super T> insert);

		/**
		 * add a final insert operation. This means: 
		 * pending insert the element after the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
		 * @param newT the element to insert
		 * @param param the extra parameter
		 * @param insert the insert iterate visitor. see {@linkplain IterateVisitor}.
		 * @return this.
		 */
		public abstract OperateManager<T> insertFinally(T newT,@Nullable Object param, IterateVisitor<? super T> insert);

		/**
		 * add a final insert operation. This means: 
		 * pending insert the list after the iteration({@linkplain Iterator} or {@linkplain ListIterator}) if possible.
		 * @param list the list to insert
		 * @param param the extra parameter
		 * @param insert the insert iterate visitor. see {@linkplain IterateVisitor}.
		 * @return this.
		 */
		public abstract OperateManager<T> insertFinally(List<T> list,@Nullable Object param, IterateVisitor<? super T> insert);

	}
 
}