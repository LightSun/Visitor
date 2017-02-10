package com.heaven7.java.visitor.test;

import java.util.concurrent.locks.LockSupport;

import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.Visitors;
import com.heaven7.java.visitor.collection.CollectionVisitService;
import com.heaven7.java.visitor.collection.MapVisitService;
import com.heaven7.java.visitor.test.help.Student;
import com.heaven7.java.visitor.test.help.Student2;

/**
 * composite test : from collection -> map -> collection ..... mutual transform
 * @author don
 *
 */
public class CompositeTest extends VisitServiceTest {
	
	
	/**
	 * CollectionVisitService<Student>  ----> MapVisitService<Student, Student2>---------> MapVisitService<Student2,Student>
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
	 * CollectionVisitService<Student>  ----> MapVisitService<Student, Student2>---------> CollectionVisitService<Student2>
	 */
	public void testTransform3() {
		int size = getStudentSize();
		//LockSupport

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
	 * CollectionVisitService<Student>  ----> MapVisitService<Student, Student2>
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
     * test simple transform :
     * CollectionVisitService<Student> ---------> CollectionVisitService<Student2>
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
	
	protected <T> int getSize(CollectionVisitService<T> service){
		return service.visitForQueryList(Visitors.truePredicateVisitor(), null).size();
	}
	protected <K,V> int getSize(MapVisitService<K,V> service){
		return service.visitForQueryList(Visitors.trueMapPredicateVisitor(), null).size();
	}
}
