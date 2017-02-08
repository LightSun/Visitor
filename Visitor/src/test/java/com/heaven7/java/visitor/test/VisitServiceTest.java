package com.heaven7.java.visitor.test;

import static com.heaven7.java.visitor.collection.Operation.OP_DELETE;
import static com.heaven7.java.visitor.collection.Operation.OP_FILTER;
import static com.heaven7.java.visitor.collection.Operation.OP_INSERT;
import static com.heaven7.java.visitor.collection.Operation.OP_UPDATE;
import static com.heaven7.java.visitor.test.help.TestUtil.createStudent;
import static com.heaven7.java.visitor.test.help.TestUtil.createStudent2;
import static com.heaven7.java.visitor.test.help.TestUtil.syso;

import java.util.ArrayList;
import java.util.List;

import com.heaven7.java.visitor.IterateVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.Visitors;
import com.heaven7.java.visitor.collection.CollectionVisitService;
import com.heaven7.java.visitor.collection.IterationInfo;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.java.visitor.test.help.Student;
import com.heaven7.java.visitor.test.help.Student2;

import junit.framework.TestCase;

/**
 * here just for list test.
 * 
 * @author heaven7
 *
 */
public class VisitServiceTest extends TestCase {

	protected CollectionVisitService<Student> mService;
	protected List<Student> mStus;

	@Override
	protected void setUp() throws Exception {
		Student.resetId();
		mService = VisitServices.from(mStus = createStudent(6));
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	protected int getStudentSize() {
		return mStus.size();
	}
	
	public void testForResult(){
		
		final int size = getStudentSize();
		final List<Student2> list = new ArrayList<Student2>();
		
		mService.beginOperateManager()
		//just fiter the student of id = 1
			.filter(new PredicateVisitor<Student>() {
				@Override
				public Boolean visit(Student t, Object param) {
					assertEquals("testForResult", param);
					return t.getId() == 1;
				}
			}).end()
		.visitForResultList("testForResult", Visitors.truePredicateVisitor(), 
				new ResultVisitor<Student, Student2>() {
					@Override
					public Student2 visit(Student t, Object param) {
						assertEquals("testForResult", param);
						if( t.getId() == 2){
							return null; //null means ignored.
						}
						return new Student2(t);
					}
				}, list);
		
		assertEquals(getStudentSize(), size);
		assertEquals(list.size(), size - 2 );
	}
	
	public void testQueryWithExtraOperate(){
		Student student = mService
				.beginOperateManager()
				//delete all student
				.delete(null, new PredicateVisitor<Student>() {
					@Override
					public Boolean visit(Student t, Object param) {
						//because the parameter of delete operate is null, so use the last visit parameter
						// here is from visitForQuery(...).
						return "testQueryWithExtraOperate".equals(param);
					}
				})
				.end()
				.visitForQuery("testQueryWithExtraOperate", new PredicateVisitor<Student>() {
			@Override
			public Boolean visit(Student t, Object param) {
				if(t.id == 1 ){
					return true;
				}
				return null;
			}
		});
		//delete all .so is null
		assertEquals(student, null);
		assertEquals(0, getStudentSize());
	}
	
	//single query
	public void testQuery(){
		Student student = mService.visitForQuery("testQuery", new PredicateVisitor<Student>() {
			@Override
			public Boolean visit(Student t, Object param) {
				if(t.id == 1 && "testQuery".equals(param)){
					return true;
				}
				return null;
			}
		});
		assertEquals(student.getId(), 1);
	}
	
	/**
	 * the operate order is: 
OP_FILTER
OP_INSERT
OP_DELETE
OP_UPDATE
	 */
	public void testIterationControl3(){
		//default order delete -> filter -> update -> insert
		mService.beginIterateControl()
		.second(OP_UPDATE)   //  delete -> update -> filter -> insert
		.second(OP_FILTER)   //  delete -> filter -> update -> insert
		.second(OP_DELETE)   //  filter -> delete -> update -> insert
		.second(OP_INSERT)   //  filter -> insert -> delete -> update 
		.end();
	}
	/**
	 * the operate order is: 
OP_INSERT
OP_DELETE
OP_FILTER
OP_UPDATE
	 */
	public void testIterationControl2(){
		mService.beginIterateControl()
		.first(OP_UPDATE) // 4
		.first(OP_FILTER) //3
		.first(OP_DELETE) //2
		.first(OP_INSERT) //1
		.end();
	}
	
	/**
	 * the operate order is: 
OP_UPDATE
OP_FILTER
OP_DELETE
OP_INSERT
	 */
	public void testIterationControl(){
		mService.beginIterateControl()
		.first(OP_UPDATE)
		.second(OP_FILTER)
		.then(OP_DELETE)
		.last(OP_INSERT)
		.end();
	}
	

	public void testComposeVisitInsertFinally() {
		int size = getStudentSize();
		mService.beginOperateManager()
				.insertFinally(createStudent("new_stu"), Visitors.trueIterateVisitor()).end()
				.visitAll();
		assertEquals(getStudentSize(), size + 1);
	}

	public void testComposeVisitFilter() {
		mService.beginOperateManager().filter("testComposeVisitFilter_1", new PredicateVisitor<Student>() {
			@Override
			public Boolean visit(Student t, Object param) {
				return t.getId() == 3;
			}
		}).end()
		.visitAll("testComposeVisitFilter_2");
	}

	public void testComposeVisitDelete() {
		int size = getStudentSize();
		mService.beginOperateManager().delete("testCompiseVisitDelete_1", new PredicateVisitor<Student>() {
			@Override
			public Boolean visit(Student t, Object param) {
				return t.getId() == 3;
			}
		}).end()
		.visitAll("testCompiseVisitDelete_2");

		assertEquals(getStudentSize(), size - 1);
	}

	public void testComposeVisitUpdate() {
		// class Student not implements Updatable
		mService.beginOperateManager()
					.update(createStudent("new_stu"), "testComposeVisit_1", Visitors.truePredicateVisitor())
					.end()
				.visitAll("testComposeVisit_2");

		for (Student stu : mStus) {
			// assertFalse("new_stu".equals(stu.getName()));
			assertTrue("new_stu".equals(stu.getName())); // in list.
															// listIterator
															// support update.
		}

		// class Student2 has implements Updatable
		List<Student2> list = createStudent2(6);
		VisitServices.from(list).beginOperateManager()
					.update(new Student2("new_stu"), "testComposeVisit_1", Visitors.truePredicateVisitor())
					.end()
				.visitAll("testComposeVisit_2");

		for (Student stu : list) {
			assertEquals("new_stu", stu.getName());
		}
	}

	public void testComposeVisitInsert() {
		// here just insert
		int size = getStudentSize();
		mService.beginOperateManager()
		.insert(createStudent("new_stu"), "testComposeVisit_1", Visitors.trueIterateVisitor())
		.end()
				.visitAll("testComposeVisit_2");

		assertEquals(getStudentSize(), size * 2);
	}

	public void testVisitAll() {
		int size = getStudentSize();
		mService.visitAll("testBaseVisit1");
		assertEquals(getStudentSize(), size);
	}

	public void testVisitUtilSuccess() {
		mService.visitUntilSuccess("testBaseVisit1", new LogVisitor() {
			@Override
			public Boolean visit(Student t, Object param, IterationInfo info) {
				if (t.getId() == 5) {
					return true;
				}
				super.visit(t, param, info);
				return false;
			}
		});
	}

	public void testVisitUtilFailed() {
		mService.visitUntilFailed("testBaseVisit1", new LogVisitor() {
			@Override
			public Boolean visit(Student t, Object param, IterationInfo info) {
				super.visit(t, param, info);
				return t.getId() == 3 ? false : true;
			}
		});
	}

	public static class LogVisitor implements IterateVisitor<Student> {
		@Override
		public Boolean visit(Student t, Object param, IterationInfo info) {
			syso(t + " , param = " + param + " , " + info);
			return true;
		}

	}

}
