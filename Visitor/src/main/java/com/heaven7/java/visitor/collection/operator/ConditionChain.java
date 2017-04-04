package com.heaven7.java.visitor.collection.operator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.heaven7.java.visitor.util.Throwables;

public class ConditionChain<T> implements CollectionCondition<T> {

	private final Map<CollectionCondition<T>, GroupCollectionCondition<T>> mMap;
	private final ArrayList<GroupCollectionCondition<T>> mGroups; //in order 
	private GroupCollectionCondition<T> mCurrent;

	public ConditionChain() {
		this.mMap = new HashMap<CollectionCondition<T>, GroupCollectionCondition<T>>();
		this.mGroups = new ArrayList<GroupCollectionCondition<T>>();
		this.mCurrent = new GroupCollectionCondition<T>();
		mGroups.add(mCurrent);
	}

	public ConditionChain<T> anchor(CollectionCondition<T> anchor) {
		Throwables.checkNull(anchor);
		GroupCollectionCondition<T> group = mMap.get(anchor);
		if (group == null) {
			group = new GroupCollectionCondition<T>();
			mMap.put(anchor, group);
		}
		this.mCurrent = group;
		return this;
	}

	public ConditionChain<T> with(CollectionCondition<T> cc) {
		mCurrent.add(cc);
		return this;
	}

	public ConditionChain<T> with(CollectionCondition<T> cc1, CollectionCondition<T> cc2) {
		mCurrent.add(cc1);
		mCurrent.add(cc2);
		return this;
	}

	public ConditionChain<T> with(CollectionCondition<T>[] cs) {
		mCurrent.addAll(cs);
		return this;
	}

	@SuppressWarnings("unchecked")
	public ConditionChain<T> after(CollectionCondition<T> cc) {
		return after(new CollectionCondition[] { cc });
	}

	@SuppressWarnings("unchecked")
	public ConditionChain<T> after(CollectionCondition<T> cc1, CollectionCondition<T> cc2) {
		return after(new CollectionCondition[] { cc1, cc2 });
	}

	public ConditionChain<T> after(CollectionCondition<T>[] ccs) {
		Throwables.checkEmpty(ccs);
		GroupCollectionCondition<T> next = getNext();
		if (next == null) {
			next = new GroupCollectionCondition<T>();
			mGroups.add(next);
		}
		next.addAll(ccs);
		// anchor current to next
		this.mCurrent = next;
		return this;
	}

	private GroupCollectionCondition<T> getNext() {
		int nextIndex = -1;
		for (int size = mGroups.size(), i = size - 1; i >= 0; i--) {
			GroupCollectionCondition<T> condition = mGroups.get(i);
			if (mCurrent == condition) {
				// have next ?
				if (i < size - 1) {
					nextIndex = i + 1;
				}
				break;
			}
		}
		return nextIndex != -1 ? mGroups.get(nextIndex) : null;
	}

	@Override
	public boolean apply(Collection<T> src) {
		for (GroupCollectionCondition<T> gcc : mGroups) {
			if (!gcc.apply(src)) {
				return false;
			}
		}
		return true;
	}

	private static class GroupCollectionCondition<T> implements CollectionCondition<T> {

		private final List<CollectionCondition<T>> mConditions = new ArrayList<CollectionCondition<T>>();

		public GroupCollectionCondition<T> add(CollectionCondition<T> cc) {
			Throwables.checkNull(cc);
			mConditions.add(cc);
			return this;
		}

		public GroupCollectionCondition<T> addAll(List<CollectionCondition<T>> cs) {
			Throwables.checkEmpty(cs);
			mConditions.addAll(cs);
			return this;
		}

		public GroupCollectionCondition<T> addAll(CollectionCondition<T>[] cs) {
			Throwables.checkEmpty(cs);
			for (CollectionCondition<T> c : cs) {
				mConditions.add(c);
			}
			return this;
		}

		@Override
		public boolean apply(Collection<T> src) {
			boolean result = true;
			for (CollectionCondition<T> c : mConditions) {
				result &= c.apply(src);
			}
			return result;
		}

	}

}
