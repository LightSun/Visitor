package com.heaven7.java.visitor.test.help;

import java.util.ArrayList;
import java.util.List;

public class TestUtil{
	
	/**
	 * default count 3
	 */
	public static List<Student> createStudent(){
		return createStudent(3);
	}
	public static Student createStudent(String name){
		return new Student(name);
	}
	
	public static List<Student> createStudent(int count){
		return createStudent("heaven7", count);
	}
	
	public static List<Student> createStudent(String prefix, int count){
		List<Student> list = new ArrayList<Student>();
		for(int i = 0  ;i<count ; i++){
			list.add(new Student(prefix + "__" + i));
		}
		return list;
	}
	
	public static List<Student2> createStudent2(int count){
		return createStudent2("heaven7", count);
	}
	
	public static List<Student2> createStudent2(String prefix , int count){
		List<Student2> list = new ArrayList<Student2>();
		for(int i = 0  ;i<count ; i++){
			list.add(new Student2(prefix + "__" + i));
		}
		return list;
	}
	
	public static void syso(Object msg){
		System.out.println(msg);
	}
	
	public static void syso(String msg, boolean debug){
		if(debug){
		     System.out.println(msg);
		}
	}
}
