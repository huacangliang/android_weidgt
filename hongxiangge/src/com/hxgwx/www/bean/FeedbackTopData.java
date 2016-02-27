package com.hxgwx.www.bean;

import java.io.Serializable;
import java.util.Date;

public class FeedbackTopData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FeedbackTopData() {
		// TODO Auto-generated constructor stub
	}
	
	private int id;
	private int aid;
	private String uname;
	private String arctitle;
	private String uid;
	private String message;
	private String postdate;
	private String ip;
	private String ftype;//备用，标识来自于什么地方，如mobile手机端
	private int ischeck;//是否审核
	private int zan;
	private int nzan;
	private int sub_feedback_count;
	private String usericon;

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
	 * @return the aid
	 */
	public int getAid() {
		return aid;
	}
	/**
	 * @param aid the aid to set
	 */
	public void setAid(int aid) {
		this.aid = aid;
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
	 * @return the arctitle
	 */
	public String getArctitle() {
		return arctitle;
	}
	/**
	 * @param arctitle the arctitle to set
	 */
	public void setArctitle(String arctitle) {
		this.arctitle = arctitle;
	}
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
	/**
	 * @return the ftype
	 */
	public String getFtype() {
		return ftype;
	}
	/**
	 * @param ftype the ftype to set
	 */
	public void setFtype(String ftype) {
		this.ftype = ftype;
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
	 * @return the zan
	 */
	public int getZan() {
		return zan;
	}
	/**
	 * @param zan the zan to set
	 */
	public void setZan(int zan) {
		this.zan = zan;
	}
	/**
	 * @return the nzan
	 */
	public int getNzan() {
		return nzan;
	}
	/**
	 * @param nzan the nzan to set
	 */
	public void setNzan(int nzan) {
		this.nzan = nzan;
	}
	/**
	 * @return the sub_feedback_count
	 */
	public int getSub_feedback_count() {
		return sub_feedback_count;
	}
	/**
	 * @param sub_feedback_count the sub_feedback_count to set
	 */
	public void setSub_feedback_count(int sub_feedback_count) {
		this.sub_feedback_count = sub_feedback_count;
	}
	/**
	 * @return the usericon
	 */
	public String getUsericon() {
		return usericon;
	}
	/**
	 * @param usericon the usericon to set
	 */
	public void setUsericon(String usericon) {
		this.usericon = usericon;
	}

}
