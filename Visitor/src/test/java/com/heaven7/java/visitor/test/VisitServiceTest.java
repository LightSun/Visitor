package com.heaven7.java.visitor.test;

import static com.heaven7.java.visitor.collection.VisitService.OP_DELETE;
import static com.heaven7.java.visitor.collection.VisitService.OP_FILTER;
import static com.heaven7.java.visitor.collection.VisitService.OP_INSERT;
import static com.heaven7.java.visitor.collection.VisitService.OP_UPDATE;
import static com.heaven7.java.visitor.test.help.TestUtil.createStudent;
import static com.heaven7.java.visitor.test.help.TestUtil.createStudent2;
import static com.heaven7.java.visitor.test.help.TestUtil.syso;

import java.util.List;

import com.heaven7.java.visitor.IterateVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.Visitors;
import com.heaven7.java.visitor.collection.IterationInfo;
import com.heaven7.java.visitor.collection.VisitService;
import com.heaven7.java.visitor.test.help.Student;
import com.heaven7.java.visitor.test.help.Student2;

import junit.framework.TestCase;

/**
 * here just for list test.
 * 
 * @author don
 *
 */
public class VisitServiceTest extends TestCase {

	VisitService<Student> mService;
	List<Student> mStus;

	@Override
	protected void setUp() throws Exception {
		Student.resetId();
		mService = VisitService.from(mStus = createStudent(6));
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	protected int getStudentSize() {
		return mStus.size();
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
				.insertFinally(createStudent("new_stu"), "testComposeVisitInsert", Visitors.trueIterateVisitor()).end()
				.visitAll("testComposeVisitFilter_2");
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
		VisitService.from(list).beginOperateManager()
					.update(new Student2("new_stu"), "testComposeVisit_1", Visitors.truePredicateVisitor())
					.end()
				.visitAll("testComposeVisit_2");

		for (Student stu : list) {
			assertEquals("new_stu", stu.getName());
		}
	}

	public void testComposeVisitInsert() {
		// here 每迭代一次，加入一个元素
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
