package com.heaven7.java.visitor.util;

import com.heaven7.java.visitor.ThrowableVisitor;
import com.heaven7.java.visitor.anno.Nullable;

/**
 * help for observer
 * 
 * @author heaven7
 * @since 1.1.7
 */
public final class Observers {
	
	private static final WrappedObserver<Object, Object> OBSERVER_DEFAULT = new
			WrappedObserver<Object, Object>(null);
	
	private Observers() {
	}

	/**
	 * get the wrapped observer
	 * @param <T> the element type
	 * @param <R> the return type.
	 * @param base the base observer.
	 * @return the wrapped observer.
	 * @since 2.0.0
	 */
	public static <T, R> Observer<T,R> wrappedObserver(Observer<T,R> base){
		return new WrappedObserver<T,R>(base);
	}
	/**
	 * get the default observer
	 * @param <T> the element type
	 * @param <R> the return type.
	 * @return the default observer.
	 * @since 2.0.0
	 */
	@SuppressWarnings("unchecked")
	public static <T, R> Observer<T,R> defaultObserver(){
		return (Observer<T, R>) OBSERVER_DEFAULT;
	}
	
	/**
	 * construct a {@linkplain Observer} from target success runnable.
	 * 
	 * @param <T>
	 *            the element type
	 * @param success
	 *            the success runnable.
	 * @return a {@linkplain Observer}
	 * @see Observer
	 * @since 1.1.7
	 */
	public static <T> Observer<T, Void> from(Runnable success) {
		return from(success, null, null);
	}

	/**
	 * construct a {@linkplain Observer} from target runnables(success and
	 * failed).
	 * 
	 * @param <T>
	 *            the element type
	 * @param success
	 *            the success runnable.
	 * @param failed
	 *            the failed runnable
	 * @return a {@linkplain Observer}
	 * @see Observer
	 * @since 1.1.7
	 */
	public static <T> Observer<T, Void> from(Runnable success, @Nullable Runnable failed) {
		return from(success, failed, null);
	}

	/**
	 * construct a {@linkplain Observer} from target callbacks(success,failed
	 * and throwable).
	 * 
	 * @param <T>
	 *            the element type
	 * @param success
	 *            the success runnable.
	 * @param failed
	 *            the failed runnable
	 * @param exception
	 *            the throwable visitor.
	 * @return a {@linkplain Observer}
	 * @see Observer
	 * @since 1.1.7
	 */
	public static <T> Observer<T, Void> from(final Runnable success, @Nullable final Runnable failed,
			final @Nullable ThrowableVisitor exception) {
		Throwables.checkNull(success);
		return new ObserverAdapter<T, Void>() {
			@Override
			public void onSuccess(Object param, Void r) {
				success.run();
			}

			@Override
			public void onFailed(Object param, T t) {
				if (failed != null) {
					failed.run();
				}
			}

			@Override
			public void onThrowable(Object param, T t, Throwable e) {
				if (exception != null) {
					exception.visit(e);
				} else {
					super.onThrowable(param, t, e);
				}
			}
		};
	}

	/**
	 * the observer adapter
	 * 
	 * @author heaven7
	 *
	 * @param <T>
	 *            the element type
	 * @param <R>
	 *            the result type
	 * @since 1.1.7
	 */
	public static abstract class ObserverAdapter<T, R> implements Observer<T, R> {

		@Override
		public void onSuccess(Object param, R r) {

		}

		@Override
		public void onFailed(Object param, T t) {

		}

		@Override
		public void onThrowable(Object param, T t, Throwable e) {
			Throwables.throwIfFatal(e);
		}
	}

	/**
	 * the observer wrapper
	 * @author heaven7
	 *
	 * @param <T> the element type
	 * @param <R> the result type.
	 * @since 2.0.0
	 */
	public static class WrappedObserver<T, R> implements Observer<T, R> {
		
		private final Observer<? super T, R> mBase;

		public WrappedObserver(@Nullable Observer<? super T, R> mBase) {
			super();
			this.mBase = mBase;
		}

		@Override
		public void onSuccess(Object param, R r) {
			if (mBase != null) {
				mBase.onSuccess(param, r);
			}
		}

		@Override
		public void onFailed(Object param, T t) {
			if (mBase != null) {
				mBase.onFailed(param, t);
			}
		}

		@Override
		public void onThrowable(Object param, T t, Throwable e) {
			if (mBase != null) {
				mBase.onThrowable(param, t, e);
			} else {
				Throwables.throwIfFatal(e);
			}
		}
	}
}
