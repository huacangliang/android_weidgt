package com.hxgwx.www.bean;

import java.io.Serializable;

public class UserBase implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String mid;
	private String mtype;
	private String userid;
	private String pwd;
	private String uname;
	private String sex;
	private String rank;
	private String uptime;
	private String exptime;
	private String money;
	private String email;
	private String scores;
	private String matt;
	private String spacesta;
	private String face;
	private String safequestion;
	private String safeanswer;
	private String jointime;
	private String joinip;
	private String logintime;
	private String loginip;
	private String checkmail;
	private String sessionId;
	/**
	 * @return the mid
	 */
	public synchronized final String getMid() {
		return mid;
	}
	/**
	 * @param mid
	 *            the mid to set
	 */
	public synchronized final void setMid(String mid) {
		this.mid = mid;
	}
	/**
	 * @return the mtype
	 */
	public synchronized final String getMtype() {
		return mtype;
	}
	/**
	 * @param mtype
	 *            the mtype to set
	 */
	public synchronized final void setMtype(String mtype) {
		this.mtype = mtype;
	}
	/**
	 * @return the userid
	 */
	public synchronized final String getUserid() {
		return userid;
	}
	/**
	 * @param userid
	 *            the userid to set
	 */
	public synchronized final void setUserid(String userid) {
		this.userid = userid;
	}
	/**
	 * @return the pwd
	 */
	public synchronized final String getPwd() {
		return pwd;
	}
	/**
	 * @param pwd
	 *            the pwd to set
	 */
	public synchronized final void setPwd(String pwd) {
		this.pwd = pwd;
	}
	/**
	 * @return the uname
	 */
	public synchronized final String getUname() {
		return uname;
	}
	/**
	 * @param uname
	 *            the uname to set
	 */
	public synchronized final void setUname(String uname) {
		this.uname = uname;
	}
	/**
	 * @return the sex
	 */
	public synchronized final String getSex() {
		return sex;
	}
	/**
	 * @param sex
	 *            the sex to set
	 */
	public synchronized final void setSex(String sex) {
		this.sex = sex;
	}
	/**
	 * @return the rank
	 */
	public synchronized final String getRank() {
		return rank;
	}
	/**
	 * @param rank
	 *            the rank to set
	 */
	public synchronized final void setRank(String rank) {
		this.rank = rank;
	}
	/**
	 * @return the uptime
	 */
	public synchronized final String getUptime() {
		return uptime;
	}
	/**
	 * @param uptime
	 *            the uptime to set
	 */
	public synchronized final void setUptime(String uptime) {
		this.uptime = uptime;
	}
	/**
	 * @return the exptime
	 */
	public synchronized final String getExptime() {
		return exptime;
	}
	/**
	 * @param exptime
	 *            the exptime to set
	 */
	public synchronized final void setExptime(String exptime) {
		this.exptime = exptime;
	}
	/**
	 * @return the money
	 */
	public synchronized final String getMoney() {
		return money;
	}
	/**
	 * @param money
	 *            the money to set
	 */
	public synchronized final void setMoney(String money) {
		this.money = money;
	}
	/**
	 * @return the email
	 */
	public synchronized final String getEmail() {
		return email;
	}
	/**
	 * @param email
	 *            the email to set
	 */
	public synchronized final void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the scores
	 */
	public synchronized final String getScores() {
		return scores;
	}
	/**
	 * @param scores
	 *            the scores to set
	 */
	public synchronized final void setScores(String scores) {
		this.scores = scores;
	}
	/**
	 * @return the matt
	 */
	public synchronized final String getMatt() {
		return matt;
	}
	/**
	 * @param matt
	 *            the matt to set
	 */
	public synchronized final void setMatt(String matt) {
		this.matt = matt;
	}
	/**
	 * @return the spacesta
	 */
	public synchronized final String getSpacesta() {
		return spacesta;
	}
	/**
	 * @param spacesta
	 *            the spacesta to set
	 */
	public synchronized final void setSpacesta(String spacesta) {
		this.spacesta = spacesta;
	}
	/**
	 * @return the face
	 */
	public synchronized final String getFace() {
		return face;
	}
	/**
	 * @param face
	 *            the face to set
	 */
	public synchronized final void setFace(String face) {
		this.face = face;
	}
	/**
	 * @return the safequestion
	 */
	public synchronized final String getSafequestion() {
		return safequestion;
	}
	/**
	 * @param safequestion
	 *            the safequestion to set
	 */
	public synchronized final void setSafequestion(String safequestion) {
		this.safequestion = safequestion;
	}
	/**
	 * @return the safeanswer
	 */
	public synchronized final String getSafeanswer() {
		return safeanswer;
	}
	/**
	 * @param safeanswer
	 *            the safeanswer to set
	 */
	public synchronized final void setSafeanswer(String safeanswer) {
		this.safeanswer = safeanswer;
	}
	/**
	 * @return the jointime
	 */
	public synchronized final String getJointime() {
		return jointime;
	}
	/**
	 * @param jointime
	 *            the jointime to set
	 */
	public synchronized final void setJointime(String jointime) {
		this.jointime = jointime;
	}
	/**
	 * @return the joinip
	 */
	public synchronized final String getJoinip() {
		return joinip;
	}
	/**
	 * @param joinip
	 *            the joinip to set
	 */
	public synchronized final void setJoinip(String joinip) {
		this.joinip = joinip;
	}
	/**
	 * @return the logintime
	 */
	public synchronized final String getLogintime() {
		return logintime;
	}
	/**
	 * @param logintime
	 *            the logintime to set
	 */
	public synchronized final void setLogintime(String logintime) {
		this.logintime = logintime;
	}
	/**
	 * @return the loginip
	 */
	public synchronized final String getLoginip() {
		return loginip;
	}
	/**
	 * @param loginip
	 *            the loginip to set
	 */
	public synchronized final void setLoginip(String loginip) {
		this.loginip = loginip;
	}
	/**
	 * @return the checkmail
	 */
	public synchronized final String getCheckmail() {
		return checkmail;
	}
	/**
	 * @param checkmail
	 *            the checkmail to set
	 */
	public synchronized final void setCheckmail(String checkmail) {
		this.checkmail = checkmail;
	}
	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}
	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
