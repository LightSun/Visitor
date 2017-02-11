package com.heaven7.java.visitor.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.heaven7.java.visitor.MapPredicateVisitor;
import com.heaven7.java.visitor.MapResultVisitor;
import com.heaven7.java.visitor.MapSaveVisitor;
import com.heaven7.java.visitor.MapTrimVisitor;
import com.heaven7.java.visitor.Visitors;
import com.heaven7.java.visitor.collection.IterationInfo;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.MapVisitService;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.java.visitor.test.help.Student;
import com.heaven7.java.visitor.util.Map;

import junit.framework.TestCase;

public class MapVisitServiceTest extends TestCase {

	static final int size = 6;
	final HashMap<String, Integer> map = new HashMap<String, Integer>();
	MapVisitService<String, Integer> service;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		for (int i = 0; i < size; i++) {
			map.put("key_" + i, i);
		}
		service = VisitServices.from(map);
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
				assertEquals(o.size(), size - 1);
				
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

		assertEquals(results.size(), size - 1);
		assertEquals(map.size(), size - 1);
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
		assertEquals(map.size(), size - 1);
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
		assertEquals(map.size(), size - 1);
	}

	public void testQueryList() {
		List<KeyValuePair<String, Integer>> queryResults = service.beginOperateManager()
				.delete(new MapPredicateVisitor<String, Integer>() {
					@Override
					public Boolean visit(KeyValuePair<String, Integer> pair, Object param) {
						return pair.getValue() == 2;
					}
				}).end().visitForQueryList(Visitors.<String, Integer>trueMapPredicateVisitor(), null);

		assertEquals(queryResults.size(), size - 1);
		assertEquals(map.size(), size - 1);
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
