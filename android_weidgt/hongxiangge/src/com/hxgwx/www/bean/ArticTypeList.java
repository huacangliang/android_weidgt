package com.hxgwx.www.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @todo 文章分类列表
 * 
 * @author dengzhijiang
 * 
 */
public class ArticTypeList extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ArticTypeBaseList> data = new ArrayList<ArticTypeBaseList>();

	/**
	 * @return the data
	 */
	public List<ArticTypeBaseList> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<ArticTypeBaseList> data) {
		this.data = data;
	}

}
