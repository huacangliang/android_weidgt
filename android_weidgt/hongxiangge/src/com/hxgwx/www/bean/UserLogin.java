package com.hxgwx.www.bean;

import java.io.Serializable;

/**
 * 
 * @todo 登录获取实体
 * 
 * @author dengzhijiang
 * 
 */
public class UserLogin extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private UserBase data;

	/**
	 * @return the data
	 */
	public UserBase getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(UserBase data) {
		this.data = data;
	}

}
