package com.heaven7.java.visitor.collection;

public class IterationInfo {

	private int deleteCount = 0;
	private int updateCount = 0;
	private int filterCount = 0;
	private int insertCount = 0;
	
	private int originSize ;
	private int currentSize ;
	private int currentIndex = -1;
	

	public int getCurrentIndex() {
		return currentIndex;
	}

	/*public*/ void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public int getOriginSize() {
		return originSize;
	}

	/*public*/ void setOriginSize(int originSize) {
		this.originSize = originSize;
	}

	public int getCurrentSize() {
		return currentSize;
	}

	/*public*/ void setCurrentSize(int currentSize) {
		this.currentSize = currentSize;
	}

	public int getDeleteCount() {
		return deleteCount;
	}

	public int getUpdateCount() {
		return updateCount;
	}

	public int getFilterCount() {
		return filterCount;
	}

	public int getInsertCount() {
		return insertCount;
	}

	public void incrementDelete() {
		 ++deleteCount;
	}

	public void incrementUpdate() {
		 ++updateCount;
	}

	public void incrementFilter() {
		 ++filterCount;
	}

	public void incrementInsert() {
		 ++insertCount;
	}
	public void incrementCurrentSize() {
		++currentSize;
	}
	public void decrementCurrentSize() {
		--currentSize;
	}
	
	public void reset() {
		deleteCount = 0;
		updateCount = 0;
		filterCount = 0;
		insertCount = 0;
		originSize = 0;
		currentSize = 0;
		currentIndex = -1;
	}

	@Override
	public String toString() {
		return "IterationInfo [deleteCount=" + deleteCount + ", updateCount=" + updateCount + ", filterCount="
				+ filterCount + ", insertCount=" + insertCount + ", originSize=" + originSize + ", currentSize="
				+ currentSize + ", currentIndex=" + currentIndex + "]";
	}

	

}
