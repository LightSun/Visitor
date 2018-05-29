package com.heaven7.java.visitor.test;

import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.Visitors;
import com.heaven7.java.visitor.collection.CollectionVisitService;
import com.heaven7.java.visitor.collection.MapVisitService;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.java.visitor.test.help.Student;
import com.heaven7.java.visitor.test.help.Student2;

import java.util.*;

/**
 * composite test : from collection -> map -> collection ..... mutual transform
 * 
 * @author don
 *
 */
public class CompositeTest extends VisitServiceTest {

	/**
	 * CollectionVisitService<Student> ----> MapVisitService<Student,
	 * Student2>---------> MapVisitService<Student2,Student>
	 */
	public void testTransform4() {
		int size = getStudentSize();

		MapVisitService<Student2, Student> service2 = mService.beginOperateManager()
				.filter(new PredicateVisitor<Student>() {
					@Override
					public Boolean visit(Student t, Object param) {
						return t.getId() == 1;
					}
				}).end().transformToMapAsKeys(new ResultVisitor<Student, Student2>() {
					@Override
					public Student2 visit(Student t, Object param) {
						return new Student2(t);
					}
				}).transformToMapBySwap();
		/*
		 * because: the student(id = 1 ) is filtered. Not delete
		 */
		assertEquals(getSize(service2), size - 1);
	}

	/**
	 * CollectionVisitService<Student> ----> MapVisitService<Student,
	 * Student2>---------> CollectionVisitService<Student2>
	 */
	public void testTransform3() {
		int size = getStudentSize();
		// LockSupport

		CollectionVisitService<Student2> service2 = mService.beginOperateManager()
				.filter(new PredicateVisitor<Student>() {
					@Override
					public Boolean visit(Student t, Object param) {
						return t.getId() == 1;
					}
				}).end().transformToMapAsKeys(new ResultVisitor<Student, Student2>() {
					@Override
					public Student2 visit(Student t, Object param) {
						return new Student2(t);
					}
				}).transformToCollectionByValues();
		/*
		 * because: the student(id = 1 ) is filtered. Not delete
		 */
		assertEquals(getSize(service2), size - 1);
	}

	/**
	 * CollectionVisitService<Student> ----> MapVisitService<Student, Student2>
	 */
	public void testTransform2() {
		int size = getStudentSize();

		MapVisitService<Student, Student2> service2 = mService.beginOperateManager()
				.filter(new PredicateVisitor<Student>() {
					@Override
					public Boolean visit(Student t, Object param) {
						return t.getId() == 1;
					}
				}).end().transformToMapAsKeys(new ResultVisitor<Student, Student2>() {
					@Override
					public Student2 visit(Student t, Object param) {
						return new Student2(t);
					}
				});
		/*
		 * because: the student(id = 1 ) is filtered. Not delete
		 */
		assertEquals(getSize(service2), size - 1);
	}

	/**
	 * test simple transform : CollectionVisitService<Student> --------->
	 * CollectionVisitService<Student2>
	 */
	public void testTransform1() {
		int size = getStudentSize();

		CollectionVisitService<Student2> service2 = mService.beginOperateManager()
				.filter(new PredicateVisitor<Student>() {
					@Override
					public Boolean visit(Student t, Object param) {
						return t.getId() == 1;
					}
				}).end().transformToCollection(new ResultVisitor<Student, Student2>() {
					@Override
					public Student2 visit(Student t, Object param) {
						return new Student2(t);
					}
				});

		/*
		 * because: the student(id = 1 ) is filtered.
		 */
		assertEquals(getSize(service2), size - 1);
	}

	public void testTransform0() {
		final int size = 10;
		List<Integer> list = new ArrayList<Integer>();
		//add ten element
		for (int i = 0; i < size; i++) {
			list.add(i);
		}
		CollectionVisitService<String> service2 = VisitServices.from(list)
		   .beginOperateManager().filter(new PredicateVisitor<Integer>() {
				@Override
				public Boolean visit(Integer t, Object param) {
					return t == 2; //filter(skip) 2
				}
	
			}).delete(new PredicateVisitor<Integer>() {
				@Override
				public Boolean visit(Integer t, Object param) {
					return t == 5; //delete 5
				}
			}).end()
			.transformToMapAsKeys(new ResultVisitor<Integer, String>() {
				@Override
				public String visit(Integer t, Object param) {
					return "" + t * 100;
				}
			}).transformToCollectionByValues();
		
		assertEquals(getSize(service2), size - 2);
	}
	
	public void testTransform0_traditional_optimize() {
		final int size = 10;
		List<Integer> list = new ArrayList<Integer>();
		//add ten element
		for (int i = 0; i < size; i++) {
			list.add(i);
		}
		//collection -> map
		java.util.Map<Integer, String> map = new HashMap<Integer, String>();
		final ListIterator<Integer> it = list.listIterator();
		for( ;it.hasNext();){
			Integer val = it.next();
			if(val == 2){
				continue; //filter 2
			}else if(val == 5){
				it.remove(); //delete 5
				continue;
			}
			
			map.put(val, "" + val * 100);
		}
		
		//map -> collection
		Collection<String> values = map.values();
		
		assertEquals(values.size(), size - 2);
	}
	
	public void testTransform0_traditional() {
		final int size = 10;
		List<Integer> list = new ArrayList<Integer>();
		//add ten element
		for (int i = 0; i < size; i++) {
			list.add(i);
		}
		//collection -> map
		java.util.Map<Integer, String> map = new HashMap<Integer, String>();
		for(Integer val : list){
			if(val == 2){
				continue; //filter 2
			}
			map.put(val, "" + val * 100);
		}
		
		//why here use new ArrayList? that is if not, here will thrown concurrent exception.
		for(Integer val : new ArrayList<Integer>(map.keySet())){
			if(val == 5){
				map.remove(val);
			}
		}
		//map -> collection
		Collection<String> values = map.values();
		assertEquals(values.size(), size - 2);
	}

	protected <T> int getSize(CollectionVisitService<T> service) {
		return service.size();
	}

	protected <K, V> int getSize(MapVisitService<K, V> service) {
		return service.size();
	}
}
