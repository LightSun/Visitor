package com.heaven7.java.visitor.test;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeMap;

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
