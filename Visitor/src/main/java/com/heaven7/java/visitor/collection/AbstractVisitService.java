package com.heaven7.java.visitor.collection;

import java.util.List;

import com.heaven7.java.visitor.IterateVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
/**
 * the abstract visit service
 * @author heaven7
 *
 * @param <T> the type
 * @see VisitService
 * @see ListVisitService
 */
public abstract class AbstractVisitService<T> implements IVisitService<T>{
	
	@Override
	public final <R> List<R> visitForResult(PredicateVisitor<? super T> predicate, ResultVisitor<? super T, R> resultVisitor,
			List<R> out) {
		return visitForResult(null, predicate, resultVisitor, out);
	}
	
	@Override
	public final <R> R visitForResult(PredicateVisitor<? super T> predicate, ResultVisitor<? super T, R> resultVisitor) {
		return visitForResult(null, predicate, resultVisitor);
	}
	
	@Override
	public final List<T> visitForQuery(PredicateVisitor<? super T> predicate, List<T> out) {
		return visitForQuery(null, predicate, out);
	}
	
	@Override
	public final T visitForQuery(PredicateVisitor<? super T> predicate) {
		return visitForQuery(null, predicate);
	}
	
	@Override
	public final boolean visitAll() {
		return visitAll(null);
	}
	
	@Override
	public final boolean visitUntilSuccess(IterateVisitor<? super T> breakVisitor) {
		return visitUntilSuccess(null, breakVisitor);
	}
	
	@Override
	public final boolean visitUntilFailed(IterateVisitor<? super T> breakVisitor) {
		return visitUntilFailed(null, breakVisitor);
	}

	@Override
	public final boolean visitAll(Object param) {
		return visit(VISIT_RULE_ALL, param, null);
	}

	@Override
	public final boolean visitUntilSuccess(Object param, IterateVisitor<? super T> breakVisitor) {
		return visit(VISIT_RULE_UNTIL_SUCCESS, param, breakVisitor);
	}

	@Override
	public final boolean visitUntilFailed(Object param, IterateVisitor<? super T> breakVisitor) {
		return visit(VISIT_RULE_UNTIL_FAILED, param, breakVisitor);
	}
	
	/**
	 * do visit service.
	 * @param rule the rule . VISIT_RULE_ALL, VISIT_RULE_UNTIL_SUCCESS or VISIT_RULE_UNTIL_FAILED.
	 * @param param the extra param 
	 * @param breakVisitor the break visitor
	 * @return true if operate success.
	 */
	protected abstract boolean visit(int rule, Object param, IterateVisitor<? super T> breakVisitor);

}