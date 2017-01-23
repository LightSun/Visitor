package com.heaven7.java.visitor.test;

import static com.heaven7.java.visitor.test.help.TestUtil.createStudent;
import static com.heaven7.java.visitor.test.help.TestUtil.createStudent2;
import static com.heaven7.java.visitor.test.help.TestUtil.syso;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.heaven7.java.visitor.IterateVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.Visitors;
import com.heaven7.java.visitor.collection.IterationInfo;
import com.heaven7.java.visitor.collection.VisitService;
import com.heaven7.java.visitor.test.help.Student;
import com.heaven7.java.visitor.test.help.Student2;

import junit.framework.TestCase;

/**
 * default test , eg: for Set.
 * 
 * @author heaven7
 *
 */
public class DefaultServiceTest extends TestCase {

	VisitService<Student> mService;
	Set<Student> mStus;

	@Override
	protected void setUp() throws Exception {
		Student.resetId();
		mService = VisitService.from(mStus = new HashSet<Student>(createStudent(6)));
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	protected int getStudentSize() {
		return mStus.size();
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
		}).end().visitAll("testComposeVisitFilter_2");
	}

	public void testComposeVisitDelete() {
		int size = getStudentSize();
		mService.beginOperateManager().delete("testCompiseVisitDelete_1", new PredicateVisitor<Student>() {
			@Override
			public Boolean visit(Student t, Object param) {
				return t.getId() == 3;
			}
		}).end().visitAll("testCompiseVisitDelete_2");

		assertEquals(getStudentSize(), size - 1);
	}

	public void testComposeVisitUpdate() {
		// class Student not implements Updatable
		mService.beginOperateManager()
				.update(createStudent("new_stu"), "testComposeVisit_1", Visitors.truePredicateVisitor()).end()
				.visitAll("testComposeVisit_2");

		for (Student stu : mStus) {
			// note this is diff with list
			assertFalse("new_stu".equals(stu.getName()));
		}

		// class Student2 has implements Updatable
		List<Student2> list = createStudent2(6);
		VisitService.from(list).beginOperateManager()
				.update(new Student2("new_stu"), "testComposeVisit_1", Visitors.truePredicateVisitor()).end()
				.visitAll("testComposeVisit_2");

		for (Student stu : list) {
			assertEquals("new_stu", stu.getName());
		}
	}

	public void testComposeVisitInsert() {
		// here 每迭代一次，加入一个元素
		int size = getStudentSize();
		mService.beginOperateManager()
				.insert(createStudent("new_stu"), "testComposeVisit_1", Visitors.trueIterateVisitor()).end()
				.visitAll("testComposeVisit_2");

		assertEquals(getStudentSize(), size * 2);
	}

	public void testVisitAll() {
		int size = getStudentSize();
		mService.visitAll("testBaseVisit1");
		assertEquals(getStudentSize(), size);
	}

	public static class LogVisitor implements IterateVisitor<Student> {
		@Override
		public Boolean visit(Student t, Object param, IterationInfo info) {
			syso(t + " , param = " + param + " , " + info);
			return true;
		}

	}

}
