package com.heaven7.java.visitor;

import com.heaven7.java.visitor.collection.IterationInfo;
import com.heaven7.java.visitor.collection.KeyValuePair;

public interface MapIterateVisitor<K,V> extends IterateVisitor<KeyValuePair<K, V>> {

	@Override
	Boolean visit(KeyValuePair<K, V> pair, Object param, IterationInfo info);
}
