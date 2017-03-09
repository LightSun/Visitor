package com.heaven7.java.visitor.util;

import com.heaven7.java.visitor.anno.Nullable;
/**
 * help for observer
 * @author heaven7
 * @since 1.1.7
 */
public final class Observers {
	private Observers(){}
	
	/**
	 * construct a {@linkplain Observer} from target success runnable.
	 * @param <T> the element type
	 * @param success the success runnable.
	 * @return a {@linkplain Observer}
	 * @see Observer
	 * @since 1.1.7
	 */
	public static <T> Observer<T, Void> from(Runnable success) {
		return from(success, null, null);
	}
	
	/**
	 * construct a {@linkplain Observer} from target runnables(success and failed).
	 * @param <T> the element type
	 * @param success the success runnable.
	 * @param failed the failed runnable
	 * @return a {@linkplain Observer}
	 * @see Observer
	 * @since 1.1.7
	 */
	public static <T> Observer<T, Void> from(Runnable success,@Nullable Runnable failed) {
		return from(success, failed, null);
	}

	/**
	 * construct a {@linkplain Observer} from target runnables(success,failed and throwable).
	 * @param <T> the element type
	 * @param success the success runnable.
	 * @param failed the failed runnable
	 * @param exception the throwable runnable.
	 * @return a {@linkplain Observer}
	 * @see Observer
	 * @since 1.1.7
	 */
	public static <T> Observer<T, Void> from(Runnable success,@Nullable Runnable failed,
			@Nullable Runnable exception) {
		Throwables.checkNull(success);
		return new ObserverAdapter<T, Void>() {
			@Override
			public void onSucess(Object param, Void r) {
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
					exception.run();
				} else {
					super.onThrowable(param, t, e);
				}
			}
		};
	}


	/**
	 * the observer adapter
	 * @author heaven7
	 *
	 * @param <T> the element type
	 * @param <R> the result type
	 * @since 1.1.7
	 */
	public static abstract class ObserverAdapter<T, R> implements Observer<T, R> {

		@Override
		public void onSucess(Object param, R r) {

		}

		@Override
		public void onFailed(Object param, T t) {

		}

		@Override
		public void onThrowable(Object param, T t, Throwable e) {
			Throwables.throwIfFatal(e);
		}
	}

}
