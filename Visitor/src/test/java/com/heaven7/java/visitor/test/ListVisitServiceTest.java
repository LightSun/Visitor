package com.heaven7.java.visitor.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.heaven7.java.visitor.SaveVisitor;
import com.heaven7.java.visitor.collection.ListVisitService;
import com.heaven7.java.visitor.collection.VisitServices;

public class ListVisitServiceTest extends VisitServiceTest {

	private final List<Integer> mList = new ArrayList<>();
	private ListVisitService<Integer> mListService;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mList.clear();
		mList.add(2017);
		mList.add(3);
		mList.add(2);
		mList.add(2015);
		mList.add(5);
		mList.add(5);
		mList.add(100);
		mListService = VisitServices.from(mList);
	}
	
	public void testReverseService(){
		mListService.headService(3).reverseService(false).save(new SaveVisitor<Integer>() {
			@Override
			public void visit(Collection<Integer> collection) {
				assertEquals(3, collection.size());
				System.out.println(collection);
			}
		});
	}
	
	public void testHeadService() {
		mListService.headService(3).save(new SaveVisitor<Integer>() {
			@Override
			public void visit(Collection<Integer> collection) {
				assertEquals(3, collection.size());
				System.out.println(collection);
			}
		});
	}
	public void testTailService() {
		mListService.tailService(3).save(new SaveVisitor<Integer>() {
			@Override
			public void visit(Collection<Integer> collection) {
				assertEquals(3, collection.size());
				System.out.println(collection);
			}
		});
	}

	public void testSubService() {
		mListService.subService(1, 3).save(new SaveVisitor<Integer>() {
			@Override
			public void visit(Collection<Integer> collection) {
				assertEquals(3, collection.size());
				System.out.println(collection);
			}
		});
	}

	public void testJoinToString() {
		mListService.joinToStringService("-", Integer.MAX_VALUE).save(new SaveVisitor<String>() {
			@Override
			public void visit(Collection<String> collection) {
				System.out.println(collection);
			}
		});

		System.out.println(mListService.joinToString("sss"));
	}

}
