package com.heaven7.java.visitor.test;

import java.util.ArrayList;
import java.util.List;

import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.ObservableCollectionService;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.java.visitor.collection.operator.ConditionChain;
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
	
	public void testFire(){
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		ObservableCollectionService<Integer> service = VisitServices.from(list).observableService();
		service.fire("testFire", new ResultVisitor<Integer, Boolean>() {
			@Override
			public Boolean visit(Integer t, Object param) {
				return t != 2;
			}
		}, mLogObserver);
	}
	
	public void testConditionChain(){
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		final int size = list.size();
		//if ! contains 1 add 101
		ConditionChain<Integer> cc = new ConditionChain<Integer>()
				.with(OperateConditions.ofContainsReverse(1, mLogObserver))
				.after(OperateConditions.ofAdd(101, mLogObserver))
		        ;
		cc.apply(list);
		assertEquals(size, list.size());
		assertTrue(!list.contains(101));
		//if contains 1 add 101
		cc = new ConditionChain<Integer>()
				.with(OperateConditions.ofContains(1, mLogObserver))
				.after(OperateConditions.ofAdd(101, mLogObserver))
		        ;
		cc.apply(list);
		assertEquals(size + 1, list.size());
		assertTrue(list.contains(101));
		//revert list, so remove 101
		list.remove(list.indexOf(101));
		
		cc = new ConditionChain<Integer>()
				.with(OperateConditions.ofContainsReverse(1, mLogObserver))
				.with(OperateConditions.ofRemove(2, mLogObserver))
				.after(OperateConditions.ofAdd(101, mLogObserver))
		        ;
		cc.apply(list);
		assertEquals(size - 1, list.size());
	}
	
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
