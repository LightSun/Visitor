package com.heaven7.java.visitor.test.help;

import java.util.ArrayList;
import java.util.List;

public class Student {

	private static int sId;

	public String name;
	public int id;
	public List<Course> courses;

	public Student(String name) {
		this(name, null);
	}

	public Student(String name, List<Course> courses) {
		super();
		id = ++this.sId;
		this.name = name;
		this.courses = courses != null ? courses : genterateTestCourse();
	}
	
	public void cpy(Student  stu){
		this.id = stu.id;
		this.name = stu.name;
		this.courses = stu.courses;
	}
	
	public static void resetId(){
		sId = 0;
	}

	protected List<Course> genterateTestCourse() {
		List<Course> courses = new ArrayList<>();
		courses.add(new Course(name + "__Course1"));
		courses.add(new Course(name + "__Course2"));
		courses.add(new Course(name + "__Course3"));
		return courses;
	}

	public List<Course> getCoursesList() {
		return courses;
	}
	
	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "Student [name=" + name + ", id=" + id + "]";
	}

	public static class Course {
		public String name;

		public Course(String name) {
			super();
			this.name = name;
		}

		@Override
		public String toString() {
			return "Course [name=" + name + "]";
		}

	}


}
