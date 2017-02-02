package com.heaven7.java.visitor;


import com.heaven7.java.visitor.collection.IterationInfo;
import com.heaven7.java.visitor.util.Map;

public interface TrimMapVisitor<K, V> extends IterateVisitor<Map<K, V>> {
	
	@Override
	Boolean visit(Map<K, V> t, Object param, IterationInfo info);

}
