package com.ks.im;

import java.util.Date;

public class Message{
    private Integer id;

    private Integer uid;

    private String fromPhoneNo;
    
    private String toPhoneNo;

    private String fromName;

    private String time=Utils.defaultDataFormat.format(new Date());

    private String msg;
    
    private int type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getFromPhoneNo() {
        return fromPhoneNo;
    }

    public void setFromPhoneNo(String fromPhoneNo) {
        this.fromPhoneNo = fromPhoneNo == null ? null : fromPhoneNo.trim();
    }
    
    public String getToPhoneNo() {
        return toPhoneNo;
    }

    public void setToPhoneNo(String toPhoneNo) {
        this.toPhoneNo = toPhoneNo == null ? null : toPhoneNo.trim();
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName == null ? null : fromName.trim();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time == null ? null : time.trim();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg == null ? null : msg.trim();
    }

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}