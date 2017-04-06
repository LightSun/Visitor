package com.heaven7.java.visitor.collection.operator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.heaven7.java.visitor.util.Throwables;
/**
 * the condition chain. that can run many groups of {@linkplain CollectionCondition}.
 * and we can make some conditions run 'in-orders' or 'in-groups'.
 * <ul>
 * <li> 'in-orders' means only previous condition run success. the after can run. false otherwise.
 * <li> 'in-groups' means the all conditions in a group must be run.whenever the previous success or not.
 * </ul>
 * @author heaven7
 * @param <T> the element type
 * @since 2.0.0
 */
public class ConditionChain<T> implements CollectionCondition<T> {

	private final Map<CollectionCondition<T>, GroupCollectionCondition> mMap;
	private final ArrayList<GroupCollectionCondition> mGroups; //in order 
	private GroupCollectionCondition mCurrent;

	public ConditionChain() {
		this.mMap = new HashMap<CollectionCondition<T>, GroupCollectionCondition>();
		this.mGroups = new ArrayList<GroupCollectionCondition>();
		this.mCurrent = new GroupCollectionCondition();
		mGroups.add(mCurrent);
	}

	public ConditionChain<T> anchor(CollectionCondition<T> anchor) {
		Throwables.checkNull(anchor);
		GroupCollectionCondition group = mMap.get(anchor);
		if (group == null) {
			group = new GroupCollectionCondition();
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
	
	@SuppressWarnings("unchecked")
	public ConditionChain<T> with(CollectionCondition<T> cc1, CollectionCondition<T> cc2, 
			CollectionCondition<T> cc3) {
		return with(new CollectionCondition[]{cc1,cc2,cc3});
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
	
	@SuppressWarnings("unchecked")
	public ConditionChain<T> after(CollectionCondition<T> cc1, CollectionCondition<T> cc2 ,
			CollectionCondition<T> cc3) {
		return after(new CollectionCondition[] { cc1, cc2 , cc3});
	}

	public ConditionChain<T> after(CollectionCondition<T>[] ccs) {
		Throwables.checkEmpty(ccs);
		GroupCollectionCondition next = getNext();
		if (next == null) {
			next = new GroupCollectionCondition();
			mGroups.add(next);
		}
		next.addAll(ccs);
		// anchor current to next
		this.mCurrent = next;
		return this;
	}
	@SuppressWarnings("unchecked")
	public ConditionChain<T> before(CollectionCondition<T> c1){
		return before(new CollectionCondition[]{c1});
	}
	@SuppressWarnings("unchecked")
	public ConditionChain<T> before(CollectionCondition<T> c1, CollectionCondition<T> c2){
		return before(new CollectionCondition[]{c1, c2, });
	}
	
	@SuppressWarnings("unchecked")
	public ConditionChain<T> before(CollectionCondition<T> c1, CollectionCondition<T> c2, 
			CollectionCondition<T> c3){
		return before(new CollectionCondition[]{c1, c2, c3});
	}
	
	public ConditionChain<T> before(CollectionCondition<T>[] ccs) {
		Throwables.checkEmpty(ccs);
		GroupCollectionCondition pre = getBefore();
		if (pre == null) {
			pre = new GroupCollectionCondition();
			mGroups.add(0, pre);
		}
		pre.addAll(ccs);
		// anchor current to next
		this.mCurrent = pre;
		return this;
	}

	private GroupCollectionCondition getNext() {
		int nextIndex = -1;
		for (int size = mGroups.size(), i = size - 1; i >= 0; i--) {
			GroupCollectionCondition condition = mGroups.get(i);
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
	
	private GroupCollectionCondition getBefore() {
		int preIndex = -1;
		for (int size = mGroups.size(), i = size - 1; i >= 0; i--) {
			GroupCollectionCondition condition = mGroups.get(i);
			if (mCurrent == condition) {
				// have next ?
				if (i >= 1) {
					preIndex = i - 1;
				}
				break;
			}
		}
		return preIndex != -1 ? mGroups.get(preIndex) : null;
	}


	@Override
	public boolean apply(Collection<T> src) {
		for (GroupCollectionCondition gcc : mGroups) {
			if (!gcc.apply(src)) {
				return false;
			}
		}
		return true;
	}

	private class GroupCollectionCondition implements CollectionCondition<T> {

		final List<CollectionCondition<T>> mConditions = new ArrayList<CollectionCondition<T>>();

		public GroupCollectionCondition add(CollectionCondition<T> cc) {
			Throwables.checkNull(cc);
			mConditions.add(cc);
			mMap.put(cc, this);
			return this;
		}

		public GroupCollectionCondition addAll(CollectionCondition<T>[] cs) {
			Throwables.checkEmpty(cs);
			for (CollectionCondition<T> c : cs) {
				mConditions.add(c);
				mMap.put(c, this);
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
