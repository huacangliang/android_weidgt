package com.hxgwx.www.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @todo ндубап╠М
 * 
 * @author dengzhijiang
 * 
 */
public class ArticList extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ArticModelBase> data = new ArrayList<ArticModelBase>();

	/**
	 * @return the data
	 */
	public List<ArticModelBase> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<ArticModelBase> data) {
		this.data = data;
	}
}
