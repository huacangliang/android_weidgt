package com.hxgwx.www.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.hxgwx.www.HongxianggeApplication;
import com.hxgwx.www.db.ArcMoelDBhelper;
import com.jingzhong.asyntask2.utils.Utils;
import com.lidroid.xutils.db.annotation.Transient;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

public class SendARCBody
		implements Serializable {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1L;

	private int						id;

	private String					dopost				= "save";

	private String					channelid			= "1";

	private String					title;

	private String					tags				= "";

	private String					writer;

	private String					typeid;

	private String					description;

	private String					body;

	private String					fmod				= "autologin";

	private String					mid;

	private String					litpic;

	@Transient
	private List<SendARCBodyImages>	images				= new ArrayList<SendARCBodyImages>();

	@Transient
	SendARCBody						bodyTemp;

	public List<SendARCBodyImages> getImages(boolean query) {
		if (query) {
			try {
				ArcMoelDBhelper adb = ArcMoelDBhelper.getInstance(HongxianggeApplication.getInstance());
				bodyTemp = adb.createDb().findFirst(Selector.from(SendARCBody.class).where("title", "=", title)
						.and("mid", "=", HongxianggeApplication.getInstance().getUser().getData().getMid()));
				if (bodyTemp != null) {
					images = adb.createDb()
							.findAll(Selector.from(SendARCBodyImages.class).where("bid", "=", bodyTemp.getId()));
				}
			}
			catch (DbException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return images;
	}

	public void setImages(List<SendARCBodyImages> images) {
		this.images = images;
	}
	
	public void delete(ArcMoelDBhelper adb){
		try {
			adb.createDb().delete(SendARCBodyImages.class, WhereBuilder.b("bid", "=", getId()));
			adb.createDb().delete(this);
		}
		catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void save(ArcMoelDBhelper adb) {
		try {
			SendARCBody bt = adb.createDb().findFirst(Selector.from(SendARCBody.class).where("title", "=", title)
					.and("mid", "=", HongxianggeApplication.getInstance().getUser().getData().getMid()));
			if (bt != null) {
				adb.createDb().delete(bt);
			}
		}
		catch (DbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			adb.createDb().save(this);
			Utils.showLongToast(HongxianggeApplication.getInstance(), "±£´æ³É¹¦");
			try {
				SendARCBody bt = adb.createDb().findFirst(Selector.from(SendARCBody.class).where("title", "=", title)
						.and("mid", "=", HongxianggeApplication.getInstance().getUser().getData().getMid()));
				if (bt != null) {
					for (int i = 0; i < this.images.size(); i++) {
						SendARCBodyImages image = this.images.get(i);
						image.setBid(bt.getId());
						adb.createDb().save(image);
					}
				}
			}
			catch (DbException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the mid
	 */
	public String getMid() {
		return mid;
	}

	/**
	 * @param mid
	 *            the mid to set
	 */
	public void setMid(String mid) {
		this.mid = mid;
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
	 * @return the channelid
	 */
	public String getChannelid() {
		return channelid;
	}

	/**
	 * @param channelid
	 *            the channelid to set
	 */
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the tags
	 */
	public String getTags() {
		return tags;
	}

	/**
	 * @param tags
	 *            the tags to set
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}

	/**
	 * @return the writer
	 */
	public String getWriter() {
		return writer;
	}

	/**
	 * @param writer
	 *            the writer to set
	 */
	public void setWriter(String writer) {
		this.writer = writer;
	}

	/**
	 * @return the typeid
	 */
	public String getTypeid() {
		return typeid;
	}

	/**
	 * @param typeid
	 *            the typeid to set
	 */
	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body
	 *            the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the fmod
	 */
	public String getFmod() {
		return fmod;
	}

	/**
	 * @param fmod
	 *            the fmod to set
	 */
	public void setFmod(String fmod) {
		this.fmod = fmod;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	public String getLitpic() {
		return litpic;
	}

	public void setLitpic(String litpic) {
		this.litpic = litpic;
	}
}
