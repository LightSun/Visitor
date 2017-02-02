package com.heaven7.java.visitor;

import com.heaven7.java.visitor.collection.KeyValuePair;

public interface MapResultVisitor<K, V, Result> extends ResultVisitor<KeyValuePair<K, V>, Result> {

    @Override
    Result visit(KeyValuePair<K, V> t, Object param);
}