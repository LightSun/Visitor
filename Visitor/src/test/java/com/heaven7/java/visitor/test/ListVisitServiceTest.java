package com.heaven7.java.visitor.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.heaven7.java.visitor.MapSaveVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.SaveVisitor;
import com.heaven7.java.visitor.collection.CollectionVisitService;
import com.heaven7.java.visitor.collection.ListVisitService;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.java.visitor.util.Map;

public class ListVisitServiceTest extends VisitServiceTest {

	private final List<Integer> mList = new ArrayList<>();
	private ListVisitService<Integer> mListService;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		//3 length 
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

	public void testTransformByGroup(){
		CollectionVisitService<List<Integer>> service = mListService
				.transformToMapByGroup(null,  null/*new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		}*/, new ResultVisitor<Integer, Integer>() {
			@Override
			public Integer visit(Integer t, Object param) {
				return t.toString().length();
			}
		}).transformToCollectionByValues();
		/**
		 * if the comparator of calling transformToMapByGroup isn't null.
           calling asListService() is ok, or else cause unsupport Exception
		 */
		service
		//.asListService()
		.save(new SaveVisitor<List<Integer>>() {
			@Override
			public void visit(Collection<List<Integer>> collection) {
				System.out.println(collection);
			}
		});
	}
	
	public void testTransformByGroupValue(){
		CollectionVisitService<List<String>> service = mListService
				.transformToMapByGroupValue(null,  /*null*/new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		}, new ResultVisitor<Integer, Integer>() {
			@Override
			public Integer visit(Integer t, Object param) {
				return t.toString().length();
			}
		}, new ResultVisitor<Integer, String>() {
			@Override
			public String visit(Integer t, Object param) {
				return "value__" + t.toString();
			}
		}).transformToCollectionByValues();
		/**
		 * if the comparator of calling transformToMapByGroupValue() isn't null.
           calling asListService() is ok, or else cause UnsupportException
		 */
		service
		.asListService()
		.save(new SaveVisitor<List<String>>() {
			@Override
			public void visit(Collection<List<String>> collection) {
				System.out.println(collection);
			}
		});
	}
	
	public void testGroupService2(){
		//K, V = Integer,List<String>
		mListService.groupService(null, new ResultVisitor<Integer, Integer>() {
			@Override
			public Integer visit(Integer t, Object param) {
				return t.toString().length();
			}
		}, new ResultVisitor<Integer, String>() {
			@Override
			public String visit(Integer t, Object param) {
				return "value__" + t.toString();
			}
			
		}).save(new MapSaveVisitor<Integer, List<String>>() {
			@Override
			public void visit(Map<Integer, List<String>> map) {
				assertEquals(3, map.size());
				System.out.println(map.toNormalMap());
			}
		});
	}
	
	public void testGroupService1(){
		mListService.groupService(null, new ResultVisitor<Integer, String>() {
			@Override
			public String visit(Integer t, Object param) {
				return t.toString().length() + "";
			}
		}).save(new MapSaveVisitor<String, List<Integer>>() {
			@Override
			public void visit(Map<String, List<Integer>> map) {
				assertEquals(3, map.size());
				System.out.println(map.toNormalMap());
			}
		});
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
