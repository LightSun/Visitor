package com.heaven7.java.visitor.collection;

import static com.heaven7.java.visitor.collection.Operation.OP_DELETE;
import static com.heaven7.java.visitor.collection.Operation.OP_FILTER;
import static com.heaven7.java.visitor.collection.Operation.OP_INSERT;
import static com.heaven7.java.visitor.collection.Operation.OP_UPDATE;
import static com.heaven7.java.visitor.internal.InternalUtil.op2String;

import java.util.ArrayList;
import java.util.List;

import com.heaven7.java.visitor.internal.Cacheable;
import com.heaven7.java.visitor.internal.Endable;

/**
 * the iterate control used to control the order of all operate (
 * {@linkplain VisitService#OP_FILTER}} and etc.). But
 * {@linkplain VisitService#OP_INSERT} only used for List.
 * <p>
 * the default order of iterate is :<br>
 * first({@linkplain VisitService#OP_DELETE} ).<br>
 * second({@linkplain VisitService#OP_FILTER} ).<br>
 * then({@linkplain VisitService#OP_UPDATE} ).<br>
 * last({@linkplain VisitService#OP_INSERT} ).<br>
 * </p>
 * 
 * <pre>
 	public void testIterationControl(){
		mService.beginIterateControl()
		.first(OP_UPDATE)
		.second(OP_FILTER)
		.then(OP_DELETE)
		.last(OP_INSERT)
		.end();
	}</br>after call this the order is : <br> OP_UPDATE ->OP_FILTER -> OP_DELETE -> OP_INSERT
 * </pre>
 * 
 * <pre>
 	public void testIterationControl2(){
		mService.beginIterateControl()
		.first(OP_UPDATE) // 4
		.first(OP_FILTER) //3
		.first(OP_DELETE) //2
		.first(OP_INSERT) //1
		.end();
	}</br>after call this the order is : <br> OP_INSERT ->OP_DELETE -> OP_FILTER -> OP_UPDATE
 * </pre>
 * 
 * @author heaven7
 *
 * @param <T>
 *            the type
 * @see {@linkplain VisitService#OP_DELETE}
 * @see {@linkplain VisitService#OP_FILTER}
 * @see {@linkplain VisitService#OP_UPDATE}
 * @see {@linkplain VisitService#OP_INSERT}
 */
public final class IterateControl<T> implements Endable<T>, Cacheable<IterateControl<T>>{
	
	private static final boolean DEBUG = false;
	
	private final T t;
	private final Callback mCallback;
	
	/** the ordered operate */
	private final ArrayList<Integer> mOrderOps = new ArrayList<>();
	/** the intercept operate */
	private final ArrayList<Integer> mInterceptOps = new ArrayList<>();

	private IterateControl(T t , Callback mCallback) {
		this.t = t;
		this.mCallback = mCallback;
	}
	
	public static <T> IterateControl<T> create(T t, Callback callback){
		return new IterateControl<T>(t, callback);
	}
	/**
	 * begin the iterate control really.
	 */
	public IterateControl<T> begin(){
		mOrderOps.clear();
		mInterceptOps.clear();
		mCallback.onStart(mOrderOps, mInterceptOps);
		return this;
	}
	
	/**
	 * end the iterate control and return the original object.
	 * 
	 * @return the original object
	 */
	@Override
	public T end(){
		if (DEBUG) {
			System.out.println("the operate order is: ");
			for (int op : mOrderOps) {
				System.out.println(op2String(op));
			}
			System.out.println();
		}
		mCallback.saveState(mOrderOps, mInterceptOps);
		return t;
	}

	/**
	 * intercept the target operate if visit success. By default
	 * {@linkplain Operation#OP_DELETE}} and {@linkplain Operation#OP_FILTER}
	 * will be intercepted when visitor visit success.
	 * 
	 * @param operate
	 *            the operate. see
	 *            {@linkplain CollectionVisitServiceImpl#OP_DELETE}} and etc.
	 * @return this.
	 * @see {@linkplain #cancelIntercept(int)}
	 */
	public IterateControl<T> interceptIfSuccess(int operate){
		mCallback.checkOperation(operate);
		int index = mInterceptOps.indexOf(operate);
		if (index == -1) {
			mInterceptOps.add(operate);
		}
		return this;
	}
	/**
	 * cancel intercept the operation in iteration. That means Anti-interception the target operate, 
	 *  no matter the visitor visit success or not.
	 * @param operate
	 *             the operate. see
	 *            {@linkplain CollectionVisitServiceImpl#OP_DELETE}} and etc.
	 * @return this.
	 * @since 1.1.0
	 * @see {@linkplain #interceptIfSuccess(int)}
	 */
	public IterateControl<T> cancelIntercept(int operate){
		mCallback.checkOperation(operate);
		int index = mInterceptOps.indexOf(operate);
		if (index != -1) {
			mInterceptOps.remove(index);
		}
		return this;
	}

	/**
	 * define the target operate run the first in current iteration, but it may
	 * change. such as recall {@linkplain #first(int)}}.the more to see in demo.
	 * 
	 * @param operate
	 *            the operate. see
	 *            {@linkplain CollectionVisitServiceImpl#OP_DELETE}} and etc.
	 * @return this.
	 */
	public IterateControl<T> first(int operate){
		mCallback.checkOperation(operate);
		int index = mOrderOps.indexOf(operate);
		if (index != -1) {
			mOrderOps.remove(index);
		}
		mOrderOps.add(0, operate);
		return this;
	}

	/**
	 * define the target operate run the second in current iteration. but may
	 * change, the more to see in demo.
	 * 
	 * @param operate
	 *            the operate. see
	 *            {@linkplain CollectionVisitServiceImpl#OP_DELETE}} and etc.
	 * @return this.
	 */
	public IterateControl<T> second(int operate){
		mCallback.checkOperation(operate);
		int index = mOrderOps.indexOf(operate);
		if (index != -1) {
			mOrderOps.remove(index);
		}
		/*
		 * if(DEBUG){ System.err.println(); }
		 */
		mOrderOps.add(mOrderOps.size() > 0 ? 1 : 0, operate);
		return this;
	}

	/**
	 * define the target operate run after the {@linkplain #second(int)} in
	 * current iteration, but may change, the more to see in demo.
	 * 
	 * @param operate
	 *            the operate. see
	 *            {@linkplain CollectionVisitServiceImpl#OP_DELETE}} and etc.
	 * @return this.
	 */
	public IterateControl<T> then(int operate){
		mCallback.checkOperation(operate);
		int index = mOrderOps.indexOf(operate);
		if (index != -1) {
			mOrderOps.remove(index);
		} 
		final int size = mOrderOps.size();
		if (size >= 3) {
			mOrderOps.add(2, operate);
		} else {
			mOrderOps.add(operate);
		}
		return this;
	}

	/**
	 * define the target operate run the last in current iteration, but may
	 * change, the more to see in demo.
	 * 
	 * @param operate
	 *            the operate. see
	 *            {@linkplain CollectionVisitServiceImpl#OP_DELETE}} and etc.
	 * @return this.
	 */
	public IterateControl<T> last(int operate){
		mCallback.checkOperation(operate);
		int index = mOrderOps.indexOf(operate);
		if (index != -1) {
			mOrderOps.remove(index);
		}
		mOrderOps.add(mOrderOps.size(), operate);
		return this;
	}
	
	/**
	 * cache the setting of current {@linkplain IterateControl}.
	 * @since 1.1.2
	 */
	@Override
	public IterateControl<T> cache() {
		mCallback.applyCache();
		return this;
	}
	
	/**
	 * the callback of iterate control
	 * @author heaven7
	 *
	 */
	public static abstract class Callback{
		
		/**
		 * called on start iterate control.
		 * @param orderOps the order ops(operation),never null
		 * @param interceptOps  the intercept ops(operation),never null
		 */
		public void onStart(List<Integer> orderOps, List<Integer> interceptOps){
			// by default only delete and fiter op will intercept iteration.
			// that means if success the iterator will skip and continue next
			// iteration.
			interceptOps.add(OP_DELETE);
			interceptOps.add(OP_FILTER);

			/**
			 * default order is delete -> filter -> update -> insert
			 */
			orderOps.add(OP_DELETE);
			orderOps.add(OP_FILTER);
			orderOps.add(OP_UPDATE);
			orderOps.add(OP_INSERT);
		}
		
		/**
		 * apply the cache cmd for {@linkplain IterateControl}.
		 * this is only called by {@linkplain IterateControl#cache()}.
		 * Default is empty implements.
		 * @since 1.1.2
		 */
		public void applyCache() {
			
		}

		/**
		 * check the target operate
		 * @param op {@linkplain Operation#OP_DELETE} and etc.
		 */
		public void checkOperation(int op) {
			
		}

		/**
		 * save state of all ops
	     * @param orderOps the order ops(opertion)
		 * @param interceptOps  the intercept ops(opertion)
		 */
		public abstract void saveState(List<Integer> orderOps, List<Integer> interceptOps);
	}

}