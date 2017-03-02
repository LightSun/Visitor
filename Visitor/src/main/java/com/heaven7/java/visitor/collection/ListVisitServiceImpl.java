package com.heaven7.java.visitor.collection;

import static com.heaven7.java.visitor.util.Throwables.checkNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.heaven7.java.visitor.IterateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.Visitors;
import com.heaven7.java.visitor.anno.Nullable;
import com.heaven7.java.visitor.internal.InternalUtil;

/**
 * list visit service
 * 
 * @author heaven7
 *
 * @param <T>
 *            the element type
 */
final class ListVisitServiceImpl<T> extends CollectionVisitServiceImpl<T>
		implements CollectionVisitService<T>, ListVisitService<T> {
	
	private StringBuilder mSb;
	private List<String>  mTempStrs;

	protected ListVisitServiceImpl(List<T> collection) {
		super(collection);
	}

	// ==============================================================
	
	@Override
	public ListVisitService<T> asListService() throws UnsupportedOperationException {
		return this;
	}
	// ==============================================================

	@Override
	protected boolean visitImpl(Collection<T> collection, int rule, Object param,
			CollectionOperateInterceptor<T> interceptor, IterateVisitor<? super T> breakVisitor,
			final IterationInfo info) {

		final boolean hasExtra = hasExtraOperateInIteration();
		final Iterator<T> it = ((List<T>) collection).listIterator();

		boolean result = false;
		Boolean visitResult;
		T t;

		// list support break
		switch (rule) {
		case VISIT_RULE_UNTIL_FAILED: {
			if (hasExtra) {
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
			} else {
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
			if (hasExtra) {
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
			} else {
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
			if (hasExtra) {
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

		// final boolean hasInfo = info != null;
		try {
			CollectionOperation<T> op;
			for (ListIterator<CollectionOperation<T>> olit = inserts.listIterator(); olit.hasNext();) {
				op = olit.next();
				if (op.insert(lit, t, param, info)) {
					// info update: impl move to internal
					return true;
				}
			}
		} catch (UnsupportedOperationException e) {
			System.err.println("insert failed. caused by the list is fixed. "
					+ "so can't modified. are your list comes from 'Arrays.asList(...)' ? ");
		}
		return false;
	}

	@Override
	public ListVisitService<T> subService(int start, int count) {
		if (count <= 0) {
			throw new IllegalArgumentException();
		}
		return VisitServices.from(asList().subList(start, start + count));
	}

	@Override
	public ListVisitService<T> headService(int count) {
		return subService(0, count);
	}

	@Override
	public ListVisitService<T> tailService(int count) {
		final List<T> list = asList();
		final int toIndex = list.size(); // 5 ,3 --> 2,5
		final int startIndex = toIndex - count;
		if (startIndex <= 0) {
			throw new IllegalArgumentException("count must smaller than list.size()");
		}
		return VisitServices.from(list.subList(startIndex, toIndex));
	}

	@Override
	public ListVisitService<T> reverseService(boolean reverseOriginList) {
		if (reverseOriginList) {
			Collections.reverse(asList());
			return this;
		} else {
			final List<T> list = new ArrayList<T>(asList());
			Collections.reverse(list);
			return VisitServices.from(list);
		}
	}

	@Override
	public ListVisitService<T> reverseService() {
		return reverseService(true);
	}

	@Override
	public ListVisitService<T> shuffleService() {
		return shuffleService(true);
	}

	@Override
	public ListVisitService<T> shuffleService(boolean shuffleOriginlist) {
		if (shuffleOriginlist) {
			Collections.shuffle(asList());
			return this;
		} else {
			final List<T> list = new ArrayList<T>(asList());
			Collections.shuffle(list);
			return VisitServices.from(list);
		}
	}

	@Override
	public ListVisitService<T> sortService(Comparator<? super T> c, boolean sortOriginList) {
		if (sortOriginList) {
			Collections.sort(asList(), c);
			return this;
		} else {
			final List<T> list = new ArrayList<T>(asList());
			Collections.sort(list, c);
			return VisitServices.from(list);
		}
	}

	@Override
	public ListVisitService<T> sortService(Comparator<? super T> comparator) {
		return sortService(comparator, true);
	}

	@Override
	public ListVisitService<String> joinToStringService(ResultVisitor<T, String> stringVisitor, String joinMark,
			int everyGroupCount) {
		return joinToStringService(null, stringVisitor, joinMark, everyGroupCount);
	}

	@Override
	public ListVisitService<String> joinToStringService(@Nullable Object param, ResultVisitor<T, String> stringVisitor,
			String joinMark, int everyGroupCount) {
		if (joinMark == null || everyGroupCount <= 0) {
			throw new IllegalArgumentException();
		}
		final List<T> list = asList();
		final List<String> strs = new ArrayList<String>();
		if (mSb == null) {
			mSb = new StringBuilder();
		}
		final StringBuilder sb = mSb;

		final int size = list.size();
		for (int i = 0; i < size; i++) {
			sb.append(stringVisitor.visit(list.get(i), param));
			if ((i + 1) % everyGroupCount == 0) {
				strs.add(sb.toString());
				sb.delete(0, sb.length());
			} else {
				sb.append(joinMark);
			}
		}
		if (size % everyGroupCount != 0) {
			int length = sb.length();
			sb.delete(length - joinMark.length(), length);
			strs.add(sb.toString());
		}
		sb.delete(0, sb.length());
		return VisitServices.from(strs);
	}

	@Override
	public ListVisitService<String> joinToStringService(String joinMark, int everyGroupCount) {
		return joinToStringService(null, Visitors.<T>toStringVisitor(), joinMark, everyGroupCount);
	}

	@Override
	public String joinToString(String joinMark) {
		if(mTempStrs == null){
			mTempStrs = new ArrayList<String>();
		}
		joinToStringService(joinMark, Integer.MAX_VALUE).save(mTempStrs, true);
		return mTempStrs.size() > 0 ? mTempStrs.get(0) : "";
	}
	
	@Override
	public <K> MapVisitService<K, List<T>> groupService(ResultVisitor<T, K> keyVisitor) {
		return groupService(null, keyVisitor);
	}
	
	@Override
	public <K> MapVisitService<K, List<T>> groupService(@Nullable Object param, 
			ResultVisitor<T, K> keyVisitor) {
		return groupService(param, keyVisitor, Visitors.<T,T>unchangeResultVisitor());
	}

	@Override
	public <K, V> MapVisitService<K, List<V>> groupService(Object param, ResultVisitor<T, K> keyVisitor,
			ResultVisitor<T, V> valueVisitor) {
		checkNull(keyVisitor);
		final List<T> list = asList();
		final Map<K, List<V>> map = InternalUtil.newMap(null);
		List<V> val;
		K key;
		for(T t : list){
			key = keyVisitor.visit(t, param);
			val = map.get(key);
			if(val == null){
				val = new ArrayList<V>();
				map.put(key, val);
			}
			val.add(valueVisitor.visit(t, param));
		}
		return VisitServices.from(map);
	}
	
	@Override
	public ListVisitService<List<T>> groupService(int everyGroupCount) {
		final List<List<T>> totalList = new ArrayList<List<T>>();
		final List<T> list = asList();
		
		final int size = list.size();
		List<T> temp = new ArrayList<T>();
		
		for (int i = 0; i < size; i++) {
			temp.add(list.get(i));
			if ((i + 1) % everyGroupCount == 0) {
				totalList.add(temp);
				temp = new ArrayList<T>();
			} 
		}
		if (size % everyGroupCount != 0) {
			totalList.add(temp);
		}
		return VisitServices.from(totalList);
	}

}
