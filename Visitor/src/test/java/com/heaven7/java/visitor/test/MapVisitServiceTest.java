package com.heaven7.java.visitor.test;

import java.util.HashMap;
import java.util.Map.Entry;

import junit.framework.TestCase;

public class MapVisitServiceTest extends TestCase{
	
	/*final int size = 6;
	final HashMap<String, Integer> map = new HashMap<String, Integer>();
	VisitService<KeyValuePair<String, Integer>> service;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		for( int i =0 ; i < size ; i++){
			map.put("key_" + i, i);
		}
		service = VisitService.from(map);
	}
	
	public void testMap(){
	}
	
	public void test1(){
		List<KeyValuePair<String, Integer>> queryResults = service.beginOperateManager()
			.delete(new PredicateVisitor<KeyValuePair<String, Integer>>() {
				@Override
				public Boolean visit(KeyValuePair<String, Integer> t, Object param) {
					return t.getValue() == 2;
				}
			})
			.end()
			.visitForQueryList(Visitors.<KeyValuePair<String, Integer>>truePredicateVisitor(), null);
		
		assertEquals(queryResults.size(), size - 1);
		assertEquals(map.size(), size - 1); //false
	}*/
	
	public static void main(String[] args) {
		int size = 6;
		final HashMap<String, Integer> map = new HashMap<String, Integer>();
		for( int i =0 ; i < size ; i++){
			map.put("key_" + i, i);
		}
		for(Entry<String, Integer> en : map.entrySet()){
			//update, filter , delete, trim.
		}
	}
	
}
