package com.heaven7.java.visitor.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.heaven7.java.visitor.MapPredicateVisitor;
import com.heaven7.java.visitor.Visitors;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.MapVisitService;
import com.heaven7.java.visitor.collection.VisitServices;

import junit.framework.TestCase;

public class MapVisitServiceTest extends TestCase{
	
	final int size = 6;
	final HashMap<String, Integer> map = new HashMap<String, Integer>();
	MapVisitService<String, Integer> service;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		for( int i =0 ; i < size ; i++){
			map.put("key_" + i, i);
		}
		service = VisitServices.from(map);
	}
	
	public void testMap(){
	}
	
	public void testQuery(){
		KeyValuePair<String, Integer> pair = service.beginOperateManager()
			.delete(new MapPredicateVisitor<String, Integer>() {
				@Override
				public Boolean visit(KeyValuePair<String, Integer> pair, Object param) {
					assertEquals(param, "123");
					return pair.getValue() == 2;
				}
			})
			.end()
			.visitForQuery("123", new MapPredicateVisitor<String, Integer>() {
				@Override
				public Boolean visit(KeyValuePair<String, Integer> pair, Object param) {
					assertEquals(param, "123");
					return pair.getValue() == 5;
				}
			});
		
		assertEquals(pair.getValue().intValue(), 5);
		assertEquals(map.size(), size - 1);
	}
	
	public void testQueryList(){
		List<KeyValuePair<String, Integer>> queryResults = service.beginOperateManager()
			.delete(new MapPredicateVisitor<String, Integer>() {
				@Override
				public Boolean visit(KeyValuePair<String, Integer> pair, Object param) {
					return pair.getValue() == 2;
				}
			})
			.end()
			.visitForQueryList(Visitors.<String, Integer>trueMapPredicateVisitor(), null);
		
		assertEquals(queryResults.size(), size - 1);
		assertEquals(map.size(), size - 1); 
	}
	
	public static void main(String[] args) {
		int size = 6;
		final HashMap<String, Integer> map = new HashMap<String, Integer>();
		for( int i =0 ; i < size ; i++){
			map.put("key_" + i, i);
		}
		for(Entry<String, Integer> en : map.entrySet()){
			//update, filter , delete, trim.
			//map.remove(en.getKey()); // 通常情况下， entry后不能remove.
		}
		
		List<Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
		for(Entry<String, Integer> en : list){
			//map.remove(en.getKey());  //ok
			//en.setValue(1); //ok
		}
		/* ok
		 * List<String> list = new ArrayList<>(map.keySet());
		for(String key : list){
			map.remove(key);
		}*/
		System.out.println(map);
	}
	
}
