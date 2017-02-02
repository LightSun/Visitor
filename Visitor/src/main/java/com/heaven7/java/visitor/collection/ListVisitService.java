package com.heaven7.java.visitor.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.heaven7.java.visitor.IterateVisitor;
/**
 * list visit service
 * @author heaven7
 *
 * @param <T>
 */
final class ListVisitService<T> extends CollectionVisitServiceImpl<T> implements CollectionVisitService<T>  {

	protected ListVisitService(List<T> collection) {
		super(collection);
	}
	
	@Override
	protected boolean visitImpl(Collection<T> collection, int rule, Object param, CollectionOperateInterceptor<T> interceptor,
			IterateVisitor<? super T> breakVisitor, final IterationInfo info) {

		final boolean hasExtra = hasExtraOperateInIteration();
		final Iterator<T> it = ((List<T>) collection).listIterator();
		
		boolean result = false;
		Boolean visitResult;
		T t;

		//list support break
		switch (rule) {
		case VISIT_RULE_UNTIL_FAILED: {
			if(hasExtra){
				for (; it.hasNext();) {
					t = it.next();
					if (interceptor.intercept(it, t, param, info)) {
						continue;
					}
					visitResult = breakVisitor.visit(t, param, info);
					if (visitResult == null || !visitResult) {
						result = true;
						break;
					}
				}
			}else{
				for (; it.hasNext();) {
					t = it.next();
					visitResult = breakVisitor.visit(t, param, info);
					if (visitResult == null || !visitResult) {
						result = true;
						break;
					}
				}
			}
		}
			break;

		case VISIT_RULE_UNTIL_SUCCESS: {
			if(hasExtra){
				for (; it.hasNext();) {
					t = it.next();
					if (interceptor.intercept(it, t, param, info)) {
						continue;
					}
					
					visitResult = breakVisitor.visit(t, param, info);
					if (visitResult != null && visitResult) {
						result = true;
						break;
					}
				}
			}else{
				for (; it.hasNext();) {
					t = it.next();
					
					visitResult = breakVisitor.visit(t, param, info);
					if (visitResult != null && visitResult) {
						result = true;
						break;
					}
				}
			}
		}
			break;

		case VISIT_RULE_ALL: {
			if(hasExtra){
				for (; it.hasNext();) {
					t = it.next();
					interceptor.intercept(it, t, param, info);
				}
			}
			result = true;
		}
			break;

		default:
			throw new RuntimeException("unsupport rule = " + rule);
		}
		return result;
	}

	@Override
	protected boolean onHandleInsert(List<CollectionOperation<T>> inserts, Iterator<T> it, T t, Object param,
			IterationInfo info) {
		final ListIterator<T> lit = (ListIterator<T>) it;
		info.setCurrentIndex(lit.previousIndex());

		//final boolean hasInfo = info != null;
		try {
			CollectionOperation<T> op;
			for (ListIterator<CollectionOperation<T>> olit = inserts.listIterator(); olit.hasNext();) {
				op = olit.next();
				if (op.insert(lit, t, param, info)) {
					//info update: impl move to internal
					return true;
				}
			}
		} catch (UnsupportedOperationException e) {
			System.err.println("insert failed. caused by the list is fixed. "
					+ "so can't modified. are your list comes from 'Arrays.asList(...)' ? ");
		}
		return false;
	}
	
	

}
