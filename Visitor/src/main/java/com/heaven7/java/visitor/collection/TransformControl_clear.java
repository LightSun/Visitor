package com.heaven7.java.visitor.collection;

import java.util.Comparator;
/**
 * the transform control.
 * @author heaven7
 *
 * @param <T> the end type which is returned by {@linkplain #end()}
 */
public class TransformControl_clear<T> {

	private final TransformParam mParam;
	private final Callback mCallback;
	private final T t;
	
	/*public*/ TransformControl_clear(T t, Callback callback) {
		super();
		this.t = t;
		this.mCallback = callback;
		mParam = new TransformParam();
	}

	public T end(){
		mCallback.onSaveTransformParam(mParam);
		return t;
	}
	
	public <K> TransformControl_clear<T> sortComparator(Comparator<? super K> comparator){
		mParam.setSortComparator(comparator);
		return this;
	}
	
	public TransformControl_clear<T> forceSort(boolean sort){
		mParam.setForce(true);
		mParam.setSorted(sort);
		return this;
	}
	
	public interface Callback{
		void onSaveTransformParam(TransformParam param);
	}
	
	public static class TransformParam{
		private Comparator mSortComparator;
		private boolean mForce;
		private boolean mSorted;
		
		private TransformParam(){}
		
		public Comparator getSortComparator() {
			return mSortComparator;
		}
		public boolean isForce() {
			return mForce;
		}
		public boolean isSorted() {
			return mSorted;
		}
		public void setSortComparator(Comparator comparator) {
			this.mSortComparator = comparator;
		}
		public void setForce(boolean mForce) {
			this.mForce = mForce;
		}
		public void setSorted(boolean sort) {
			this.mSorted = sort;
		}
		
	}
}
