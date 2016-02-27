package com.hxgwx.www.bean;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Transient;

public class SendARCBodyImages
		implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8225779284889051731L;

	private int					id;

	private String				key;

	private String				image;

	private int					bid;

	@Transient
	private boolean				upload;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBid() {
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid;
	}

	public boolean isUpload() {
		return upload;
	}

	public void setUpload(boolean upload) {
		this.upload = upload;
	}

}
