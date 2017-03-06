package com.heaven7.java.visitor.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.heaven7.java.visitor.MapFireBatchVisitor;
import com.heaven7.java.visitor.MapFireVisitor;
import com.heaven7.java.visitor.MapPredicateVisitor;
import com.heaven7.java.visitor.MapResultVisitor;
import com.heaven7.java.visitor.MapSaveVisitor;
import com.heaven7.java.visitor.MapTrimVisitor;
import com.heaven7.java.visitor.SaveVisitor;
import com.heaven7.java.visitor.ThrowableVisitor;
import com.heaven7.java.visitor.Visitors;
import com.heaven7.java.visitor.collection.IterationInfo;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.MapVisitService;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.java.visitor.test.help.Student;
import com.heaven7.java.visitor.util.Map;

import junit.framework.TestCase;

public class MapVisitServiceTest extends TestCase {

	static final int SIZE = 6;
	final HashMap<String, Integer> map = new HashMap<String, Integer>();
	MapVisitService<String, Integer> service;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// 0 - 5
		for (int i = 0; i < SIZE; i++) {
			map.put("key_" + i, i);
		}
		service = VisitServices.from(map);
	}
	
	public void testSubService(){
		final String str = "testSubService";
		MapVisitService<String, Integer> service2 = service.subService(str, new MapPredicateVisitor<String, Integer>() {
			@Override
			public Boolean visit(KeyValuePair<String, Integer> pair, Object param) {
				assertEquals(str, param);
				return pair.getValue() <= 3;
			}
		}, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		assertEquals(service2.size(), 4); //0,1,2,3
		service2.save(new MapSaveVisitor<String, Integer>() {
			@Override
			public void visit(Map<String, Integer> map) {
				System.out.println(map.toString());
			}
		});
	}
	
	public void testTransformToCollection2(){
		//service.transformToCollection2().asListService(); //must cause exception
		int size = service.transformToCollectionByPairs(new Comparator<KeyValuePair<String,Integer>>() {
			@Override
			public int compare(KeyValuePair<String, Integer> o1, KeyValuePair<String, Integer> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		}).asListService().save(new SaveVisitor<KeyValuePair<String,Integer>>() {
			@Override
			public void visit(Collection<KeyValuePair<String, Integer>> collection) {
				System.out.println(collection);
			}
		}).size();
		assertEquals(SIZE, size);
	}
	
	public void testFire2(){
		 service.beginOperateManager()
			.delete(new MapPredicateVisitor<String, Integer>() {
				@Override
				public Boolean visit(KeyValuePair<String, Integer> pair, Object param) {
					return pair.getValue() == 2;
				}
	       }).end().fireBatch(new MapFireBatchVisitor<String, Integer>() {
			@Override
			public Void visit(Collection<KeyValuePair<String, Integer>> collection, Object param) {
				assertEquals(collection.size(), SIZE - 1);
				return null;
			}
		});
	}
	
	public void testFire(){
		 service.beginOperateManager()
			.delete(new MapPredicateVisitor<String, Integer>() {
				@Override
				public Boolean visit(KeyValuePair<String, Integer> pair, Object param) {
					return pair.getValue() == 2;
				}
	       }).end().fire(new MapFireVisitor<String, Integer>() {
			
			@Override
			public Boolean visit(KeyValuePair<String, Integer> pair, Object param) {
				throw new IllegalArgumentException("");
			}
		}, new ThrowableVisitor() {
			@Override
			public Void visit(Throwable t) {
				assertTrue( t instanceof IllegalArgumentException);
				return null;
			}
		});
	}
	
	public void testSave(){
		 service.beginOperateManager()
			.delete(new MapPredicateVisitor<String, Integer>() {
				@Override
				public Boolean visit(KeyValuePair<String, Integer> pair, Object param) {
					return pair.getValue() == 2;
				}
	       }).end().save(new MapSaveVisitor<String, Integer>() {
			@Override
			public void visit(Map<String, Integer> o) {
				assertEquals(o.size(), SIZE - 1);
				
				/**
				 * must cause exception, this map is read-only map, can' be modify.
				 */
				try { 
					o.clear();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void testTrim(){
		service.beginOperateManager().trim(new MapTrimVisitor<String, Integer>() {
			@Override
			public Boolean visit(Map<String, Integer> t, Object param, IterationInfo info) {
				// trim impl in here. but for test we just clear map.
				t.clear();
				return null;
			}
		}).end().save(new MapSaveVisitor<String, Integer>() {
			@Override
			public void visit(Map<String, Integer> o) {
				assertEquals(o.size(), 0);
			}
		});
	}

	public void testResultList() {
		List<Student> results = service.beginOperateManager()
				.delete(new MapPredicateVisitor<String, Integer>() {
					@Override
					public Boolean visit(KeyValuePair<String, Integer> pair, Object param) {
						return pair.getValue() == 2;
					}
		       }).end()
				.visitForResultList(Visitors.<String, Integer>trueMapPredicateVisitor(),
					new MapResultVisitor<String, Integer, Student>() {
						@Override
						public Student visit(KeyValuePair<String, Integer> t, Object param) {
							return new Student(t.getKey());
						}
					}, null);

		assertEquals(results.size(), SIZE - 1);
		assertEquals(map.size(), SIZE - 1);
	}

	public void testResult() {
		String result = service.beginOperateManager().delete(new MapPredicateVisitor<String, Integer>() {
			@Override
			public Boolean visit(KeyValuePair<String, Integer> pair, Object param) {
				return pair.getValue() == 2;
			}
		}).end().visitForResult("123", new MapPredicateVisitor<String, Integer>() {
			@Override
			public Boolean visit(KeyValuePair<String, Integer> pair, Object param) {
				return pair.getValue() == 5;
			}
		}, new MapResultVisitor<String, Integer, String>() {
			@Override
			public String visit(KeyValuePair<String, Integer> t, Object param) {
				assertEquals(param, "123");
				return t.getValue() + "___" + t.getValue() * 100;
			}
		});

		assertEquals(result, "5___500");
		assertEquals(map.size(), SIZE - 1);
	}

	public void testQuery() {
		KeyValuePair<String, Integer> pair = service.beginOperateManager()
				.delete(new MapPredicateVisitor<String, Integer>() {
					@Override
					public Boolean visit(KeyValuePair<String, Integer> pair, Object param) {
						assertEquals(param, "123");
						return pair.getValue() == 2;
					}
				}).end().visitForQuery("123", new MapPredicateVisitor<String, Integer>() {
					@Override
					public Boolean visit(KeyValuePair<String, Integer> pair, Object param) {
						assertEquals(param, "123");
						return pair.getValue() == 5;
					}
				});

		assertEquals(pair.getValue().intValue(), 5);
		assertEquals(map.size(), SIZE - 1);
	}

	public void testQueryList() {
		List<KeyValuePair<String, Integer>> queryResults = service.beginOperateManager()
				.delete(new MapPredicateVisitor<String, Integer>() {
					@Override
					public Boolean visit(KeyValuePair<String, Integer> pair, Object param) {
						return pair.getValue() == 2;
					}
				}).end().visitForQueryList(Visitors.<String, Integer>trueMapPredicateVisitor(), null);

		assertEquals(queryResults.size(), SIZE - 1);
		assertEquals(map.size(), SIZE - 1);
	}

	public static void main(String[] args) {
		int size = 6;
		final HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < size; i++) {
			map.put("key_" + i, i);
		}
		for (Entry<String, Integer> en : map.entrySet()) {
			// update, filter , delete, trim.
			// map.remove(en.getKey()); // in common， after entry can't remove.
		}

		List<Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
		for (Entry<String, Integer> en : list) {
			// map.remove(en.getKey()); //ok
			// en.setValue(1); //ok
		}
		/*
		 * ok List<String> list = new ArrayList<>(map.keySet()); for(String key
		 * : list){ map.remove(key); }
		 */
		System.out.println(map);
	}

}
