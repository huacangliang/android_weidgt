package com.hxgwx.www.bean;

import java.io.Serializable;

public class RegisterParams implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dopost = "regbase";
	private String step="1";
	private String mtype="¸öÈË";
	private String userid;
	private String uname;
	private String userpwd;
	private String userpwdok;
	private String email;
	private String sex;

	/**
	 * @return the dopost
	 */
	public String getDopost() {
		return dopost;
	}
	/**
	 * @param dopost
	 *            the dopost to set
	 */
	public void setDopost(String dopost) {
		this.dopost = dopost;
	}
	/**
	 * @return the step
	 */
	public String getStep() {
		return step;
	}
	/**
	 * @param step
	 *            the step to set
	 */
	public void setStep(String step) {
		this.step = step;
	}
	/**
	 * @return the mtype
	 */
	public String getMtype() {
		return mtype;
	}
	/**
	 * @param mtype
	 *            the mtype to set
	 */
	public void setMtype(String mtype) {
		this.mtype = mtype;
	}
	/**
	 * @return the userid
	 */
	public String getUserid() {
		return userid;
	}
	/**
	 * @param userid
	 *            the userid to set
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}
	/**
	 * @return the uname
	 */
	public String getUname() {
		return uname;
	}
	/**
	 * @param uname
	 *            the uname to set
	 */
	public void setUname(String uname) {
		this.uname = uname;
	}
	/**
	 * @return the userpwd
	 */
	public String getUserpwd() {
		return userpwd;
	}
	/**
	 * @param userpwd
	 *            the userpwd to set
	 */
	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}
	/**
	 * @return the userpwdok
	 */
	public String getUserpwdok() {
		return userpwdok;
	}
	/**
	 * @param userpwdok
	 *            the userpwdok to set
	 */
	public void setUserpwdok(String userpwdok) {
		this.userpwdok = userpwdok;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}
	/**
	 * @param sex
	 *            the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

}
