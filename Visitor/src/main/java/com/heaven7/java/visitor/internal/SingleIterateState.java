package com.heaven7.java.visitor.internal;

import java.util.Iterator;
import java.util.List;

import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.CollectionVisitService.OperateInterceptor;
import com.heaven7.java.visitor.collection.IterationInfo;

/*public*/ class SingleIterateState<T, R> extends IterateState<T, R>{

	@Override
	protected T visitImpl(boolean hasExtra, OperateInterceptor<T> interceptor, Object param,
			PredicateVisitor<? super T> predicate, Iterator<T> it, IterationInfo info, List<T> out) {
		T result = null;
		T t ;
		Boolean pResult;
		for (; it.hasNext();) {
			t = it.next();
			if(hasExtra && interceptor.intercept(it, t, param, info)){
				continue;
			}
			if (result == null ) {
				pResult = predicate.visit(t, param);
				if(pResult !=null && pResult){
					result = t;
					//no extra , just break it.
					if(!hasExtra){
						break;
					}
				}
			}
		}
		return result;
	}


	@Override
	protected R visitForResultImpl(boolean hasExtra, OperateInterceptor<T> interceptor, Object param,
			PredicateVisitor<? super T> predicate, ResultVisitor<? super T, R> resultVistor, Iterator<T> it,
			IterationInfo info, List<R> out) {
		R result = null;
		T t ;
		Boolean pResult;
		for (; it.hasNext();) {
			t = it.next();
			if(hasExtra && interceptor.intercept(it, t, param, info)){
				continue;
			}
			if (result == null) {
				pResult = predicate.visit(t, param);
				if(pResult !=null && pResult){
					result = resultVistor.visit(t, param);
					//no extra , just break it.
					if(!hasExtra){
						break;
					}
				}
			}
		}
		return result;
	}

}
