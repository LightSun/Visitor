package com.heaven7.java.visitor.test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.heaven7.java.visitor.MapSaveVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.SaveVisitor;
import com.heaven7.java.visitor.test.help.Student;
import com.heaven7.java.visitor.util.Map;

public class OrderTest extends VisitServiceTest{
	
	//as in TreeMap key must implement Comparable/Comparator.
	public void testOrder2(){
		System.out.println(mStus);
		mService.transformToMapAsKeys(new ResultVisitor<Student, String>() {
			@Override
			public String visit(Student t, Object param) {
				return t.getName();
			}
		}).save(new MapSaveVisitor<Student, String>() {
			@Override
			public void visit(Map<Student, String> o) {
				System.out.println(o);
			}
		});
	}
	
	public void testOrder(){
		System.out.println(mStus);
		mService.transformToCollection(new ResultVisitor<Student, String>() {

			@Override
			public String visit(Student t, Object param) {
				return t.getName();
			}
		}).save(new SaveVisitor<String>() {
			@Override
			public void visit(Collection<String> o) {
				System.out.println(o);
			}
		});
	}
}
