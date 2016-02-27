package com.hxgwx.www.bean;

import java.io.Serializable;

public class FeedbackBase extends BaseBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8205657077477510730L;

	public FeedbackBase() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	private int count;

}
