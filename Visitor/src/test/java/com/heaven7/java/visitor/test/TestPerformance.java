package com.heaven7.java.visitor.test;

import static com.heaven7.java.visitor.test.help.TestUtil.createStudent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.Visitors;
import com.heaven7.java.visitor.collection.CollectionVisitService;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.java.visitor.test.help.Student;
import com.heaven7.java.visitor.test.help.Student2;
import com.heaven7.java.visitor.test.help.TestUtil;
import com.heaven7.java.visitor.test.help.TimeRecorder;

public class TestPerformance{
	
	protected final TimeRecorder mTr = TimeRecorder.create();;
	private CollectionVisitService<Student> mService;
	private List<Student> mStus;
	
	public TestPerformance() {
		super();
	}
	
	public static void main(String[] args) {
		final int count = 10000;
		final TestPerformance tp = new TestPerformance();
		//在for 循环内外. 调用花费的时间不同？
		//tp.testVisitResult();      // 8, 9, 13
		tp.testTraditionalResult();  // 0,1, 2
		for(int i= 0  ; i < count; i++){
			//tp.testVisitDelete();         //基本是0
			//tp.testTraditionalDelete();   //基本是0
			//tp.testVisitResult();         //基本是0
		}
		
		
	}
	
	private void resetService(){
		mService = VisitServices.from(mStus = createStudent(6));
	}
	private int size(){
		return mStus.size();
	}
	
	public void testVisitResult(){
		resetService();
        final List<Student2> list = new ArrayList<Student2>();
		
        mTr.begin();
		mService.beginOperateManager()
		//just fiter the student of id = 1
			.filter(new PredicateVisitor<Student>() {
				@Override
				public Boolean visit(Student t, Object param) {
					return t.getId() == 1;
				}
			}).end()
		.visitForResultList(Visitors.truePredicateVisitor(), 
				new ResultVisitor<Student, Student2>() {
					@Override
					public Student2 visit(Student t, Object param) {
						if( t.getId() == 2){
							return null; //null means ignored.
						}
						return new Student2(t);
					}
				}, list);
		long cost = mTr.endAndGetCost();
    	TestUtil.syso("cost = " + cost + " , called by # testVisitResult(). + current size = " + size());
	}

    public void testVisitDelete() {
    	Student.resetId();
    	//mTr.begin();
    	
    	resetService();
        mTr.begin();
    	mService.beginOperateManager().delete("testCompiseVisitDelete_1", new PredicateVisitor<Student>() {
			@Override
			public Boolean visit(Student t, Object param) {
				return t.getId() == 3;
			}
		}).end()
		.visitAll();
    	long cost = mTr.endAndGetCost();
    	TestUtil.syso("cost = " + cost + " , called by # testVisitDelete(). + current size = " + size());
    }  	
    //================================= traditional ===============================
    
    public void testTraditionalDelete(){
    	Student.resetId();
    	//mTr.begin();
    	final List<Student> list = createStudent(6);
    	
    	mTr.begin();
    	Iterator<Student> it = list.iterator();
    	while(it.hasNext()){
    		if(it.next().id == 3){
    			it.remove();
    		}
    	}
    	long cost = mTr.endAndGetCost();
    	TestUtil.syso("cost = " + cost + " , called by # testTraditionalDelete() + , current size = " + list.size());
    }
    
    public void testTraditionalResult(){
    	Student.resetId();
    	//mTr.begin();
    	final List<Student> list = createStudent(6);
    	final List<Student2> stu2s = new ArrayList<Student2>();
    	
    	mTr.begin();
    	Iterator<Student> it = list.iterator();
    	while(it.hasNext()){
    		Student stu = it.next();
    		if( stu.getId() == 2 || stu.getId() == 1){
				continue; //null means ignored.
			}
    		stu2s.add(new Student2(stu));
    	}
    	long cost = mTr.endAndGetCost();
    	TestUtil.syso("cost = " + cost + " , called by # testTraditionalDelete() + , current size = " + list.size());
    }
    

}
