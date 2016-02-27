package com.hxgwx.www.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 *@todo ÎÄÕÂÄÚÈİ 
 *
 * @author dengzhijiang
 *
 */
public class ArticBody extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ArticBodyBase> data = new ArrayList<ArticBodyBase>();

	/**
	 * @return the data
	 */
	public synchronized final List<ArticBodyBase> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public synchronized final void setData(List<ArticBodyBase> data) {
		this.data = data;
	}

}
