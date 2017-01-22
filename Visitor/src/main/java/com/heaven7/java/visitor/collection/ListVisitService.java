package com.heaven7.java.visitor.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

final class ListVisitService<T> extends VisitService<T> {

	protected ListVisitService(List<T> collection) {
		super(collection);
	}
	
	@Override
	protected boolean visitImpl(Collection<T> collection, int rule, Object param, OperateInterceptor<T> interceptor,
			IterateVisitor<? super T> breakVisitor, final IterationInfo info) {

		final Iterator<T> it = ((List<T>) collection).listIterator();
		
		boolean result = false;
		Boolean visitResult;
		T t;

		switch (rule) {
		case VISIT_RULE_UNTIL_FAILED: {
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
		}
			break;

		case VISIT_RULE_UNTIL_SUCCESS: {
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
		}
			break;

		case VISIT_RULE_ALL: {
			for (; it.hasNext();) {
				t = it.next();
				interceptor.intercept(it, t, param, info);
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
	protected boolean onHandleInsert(List<Operation<T>> inserts, Iterator<T> it, T t, Object param,
			IterationInfo info) {
		final ListIterator<T> lit = (ListIterator<T>) it;
		info.setCurrentIndex(lit.previousIndex());

		try {
			Operation<T> op;
			for (ListIterator<Operation<T>> olit = inserts.listIterator(); olit.hasNext();) {
				op = olit.next();
				if (op.insert(lit, t, param, info)) {
					info.incrementInsert();
					info.incrementCurrentSize();
					return true;
				}
			}
		} catch (UnsupportedOperationException e) {
			System.err.println("insert failed. caused by the list is fixed. "
					+ "so can't modified. are your list comes from 'Arrays.asList(...)'");
            return false;
		}
		return false;
	}
	
	

}
