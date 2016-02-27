package com.hxgwx.www.bean;

import java.io.Serializable;

public class BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean statu;
	private int code;
	private String msg;
	/**
	 * @return the statu
	 */
	public  boolean isStatu() {
		return statu;
	}
	/**
	 * @param statu
	 *            the statu to set
	 */
	public  void setStatu(boolean statu) {
		this.statu = statu;
	}
	/**
	 * @return the code
	 */
	public  int getCode() {
		return code;
	}
	/**
	 * @param code
	 *            the code to set
	 */
	public  void setCode(int code) {
		this.code = code;
	}
	/**
	 * @return the msg
	 */
	public  String getMsg() {
		return msg;
	}
	/**
	 * @param msg
	 *            the msg to set
	 */
	public  void setMsg(String msg) {
		this.msg = msg;
	}
}
