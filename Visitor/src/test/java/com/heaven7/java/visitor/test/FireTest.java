package com.heaven7.java.visitor.test;

import com.heaven7.java.visitor.FireMultiVisitor;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.ThrowableVisitor;
import com.heaven7.java.visitor.collection.ListVisitService;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.java.visitor.test.help.Student;

import java.util.List;

import static com.heaven7.java.visitor.test.help.TestUtil.createStudent;

public class FireTest extends VisitServiceTest{
	

	public void testFireMulti(){
		VisitServices.from(mStus = createStudent(8)).fireMulti(3, 3,
				"hello", new FireMultiVisitor<Student>() {
			@Override
			public void visit(Object param, List<Student> students) {
				System.out.println(students);
			}
		});
	}

	public void testFire2(){
		mService.fire("testFire2", new FireVisitor<Student>() {
			
			@Override
			public Boolean visit(Student t, Object param) {
				assertEquals("testFire2", param);
				System.out.println(t);
				return true;
			}
		}, null);
	}
	

	public void testFire(){
		mService.fire(new FireVisitor<Student>() {
			
			@Override
			public Boolean visit(Student t, Object param) {
				throw new IllegalStateException();
			}
		}, new ThrowableVisitor() {
			
			@Override
			public Void visit(Throwable t) {
				assertTrue(t instanceof IllegalStateException);
				return null;
			}
		});
	}
}
