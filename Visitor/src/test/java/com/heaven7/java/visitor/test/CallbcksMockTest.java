package com.heaven7.java.visitor.test;

import java.util.ArrayList;

import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ThrowableVisitor;
import com.heaven7.java.visitor.collection.ListVisitService;
import com.heaven7.java.visitor.collection.VisitServices;

import junit.framework.TestCase;

/**
 * Created by heaven7 on 2017/3/9 0009.
 */
public class CallbcksMockTest extends TestCase {

	private final CallbackManager mCm = new CallbackManager();
	private static final String sMsg = "testCallbackManager";
	private final Callback mCl = new Callback() {
		@Override
		public void callback(String msg) {
			assertEquals(sMsg, msg);
			System.out.println("callback: " + msg);
		}
	};

	public void testCallbackManager() {
		mCm.register(mCl);
		assertEquals(mCm.getSize(), 1);
		mCm.register2(mCl);
		assertEquals(mCm.getSize(), 1);
		mCm.unregister(mCl);
		assertEquals(mCm.getSize(), 0);

		mCm.register(mCl);
		assertEquals(mCm.getSize(), 1);
		mCm.dispatchCallback(sMsg);
	}

	public static void main(String[] args) {
		new CallbcksMockTest().testCallbackManager();
	}

	private static class CallbackManager {
		final ListVisitService<Callback> mService;

		public CallbackManager() {
			this.mService = VisitServices.from(new ArrayList<Callback>());
		}

		public void register(Callback cl) {
			mService.addIfNotExist(cl);
		}

		public int getSize() {
			return mService.size();
		}

		public void unregister(Callback cl) {
			mService.removeIfExist(cl);
		}

		public void dispatchCallback(String msg) {
			mService.fire(msg, new FireVisitor<Callback>() {
				@Override
				public Boolean visit(Callback callback, Object param) {
					assertEquals(param, sMsg);
					callback.callback(param.toString());
					return null;
				}
			}, new ThrowableVisitor() {
				@Override
				public Void visit(Throwable t) {
					System.err.println(t.toString());
					return null;
				}
			});
		}

		public void unregister2(Callback cl) {
			mService.beginOperateManager().delete(cl, new PredicateVisitor<Callback>() {
				@Override
				public Boolean visit(Callback callback, Object param) {
					return callback == param;
				}
			}).endAsList().visitAll();
		}
		
		public void register2(Callback cl) {
			mService.beginOperateManager()
				.insertFinallyIfNotExist(cl)
				.endAsList()
				.visitAll();
		}

	}

	public interface Callback {
		void callback(String msg);
	}
}
