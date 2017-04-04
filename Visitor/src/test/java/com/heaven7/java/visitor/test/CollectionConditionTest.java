package com.heaven7.java.visitor.test;

import java.util.ArrayList;
import java.util.List;

import com.heaven7.java.visitor.collection.operator.OperateConditions;
import com.heaven7.java.visitor.util.Observers;
import com.heaven7.java.visitor.util.Observers.ObserverAdapter;

import junit.framework.TestCase;

public class CollectionConditionTest extends TestCase{
	
	protected final ObserverAdapter<Integer, Boolean> mLogObserver = new 
			Observers.ObserverAdapter<Integer, Boolean>() {
		@Override
		public void onSuccess(Object param, Boolean r) {
			System.out.println("success = true");
		}
		@Override
		public void onFailed(Object param, Integer t) {
			System.out.println("success = failed");
		}
	};
	
	public void testRemove(){
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		OperateConditions.ofRemove(1, mLogObserver).apply(list);
		assertEquals(list.size(), 2);
		OperateConditions.ofRemove(5, mLogObserver).apply(list);
	}

}
