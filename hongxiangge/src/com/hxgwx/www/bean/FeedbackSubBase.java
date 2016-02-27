package com.hxgwx.www.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FeedbackSubBase extends FeedbackBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -333698683291023183L;

	public FeedbackSubBase() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the data
	 */
	public List<FeedbackSubData> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<FeedbackSubData> data) {
		this.data = data;
	}

	private List<FeedbackSubData> data = new ArrayList<FeedbackSubData>();

}
