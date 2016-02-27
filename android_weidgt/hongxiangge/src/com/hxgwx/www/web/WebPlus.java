package com.hxgwx.www.web;

import com.hxgwx.www.bean.ArticBody;
import com.hxgwx.www.bean.ArticList;
import com.hxgwx.www.bean.ArticTypeList;
import com.hxgwx.www.bean.BaseBean;
import com.hxgwx.www.bean.CheckVersionBean;
import com.hxgwx.www.bean.FeedbackBase;
import com.hxgwx.www.bean.FeedbackSubBase;
import com.hxgwx.www.bean.FeedbackTopBase;
import com.hxgwx.www.bean.LoginParams;
import com.hxgwx.www.bean.RegisterParams;
import com.hxgwx.www.bean.SendARCBody;
import com.hxgwx.www.bean.UserLogin;
import com.jingzhong.asyntask2.utils.HttpUtils.OnNetListenear;

public interface WebPlus {
	/**
	 * 登录
	 * 
	 * @param params
	 * @return
	 */
	UserLogin login(LoginParams params);
	/**
	 * 注册
	 * 
	 * @param params
	 * @return
	 */
	BaseBean register(RegisterParams params);
	/**
	 * 分类列表
	 * 
	 * @return
	 */
	ArticTypeList queryArticType();
	/**
	 * 根据分类查询文章列表
	 * 
	 * @param typeid
	 * @param page
	 * @param size
	 * @return
	 */
	ArticList queryArticList(String typeid, int page, int size);
	/**
	 * 根据文章id查询文章
	 * 
	 * @param aid
	 * @return
	 */
	ArticBody queryArtic(String aid);

	/**
	 * 推荐
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	ArticList recommend(int page, int size);
	
	/**
	 * 最新更新列表
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	ArticList newUpdate(int page, int size);

	/**
	 * 排行榜
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	ArticList ranking(int page, int size);

	/**
	 * 我的文章列表
	 * 
	 * @param mid
	 * @param page
	 * @param size
	 * @return
	 */
	ArticList myarticle(String mid, int page, int size);

	/**
	 * 搜索文章
	 * 
	 * @param q
	 * @return
	 */
	ArticList search(String q);

	/**
	 * 发表文章
	 * 
	 * @param body
	 * @return
	 */
	BaseBean publicArc(SendARCBody body);
	/**
	 * 检测版本升级
	 * 
	 * @param versioncode
	 * @return
	 */
	CheckVersionBean checkVersion(String versioncode);

	/**
	 * 发送评论
	 * 
	 * @param aid
	 * @param uname
	 * @param arctitle
	 * @param uid
	 * @param message
	 * @param ftype
	 * @param usericon
	 * @return
	 */
	FeedbackBase sendFeedback(int aid, String uname, String arctitle, String uid,
			String message, String ftype, String usericon);
	/**
	 * 发送回复
	 * 
	 * @param tf_id
	 * @param uid
	 * @param uname
	 * @param tf_title
	 * @param message
	 * @return
	 */
	FeedbackBase sendReply(int tf_id,String uid,String uname, String tf_title, String message);
	/**
	 * 查询评论
	 * 
	 * @param aid
	 * @param page
	 * @param size
	 * @return
	 */
	FeedbackTopBase queryFeedback(int aid, int page, int size);
	/**
	 * 查询回复
	 * 
	 * @param tf_id
	 * @param page
	 * @param size
	 * @return
	 */
	FeedbackSubBase queryReply(int tf_id, int page, int size);
	/**
	 * 删除评论
	 * 
	 * @param id
	 * @return
	 */
	FeedbackBase deleteFeedback(int id);
	/**
	 * 删除回复
	 * 
	 * @param id
	 * @return
	 */
	FeedbackBase deleteReply(int id);
	
	FeedbackBase queryFeedbackCount(int aid);
	
	void editFace(String mid,String path,String oldPath,OnNetListenear listenear);
}
