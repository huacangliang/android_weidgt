package com.hxgwx.www.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FeedbackTopBase extends FeedbackBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -333698683291023183L;

	public FeedbackTopBase() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the data
	 */
	public List<FeedbackTopData> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<FeedbackTopData> data) {
		this.data = data;
	}

	private List<FeedbackTopData> data = new ArrayList<FeedbackTopData>();

}
