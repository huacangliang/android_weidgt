package com.hxgwx.www.bean;

public class LoginParams {
	private String userid;
	private String pwd;
	private String fmdo = "login2";
	private String dopost = "login2";
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
	 * @return the pwd
	 */
	public String getPwd() {
		return pwd;
	}
	/**
	 * @param pwd
	 *            the pwd to set
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	/**
	 * @return the fmdo
	 */
	public String getFmdo() {
		return fmdo;
	}
	/**
	 * @param fmdo
	 *            the fmdo to set
	 */
	public void setFmdo(String fmdo) {
		this.fmdo = fmdo;
	}
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
	 * @return the gourl
	 */
	public String getGourl() {
		return Gourl;
	}
	/**
	 * @param gourl
	 *            the gourl to set
	 */
	public void setGourl(String gourl) {
		Gourl = gourl;
	}
	/**
	 * @return the form1
	 */
	public String getForm1() {
		return form1;
	}
	/**
	 * @param form1
	 *            the form1 to set
	 */
	public void setForm1(String form1) {
		this.form1 = form1;
	}
	private String Gourl;
	private String form1;
}
