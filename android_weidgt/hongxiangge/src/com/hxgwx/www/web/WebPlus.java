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
	 * ��¼
	 * 
	 * @param params
	 * @return
	 */
	UserLogin login(LoginParams params);
	/**
	 * ע��
	 * 
	 * @param params
	 * @return
	 */
	BaseBean register(RegisterParams params);
	/**
	 * �����б�
	 * 
	 * @return
	 */
	ArticTypeList queryArticType();
	/**
	 * ���ݷ����ѯ�����б�
	 * 
	 * @param typeid
	 * @param page
	 * @param size
	 * @return
	 */
	ArticList queryArticList(String typeid, int page, int size);
	/**
	 * ��������id��ѯ����
	 * 
	 * @param aid
	 * @return
	 */
	ArticBody queryArtic(String aid);

	/**
	 * �Ƽ�
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	ArticList recommend(int page, int size);
	
	/**
	 * ���¸����б�
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	ArticList newUpdate(int page, int size);

	/**
	 * ���а�
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	ArticList ranking(int page, int size);

	/**
	 * �ҵ������б�
	 * 
	 * @param mid
	 * @param page
	 * @param size
	 * @return
	 */
	ArticList myarticle(String mid, int page, int size);

	/**
	 * ��������
	 * 
	 * @param q
	 * @return
	 */
	ArticList search(String q);

	/**
	 * ��������
	 * 
	 * @param body
	 * @return
	 */
	BaseBean publicArc(SendARCBody body);
	/**
	 * ���汾����
	 * 
	 * @param versioncode
	 * @return
	 */
	CheckVersionBean checkVersion(String versioncode);

	/**
	 * ��������
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
	 * ���ͻظ�
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
	 * ��ѯ����
	 * 
	 * @param aid
	 * @param page
	 * @param size
	 * @return
	 */
	FeedbackTopBase queryFeedback(int aid, int page, int size);
	/**
	 * ��ѯ�ظ�
	 * 
	 * @param tf_id
	 * @param page
	 * @param size
	 * @return
	 */
	FeedbackSubBase queryReply(int tf_id, int page, int size);
	/**
	 * ɾ������
	 * 
	 * @param id
	 * @return
	 */
	FeedbackBase deleteFeedback(int id);
	/**
	 * ɾ���ظ�
	 * 
	 * @param id
	 * @return
	 */
	FeedbackBase deleteReply(int id);
	
	FeedbackBase queryFeedbackCount(int aid);
	
	void editFace(String mid,String path,String oldPath,OnNetListenear listenear);
}
