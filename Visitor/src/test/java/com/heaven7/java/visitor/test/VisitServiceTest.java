package com.heaven7.java.visitor.test;

import com.heaven7.java.visitor.*;
import com.heaven7.java.visitor.collection.CollectionVisitService;
import com.heaven7.java.visitor.collection.IterationInfo;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.java.visitor.test.help.Student;
import com.heaven7.java.visitor.test.help.Student2;
import com.heaven7.java.visitor.util.Observers;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.heaven7.java.visitor.collection.Operation.*;
import static com.heaven7.java.visitor.test.help.TestUtil.*;

/**
 * here just for list test.
 * 
 * @author heaven7
 *
 */
public class VisitServiceTest extends TestCase {

	protected CollectionVisitService<Student> mService;
	protected List<Student> mStus;

	@Override
	protected void setUp() throws Exception {
		Student.resetId();
		mService = VisitServices.from(mStus = createStudent(6));
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	protected int getStudentSize() {
		return mStus.size();
	}


	//================================================

	public void testZipServiceThrowable(){
		final String str = "testZipServiceFiled";
		final int targetId = 2;
		int size = mService.zipService(str, new ResultVisitor<Student, Student2>() {
			@Override
			public Student2 visit(Student s, Object param) {
				assertEquals(str, param);
				if(s .getId() == targetId){
					throw new IllegalStateException();
				}
				return new Student2(s);
			}
		}, new Observers.ObserverAdapter<Student, List<Student2>>() {
			@Override
			public void onThrowable(Object param, Student student, Throwable e) {
				syso("onThrowable");
				assertEquals(param, str);
				assertEquals(student.getId(), targetId);
				assertTrue(e instanceof IllegalStateException);
			}
		}).size();
		assertEquals(size , 1); // when 2 is breaked. so only 1
	}

	public void testZipServiceFiled(){
		final String str = "testZipServiceFiled";
		final int targetId = 2;
		int size = mService.zipService(str, new ResultVisitor<Student, Student2>() {
			@Override
			public Student2 visit(Student s, Object param) {
				assertEquals(str, param);
				if(s .getId() == targetId){
					return null;
				}
				return new Student2(s);
			}
		}, new Observers.ObserverAdapter<Student, List<Student2>>() {
			@Override
			public void onFailed(Object param, Student student) {
				syso("onFailed");
				assertEquals(param, str);
				assertEquals(student.getId(), targetId);
			}
		}).size();
		assertEquals(size , 1); // when 2 is breaked. so only 1
	}

	public void testZipServiceSuccess(){
		final String str = "testZipService";
		int size = mService.zipService(str, new ResultVisitor<Student, Student2>() {
			@Override
			public Student2 visit(Student s, Object param) {
				return new Student2(s);
			}
		}, new Observers.ObserverAdapter<Student, List<Student2>>() {
			@Override
			public void onSuccess(Object param, List<Student2> student2s) {
				syso("onSuccess");
				assertEquals(student2s.size() , mService.size());
				assertEquals(param, str);
			}
		}).size();
		assertEquals(size , mService.size());
	}
	
	public void testCacheIterateControl(){
		final int size = getStudentSize();
		List<Student> list = new ArrayList<Student>();
		
		final String newNameOfStudent = "testCacheIterateControl"; 
		final Student preStudent = mStus.get(0);
		/**
		 *  cache the setting of IterateControl and OperateManager.
		 * here we just test update one and then suppose to delete it.
		 */
		mService.beginIterateControl()
			.first(OP_UPDATE)
			.second(OP_FILTER)
			.then(OP_DELETE)
			.last(OP_INSERT)
			/**
			 * intercept the op--->update.
			 * this means: the next visitor will not visit it.
			 */
			.interceptIfSuccess(OP_UPDATE) 
			.cache().end()
			.beginOperateManager().update(createStudent(newNameOfStudent),
					new PredicateVisitor<Student>() {
						@Override
						public Boolean visit(Student t, Object param) {
							//id = 1 is the first element of list
							return t.getId() == 1; 
						}
					}).delete(new PredicateVisitor<Student>() {
						@Override
						public Boolean visit(Student t, Object param) {
							assertNotSame(t.getName(), newNameOfStudent);
							return t.getName().equals(newNameOfStudent);
						}
					}).cache().end()
			.save(list, true);
		assertEquals(size -1 , list.size());
		assertEquals(2, list.get(0).getId());
		
		//before test cache, we replace the student(name is 'testCacheIterateControl' ) to previous.
		mStus.set(0, preStudent);
		
		mService.save(list, true);
		assertEquals(size - 1, list.size());
		assertEquals(2, list.get(0).getId());
	}
	
	public void testCacheOperateManager(){
		final int size = getStudentSize();
		List<Student> list = new ArrayList<Student>();
		//how to delete last?
		mService.beginOperateManager()
		//just delete the student of id = 1
			.filter(new PredicateVisitor<Student>() {
				@Override
				public Boolean visit(Student t, Object param) {
					return t.getId() == 1;
				}
			}).cache().end().visitForQueryList(Visitors.truePredicateVisitor(), list);
		assertEquals(size - 1, list.size());
		list.clear();
		
		//last called cache.  so the operateManager's op is cached.
		syso("at last ..1");
		mService.visitForQueryList(Visitors.truePredicateVisitor(), list);
		assertEquals(size - 1, list.size());
		list.clear();
		syso("at last ..2");
	}
	
	public void testSave3(){
		final int size = getStudentSize();
		List<Student> list = new ArrayList<Student>();
		
		mService.beginOperateManager()
		//just delete the student of id = 1
			.delete(new PredicateVisitor<Student>() {
				@Override
				public Boolean visit(Student t, Object param) {
					return t.getId() == 1;
				}
			}).end().save(list, true);
		assertEquals(list.size(), size - 1);
		
		mService.save(list,true);
		assertEquals(list.size(), size - 1);
	}
	
	public void testSave2(){
		final int size = getStudentSize();
		mService.beginOperateManager()
		//just delete the student of id = 1
			.delete(new PredicateVisitor<Student>() {
				@Override
				public Boolean visit(Student t, Object param) {
					return t.getId() == 1;
				}
			}).end().save(new SaveVisitor<Student>() {
				@Override
				public void visit(Collection<Student> o) {
					assertEquals(o.size(), size - 1);
					/**
					 * must cause exception. because this collection is read-only.
					 */
					try {
						o.clear();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		
	}
	
	public void testSave(){
		final int size = getStudentSize();
		mService.beginOperateManager()
		//just fiter the student of id = 1
			.filter(new PredicateVisitor<Student>() {
				@Override
				public Boolean visit(Student t, Object param) {
					return t.getId() == 1;
				}
			}).end().save(new SaveVisitor<Student>() {
				@Override
				public void visit(Collection<Student> o) {
					//filter not delete. so size unchanged.
					assertEquals(o.size(), size - 1);
				}
			});
	}
	
	public void testForResult(){
		
		final int size = getStudentSize();
		final List<Student2> list = new ArrayList<Student2>();
		
		mService.beginOperateManager()
		//just fiter the student of id = 1
			.filter(new PredicateVisitor<Student>() {
				@Override
				public Boolean visit(Student t, Object param) {
					assertEquals("testForResult", param);
					return t.getId() == 1;
				}
			}).end()
		.visitForResultList("testForResult", Visitors.truePredicateVisitor(), 
				new ResultVisitor<Student, Student2>() {
					@Override
					public Student2 visit(Student t, Object param) {
						assertEquals("testForResult", param);
						if( t.getId() == 2){
							return null; //null means ignored.
						}
						return new Student2(t);
					}
				}, list);
		
		assertEquals(getStudentSize(), size);
		assertEquals(list.size(), size - 2 );
	}
	
	public void testQueryWithExtraOperate(){
		Student student = mService
				.beginOperateManager()
				//delete all student
				.delete(null, new PredicateVisitor<Student>() {
					@Override
					public Boolean visit(Student t, Object param) {
						//because the parameter of delete operate is null, so use the last visit parameter
						// here is from visitForQuery(...).
						return "testQueryWithExtraOperate".equals(param);
					}
				})
				.end()
				.visitForQuery("testQueryWithExtraOperate", new PredicateVisitor<Student>() {
			@Override
			public Boolean visit(Student t, Object param) {
				if(t.id == 1 ){
					return true;
				}
				return null;
			}
		});
		//delete all .so is null
		assertEquals(student, null);
		assertEquals(0, getStudentSize());
	}
	
	//single query
	public void testQuery(){
		Student student = mService.visitForQuery("testQuery", new PredicateVisitor<Student>() {
			@Override
			public Boolean visit(Student t, Object param) {
				if(t.id == 1 && "testQuery".equals(param)){
					return true;
				}
				return null;
			}
		});
		assertEquals(student.getId(), 1);
	}
	
	/**
	 * the operate order is: 
OP_FILTER
OP_INSERT
OP_DELETE
OP_UPDATE
	 */
	public void testIterationControl3(){
		//default order delete -> filter -> update -> insert
		mService.beginIterateControl()
		.second(OP_UPDATE)   //  delete -> update -> filter -> insert
		.second(OP_FILTER)   //  delete -> filter -> update -> insert
		.second(OP_DELETE)   //  filter -> delete -> update -> insert
		.second(OP_INSERT)   //  filter -> insert -> delete -> update 
		.end();
	}
	/**
	 * the operate order is: 
OP_INSERT
OP_DELETE
OP_FILTER
OP_UPDATE
	 */
	public void testIterationControl2(){
		mService.beginIterateControl()
		.first(OP_UPDATE) // 4
		.first(OP_FILTER) //3
		.first(OP_DELETE) //2
		.first(OP_INSERT) //1
		.end();
	}
	
	/**
	 * the operate order is: 
OP_UPDATE
OP_FILTER
OP_DELETE
OP_INSERT
	 */
	public void testIterationControl(){
		mService.beginIterateControl()
		.first(OP_UPDATE)
		.second(OP_FILTER)
		.then(OP_DELETE)
		.last(OP_INSERT)
		.end();
	}
	

	public void testComposeVisitInsertFinally() {
		int size = getStudentSize();
		mService.beginOperateManager()
				.insertFinally(createStudent("new_stu"), Visitors.trueIterateVisitor()).end()
				.visitAll();
		assertEquals(getStudentSize(), size + 1);
	}

	public void testComposeVisitFilter() {
		mService.beginOperateManager().filter("testComposeVisitFilter_1", new PredicateVisitor<Student>() {
			@Override
			public Boolean visit(Student t, Object param) {
				return t.getId() == 3;
			}
		}).end()
		.visitAll("testComposeVisitFilter_2");
	}

	public void testComposeVisitDelete() {
		int size = getStudentSize();
		mService.beginOperateManager().delete("testCompiseVisitDelete_1", new PredicateVisitor<Student>() {
			@Override
			public Boolean visit(Student t, Object param) {
				return t.getId() == 3;
			}
		}).end()
		.visitAll("testCompiseVisitDelete_2");

		assertEquals(getStudentSize(), size - 1);
	}

	public void testComposeVisitUpdate() {
		// class Student not implements Updatable
		mService.beginOperateManager()
					.update(createStudent("new_stu"), "testComposeVisit_1", Visitors.truePredicateVisitor())
					.end()
				.visitAll("testComposeVisit_2");

		for (Student stu : mStus) {
			// assertFalse("new_stu".equals(stu.getName()));
			assertTrue("new_stu".equals(stu.getName())); // in list.
															// listIterator
															// support update.
		}

		// class Student2 has implements Updatable
		List<Student2> list = createStudent2(6);
		VisitServices.from(list).beginOperateManager()
					.update(new Student2("new_stu"), "testComposeVisit_1", Visitors.truePredicateVisitor())
					.end()
				.visitAll("testComposeVisit_2");

		for (Student stu : list) {
			assertEquals("new_stu", stu.getName());
		}
	}

	public void testComposeVisitInsert() {
		// here just insert
		int size = getStudentSize();
		mService.beginOperateManager()
		.insert(createStudent("new_stu"), "testComposeVisit_1", Visitors.trueIterateVisitor())
		.end()
				.visitAll("testComposeVisit_2");

		assertEquals(getStudentSize(), size * 2);
	}

	public void testVisitAll() {
		int size = getStudentSize();
		mService.visitAll("testBaseVisit1");
		assertEquals(getStudentSize(), size);
	}

	public void testVisitUtilSuccess() {
		mService.visitUntilSuccess("testBaseVisit1", new LogVisitor() {
			@Override
			public Boolean visit(Student t, Object param, IterationInfo info) {
				if (t.getId() == 5) {
					return true;
				}
				super.visit(t, param, info);
				return false;
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
