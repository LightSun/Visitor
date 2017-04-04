package com.heaven7.java.visitor.collection.operator;

import static com.heaven7.java.visitor.collection.operator.OperateCondition.*;
import static com.heaven7.java.visitor.util.Collections2.update;
import static com.heaven7.java.visitor.util.Collections2.updateAll;
import static com.heaven7.java.visitor.util.Predicates.isTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
/**
 * the all common operator in here.
 * @author heaven7
 * @since 2.0.0
 */
public final class Operators {
	
	public static <T, R> Operator<T, List<R>> ofZipResult(final int maxSize) {
		return new ListResultOperator<T, R>() {
			@SuppressWarnings("unchecked")
			@Override
			protected List<R> executeOperator(Collection<T> src, OperateCondition<T, List<R>> condition) {
				final ResultVisitor<? super T, R> visitor = (ResultVisitor<? super T, R>)
						condition.getResultVisitor();
				final Object param = condition.getParam();
				
				List<R> list = new ArrayList<R>();
				int size = 0;
				R result = null;
				for(T t : src){
					startVisitElement(t);
					result = visitor.visit(t, param);
					//null means failed.
					if(result == null){
						return null;
					}
					list.add(result);
					if( (size ++ ) >= maxSize){
						break;
					}
				}
				//success
				return list;
			}
		};
	}
	
	public static <T> Operator<T, Boolean> ofZip() {
		return new BooleanOperator<T>(){
			@Override
			protected Boolean executeOperator(Collection<T> src, OperateCondition<T, Boolean> condition) {
				@SuppressWarnings("unchecked")
				final ResultVisitor<? super T, Boolean> visitor = (ResultVisitor<? super T, Boolean>)
						condition.getResultVisitor();
				final Object param = condition.getParam();
				for(T t : src){
					startVisitElement(t);
					if(!isTrue(visitor.visit(t, param))){
						return false;
					}
				}
				return true;
			}
			
			@Override
			public int getRequireArgsFlags() {
				return FLAG_RESULT;
			}
		
		};
	}
	
	public static <T> Operator<T, Boolean> ofUpdateAll(final int maxLength) {
		return new BooleanOperator<T>() {
			@SuppressWarnings("unchecked")
			@Override
			protected Boolean executeOperator(Collection<T> src, OperateCondition<T, Boolean> condition) {
				return updateAll(src, condition.getParam(), maxLength,
						(ResultVisitor<? super T, T>)condition.getResultVisitor(), this);
			}
			@Override
			public int getRequireArgsFlags() {
				return FLAG_RESULT;
			}
		};
	}
	//need ResultVisitor<? super T, T>
	public static <T> Operator<T, Boolean> ofUpdate2() {
		return ofUpdateAll(1);
	}
	//need new element and predicate
	public static <T> Operator<T, Boolean> ofUpdate() {
		return new BooleanOperator<T>() {
			@Override
			protected Boolean executeOperator(Collection<T> src, OperateCondition<T, Boolean> condition) {
				return update(src, condition.getElement(), condition.getParam(),
						condition.getPredicateVisitor(), this);
			}
			@Override
			public int getRequireArgsFlags() {
				return FLAG_SINGLE_ELEMENT | FLAG_PREDICATE;
			}
		};
	}
	
	public static <T> Operator<T, List<T>> ofFilter() {
		return ofFilter(Integer.MAX_VALUE);
	}
	
	//query all
	public static <T> Operator<T, List<T>> ofFilter(final int maxSize) {
		return new ListOperator<T>() {
			@Override
			protected List<T> executeOperator(Collection<T> src, OperateCondition<T, List<T>> condition) {
				final PredicateVisitor<? super T> visitor = condition.getPredicateVisitor();
				final Object param = condition.getParam();
				final List<T> list = new ArrayList<T>();
				int size = 0;
				for( T t : src){
					startVisitElement(t);
					if(isTrue(visitor.visit(t, param))){
						list.add(t);
						if( (size++) >= maxSize){
							break;
						}
					}
				}
				return list;
			}
		};
	}
	
	public static <T> Operator<T, Boolean> ofAddList() {
		return new BooleanOperator<T>() {
			@Override
			protected Boolean executeOperator(Collection<T> src, OperateCondition<T, Boolean> condition) {
				return src.addAll(condition.getOtherCollectionr()) ;
			}
		};
	}
	public static <T> Operator<T, Boolean> ofAddIfNotExist() {
		return new BooleanOperator<T>() {
			@Override
			protected Boolean executeOperator(Collection<T> src, OperateCondition<T, Boolean> condition) {
				if(!src.contains(condition.getElement())){
					return src.add(condition.getElement());
				}
				return false;
			}
			@Override
			public int getRequireArgsFlags() {
				return FLAG_SINGLE_ELEMENT;
			}
		};
	}
	public static <T> Operator<T, Boolean> ofAdd() {
		return new BooleanOperator<T>() {
			@Override
			protected Boolean executeOperator(Collection<T> src, OperateCondition<T, Boolean> condition) {
				return src.add(condition.getElement()) ;
			}
			@Override
			public int getRequireArgsFlags() {
				return FLAG_SINGLE_ELEMENT;
			}
		};
	}
	public static <T> Operator<T, Boolean> ofSizeEquals(final int size) {
		return new Operator<T, Boolean>() {
			@Override
			protected Boolean executeOperator(Collection<T> src, OperateCondition<T, Boolean> condition) {
				return src.size() == size ;
			}
		};
	}
	public static <T> Operator<T, Boolean> ofSizeMin(final int min) {
		return new Operator<T, Boolean>() {
			@Override
			protected Boolean executeOperator(Collection<T> src, OperateCondition<T, Boolean> condition) {
				return src.size() >= min ;
			}
		};
	}
	public static <T> Operator<T, Boolean> ofSizeMax(final int max) {
		return new Operator<T, Boolean>() {
			@Override
			protected Boolean executeOperator(Collection<T> src, OperateCondition<T, Boolean> condition) {
				return src.size() <= max ;
			}
		};
	}
	public static <T> Operator<T, Integer> ofSize() {
		return new Operator<T, Integer>() {
			@Override
			protected Integer executeOperator(Collection<T> src, OperateCondition<T, Integer> condition) {
				return src.size();
			}
		};
	}

	public static <T> Operator<T, Boolean> ofContains() {
		return new BooleanOperator<T>() {
			@Override
			protected Boolean executeOperator(Collection<T> src, OperateCondition<T, Boolean> condition) {
				return src.contains(condition.getElement());
			}

			@Override
			public int getRequireArgsFlags() {
				return FLAG_SINGLE_ELEMENT;
			}
		};
	}

	public static <T> Operator<T, Boolean> ofClear() {
		return new BooleanOperator<T>() {
			@Override
			protected Boolean executeOperator(Collection<T> src, OperateCondition<T, Boolean> condition) {
				if (src.size() > 0) {
					src.clear();
					return true;
				}
				return false;
			}
		};
	}
	public static <T> Operator<T, Boolean> ofRemoveIfExist() {
		return ofRemove();
	}
	public static <T> Operator<T, Boolean> ofRemove() {
		return new BooleanOperator<T>() {
			@Override
			protected Boolean executeOperator(Collection<T> src, OperateCondition<T, Boolean> condition) {
				return src.remove(condition.getElement());
			}

			@Override
			public int getRequireArgsFlags() {
				return FLAG_SINGLE_ELEMENT;
			}
		};
	}

	public static <T> Operator<T, Boolean> ofRemoveIf() {
		return new BooleanOperator<T>() {
			@Override
			protected Boolean executeOperator(Collection<T> src, OperateCondition<T, Boolean> condition) {
				final PredicateVisitor<? super T> predicate = condition.getPredicateVisitor();
				final Object param = condition.getParam();
				final Iterator<T> each = src.iterator();

				boolean removed = false;
				T t = null;
				while (each.hasNext()) {
					startVisitElement(t = each.next());
					if (predicate.visit(t, param)) {
						each.remove();
						removed = true;
					}
				}
				return removed;
			}

			@Override
			public int getRequireArgsFlags() {
				return FLAG_PREDICATE;
			}
		};
	}

	public static <T> Operator<T, Boolean> ofRetainAll() {
		return new BooleanOperator<T>() {
			@Override
			protected Boolean executeOperator(Collection<T> src, OperateCondition<T, Boolean> condition) {
				return src.retainAll(condition.getOtherCollectionr());
			}
		};
	}

	public static <T> Operator<T, Boolean> ofRemoveAll() {
		return new BooleanOperator<T>() {
			@Override
			protected Boolean executeOperator(Collection<T> src, OperateCondition<T, Boolean> condition) {
				return src.removeAll(condition.getOtherCollectionr());
			}
		};
	}

	public static abstract class BooleanOperator<T> extends Operator<T, Boolean> {
		@Override
		public int getRequireArgsFlags() {
			return FLAG_COLLECTION;
		}
	}

	public static abstract class ListOperator<T> extends Operator<T, List<T>> {
		@Override
		public int getRequireArgsFlags() {
			return FLAG_PREDICATE;
		}
	}

	public static abstract class ListResultOperator<T, R> extends Operator<T, List<R>> {
		@Override
		public int getRequireArgsFlags() {
			return FLAG_RESULT;
		}
	}

}
