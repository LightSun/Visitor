package com.heaven7.java.visitor.collection;

import com.heaven7.java.visitor.*;
import com.heaven7.java.visitor.anno.Nullable;
import com.heaven7.java.visitor.internal.InternalUtil;
import com.heaven7.java.visitor.util.Observer;
import com.heaven7.java.visitor.util.Predicates;
import com.heaven7.java.visitor.util.Throwables;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static com.heaven7.java.visitor.internal.InternalUtil.*;

/**
 * the abstract visit service
 *
 * @param <T> the type
 * @author heaven7
 * @see CollectionVisitServiceImpl
 * @see ListVisitServiceImpl
 */
public abstract class AbstractCollectionVisitService<T> implements CollectionVisitService<T> {

    /*private*/ final List<T> mCacheList = new ArrayList<T>();

    protected AbstractCollectionVisitService() {
        super();
    }

    @Override
    public <R> CollectionVisitService<R> filterMap(PredicateVisitor<T> predicate, ResultVisitor<T, R> mapVisitor) {
        return filterMap(null, predicate, mapVisitor, null);
    }

    @Override
    public <R> CollectionVisitService<R> filterMap(PredicateVisitor<T> predicate, ResultVisitor<T, R> mapVisitor, List<T> dropOut) {
        return filterMap(null, predicate, mapVisitor, dropOut);
    }

    @Override
    public <R> CollectionVisitService<R> filterMap(Object p,PredicateVisitor<T> predicate, ResultVisitor<T, R> mapVisitor, List<T> dropOut) {
        List<R> list = new ArrayList<>();
        for (T t : get()){
            if(Predicates.isTrue(predicate.visit(t, p))){
                R r = mapVisitor.visit(t, p);
                if(r != null){
                    list.add(r);
                }
            }else {
                if(dropOut != null){
                    dropOut.add(t);
                }
            }
        }
        return VisitServices.from(list);
    }

    @Override
    public <T2> CollectionVisitService<T2> asAnother() {
        return asAnother(null);
    }

    @Override @SuppressWarnings("unchecked")
    public <T2> CollectionVisitService<T2> asAnother(@Nullable Class<T2> clazz) {
        List<T> list = getAsList();
        if(clazz != null && list.size() > 0){
            Class<?> cur = list.get(0).getClass();
            if(!clazz.isAssignableFrom(cur)){
                throw new ClassCastException("type '"+ cur.getName()
                        +"' can't cast to '"+ clazz.getName() +"'");
            }
        }
        return (CollectionVisitService<T2>) VisitServices.from(list);
    }

    @Override
    public T max(final Comparator<? super T> com) {
        return pile(new PileVisitor<T>() {
            @Override
            public T visit(Object o, T t, T t2) {
                return com.compare(t, t2) >=0 ? t : t2;
            }
        });
    }
    @Override
    public T min(final Comparator<? super T> com) {
        return pile(new PileVisitor<T>() {
            @Override
            public T visit(Object o, T t, T t2) {
                return com.compare(t, t2) < 0 ? t : t2;
            }
        });
    }

    @Override
    public CollectionVisitService<T> removeRepeat() {
        return removeRepeat(null, null, null);
    }

    @Override
    public CollectionVisitService<T> removeRepeat(WeightVisitor<T> weightVisitor) {
        return removeRepeat(null, null, weightVisitor);
    }

    @Override
    public CollectionVisitService<T> removeRepeat(Object param, Comparator<? super T> com) {
        return removeRepeat(param, com, null);
    }

    @Override
    public CollectionVisitService<T> removeRepeat(Object param, Comparator<? super T> com, final WeightVisitor<T> weightVisitor) {
        final List<T> result = new ArrayList<>();
        final CompareHelper<T> helper = new CompareHelper<>(com);
        filter(null, new PredicateVisitor<T>() {
            @Override
            public Boolean visit(T t, Object param) {
                int index = helper.getContainsIndex(result, t);
                if(index >= 0 && weightVisitor != null){
                    T oldT = result.get(index);
                    Integer w1 = weightVisitor.visit(oldT, param);
                    Integer w2 = weightVisitor.visit(t, param);
                    //new element is more important
                    if(w2 > w1){
                        result.set(index, t);
                    }
                }
                //contains means will filter . or else will be in drop out list
                return index >= 0;
            }
        }, result);
        return VisitServices.from(result);
    }

    @Override
    public final <R> CollectionVisitService<R> zipService(
            ResultVisitor<T, R> resultVisitor, Observer<T, List<R>> observer) {
        return zipService(null, resultVisitor, observer);
    }

    @Override
    public CollectionVisitService<T> zip(
            @Nullable Object param, PredicateVisitor<T> visitor, Observer<T, Void> observer) {
        Throwables.checkNull(visitor);
        Throwables.checkNull(observer);
        final WrappedObserveVisitor<T, Void> observeVisitor =
                new WrappedObserveVisitor<T, Void>(param, visitor, observer);
        try {
            if (visitUntilFailed(param, observeVisitor)) {
                observeVisitor.onSuccess(null);
            } else {
                observeVisitor.onFailed();
            }
        } catch (Throwable e) {
            observeVisitor.onThrowable(e);
        }
        return this;
    }

    @Override
    public <R> CollectionVisitService<T> zipResult(
            Object param, final ResultVisitor<T, R> visitor, Observer<T, List<R>> observer) {
        Throwables.checkNull(visitor);
        Throwables.checkNull(observer);

        final List<R> results = new ArrayList<R>();
        final WrappedObserveVisitor<T, List<R>> observeVisitor =
                new WrappedObserveVisitor<T, List<R>>(
                        param,
                        new PredicateVisitor<T>() {
                            @Override
                            public Boolean visit(T t, Object param) {
                                R r = visitor.visit(t, param);
                                if (r != null) {
                                    results.add(r);
                                    return true;
                                }
                                return false;
                            }
                        },
                        observer);
        try {
            if (visitUntilFailed(param, observeVisitor)) {
                observeVisitor.onSuccess(results);
            } else {
                observeVisitor.onFailed();
            }
        } catch (Throwable e) {
            observeVisitor.onThrowable(e);
        }
        return this;
    }

    @Override
    public CollectionVisitService<T> fireBatch(FireBatchVisitor<T> visitor) {
        return fireBatch(visitor, null);
    }

    @Override
    public CollectionVisitService<T> fireBatch(
            FireBatchVisitor<T> visitor, ThrowableVisitor tv) {
        return fireBatch(null, visitor, tv);
    }

    @Override
    public CollectionVisitService<T> fireBatch(
            Object param, FireBatchVisitor<T> visitor, ThrowableVisitor tv) {
        Throwables.checkNull(visitor);
        final List<T> list = visitForQueryList(Visitors.truePredicateVisitor(), mCacheList);
        try {
            visitor.visit(list, param);
        } catch (Throwable e) {
            processThrowable(e, tv);
        } finally {
            list.clear();
        }
        return this;
    }

    @Override
    public CollectionVisitService<T> fire(FireVisitor<T> fireVisitor) {
        return fire(fireVisitor, null);
    }

    @Override
    public CollectionVisitService<T> fire(FireVisitor<T> visitor, ThrowableVisitor tv) {
        return fire(null, visitor, tv);
    }

    @Override
    public CollectionVisitService<T> fire(
            Object param, FireVisitor<T> visitor, ThrowableVisitor tv) {
        Throwables.checkNull(visitor);
        final List<T> list = visitForQueryList(Visitors.truePredicateVisitor(), mCacheList);
        try {
            for (T t : list) {
                visitor.visit(t, param);
            }
        } catch (Throwable e) {
            processThrowable(e, tv);
        } finally {
            list.clear();
        }
        return this;
    }

    @Override
    public CollectionVisitService<T> resetAll() {
        return reset(FLAG_ALL);
    }

    @Override
    public  CollectionVisitService<T> save(Collection<T> out) {
        return save(out, false);
    }

    @Override
    public CollectionVisitService<T> save(SaveVisitor<T> visitor) {
        Throwables.checkNull(visitor);
        final List<T> results = visitForQueryList(Visitors.truePredicateVisitor(), mCacheList);
        try {
            visitor.visit(unmodifiable(results));
        } finally {
            results.clear();
        }
        return this;
    }

    @Override
    public CollectionVisitService<T> save(Collection<T> out, boolean clearBeforeSave) {
        Throwables.checkNull(out);
        if (clearBeforeSave) {
            out.clear();
        }
        final List<T> results = visitForQueryList(Visitors.truePredicateVisitor(), mCacheList);
        out.addAll(results);
        results.clear();
        return this;
    }

    @Override
    public final <K> MapVisitService<K, List<T>> transformToMapByGroup(
            ResultVisitor<T, K> keyVisitor) {
        return transformToMapByGroup(null, keyVisitor);
    }

    @Override
    public final <K> MapVisitService<K, List<T>> transformToMapByGroup(
            Comparator<? super K> comparator, ResultVisitor<T, K> keyVisitor) {
        return transformToMapByGroup(null, comparator, keyVisitor);
    }

    @Override
    public final <K> MapVisitService<K, List<T>> transformToMapByGroup(
            Object param, Comparator<? super K> comparator, ResultVisitor<T, K> keyVisitor) {
        Throwables.checkNull(keyVisitor);
        final List<T> list = visitForQueryList(Visitors.truePredicateVisitor(), mCacheList);
        try {
            final Map<K, List<T>> map = InternalUtil.newMap(comparator);
            K key;
            List<T> value;
            for (T t : list) {
                key = keyVisitor.visit(t, param);
                value = map.get(key);
                if (value == null) {
                    value = new ArrayList<T>();
                    map.put(key, value);
                }
                value.add(t);
            }
            return VisitServices.from(map);
        } finally {
            list.clear();
        }
    }

    @Override
    public final <K, V> MapVisitService<K, List<V>> transformToMapByGroupValue(
            ResultVisitor<T, K> keyVisitor, ResultVisitor<T, V> valueVisitor) {
        return transformToMapByGroupValue(null, keyVisitor, valueVisitor);
    }

    @Override
    public final <K, V> MapVisitService<K, List<V>> transformToMapByGroupValue(
            Comparator<? super K> comparator,
            ResultVisitor<T, K> keyVisitor,
            ResultVisitor<T, V> valueVisitor) {
        return transformToMapByGroupValue(null, comparator, keyVisitor, valueVisitor);
    }

    @Override
    public final <K, V> MapVisitService<K, List<V>> transformToMapByGroupValue(
            Object param,
            Comparator<? super K> comparator,
            ResultVisitor<T, K> keyVisitor,
            ResultVisitor<T, V> valueVisitor) {
        Throwables.checkNull(keyVisitor);
        Throwables.checkNull(valueVisitor);
        final List<T> list = visitForQueryList(Visitors.truePredicateVisitor(), mCacheList);
        try {
            final Map<K, List<V>> map = InternalUtil.newMap(comparator);
            K key;
            List<V> value;
            for (T t : list) {
                key = keyVisitor.visit(t, param);
                value = map.get(key);
                if (value == null) {
                    value = new ArrayList<V>();
                    map.put(key, value);
                }
                value.add(valueVisitor.visit(t, param));
            }
            return VisitServices.from(map);
        } finally {
            list.clear();
        }
    }

    @Override
    public final <K> MapVisitService<K, T> transformToMapAsValues(
            ResultVisitor<? super T, K> keyVisitor) {
        return transformToMapAsValues(null, keyVisitor);
    }

    @Override
    public final <K> MapVisitService<K, T> transformToMapAsValues(
            Object param, ResultVisitor<? super T, K> keyVisitor) {
        return transformToMapAsValues(param, null, keyVisitor);
    }

    @Override
    public final <K> MapVisitService<K, T> transformToMapAsValues(
            Object param, Comparator<? super K> comparator, ResultVisitor<? super T, K> keyVisitor) {
        Throwables.checkNull(keyVisitor);
        final List<T> list = visitForQueryList(Visitors.truePredicateVisitor(), mCacheList);
        final Map<K, T> map = newMap(comparator);
        for (T t : list) {
            map.put(keyVisitor.visit(t, param), t);
        }
        list.clear();
        return VisitServices.from(map);
    }

    @Override
    public final <V> MapVisitService<T, V> transformToMapAsKeys(
            ResultVisitor<? super T, V> valueVisitor) {
        return transformToMapAsKeys(null, valueVisitor);
    }

    @Override
    public final <V> MapVisitService<T, V> transformToMapAsKeys(
            Object param, ResultVisitor<? super T, V> valueVisitor) {
        return transformToMapAsKeys(param, null, valueVisitor);
    }

    @Override
    public final <V> MapVisitService<T, V> transformToMapAsKeys(
            Object param, Comparator<? super T> comparator, ResultVisitor<? super T, V> valueVisitor) {
        Throwables.checkNull(valueVisitor);

        final List<T> list = visitForQueryList(Visitors.truePredicateVisitor(), mCacheList);
        final Map<T, V> map = newMap(comparator);
        for (T t : list) {
            map.put(t, valueVisitor.visit(t, param));
        }
        list.clear();
        return VisitServices.from(map);
    }

    @Override
    public final <K, V> MapVisitService<K, V> transformToMap(
            ResultVisitor<? super T, K> keyVisitor, ResultVisitor<? super T, V> valueVisitor) {
        return transformToMap(null, keyVisitor, valueVisitor);
    }

    @Override
    public final <K, V> MapVisitService<K, V> transformToMap(
            Object param,
            ResultVisitor<? super T, K> keyVisitor,
            ResultVisitor<? super T, V> valueVisitor) {
        return transformToMap(param, null, keyVisitor, valueVisitor);
    }

    @Override
    public final <K, V> MapVisitService<K, V> transformToMap(
            Object param,
            Comparator<? super K> comparator,
            ResultVisitor<? super T, K> keyVisitor,
            ResultVisitor<? super T, V> valueVisitor) {
        Throwables.checkNull(keyVisitor);
        Throwables.checkNull(valueVisitor);

        final List<T> list = visitForQueryList(param, Visitors.truePredicateVisitor(), mCacheList);
        final Map<K, V> map = newMap(comparator);
        for (T t : list) {
            map.put(keyVisitor.visit(t, param), valueVisitor.visit(t, param));
        }
        list.clear();
        return VisitServices.from(map);
    }

    @Override
    public final <R> CollectionVisitService<R> transformToCollection(
            Object param, Comparator<? super R> sort, ResultVisitor<? super T, R> resultVisitor) {
        Throwables.checkNull(resultVisitor);
        return getVisitService(
                visitForResultList(param, resultVisitor, null), sort, (this instanceof ListVisitService));
    }

    @Override
    public final <R> CollectionVisitService<R> transformToCollection(
            ResultVisitor<? super T, R> resultVisitor) {
        return transformToCollection(null, resultVisitor);
    }

    @Override
    public final <R> CollectionVisitService<R> transformToCollection(
            Object param, ResultVisitor<? super T, R> resultVisitor) {
        return transformToCollection(param, null, resultVisitor);
    }

    // =======================================================================

    @Override
    public final <R> List<R> visitForResultList(
            PredicateVisitor<? super T> predicate,
            ResultVisitor<? super T, R> resultVisitor,
            List<R> out) {
        return visitForResultList(null, predicate, resultVisitor, out);
    }

    @Override
    public final <R> List<R> visitForResultList(
            Object param, ResultVisitor<? super T, R> resultVisitor, List<R> out) {
        return visitForResultList(param, Visitors.truePredicateVisitor(), resultVisitor, out);
    }

    @Override
    public final <R> List<R> visitForResultList(
            ResultVisitor<? super T, R> resultVisitor, List<R> out) {
        return visitForResultList(null, Visitors.truePredicateVisitor(), resultVisitor, out);
    }

    @Override
    public final <R> R visitForResult(
            PredicateVisitor<? super T> predicate, ResultVisitor<? super T, R> resultVisitor) {
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
    public boolean visitAll(Object param, IterateVisitor<? super T> visitor) {
        return visit(VISIT_RULE_ALL, param, visitor);
    }

    @Override
    public final boolean visitAll(Object param) {
        return visit(VISIT_RULE_ALL, param, null);
    }

    @Override
    public final boolean visitUntilSuccess(Object param, IterateVisitor<? super T> breakVisitor) {
        Throwables.checkNull(breakVisitor);
        return visit(VISIT_RULE_UNTIL_SUCCESS, param, breakVisitor);
    }

    @Override
    public final boolean visitUntilFailed(Object param, IterateVisitor<? super T> breakVisitor) {
        Throwables.checkNull(breakVisitor);
        return visit(VISIT_RULE_UNTIL_FAILED, param, breakVisitor);
    }

    /**
     * do visit service. if current collection is empty, the parameter and break visitor will be
     * ignored. instead it will directly execute the insert-finally operation and return true.
     *
     * @param rule         the rule . VISIT_RULE_ALL, VISIT_RULE_UNTIL_SUCCESS or VISIT_RULE_UNTIL_FAILED.
     * @param param        the extra parameter
     * @param breakVisitor the break visitor
     * @return true if operate success.
     */
    protected abstract boolean visit(int rule, Object param, IterateVisitor<? super T> breakVisitor);

    // =======================================

    @Override
    public ListVisitService<T> asListService() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public final CollectionVisitService<T> subService(PredicateVisitor<T> visitor) {
        return subService(null, visitor);
    }

    // ========================================== start 1.2.0
    // ==========================================

    @Override
    public final Collection<T> copy() {
        return new ArrayList<T>(get());
    }

    @Override
    public final T query(PredicateVisitor<? super T> predicate) {
        return visitForQuery(null, predicate);
    }

    @Override
    public final T query(Object param, PredicateVisitor<? super T> predicate) {
        return visitForQuery(param, predicate);
    }

    @Override
    public  CollectionVisitService<T> queryList(PredicateVisitor<? super T> predicate) {
        return queryList(null, predicate);
    }

    @Override
    public CollectionVisitService<T> queryList(Object param, PredicateVisitor<? super T> predicate) {
        Throwables.checkNull(predicate);
        return VisitServices.from(visitForQueryList(param, predicate, null));
    }

    @Override
    public <R> CollectionVisitService<R> map(
            Object param, Comparator<? super R> sort, ResultVisitor<? super T, R> resultVisitor) {
        return transformToCollection(param, sort, resultVisitor);
    }

    @Override
    public <R> CollectionVisitService<R> map(ResultVisitor<? super T, R> visitor) {
        return transformToCollection(visitor);
    }

    @Override
    public  <R> CollectionVisitService<R> map(Object param, ResultVisitor<? super T, R> visitor) {
        return transformToCollection(param, visitor);
    }

    @Override
    public final <K, V> MapVisitService<K, V> map2map(
            Object param,
            Comparator<? super K> comparator,
            ResultVisitor<? super T, K> keyVisitor,
            ResultVisitor<? super T, V> valueVisitor) {
        return transformToMap(param, comparator, keyVisitor, valueVisitor);
    }

    @Override
    public final <K, V> MapVisitService<K, V> map2map(
            Object param,
            ResultVisitor<? super T, K> keyVisitor,
            ResultVisitor<? super T, V> valueVisitor) {
        return transformToMap(param, null, keyVisitor, valueVisitor);
    }

    @Override
    public final <K, V> MapVisitService<K, V> map2map(
            ResultVisitor<? super T, K> keyVisitor, ResultVisitor<? super T, V> valueVisitor) {
        return transformToMap(null, null, keyVisitor, valueVisitor);
    }

    @Override
    public final <K> MapVisitService<K, T> map2mapAsValue(ResultVisitor<? super T, K> valueVisitor) {
        return transformToMapAsValues(valueVisitor);
    }

    @Override
    public final <K> MapVisitService<K, T> map2mapAsValue(
            Object param, ResultVisitor<? super T, K> valueVisitor) {
        return transformToMapAsValues(param, valueVisitor);
    }

    @Override
    public final <K> MapVisitService<K, T> map2mapAsValue(
            Object param, Comparator<? super K> comparator, ResultVisitor<? super T, K> valueVisitor) {
        return transformToMapAsValues(param, comparator, valueVisitor);
    }

    @Override
    public final <V> MapVisitService<T, V> map2mapAsKey(ResultVisitor<? super T, V> valueVisitor) {
        return transformToMapAsKeys(valueVisitor);
    }

    @Override
    public final <V> MapVisitService<T, V> map2mapAsKey(
            Object param, ResultVisitor<? super T, V> valueVisitor) {
        return transformToMapAsKeys(param, valueVisitor);
    }

    @Override
    public final <V> MapVisitService<T, V> map2mapAsKey(
            Object param, Comparator<? super T> comparator, ResultVisitor<? super T, V> valueVisitor) {
        return transformToMapAsKeys(param, comparator, valueVisitor);
    }

    @Override
    public CollectionVisitService<T> intersect(Collection<? super T> coll) {
        List<T> list = new ArrayList<T>();
        for (T t : get()) {
            if (coll.contains(t)) {
                list.add(t);
            }
        }
        return VisitServices.from(list);
    }

    @Override
    public <K, V> MapVisitService<K, V> diff(Object param, Collection<T> l1, ResultVisitor<T, K> keyVisitor,
                                             NormalizeVisitor<K, T, T, Void, V> visitor,
                                             final DiffPredicateVisitor<V, T> diffPredicateCur,
                                             final DiffPredicateVisitor<V, T> diffPredicateOther,
                                             DiffResultVisitor<V, T> diffVisitor) {
        Throwables.checkNull(keyVisitor);
        Throwables.checkNull(visitor);
        Throwables.checkNull(diffPredicateCur);
        Throwables.checkNull(diffPredicateOther);
        Throwables.checkNull(diffVisitor);
        //build map
        final List<T> list = visitForQueryList(Visitors.truePredicateVisitor(), mCacheList);
        final Map<K, T> map = newMap(null);
        for (T t : list) {
            map.put(keyVisitor.visit(t, param), t);
        }
        //normalize
        MapVisitService<K, T> s1 = VisitServices.from(l1).map2mapAsValue(param, keyVisitor);
        MapVisitService<K, V> normalizeMap = VisitServices.from(map).normalize(param, s1, visitor);

        CollectionVisitService<V> normalService = normalizeMap.mapValue();
        //diff current
        List<T> diff_cur = new ArrayList<>();
        for (final T t : list){
            boolean empty = normalService.filter(param, new PredicateVisitor<V>() {
                @Override
                public Boolean visit(V v, Object param) {
                    return !diffPredicateCur.visit(param, v, t);
                }
            }, 1, null).get().isEmpty();
            //empty means not contains is the normalize list.
            if(empty){
                diff_cur.add(t);
            }
        }
        list.clear();
        //diff other
        List<T> diff_other = new ArrayList<>();
        for (final T t : l1){
            boolean empty = normalService.filter(param, new PredicateVisitor<V>() {
                @Override
                public Boolean visit(V v, Object param) {
                    return !diffPredicateOther.visit(param, v, t);
                }
            }, 1, null).get().isEmpty();
            //empty means not contains is the normalize list.
            if(empty){
                diff_other.add(t);
            }
        }
        diffVisitor.visit(param, normalService.getAsList(), diff_cur, diff_other);
        return normalizeMap;
    }

    @Override
    public <K, V> MapVisitService<K, V> normalize(Object param, Collection<T> l1, ResultVisitor<T, K> keyVisitor,
                                                  NormalizeVisitor<K, T, T, Void, V> visitor) {
        return normalize(param, l1, keyVisitor, keyVisitor, visitor);
    }

    @Override
    public <K, V, T1> MapVisitService<K, V> normalize(Object param, List<T1> l1,
                                                      ResultVisitor<T, K> main, ResultVisitor<T1, K> v1,
                                                      NormalizeVisitor<K, T, T1, Void, V> visitor) {
        MapVisitService<K, T1> s1 = VisitServices.from(l1).map2mapAsValue(param, v1);
        return map2mapAsValue(param, main).normalize(param, s1, visitor);
    }

    @Override
    public <K, V, T1> MapVisitService<K, V> normalize(Object param, Collection<T1> l1, ResultVisitor<T, K> main, ResultVisitor<T1, K> v1,
                                                      NormalizeVisitor<K, T, T1, Void, V> visitor) {
        MapVisitService<K, T1> s1 = VisitServices.from(l1).map2mapAsValue(param, v1);
        return map2mapAsValue(param, main).normalize(param, s1, visitor);
    }

    @Override
    public <K, V, T1, T2> MapVisitService<K, V> normalize(Object param, List<T1> l1, List<T2> l2, ResultVisitor<T, K> main,
                                                          ResultVisitor<T1, K> v1,  ResultVisitor<T2, K> v2,
                                                          NormalizeVisitor<K, T, T1, T2, V> visitor) {
        MapVisitService<K, T1> s1 = VisitServices.from(l1).map2mapAsValue(param, v1);
        MapVisitService<K, T2> s2 = VisitServices.from(l2).map2mapAsValue(param, v2);
        return map2mapAsValue(param, main).normalize(param, s1, s2, visitor);
    }

    @Override
    public <K, V, T1, T2> MapVisitService<K, V> normalize(Object param, Collection<T1> l1, Collection<T2> l2,
                                                          ResultVisitor<T, K> main, ResultVisitor<T1, K> v1, ResultVisitor<T2, K> v2,
                                                          NormalizeVisitor<K, T, T1, T2, V> visitor) {
        MapVisitService<K, T1> s1 = VisitServices.from(l1).map2mapAsValue(param, v1);
        MapVisitService<K, T2> s2 = VisitServices.from(l2).map2mapAsValue(param, v2);
        return map2mapAsValue(param, main).normalize(param, s1, s2, visitor);
    }

    @Override
    public CollectionVisitService<T> trim() {
        Iterator<T> it = get().iterator();
        for (; it.hasNext(); ) {
            if (it.next() == null) {
                it.remove();
            }
        }
        return this;
    }

    @Override
    public CollectionVisitService<T> filter(PredicateVisitor<T> predicate) {
        return filter(null, predicate, null);
    }

    @Override
    public CollectionVisitService<T> filter(Object param, PredicateVisitor<T> predicate, List<T> dropOut) {
        List<T> result = new ArrayList<T>();
        for (T t : get()) {
            if (Predicates.isTrue(predicate.visit(t, param))) {
                result.add(t);
            } else if (dropOut != null) {
                dropOut.add(t);
            }
        }
        return VisitServices.from(result);
    }

    @Override
    public CollectionVisitService<T> filter(Object param, PredicateVisitor<T> predicate, int maxCount, List<T> dropOut) {
        return filter(param, null, predicate, maxCount, dropOut);
    }

    @Override
    public CollectionVisitService<T> filter(Object param, Comparator<? super T> com ,PredicateVisitor<T> predicate, int maxCount, List<T> dropOut) {
        Throwables.checkNull(predicate);
        if(maxCount <= 0){
            throw new IllegalArgumentException("maxCount must > 0");
        }
        //sort
        ArrayList<T> newList = new ArrayList<>(get());
        if(com != null) {
            Collections.sort(newList, com);
        }
        //start build result
        List<T> result = new ArrayList<T>();
        int matchSize = 0;
        for (T t : newList) {
            if(matchSize == maxCount){
                if(dropOut == null){
                    break;
                }else{
                    dropOut.add(t);
                }
            }else if (Predicates.isTrue(predicate.visit(t, param))) {
                result.add(t);
                matchSize ++;
            } else if (dropOut != null) {
                dropOut.add(t);
            }
        }
        return VisitServices.from(result);
    }

    @Override
    public final <R> R pile(Object param, ResultVisitor<T, R> mapper, PileVisitor<R> pileVisitor) {
        R pileResult = null;
        for (T t : get()) {
            R r = mapper.visit(t, param);
            if (pileResult != null) {
                pileResult = pileVisitor.visit(param, pileResult, r);
            } else {
                pileResult = r;
            }
        }
        return pileResult;
    }

    @Override
    public final T pile(Object param, PileVisitor<T> pileVisitor) {
        return pile(param, Visitors.<T, T>unchangeResultVisitor(), pileVisitor);
    }

    @Override
    public T pile(PileVisitor<T> pileVisitor) {
        return pile(null, Visitors.<T, T>unchangeResultVisitor(), pileVisitor);
    }

    @Override
    public List<T> getAsList() {
        Collection<T> coll = get();
        if (coll instanceof List) {
            return (List<T>) coll;
        }
        return new ArrayList<>(coll);
    }

    @Override
    public List<T> copyAsList() {
        return new ArrayList<T>(get());
    }

    @Override
    public CollectionVisitService<T> copyTo(Collection<T> out) {
        out.addAll(get());
        return this;
    }

    @Override
    public CollectionVisitService<T> copyService() {
        return VisitServices.from(copy());
    }
    // ========================================== end 1.2.0 ==========================================

    // ===================== start inner classes
    // ====================================

    private static class CompareHelper<T>{

        final Comparator<? super T> com;

        public CompareHelper(@Nullable  Comparator<? super T> com) {
            this.com = com;
        }
        public int getContainsIndex(List<T> container, T target){
            if(com == null){
                return container.indexOf(target);
            }else{
                for(int size = container.size(), i = size -1 ; i >=0 ; i --){
                    if(com.compare(container.get(i), target) == 0){
                        return i;
                    }
                }
                return -1;
            }
        }
    }

    private static class WrappedObserveVisitor<T, R> implements IterateVisitor<T> {

        private final PredicateVisitor<T> mPredicate;
        private final Observer<T, R> mObserver;
        private final Object mParam;
        private T mLastT;

        public WrappedObserveVisitor(
                Object param, PredicateVisitor<T> mPredicate, Observer<T, R> mObserver) {
            super();
            this.mParam = param;
            this.mPredicate = mPredicate;
            this.mObserver = mObserver;
        }

        @Override
        public Boolean visit(T t, Object param, IterationInfo info) {
            mLastT = t;
            return mPredicate.visit(t, param);
        }

        public void onFailed() {
            mObserver.onFailed(mParam, mLastT);
        }

        public void onSuccess(R result) {
            mObserver.onSuccess(mParam, result);
        }

        public void onThrowable(Throwable e) {
            mObserver.onThrowable(mParam, mLastT, e);
        }
    }
    // ====================== end inner classes ============================
}
