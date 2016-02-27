package com.hxgwx.www.bean;

import java.io.Serializable;

public class FeedbackSubData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -333698683291023183L;

	public FeedbackSubData() {
		// TODO Auto-generated constructor stub
	}

	private int id;
	private int tf_id;
	private String tf_title;
	private String message;
	private String postdate;
	private int ischeck;
	private String ip;
	private String uid;
	private String uname;
	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	/**
	 * @return the uname
	 */
	public String getUname() {
		return uname;
	}
	/**
	 * @param uname the uname to set
	 */
	public void setUname(String uname) {
		this.uname = uname;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the tf_id
	 */
	public int getTf_id() {
		return tf_id;
	}
	/**
	 * @param tf_id the tf_id to set
	 */
	public void setTf_id(int tf_id) {
		this.tf_id = tf_id;
	}
	/**
	 * @return the tf_title
	 */
	public String getTf_title() {
		return tf_title;
	}
	/**
	 * @param tf_title the tf_title to set
	 */
	public void setTf_title(String tf_title) {
		this.tf_title = tf_title;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the postdate
	 */
	public String getPostdate() {
		return postdate;
	}
	/**
	 * @param postdate the postdate to set
	 */
	public void setPostdate(String postdate) {
		this.postdate = postdate;
	}
	/**
	 * @return the ischeck
	 */
	public int getIscheck() {
		return ischeck;
	}
	/**
	 * @param ischeck the ischeck to set
	 */
	public void setIscheck(int ischeck) {
		this.ischeck = ischeck;
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
}
