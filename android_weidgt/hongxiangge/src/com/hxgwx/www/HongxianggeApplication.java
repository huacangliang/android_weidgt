package com.hxgwx.www;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.hongxiangge.image.MD5;
import com.hxgwx.www.activity.LoginActivity;
import com.hxgwx.www.bean.UserLogin;
import com.hxgwx.www.utils.Utils;
import com.sohu.cyan.android.sdk.api.Config;
import com.sohu.cyan.android.sdk.api.CyanSdk;
import com.sohu.cyan.android.sdk.exception.CyanException;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.text.TextUtils;
import net.youmi.android.AdManager;
import net.youmi.android.spot.SpotManager;

@SuppressWarnings("deprecation")
public class HongxianggeApplication extends android.app.Application {

	private Map<String, String> errorCodeDescript = new HashMap<String, String>();

	private Map<String, String> httpUrl = new HashMap<String, String>();

	private static String domain = "";

	private static String phone_id;

	private static String appkey;

	public static void setAppkey(String appkey) {
		HongxianggeApplication.appkey = appkey;
	}

	private HttpClient httpClient;
	public boolean isLogin = false;

	private UserLogin user;

	private CyanSdk cyanSdk;

	// 畅言登录过时，单位秒
	private long expires_in;

	private String changyan_AccessToken;

	private String changyan_AuthorizationCode;

	private String changyan_Code;

	/**
	 * @return the domain
	 */
	public synchronized final String getDomain() {
		return domain;
	}

	/**
	 * @return the errorCodeDescript
	 */
	public synchronized final Map<String, String> getErrorCodeDescript() {
		return errorCodeDescript;
	}

	private boolean first = false;

	public String getDescriptionByCode(String code) {
		String res = errorCodeDescript.get(code);
		if (res == null)
			return "未知错误";

		return res;
	}

	/**
	 * 初始化代码描述
	 */
	private void initCodeDescript() {

		try {
			XmlPullParser xml = XmlPullParserFactory.newInstance().newPullParser();
			String res = Utils.readXMLToString_Config(getApplicationContext(), "code.xml");
			xml.setInput(new ByteArrayInputStream(res.getBytes()), "utf-8");
			int event = xml.getEventType();
			String key = "";
			String value = "";

			while (event != XmlPullParser.END_DOCUMENT) {
				event = xml.next();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:

					switch (xml.getName()) {
					case "key":
						key = xml.nextText();
						break;
					case "value":
						value = xml.nextText();
						errorCodeDescript.put(key, value);
						break;

					default:
						break;
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				default:
					break;
				}
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 初始化服务器接口
	 */
	private void initURL() {
		try {
			XmlPullParser xml = XmlPullParserFactory.newInstance().newPullParser();
			String res = Utils.decodeRes(getApplicationContext(), "config.hx").trim();
			xml.setInput(new ByteArrayInputStream(res.getBytes()), "utf-8");
			int event = xml.getEventType();
			String key = "";
			String value = "";

			while (event != XmlPullParser.END_DOCUMENT) {
				event = xml.next();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:

					switch (xml.getName()) {
					case "key":
						key = xml.nextText();
						break;
					case "path":
						value = xml.nextText();
						httpUrl.put(key, value);
						break;

					default:
						break;
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				default:
					break;
				}
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 初始化入口
	 */
	private void init() {
		new Thread() {
			public void run() {
				AdManager.getInstance(HongxianggeApplication.this).init("b071f4f5d84dd3d6", "28aee1064415fdbf", false);
				httpClient = HongxianggeApplication.this.createHttpClient();
				initCodeDescript();
				initDomain();
				initURL();
				readPhoneId();
				SpotManager.getInstance(HongxianggeApplication.this).loadSpotAds();
				SpotManager.getInstance(HongxianggeApplication.this)
						.setSpotOrientation(SpotManager.ORIENTATION_PORTRAIT);
			};
		}.start();
	}

	/**
	 * 初始化主机地址
	 */
	private void initDomain() {
		try {
			ApplicationInfo info = getPackageManager().getApplicationInfo(getPackageName(),
					PackageManager.GET_META_DATA);
			domain = info.metaData.getString("domain");
			changyan_AccessToken = info.metaData.getString("changyan_AccessToken");
			changyan_AuthorizationCode = info.metaData.getString("changyan_AuthorizationCode");
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 读取手机身份标识
	 */
	private void readPhoneId() {
		phone_id = com.jingzhong.asyntask2.utils.Utils.autoSetPhoneMoberId(getApplicationContext());
	}

	public void setAppk() {
		//setAppkey(MD5.Md5(Utils.getSign(this)));
		setAppkey("61ccc05058da9b0ccd73df8a46428a22");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
		init();
		initChangyan();
	}

	/**
	 * @return the httpUrl
	 */
	public Map<String, String> getHttpUrl() {
		return httpUrl;
	}

	/**
	 * @return the phone_id
	 */
	public String getPhone_id() {
		return phone_id;
	}

	/**
	 * @return the appkey
	 */
	public String getAppkey() {
		return appkey;
	}

	public String getPackegePath() {
		Context context = getApplicationContext();

		return context.getPackageResourcePath();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		this.shutdownHttpClient();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		this.shutdownHttpClient();
	}

	private HttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);
		HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
		HttpConnectionParams.setSoTimeout(params, 20 * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

		ClientConnectionManager connMgr = new ThreadSafeClientConnManager(params, schReg);

		return new DefaultHttpClient(connMgr, params);
	}

	private void shutdownHttpClient() {
		if (httpClient != null && httpClient.getConnectionManager() != null) {
			httpClient.getConnectionManager().shutdown();
		}
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	/**
	 * @return the user
	 */
	public UserLogin getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(UserLogin user) {
		this.user = user;
		if (user != null && user.isStatu() && user.getData() != null) {
			isLogin = true;
		} else {
			isLogin = false;
		}
	}

	/**
	 * @return the cyanSdk
	 */
	public CyanSdk getCyanSdk() {
		if (cyanSdk == null) {
			initChangyan();
		}
		return cyanSdk;
	}

	/**
	 * @param cyanSdk
	 *            the cyanSdk to set
	 */
	public void setCyanSdk(CyanSdk cyanSdk) {
		this.cyanSdk = cyanSdk;
	}

	private void initChangyan() {
		Config config = new Config();
		config.ui.toolbar_bg = Color.WHITE;
		config.comment.showScore = true;
		config.comment.uploadFiles = true;
		config.comment.anonymous_token = "V_gId7Nnjuq7a8kNkmv8ll6O655A3pMPs2Qe-Q9FUEM";
		config.ui.style = "indent";
		config.ui.depth = 1;
		config.ui.sub_size = 20;
		config.login.Custom_oauth_login = true;
		config.login.SSOLogin = false;
		config.login.QQ = false;
		config.login.SINA = false;
		config.login.loginActivityClass = LoginActivity.class;
		try {
			CyanSdk.register(this, "cyrM1z0io", "c262f16a04e71bf8202b38cade3df988", "http://www.hxgwx.com", config);
		} catch (CyanException e) {
			e.printStackTrace();
		}
		cyanSdk = CyanSdk.getInstance(this);

	}

	private static HongxianggeApplication instance;

	public static HongxianggeApplication getInstance() {
		return instance;
	}

	/**
	 * @return the expires_in
	 */
	public long getExpires_in() {
		if (expires_in == 0) {
			getSharedPreferences(getPackageName(), Context.MODE_PRIVATE).getLong("expires_in", 0);
		}
		return expires_in;
	}

	/**
	 * @param expires_in
	 *            the expires_in to set
	 */
	public void setExpires_in(long expires_in) {
		getSharedPreferences(getPackageName(), Context.MODE_PRIVATE).edit().putLong("expires_in", expires_in).commit();
		getSharedPreferences(getPackageName(), Context.MODE_PRIVATE).edit()
				.putLong("expires_now", System.currentTimeMillis()).commit();
		this.expires_in = expires_in;
	}

	/**
	 * 畅言登录时间是否超时
	 * 
	 * @return
	 */
	public boolean aouth_time_out() {
		long saveTime = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE).getLong("", 0);
		long ei = getExpires_in();
		if (saveTime == 0 || ei == 0)
			return true;

		long newTime = System.currentTimeMillis();
		long diff = newTime - saveTime;
		if (diff < ei) {
			return false;
		}
		return true;
	}

	/**
	 * @return the changyan_AccessToken
	 */
	public String getChangyan_AccessToken() {
		return changyan_AccessToken;
	}

	/**
	 * @param changyan_AccessToken
	 *            the changyan_AccessToken to set
	 */
	public void setChangyan_AccessToken(String changyan_AccessToken) {
		this.changyan_AccessToken = changyan_AccessToken;
	}

	/**
	 * @return the changyan_AuthorizationCode
	 */
	public String getChangyan_AuthorizationCode() {
		return changyan_AuthorizationCode;
	}

	/**
	 * @param changyan_AuthorizationCode
	 *            the changyan_AuthorizationCode to set
	 */
	public void setChangyan_AuthorizationCode(String changyan_AuthorizationCode) {
		this.changyan_AuthorizationCode = changyan_AuthorizationCode;
	}

	/**
	 * @return the changyan_Code
	 */
	public String getChangyan_Code() {
		if (TextUtils.isEmpty(changyan_Code)) {
			changyan_Code = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE).getString("changyan_Code", "");
		}
		return changyan_Code;
	}

	/**
	 * @param changyan_Code
	 *            the changyan_Code to set
	 */
	public void setChangyan_Code(String changyan_Code) {
		if (TextUtils.isEmpty(changyan_Code)) {
			getSharedPreferences(getPackageName(), Context.MODE_PRIVATE).edit()
					.putString("changyan_Code", changyan_Code).commit();
		}
		this.changyan_Code = changyan_Code;
	}

	public boolean isFirst() {
		first = getSharedPreferences("first", Context.MODE_PRIVATE).getBoolean("first", false);
		return first;
	}

	public void setFirst(boolean first) {
		getSharedPreferences("first", Context.MODE_PRIVATE).edit().putBoolean("first", first).apply();
		this.first = first;
	}

}