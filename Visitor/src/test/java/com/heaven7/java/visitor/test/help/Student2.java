package com.heaven7.java.visitor.test.help;

import com.heaven7.java.visitor.collection.Updatable;

public class Student2 extends Student implements Updatable<Student2>{

	public Student2(String name) {
		super(name);
	}

	@Override
	public void updateFrom(Student2 t) {
		this.name = t.name;
		this.id = t.id;
		this.courses = t.courses;
	}


}
