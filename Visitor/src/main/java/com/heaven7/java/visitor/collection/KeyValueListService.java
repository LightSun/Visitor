package com.heaven7.java.visitor.collection;

import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.Visitors;
import com.heaven7.java.visitor.anno.Nullable;

import java.util.Comparator;
import java.util.List;

/**
 * the key-value list service
 * @param <K> the key type
 * @param <V> the value type
 * @since 1.2.0
 */
public class KeyValueListService<K,V> extends ListVisitServiceImpl<KeyValuePair<K,V>> {

    protected KeyValueListService(List<KeyValuePair<K, V>> collection) {
        super(collection);
    }

    public MapVisitService<K, V> map2map(@Nullable Comparator<? super  K> comparator){
        return map2map(null, comparator, new ResultVisitor<KeyValuePair<K, V>, K>() {
                    @Override
                    public K visit(KeyValuePair<K, V> pair, Object param) {
                        return pair.getKey();
                    }
                },
                new ResultVisitor<KeyValuePair<K, V>, V>() {
                    @Override
                    public V visit(KeyValuePair<K, V> pair, Object param) {
                        return pair.getValue();
                    }
                });
    }
    public MapVisitService<K, V> map2map(){
        return map2map(null);
    }

}
