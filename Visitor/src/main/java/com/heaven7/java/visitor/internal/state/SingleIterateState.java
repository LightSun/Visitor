package com.heaven7.java.visitor.internal.state;

import java.util.Iterator;
import java.util.List;

import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.CollectionVisitService.CollectionOperateInterceptor;
import com.heaven7.java.visitor.collection.IterationInfo;
import com.heaven7.java.visitor.util.Predicates;

/*public*/ class SingleIterateState<T> extends IterateState<T>{

	@Override
	protected T visitImpl(boolean hasExtra, CollectionOperateInterceptor<T> interceptor, Object param,
			PredicateVisitor<? super T> predicate, Iterator<T> it, IterationInfo info, List<T> out) {
		T result = null;
		T t ;
		Boolean pResult;
		//move this out. for fast
		if(hasExtra){
			for (; it.hasNext();) {
				t = it.next();
				if(interceptor.intercept(it, t, param, info)){
					continue;
				}
				if (result == null ) {
					pResult = predicate.visit(t, param);
					if(pResult !=null && pResult){
						result = t;
					}
				}
			}
		}else{
			//no extra
			for (; it.hasNext();) {
				t = it.next();
				if (result == null ) {
					pResult = predicate.visit(t, param);
					if(pResult !=null && pResult){
						result = t;
						//no extra , just break it.
						break;
					}
				}
			}
		}
		return result;
	}


	@Override
	protected <R> R visitForResultImpl(boolean hasExtra, CollectionOperateInterceptor<T> interceptor, Object param,
			PredicateVisitor<? super T> predicate, ResultVisitor<? super T, R> resultVistor, Iterator<T> it,
			IterationInfo info, List<R> out) {
		R result = null;
		T t ;
		Boolean pResult;
		if(hasExtra){
			for (; it.hasNext();) {
				t = it.next();
				if(interceptor.intercept(it, t, param, info)){
					continue;
				}
				if (result == null) {
					pResult = predicate.visit(t, param);
					if(pResult !=null && pResult){
						result = resultVistor.visit(t, param);
						//has extra , no break.
					}
				}
			}
		}else{
			//no extra
			for (; it.hasNext();) {
				t = it.next();
				if(Predicates.isTrue(predicate.visit(t, param))){
					result = resultVistor.visit(t, param);
					//no extra , just break it.
					break;
				}
			}
		}
		return result;
	}

}
