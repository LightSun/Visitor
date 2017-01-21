package com.heaven7.java.visitor.collection;

import java.util.Collection;

public final class ListVisitorService<T> extends VisitService<T> {

	protected ListVisitorService(Collection<T> collection) {
		super(collection);
	}

}
