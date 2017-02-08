package com.heaven7.java.visitor.test.help;

public class TimeRecorder {

	private long mStartTime;
	private long mEndTime;

	public static TimeRecorder create() {
		return new TimeRecorder();
	}

	public void begin() {
		mStartTime = System.currentTimeMillis();
	}

	public void end() {
		mEndTime = System.currentTimeMillis();
	}

	public void reset() {
		mStartTime = 0;
		mEndTime = 0;
	}

	public long endAndGetCost() {
		end();
		return cost();
	}

	public long cost() {
		if (mStartTime == 0 || mEndTime == 0) {
			throw new IllegalStateException("you must call begin() and end().");
		}
		return mEndTime - mStartTime;
	}
}
