package com.heaven7.java.visitor.internal;

import static com.heaven7.java.visitor.util.Predicates.isTrue;
import static com.heaven7.java.visitor.util.Throwables.checkNull;

import java.util.Iterator;
import java.util.List;

import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.CollectionVisitService.OperateInterceptor;
import com.heaven7.java.visitor.collection.IterationInfo;

/*public*/ class MultipleIterateState<T, R> extends IterateState<T, R> {

	@Override
	protected T visitImpl(boolean hasExtra, OperateInterceptor<T> interceptor, Object param,
			PredicateVisitor< ? super T> predicate, Iterator<T> it, IterationInfo info, List<T> out) {
		checkNull(out);
		T t;
		for (; it.hasNext();) {
			t = it.next();
			if (hasExtra && interceptor.intercept(it, t, param, info)) {
				continue;
			}
			if (isTrue(predicate.visit(t, param))) {
				out.add(t);
			}
		}
		return null;
	}

	@Override
	protected R visitForResultImpl(boolean hasExtra, OperateInterceptor<T> interceptor, Object param,
			PredicateVisitor<? super T> predicate, ResultVisitor<? super T, R> resultVistor, Iterator<T> it,
			IterationInfo info, List<R> out) {
		checkNull(out);
		T t;
		R result;
		for (; it.hasNext();) {
			t = it.next();
			if (hasExtra && interceptor.intercept(it, t, param, info)) {
				continue;
			}
			if (isTrue(predicate.visit(t, param))) {
				//null will be filter.
				if((result = resultVistor.visit(t, param) ) != null){
					out.add(result);
				}
			}
		}
		return null;
	}


}
