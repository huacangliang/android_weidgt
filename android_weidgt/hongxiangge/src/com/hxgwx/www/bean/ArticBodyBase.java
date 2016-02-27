package com.hxgwx.www.bean;

import java.io.Serializable;

public class ArticBodyBase implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int aid;//文章id
	private int typeid;//分类id
	private String body;//内容
	private String redirecturl;
	private String templet;
	private String userip;
	/**
	 * @return the aid
	 */
	public synchronized final int getAid() {
		return aid;
	}
	/**
	 * @param aid
	 *            the aid to set
	 */
	public synchronized final void setAid(int aid) {
		this.aid = aid;
	}
	/**
	 * @return the typeid
	 */
	public synchronized final int getTypeid() {
		return typeid;
	}
	/**
	 * @param typeid
	 *            the typeid to set
	 */
	public synchronized final void setTypeid(int typeid) {
		this.typeid = typeid;
	}
	/**
	 * @return the body
	 */
	public synchronized final String getBody() {
		return body;
	}
	/**
	 * @param body
	 *            the body to set
	 */
	public synchronized final void setBody(String body) {
		this.body = body;
	}
	/**
	 * @return the redirecturl
	 */
	public synchronized final String getRedirecturl() {
		return redirecturl;
	}
	/**
	 * @param redirecturl
	 *            the redirecturl to set
	 */
	public synchronized final void setRedirecturl(String redirecturl) {
		this.redirecturl = redirecturl;
	}
	/**
	 * @return the templet
	 */
	public synchronized final String getTemplet() {
		return templet;
	}
	/**
	 * @param templet
	 *            the templet to set
	 */
	public synchronized final void setTemplet(String templet) {
		this.templet = templet;
	}
	/**
	 * @return the userip
	 */
	public synchronized final String getUserip() {
		return userip;
	}
	/**
	 * @param userip
	 *            the userip to set
	 */
	public synchronized final void setUserip(String userip) {
		this.userip = userip;
	}
}
