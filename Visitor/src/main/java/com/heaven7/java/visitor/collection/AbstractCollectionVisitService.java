package com.heaven7.java.visitor.collection;

import static com.heaven7.java.visitor.internal.InternalUtil.newMap;
import static com.heaven7.java.visitor.internal.InternalUtil.getVisitService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.heaven7.java.visitor.IterateVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.Visitors;
import com.heaven7.java.visitor.internal.InternalUtil;
import com.heaven7.java.visitor.util.Throwables;

/**
 * the abstract visit service
 * 
 * @author heaven7
 *
 * @param <T>
 *            the type
 * @see CollectionVisitServiceImpl
 * @see ListVisitService
 */
public abstract class AbstractCollectionVisitService<T> implements CollectionVisitService<T>{
	
	private final List<T> mCacheList = new ArrayList<T>();
	
	protected AbstractCollectionVisitService() {
		super();
	}
	
	@Override
	public final CollectionVisitService<T> resetAll() {
		return reset(FLAG_OPERATE_MANAGER | FLAG_OPERATE_ITERATE_CONTROL);
	}
	
	@Override
	public CollectionVisitService<T> save(Collection<T> out) {
		return save(out, false);
	}
	
	@Override
	public <K> MapVisitService<K, T> transformToMapAsValues(ResultVisitor<? super T, K> keyVisitor) {
		return transformToMapAsValues(null, keyVisitor);
	}
	
	@Override
	public <K> MapVisitService<K, T> transformToMapAsValues(Object param, ResultVisitor<? super T, K> keyVisitor) {
		return transformToMapAsValues(param, null, keyVisitor);
	}
	@Override
	public <K> MapVisitService<K, T> transformToMapAsValues(Object param,  
			Comparator<? super K> comparator, ResultVisitor<? super T, K> keyVisitor) {
		Throwables.checkNull(keyVisitor);
		final List<T> list = visitForQueryList(Visitors.truePredicateVisitor(), mCacheList);
		final Map<K, T> map = newMap(comparator);
		for(T t: list){
			map.put(keyVisitor.visit(t, param), t);
		}
		list.clear();
		return VisitServices.from(map);
	}
	
	@Override
	public <V> MapVisitService<T, V> transformToMapAsKeys(ResultVisitor<? super T, V> valueVisitor) {
		return transformToMapAsKeys(null, valueVisitor);
	}
	
	@Override
	public <V> MapVisitService<T, V> transformToMapAsKeys(Object param, ResultVisitor<? super T, V> valueVisitor) {
		return transformToMapAsKeys(param, null, valueVisitor);
	}
	
	@Override
	public <V> MapVisitService<T, V> transformToMapAsKeys(Object param,
			Comparator<? super T> comparator, ResultVisitor<? super T, V> valueVisitor) {
		Throwables.checkNull(valueVisitor);
		
		final List<T> list = visitForQueryList(Visitors.truePredicateVisitor(), mCacheList);
		final Map<T, V> map = newMap(comparator);
		for(T t: list){
			map.put(t, valueVisitor.visit(t, param));
		}
		list.clear();
		return VisitServices.from(map);
	}
	
	
	@Override
	public <K, V> MapVisitService<K, V> transformToMap(ResultVisitor<? super T, K> keyVisitor,
			ResultVisitor<? super T, V> valueVisitor) {
		return transformToMap(null, keyVisitor, valueVisitor);
	}
	
	@Override
	public <K, V> MapVisitService<K, V> transformToMap(Object param, ResultVisitor<? super T, K> keyVisitor,
			ResultVisitor<? super T, V> valueVisitor) {
		return transformToMap(param, null, keyVisitor, valueVisitor);
	}
	
	@Override
	public <K, V> MapVisitService<K, V> transformToMap(Object param, Comparator<? super K> comparator,
			ResultVisitor<? super T, K> keyVisitor, ResultVisitor<? super T, V> valueVisitor) {
		Throwables.checkNull(keyVisitor);
		Throwables.checkNull(valueVisitor);
		
		final List<T> list = visitForQueryList(param, Visitors.truePredicateVisitor(), mCacheList);
		final Map<K,V> map = newMap(comparator);
		for(T t: list){
			map.put(keyVisitor.visit(t, param), valueVisitor.visit(t, param));
		}
		list.clear();
		return VisitServices.from(map);
	}
	
	@Override
	public <R> CollectionVisitService<R> transformToCollection(Object param, 
			Comparator<? super R> sort, ResultVisitor<? super T, R> resultVisitor) {
		Throwables.checkNull(resultVisitor);
		return getVisitService(visitForResultList(param, resultVisitor, null), sort, 
				( this instanceof ListVisitService));
	}
	
	@Override
	public <R> CollectionVisitService<R> transformToCollection(ResultVisitor<? super T, R> resultVisitor) {
		return transformToCollection(null, resultVisitor);
	}
	
	@Override
	public <R> CollectionVisitService<R> transformToCollection(Object param, ResultVisitor<? super T, R> resultVisitor) {
		return transformToCollection(param, null, resultVisitor);
	}
	
	//=======================================================================
	
	@Override
	public final <R> List<R> visitForResultList(PredicateVisitor<? super T> predicate,
			ResultVisitor<? super T, R> resultVisitor, List<R> out) {
		return visitForResultList(null, predicate, resultVisitor, out);
	}

	@Override
	public final <R> List<R> visitForResultList(Object param, ResultVisitor<? super T, R> resultVisitor, List<R> out) {
		return visitForResultList(param, Visitors.truePredicateVisitor(), resultVisitor, out);
	}

	@Override
	public final <R> List<R> visitForResultList(ResultVisitor<? super T, R> resultVisitor, List<R> out) {
		return visitForResultList(null, Visitors.truePredicateVisitor(), resultVisitor, out);
	}

	@Override
	public final <R> R visitForResult(PredicateVisitor<? super T> predicate,
			ResultVisitor<? super T, R> resultVisitor) {
		return visitForResult(null, predicate, resultVisitor);
	}

	@Override
	public final List<T> visitForQueryList(PredicateVisitor<? super T> predicate, List<T> out) {
		return visitForQueryList(null, predicate, out);
	}

	@Override
	public final T visitForQuery(PredicateVisitor<? super T> predicate) {
		return visitForQuery(null, predicate);
	}

	@Override
	public final boolean visitAll() {
		return visitAll(null);
	}

	@Override
	public final boolean visitUntilSuccess(IterateVisitor<? super T> breakVisitor) {
		return visitUntilSuccess(null, breakVisitor);
	}

	@Override
	public final boolean visitUntilFailed(IterateVisitor<? super T> breakVisitor) {
		return visitUntilFailed(null, breakVisitor);
	}

	@Override
	public final boolean visitAll(Object param) {
		return visit(VISIT_RULE_ALL, param, null);
	}

	@Override
	public final boolean visitUntilSuccess(Object param, IterateVisitor<? super T> breakVisitor) {
		return visit(VISIT_RULE_UNTIL_SUCCESS, param, breakVisitor);
	}

	@Override
	public final boolean visitUntilFailed(Object param, IterateVisitor<? super T> breakVisitor) {
		return visit(VISIT_RULE_UNTIL_FAILED, param, breakVisitor);
	}

	/**
	 * do visit service.
	 * 
	 * @param rule
	 *            the rule . VISIT_RULE_ALL, VISIT_RULE_UNTIL_SUCCESS or
	 *            VISIT_RULE_UNTIL_FAILED.
	 * @param param
	 *            the extra parameter
	 * @param breakVisitor
	 *            the break visitor
	 * @return true if operate success.
	 */
	protected abstract boolean visit(int rule, Object param, 
			IterateVisitor<? super T> breakVisitor);

}
