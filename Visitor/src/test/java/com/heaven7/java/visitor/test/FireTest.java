package com.heaven7.java.visitor.test;

import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.ThrowableVisitor;
import com.heaven7.java.visitor.test.help.Student;

public class FireTest extends VisitServiceTest{

	public void testFire(){
		mService.fire(new FireVisitor<Student>() {
			
			@Override
			public Void visit(Student t, Object param) {
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
