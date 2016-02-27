package com.hxgwx.www.bean;

import java.io.Serializable;
import java.util.List;

public class CheckVersionBean extends BaseBean implements Serializable {
	
	private List<CheckVersionBean> data;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String version;

	private String path;

	private String versioncode;

	private String descrption;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the descrption
	 */
	public String getDescrption() {
		return descrption;
	}

	/**
	 * @param descrption
	 *            the descrption to set
	 */
	public void setDescrption(String descrption) {
		this.descrption = descrption;
	}

	/**
	 * @return the versioncode
	 */
	public String getVersioncode() {
		return versioncode;
	}

	/**
	 * @param versioncode
	 *            the versioncode to set
	 */
	public void setVersioncode(String versioncode) {
		this.versioncode = versioncode;
	}

	/**
	 * @return the data
	 */
	public List<CheckVersionBean> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<CheckVersionBean> data) {
		this.data = data;
	}

}
