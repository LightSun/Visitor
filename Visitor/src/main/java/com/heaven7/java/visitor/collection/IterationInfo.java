package com.heaven7.java.visitor.collection;

import java.io.Serializable;
import java.util.List;

/**
 * the iteration info
 * 
 * @author heaven7
 *
 */
public class IterationInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/** the count of delete */
	private int deleteCount = 0;
	/** the count of update */
	private int updateCount = 0;
	/** the count of filter */
	private int filterCount = 0;
	/** the count of insert (may be the sum of insert with insertFinally) */
	private int insertCount = 0;

	/** the origin size , before iteration */
	private int originSize;
	/**
	 * the current size , in iteration, often is the same as originSize while in
	 * set/map.
	 */
	private int currentSize;
	/**
	 * the current index in iteration. this is only used for {{@linkplain List}}
	 */
	private int currentIndex = -1;

	/**
	 * get the current index
	 * 
	 * @return the current index
	 */
	public int getCurrentIndex() {
		return currentIndex;
	}

	/**
	 * set the current index
	 * 
	 * @param currentIndex
	 *            the index
	 */
	/* public */ void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	/**
	 * get the origin size
	 * 
	 * @return the origin size
	 */
	public int getOriginSize() {
		return originSize;
	}

	/**
	 * set the origin size
	 * 
	 * @param originSize
	 *            the origin size
	 */
	/* public */ void setOriginSize(int originSize) {
		this.originSize = originSize;
	}

	/**
	 * get the current size
	 * 
	 * @return the current size
	 */
	public int getCurrentSize() {
		return currentSize;
	}

	/**
	 * set the current size
	 * 
	 * @param currentSize
	 *            the current size
	 */
	/* public */ void setCurrentSize(int currentSize) {
		this.currentSize = currentSize;
	}

	/**
	 * get delete count
	 * 
	 * @return the delete count
	 */
	public int getDeleteCount() {
		return deleteCount;
	}

	/**
	 * get update count
	 * 
	 * @return the update count
	 */
	public int getUpdateCount() {
		return updateCount;
	}

	/**
	 * get filter count
	 * 
	 * @return the filter count
	 */
	public int getFilterCount() {
		return filterCount;
	}

	/**
	 * get insert count
	 * 
	 * @return the insert count
	 */
	public int getInsertCount() {
		return insertCount;
	}

	/**
	 * increase the count of delete
	 */
	/* public */ void incrementDelete() {
		++deleteCount;
		--currentSize;
	}

	/**
	 * increase the count of update
	 */
	/* public */ void incrementUpdate() {
		++updateCount;
	}

	/**
	 * increase the count of filter
	 */
	/* public */ void incrementFilter() {
		++filterCount;
	}

	/**
	 * increase the insert of filter
	 */
	/* public */ void incrementInsert() {
		++insertCount;
		++currentSize;
	}


	/* public */ void addInsert(int size) {
		insertCount += size;
		currentSize += size;
	}

	/**
	 * reset this to default.
	 */
	/* public */ void reset() {
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
