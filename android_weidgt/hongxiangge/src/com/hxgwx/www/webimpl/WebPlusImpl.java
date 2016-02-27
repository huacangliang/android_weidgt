package com.hxgwx.www.webimpl;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.hxgwx.www.HongxianggeApplication;
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
import com.hxgwx.www.http.HttpUtils;
import com.hxgwx.www.utils.UploadImageUtils;
import com.hxgwx.www.utils.Utils;
import com.hxgwx.www.web.WebPlus;
import com.jingzhong.asyntask2.utils.HttpUtils.OnNetListenear;

import android.app.Activity;
import android.graphics.Bitmap;

public class WebPlusImpl
		implements WebPlus {

	HttpUtils						http	= new HttpUtils();

	private HongxianggeApplication	application;

	public WebPlusImpl(Activity context) {
		application = (HongxianggeApplication) context.getApplication();
	}

	@Override
	public UserLogin login(LoginParams params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseBean register(RegisterParams params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArticTypeList queryArticType() {
		// TODO Auto-generated method stub
		String url = application.getDomain() + application.getHttpUrl().get("arctype");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("version", "1");
		String res = http.doPost(url, params, application.getAppkey());
		if (res == null) return null;
		ArticTypeList list = (ArticTypeList) Utils.jsonPasreToObject(ArticTypeList.class, res);
		return list;
	}

	@Override
	public ArticList queryArticList(String typeid, int page, int size) {
		String url = application.getDomain() + application.getHttpUrl().get("arc_list");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("typeid", typeid);
		params.put("page", "" + page);
		params.put("size", "" + size);
		String res = http.doPost(url, params, application.getAppkey());
		if (res == null) return null;
		ArticList list = (ArticList) Utils.jsonPasreToObject(ArticList.class, res);
		return list;
	}

	@Override
	public ArticBody queryArtic(String aid) {
		// TODO Auto-generated method stub
		String url = application.getDomain() + application.getHttpUrl().get("arc_body");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("aid", aid);
		String res = http.doPost(url, params, application.getAppkey());
		if (res == null) return null;
		ArticBody body = (ArticBody) Utils.jsonPasreToObject(ArticBody.class, res);
		return body;
	}

	@Override
	public ArticList recommend(int page, int size) {
		// TODO Auto-generated method stub
		String url = application.getDomain() + application.getHttpUrl().get("recommend");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("page", "" + page);
		params.put("size", "" + size);
		String res = http.doPost(url, params, application.getAppkey());
		if (res == null) return null;
		ArticList list = (ArticList) Utils.jsonPasreToObject(ArticList.class, res);
		return list;
	}

	@Override
	public ArticList ranking(int page, int size) {
		// TODO Auto-generated method stub
		String url = application.getDomain() + application.getHttpUrl().get("ranking");
		String appkey = application.getAppkey();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("page", "" + page);
		params.put("size", "" + size);
		String res = http.doPost(url, params, appkey);
		if (res == null) return null;
		ArticList list = (ArticList) Utils.jsonPasreToObject(ArticList.class, res);
		return list;
	}

	@Override
	public ArticList myarticle(String mid, int page, int size) {
		// TODO Auto-generated method stub
		String url = application.getDomain() + application.getHttpUrl().get("myarticle");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("page", "" + page);
		params.put("size", "" + size);
		params.put("mid", mid);
		String res = http.doPost(url, params, application.getAppkey());
		if (res == null) return null;
		ArticList list = (ArticList) Utils.jsonPasreToObject(ArticList.class, res);
		return list;
	}

	@Override
	public ArticList search(String q) {
		// TODO Auto-generated method stub
		String url = application.getDomain() + application.getHttpUrl().get("search");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("q", "" + q);
		String res = http.doPost(url, params, application.getAppkey());
		if (res == null) return null;
		ArticList list = (ArticList) Utils.jsonPasreToObject(ArticList.class, res);
		return list;
	}

	@Override
	public BaseBean publicArc(SendARCBody body) {
		// TODO Auto-generated method stub
		if (application.getUser().getData().getRank().equals("-1")) {
			return null;
		}
		String url = application.getDomain() + application.getHttpUrl().get("send");
		Map<String, Object> params = Utils.getValueMapObj(body);
		String tPath = null;
		if (body.getLitpic() != null) {
			Bitmap bit = UploadImageUtils.getBitmapFromPath(body.getLitpic(), 180, 240);

			InputStream is = com.jingzhong.asyntask2.utils.Utils.Bitmap2IS(bit);
			try {
				tPath = com.jingzhong.asyntask2.utils.Utils.save(is,
						Utils.getExtenalPath(application, application.getPackageName() + "/data/cache/image/"),
						new File(body.getLitpic()).getName());
				params.put("litpic", new File(tPath));
			}
			catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		params.put("appkey", HongxianggeApplication.getInstance().getAppkey());
		String res = http.doPost(url, params, true);
		if (tPath != null) {
			new File(tPath).delete();
		}
		if (res == null) return null;
		BaseBean bean = (BaseBean) Utils.jsonPasreToObject(BaseBean.class, res);
		return bean;
	}

	@Override
	public CheckVersionBean checkVersion(String versioncode) {
		// TODO Auto-generated method stub
		String url = application.getDomain() + application.getHttpUrl().get("checkversion");
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("versioncode", versioncode);

		String res = http.doPost(url, params, application.getAppkey());
		if (res == null) return null;
		CheckVersionBean bean = (CheckVersionBean) Utils.jsonPasreToObject(CheckVersionBean.class, res);
		return bean;
	}

	@Override
	public FeedbackBase sendFeedback(int aid, String uname, String arctitle, String uid, String message, String ftype, String usericon) {
		// TODO Auto-generated method stub
		if (application.getUser().getData().getRank().equals("-1")) {
			return null;
		}
		String url = application.getDomain() + application.getHttpUrl().get("feedback");
		String fmod = "send_feedback_top";
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("aid", aid + "");
		params.put("uname", uname);
		params.put("arctitle", arctitle);
		params.put("uid", uid);
		params.put("message", message + "");
		params.put("ftype", ftype);
		params.put("fmod", fmod);
		params.put("usericon", usericon);

		String res = http.doPost(url, params, application.getAppkey());
		if (res == null) return null;
		FeedbackBase bean = (FeedbackBase) Utils.jsonPasreToObject(FeedbackBase.class, res);
		return bean;
	}

	@Override
	public FeedbackBase sendReply(int tf_id, String uid, String uname, String tf_title, String message) {
		// TODO Auto-generated method stub
		if (application.getUser().getData().getRank().equals("-1")) {
			return null;
		}
		String url = application.getDomain() + application.getHttpUrl().get("feedback");
		String fmod = "send_feedback_sub";

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("tf_id", tf_id + "");
		params.put("tf_title", tf_title);
		params.put("message", message + "");
		params.put("fmod", fmod);
		params.put("uid", uid);
		params.put("uname", uname);
		String res = http.doPost(url, params, application.getAppkey());
		if (res == null) return null;
		FeedbackBase bean = (FeedbackBase) Utils.jsonPasreToObject(FeedbackBase.class, res);
		return bean;
	}

	@Override
	public FeedbackTopBase queryFeedback(int aid, int page, int size) {
		// TODO Auto-generated method stub
		String url = application.getDomain() + application.getHttpUrl().get("feedback");
		String fmod = "query_feedback_top";
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("aid", aid + "");
		params.put("page", page + "");
		params.put("size", size + "");
		params.put("fmod", fmod);

		String res = http.doPost(url, params, application.getAppkey());
		if (res == null) return null;
		FeedbackTopBase bean = (FeedbackTopBase) Utils.jsonPasreToObject(FeedbackTopBase.class, res);
		return bean;
	}

	@Override
	public FeedbackSubBase queryReply(int tf_id, int page, int size) {
		// TODO Auto-generated method stub
		String url = application.getDomain() + application.getHttpUrl().get("feedback");
		String fmod = "query_feedback_sub";
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("tf_id", tf_id + "");
		params.put("page", page + "");
		params.put("size", size + "");
		params.put("fmod", fmod);

		String res = http.doPost(url, params, application.getAppkey());
		if (res == null) return null;
		FeedbackSubBase bean = (FeedbackSubBase) Utils.jsonPasreToObject(FeedbackSubBase.class, res);
		return bean;
	}

	@Override
	public FeedbackBase deleteFeedback(int id) {
		// TODO Auto-generated method stub
		if (application.getUser().getData().getRank().equals("-1")) {
			return null;
		}
		String url = application.getDomain() + application.getHttpUrl().get("feedback");
		String fmod = "delete_fb_top";
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("id", id + "");
		params.put("fmod", fmod);

		String res = http.doPost(url, params, application.getAppkey());
		if (res == null) return null;
		FeedbackBase bean = (FeedbackBase) Utils.jsonPasreToObject(FeedbackBase.class, res);
		return bean;
	}

	@Override
	public FeedbackBase deleteReply(int id) {
		// TODO Auto-generated method stub
		if (application.getUser().getData().getRank().equals("-1")) {
			return null;
		}
		String url = application.getDomain() + application.getHttpUrl().get("feedback");
		String fmod = "delete_fb_sub";
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("id", id + "");
		params.put("fmod", fmod);

		String res = http.doPost(url, params, application.getAppkey());
		if (res == null) return null;
		FeedbackBase bean = (FeedbackBase) Utils.jsonPasreToObject(FeedbackBase.class, res);
		return bean;
	}

	@Override
	public FeedbackBase queryFeedbackCount(int aid) {
		// TODO Auto-generated method stub
		String url = application.getDomain() + application.getHttpUrl().get("feedback");
		String fmod = "query_feedback_top_count";
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("aid", aid + "");
		params.put("fmod", fmod);

		String res = http.doPost(url, params, application.getAppkey());
		if (res == null) return null;
		FeedbackBase bean = (FeedbackBase) Utils.jsonPasreToObject(FeedbackBase.class, res);
		return bean;
	}

	@Override
	public ArticList newUpdate(int page, int size) {
		String url = application.getDomain() + application.getHttpUrl().get("newArticle");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("page", "" + page);
		params.put("size", "" + size);
		String res = http.doPost(url, params, application.getAppkey());
		if (res == null) return null;
		ArticList list = (ArticList) Utils.jsonPasreToObject(ArticList.class, res);
		return list;
	}

	@Override
	public void editFace(String mid, String path, String oldPath, OnNetListenear listenear){
		// TODO Auto-generated method stub
		if (application.getUser().getData().getRank().equals("-1")) {
			listenear.onError(null, 0);
			return;
		}
		final String url = application.getDomain() + application.getHttpUrl().get("editFace");
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("mid", mid);
		params.put("dopost", "saveFromApp");
		params.put("oldface", oldPath);
		params.put("appkey", application.getAppkey());
		params.put("face", new File(path));
		final com.jingzhong.asyntask2.utils.HttpUtils http = new com.jingzhong.asyntask2.utils.HttpUtils();
		http.setOnNetListenear(listenear);
		http.setCharset("GBK");
		http.setTimeOut(10*1000);
		http.postMethod(application, url, params);
	}

}
