package com.heaven7.java.visitor;

import com.heaven7.java.visitor.collection.KeyValuePair;

public interface MapPredicateVisitor<K, V> extends PredicateVisitor<KeyValuePair<K, V>> {

}