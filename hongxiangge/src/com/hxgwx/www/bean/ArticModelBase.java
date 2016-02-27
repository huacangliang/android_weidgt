package com.hxgwx.www.bean;

import java.io.Serializable;
import java.util.Date;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "ArticModelBase")
public class ArticModelBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(column = "id")
	@NoAutoIncrement
	private int id;
	@Column(column = "typeid")
	private String typeid;
	@Column(column = "typeid2")
	private String typeid2;
	@Column(column = "sortrank")
	private Long sortrank;
	@Column(column = "flag")
	private String flag;
	@Column(column = "ismake")
	private int ismake;
	@Column(column = "channel")
	private int channel;
	@Column(column = "arcrank")
	private int arcrank;
	@Column(column = "click")
	private int click;
	@Column(column = "money")
	private int money;
	@Column(column = "title")
	private String title;
	@Column(column = "shorttitle")
	private String shorttitle;
	@Column(column = "color")
	private String color;
	@Column(column = "writer")
	private String writer;
	@Column(column = "source")
	private String source;
	@Column(column = "litpic")
	private String litpic;
	@Column(column = "pubdate")
	private Long pubdate;
	@Column(column = "senddate")
	private Long senddate;
	@Column(column = "mid")
	private Integer mid;
	@Column(column = "keywords")
	private String keywords;
	@Column(column = "lastpost")
	private Long lastpost;
	@Column(column = "scores")
	private Integer scores;
	@Column(column = "goodpost")
	private Integer goodpost;
	@Column(column = "badpost")
	private Integer badpost;
	@Column(column = "voteid")
	private Integer voteid;
	@Column(column = "notpost")
	private Integer notpost;
	@Column(column = "description")
	private String description;
	@Column(column = "filename")
	private String filename;
	@Column(column = "dutyadmin")
	private String dutyadmin;
	@Column(column = "tackid")
	private Integer tackid;
	@Column(column = "mtype")
	private Integer mtype;
	@Column(column = "weight")
	private Integer weight;
	@Column(column = "readTime")
	private Date readTime;
	@Column(column = "loginId")
	private String loginId;
	private boolean isChecked=false;
	/**
	 * @return the loginId
	 */
	public String getLoginId() {
		return loginId;
	}
	/**
	 * @param loginId
	 *            the loginId to set
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
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
	 * @return the typeid2
	 */
	public String getTypeid2() {
		return typeid2;
	}
	/**
	 * @param typeid2
	 *            the typeid2 to set
	 */
	public void setTypeid2(String typeid2) {
		this.typeid2 = typeid2;
	}
	/**
	 * @return the sortrank
	 */
	public Long getSortrank() {
		return sortrank;
	}
	/**
	 * @param sortrank
	 *            the sortrank to set
	 */
	public void setSortrank(Long sortrank) {
		this.sortrank = sortrank;
	}
	/**
	 * @return the flag
	 */
	public String getFlag() {
		return flag;
	}
	/**
	 * @param flag
	 *            the flag to set
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}
	/**
	 * @return the ismake
	 */
	public int getIsmake() {
		return ismake;
	}
	/**
	 * @param ismake
	 *            the ismake to set
	 */
	public void setIsmake(int ismake) {
		this.ismake = ismake;
	}
	/**
	 * @return the channel
	 */
	public int getChannel() {
		return channel;
	}
	/**
	 * @param channel
	 *            the channel to set
	 */
	public void setChannel(int channel) {
		this.channel = channel;
	}
	/**
	 * @return the arcrank
	 */
	public int getArcrank() {
		return arcrank;
	}
	/**
	 * @param arcrank
	 *            the arcrank to set
	 */
	public void setArcrank(int arcrank) {
		this.arcrank = arcrank;
	}
	/**
	 * @return the click
	 */
	public int getClick() {
		return click;
	}
	/**
	 * @param click
	 *            the click to set
	 */
	public void setClick(int click) {
		this.click = click;
	}
	/**
	 * @return the money
	 */
	public int getMoney() {
		return money;
	}
	/**
	 * @param money
	 *            the money to set
	 */
	public void setMoney(int money) {
		this.money = money;
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
	 * @return the shorttitle
	 */
	public String getShorttitle() {
		return shorttitle;
	}
	/**
	 * @param shorttitle
	 *            the shorttitle to set
	 */
	public void setShorttitle(String shorttitle) {
		this.shorttitle = shorttitle;
	}
	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}
	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(String color) {
		this.color = color;
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
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the litpic
	 */
	public String getLitpic() {
		return litpic;
	}
	/**
	 * @param litpic
	 *            the litpic to set
	 */
	public void setLitpic(String litpic) {
		this.litpic = litpic;
	}
	/**
	 * @return the pubdate
	 */
	public Long getPubdate() {

		return pubdate;
	}
	/**
	 * @param pubdate
	 *            the pubdate to set
	 */
	public void setPubdate(Long pubdate) {
		this.pubdate = pubdate;
	}
	/**
	 * @return the senddate
	 */
	public Long getSenddate() {
		return senddate;
	}
	/**
	 * @param senddate
	 *            the senddate to set
	 */
	public void setSenddate(Long senddate) {
		this.senddate = senddate;
	}
	/**
	 * @return the mid
	 */
	public Integer getMid() {
		return mid;
	}
	/**
	 * @param mid
	 *            the mid to set
	 */
	public void setMid(Integer mid) {
		this.mid = mid;
	}
	/**
	 * @return the keywords
	 */
	public String getKeywords() {
		return keywords;
	}
	/**
	 * @param keywords
	 *            the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	/**
	 * @return the lastpost
	 */
	public Long getLastpost() {
		return lastpost;
	}
	/**
	 * @param lastpost
	 *            the lastpost to set
	 */
	public void setLastpost(Long lastpost) {
		this.lastpost = lastpost;
	}
	/**
	 * @return the scores
	 */
	public Integer getScores() {
		return scores;
	}
	/**
	 * @param scores
	 *            the scores to set
	 */
	public void setScores(Integer scores) {
		this.scores = scores;
	}
	/**
	 * @return the goodpost
	 */
	public Integer getGoodpost() {
		return goodpost;
	}
	/**
	 * @param goodpost
	 *            the goodpost to set
	 */
	public void setGoodpost(Integer goodpost) {
		this.goodpost = goodpost;
	}
	/**
	 * @return the badpost
	 */
	public Integer getBadpost() {
		return badpost;
	}
	/**
	 * @param badpost
	 *            the badpost to set
	 */
	public void setBadpost(Integer badpost) {
		this.badpost = badpost;
	}
	/**
	 * @return the voteid
	 */
	public Integer getVoteid() {
		return voteid;
	}
	/**
	 * @param voteid
	 *            the voteid to set
	 */
	public void setVoteid(Integer voteid) {
		this.voteid = voteid;
	}
	/**
	 * @return the notpost
	 */
	public Integer getNotpost() {
		return notpost;
	}
	/**
	 * @param notpost
	 *            the notpost to set
	 */
	public void setNotpost(Integer notpost) {
		this.notpost = notpost;
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
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * @param filename
	 *            the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**
	 * @return the dutyadmin
	 */
	public String getDutyadmin() {
		return dutyadmin;
	}
	/**
	 * @param dutyadmin
	 *            the dutyadmin to set
	 */
	public void setDutyadmin(String dutyadmin) {
		this.dutyadmin = dutyadmin;
	}
	/**
	 * @return the tackid
	 */
	public Integer getTackid() {
		return tackid;
	}
	/**
	 * @param tackid
	 *            the tackid to set
	 */
	public void setTackid(Integer tackid) {
		this.tackid = tackid;
	}
	/**
	 * @return the mtype
	 */
	public Integer getMtype() {
		return mtype;
	}
	/**
	 * @param mtype
	 *            the mtype to set
	 */
	public void setMtype(Integer mtype) {
		this.mtype = mtype;
	}
	/**
	 * @return the weight
	 */
	public Integer getWeight() {
		return weight;
	}
	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	/**
	 * @return the readTime
	 */
	public Date getReadTime() {
		return readTime;
	}
	/**
	 * @param readTime
	 *            the readTime to set
	 */
	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
}
