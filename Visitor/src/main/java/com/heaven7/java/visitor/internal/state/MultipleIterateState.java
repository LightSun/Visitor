package com.heaven7.java.visitor.internal.state;

import static com.heaven7.java.visitor.util.Predicates.isTrue;
import static com.heaven7.java.visitor.util.Throwables.checkNull;

import java.util.Iterator;
import java.util.List;

import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.CollectionVisitService.CollectionOperateInterceptor;
import com.heaven7.java.visitor.collection.IterationInfo;

/*public*/ class MultipleIterateState<T, R> extends IterateState<T, R> {

	@Override
	protected T visitImpl(boolean hasExtra, CollectionOperateInterceptor<T> interceptor, Object param,
			PredicateVisitor< ? super T> predicate, Iterator<T> it, IterationInfo info, List<T> out) {
		checkNull(out);
		T t;
		if(hasExtra){
			for (; it.hasNext();) {
				t = it.next();
				if (interceptor.intercept(it, t, param, info)) {
					continue;
				}
				if (isTrue(predicate.visit(t, param))) {
					out.add(t);
				}
			}
		}else{
			//no extra ops
			for (; it.hasNext();) {
				t = it.next();
				if (isTrue(predicate.visit(t, param))) {
					out.add(t);
				}
			}
		}
		return null;
	}

	@Override
	protected R visitForResultImpl(boolean hasExtra, CollectionOperateInterceptor<T> interceptor, Object param,
			PredicateVisitor<? super T> predicate, ResultVisitor<? super T, R> resultVistor, Iterator<T> it,
			IterationInfo info, List<R> out) {
		checkNull(out);
		T t;
		R result;
		if(hasExtra){
			for (; it.hasNext();) {
				t = it.next();
				if (interceptor.intercept(it, t, param, info)) {
					continue;
				}
				if (isTrue(predicate.visit(t, param))) {
					//null will be filter.
					if((result = resultVistor.visit(t, param) ) != null){
						out.add(result);
					}
				}
			}
		}else{
			//no extra ops
			for (; it.hasNext();) {
				t = it.next();
				if (isTrue(predicate.visit(t, param))) {
					//null will be filter.
					if((result = resultVistor.visit(t, param) ) != null){
						out.add(result);
					}
				}
			}
		}
		return null;
	}


}
