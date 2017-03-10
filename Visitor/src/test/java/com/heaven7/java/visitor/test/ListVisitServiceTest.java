package com.heaven7.java.visitor.test;

import com.heaven7.java.visitor.*;
import com.heaven7.java.visitor.collection.CollectionVisitService;
import com.heaven7.java.visitor.collection.ListVisitService;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.java.visitor.util.Map;
import com.heaven7.java.visitor.util.Observer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

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
	
	public void testZipResultThrowable(){
		final String str = "testZipResultThrowable";
		final int target = 2015;
		mListService.zipResult(str,
				new ResultVisitor<Integer, String>() {
			@Override
			public String visit(Integer t, Object param) {
				assertEquals(str, param);
				if(t == target){
					throw new RuntimeException();
				}
				return t.toString();
			}
		},new Observer<Integer, List<String>>() {
			@Override
			public void onSuccess(Object param, List<String> r) {
			}
			@Override
			public void onFailed(Object param, Integer t) {
			}
			@Override
			public void onThrowable(Object param, Integer t, Throwable e) {
				System.out.println(param);
				assertEquals(str, param);
				assertEquals(target, t.intValue());
				assertTrue(e instanceof RuntimeException);
			}
		});
	}
	
	public void testZipResultFailed(){
		final String str = "testZipResultFailed";
		final int target = 2015;
		mListService.zipResult(str,
				new ResultVisitor<Integer, String>() {
			@Override
			public String visit(Integer t, Object param) {
				assertEquals(str, param);
				return t == target ? null : t.toString();
			}
		},new Observer<Integer, List<String>>() {
			@Override
			public void onSuccess(Object param, List<String> r) {
			}
			@Override
			public void onFailed(Object param, Integer t) {
				System.out.println(param);
				assertEquals(str, param);
				assertEquals(target, t.intValue());
				
			}
			@Override
			public void onThrowable(Object param, Integer t, Throwable e) {
				
			}
		});
	}
	
	public void testZipResultSuccess(){
		final String str = "testZipResultSuccess";
		mListService.zipResult(str, 
				new ResultVisitor<Integer, String>() {
			@Override
			public String visit(Integer t, Object param) {
				assertEquals(str, param);
				return t.toString();
			}
		},new Observer<Integer, List<String>>() {
			@Override
			public void onSuccess(Object param, List<String> r) {
				assertEquals(str, param);
				System.out.println(r);
			}
			@Override
			public void onFailed(Object param, Integer t) {
				
			}
			@Override
			public void onThrowable(Object param, Integer t, Throwable e) {
				
			}
		});
	}
	
	public void testZipThrowable(){
		final String str = "testZipThrowable";
		final int target = 2015;
		mListService.zip(str, new PredicateVisitor<Integer>() {
			@Override
			public Boolean visit(Integer t, Object param) {
				assertEquals(str, param);
				if(t == target){
					throw new RuntimeException();
				}
				return true;
			}
		}, new Observer<Integer,Void>() {
			@Override
			public void onSuccess(Object param, Void r) {
			}
			@Override
			public void onFailed(Object param, Integer t) {
			}
			@Override
			public void onThrowable(Object param, Integer t, Throwable e) {
				System.out.println(param);
				assertEquals(str, param);
				assertEquals(target, t.intValue());
				assertTrue(RuntimeException.class.isAssignableFrom(e.getClass()));
			}
		});
	}
	
	public void testZipFailed(){
		final String str = "testZipFailed";
		final int target = 2015;
		mListService.zip(str, new PredicateVisitor<Integer>() {
			@Override
			public Boolean visit(Integer t, Object param) {
				assertEquals(str, param);
				return t != target;
			}
		}, new Observer<Integer, Void>() {
			@Override
			public void onSuccess(Object param, Void r) {
			}
			@Override
			public void onFailed(Object param, Integer t) {
				System.out.println(param);
				assertEquals(str, param);
				assertEquals(target, t.intValue());
			}
			@Override
			public void onThrowable(Object param, Integer t, Throwable e) {
				
			}
		});
	}
	
	public void testZipSuccess(){
		final String str = "testZipSuccess";
		mListService.zip(str, new PredicateVisitor<Integer>() {
			@Override
			public Boolean visit(Integer t, Object param) {
				assertEquals(str, param);
				return true;
			}
		}, new Observer<Integer,Void>() {
			@Override
			public void onSuccess(Object param, Void r) {
				assertEquals(str, param);
			}
			@Override
			public void onFailed(Object param, Integer t) {
				
			}
			@Override
			public void onThrowable(Object param, Integer t, Throwable e) {
				
			}
		});
	}
	
	public void testSubserviceFromCollection(){
		int size = mListService.subService(new PredicateVisitor<Integer>() {
			@Override
			public Boolean visit(Integer t, Object param) {
				return t.toString().length() == 1;
			}
		}).size();
		assertEquals(4, size);
		
		 mListService.subService(new PredicateVisitor<Integer>() {
				@Override
				public Boolean visit(Integer t, Object param) {
					return t.toString().length() == 1;
				}
			}).asListService().headService(2).fire(new FireVisitor<Integer>() {
				@Override
				public Boolean visit(Integer t, Object param) {
					System.out.println("fire: " + t);
					return null;
				}
			});
	}
	
	public void testGroupService3(){
		int groupSize = 3;
		mListService.groupService(groupSize).save(new SaveVisitor<List<Integer>>() {
			@Override
			public void visit(Collection<List<Integer>> collection) {
				System.out.println(collection);
				assertEquals(mList.size() / groupSize + 1, collection.size());
			}
		});
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
