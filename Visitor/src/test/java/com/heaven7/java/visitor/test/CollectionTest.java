package com.heaven7.java.visitor.test;

import junit.framework.TestCase;
import static com.heaven7.java.visitor.test.help.TestUtil.*;

import java.util.List;

import com.heaven7.java.visitor.collection.VisitService;
import com.heaven7.java.visitor.collection.IterationInfo;
import com.heaven7.java.visitor.collection.PredicateVisitor;
import com.heaven7.java.visitor.collection.Visitors;
import com.heaven7.java.visitor.collection.IterateVisitor;
import com.heaven7.java.visitor.test.help.Student;
import com.heaven7.java.visitor.test.help.Student2;

public class CollectionTest extends TestCase {

	VisitService<Student> mService;
	List<Student> mStus;

	@Override
	protected void setUp() throws Exception {
		mService = VisitService.from(mStus = createStudent(6));
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testComposeVisitFilter() {
		mService.filter("testComposeVisitFilter_1", new PredicateVisitor<Student>() {
			@Override
			public Boolean visit(Student t, Object param) {
				return t.getId() == 3;
			}
		}).visitAll("testComposeVisitFilter_2", new LogVisitor());
	}

	public void testComposeVisitDelete() {
		mService.delete("testCompiseVisitDelete_1", new PredicateVisitor<Student>() {
			@Override
			public Boolean visit(Student t, Object param) {
				return t.getId() == 3;
			}
		}).visitAll("testCompiseVisitDelete_2", new LogVisitor());

		assertEquals(mStus.size(), 5);
	}

	public void testComposeVisitUpdate() {
		// class Student not implements Updatable
		mService.update(createStudent("new_stu"), "testComposeVisit_1", Visitors.truePredicateVisitor())
				.visitAll("testComposeVisit_2", new LogVisitor());

		for (Student stu : mStus) {
			assertFalse("new_stu".equals(stu.getName()));
		}

		// class Student2 has implements Updatable
		List<Student2> list = createStudent2(6);
		VisitService.from(list).update(new Student2("new_stu"), "testComposeVisit_1",
				Visitors.truePredicateVisitor())
				.visitAll("testComposeVisit_2", new LogVisitor());

		for (Student stu : list) {
			assertEquals("new_stu", stu.getName());
		}
	}

	public void testComposeVisitInsert() {
		mService.insert(createStudent("new_stu"), "testComposeVisit_1", Visitors.trueIterateVisitor())
				.visitAll("testComposeVisit_2", new LogVisitor());

		assertEquals(mStus.size(), 7);
	}

	public void testVisitAll() {
		mService.visitAll("testBaseVisit1", new LogVisitor());
		assertEquals(mStus.size(), 6);
	}

	public void testVisitUtilSuccess() {
		mService.visitUntilSuccess("testBaseVisit1", new LogVisitor() {
			@Override
			public Boolean visit(Student t, Object param, IterationInfo info) {
				super.visit(t, param, info);
				return t.getId() == 5;
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
