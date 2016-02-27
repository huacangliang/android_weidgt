package com.hxgwx.www.activity;

import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.hxgwx.www.HongxianggeApplication;
import com.hxgwx.www.R;
import com.hxgwx.www.bean.LoginParams;
import com.hxgwx.www.bean.UserLogin;
import com.hxgwx.www.http.HttpUtils;
import com.hxgwx.www.utils.ActivityManagerUtils;
import com.hxgwx.www.utils.SystemBarTintManager;
import com.hxgwx.www.utils.Utils.CheckIsEmptySTR;
import com.jingzhong.asyntask2.Asyntask2;
import com.jingzhong.asyntask2.annotation.CreateView;
import com.jingzhong.asyntask2.utils.Utils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

public class LoginActivity extends Activity implements ActivityManagerUtils, CheckIsEmptySTR {
	@CreateView("et_input_user")
	private EditText et_input_user;
	@CreateView("et_input_pwd")
	private EditText et_input_pwd;
	@CreateView("tv_ask_pwd")
	private TextView tv_ask_pwd;
	@CreateView("rb_remenber_pwd")
	private CheckBox rb_remenber_pwd;

	private int fromLgon = -1;

	private HongxianggeApplication application;

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	public void onLook(View view){
		Intent i = new Intent();
		i.setData(Uri.parse(HongxianggeApplication.getInstance().getDomain()));
		i.setAction("android.intent.action.VIEW");
		startActivity(i);
	}

	private ProgressDialog progress;

	private void showCheck() {
		View v = LayoutInflater.from(this).inflate(R.layout.layout_load_pb, null);
		progress = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		progress.setCancelable(true);
		progress.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				log.cancel();
			}
		});

		progress.setCanceledOnTouchOutside(false);
		Utils.showProgress(progress, "请稍后……", v);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		add();
		setContentView(R.layout.login);
		SystemBarTintManager.init(this);
		application = (HongxianggeApplication) getApplication();
		try {
			Utils.injectObject(this, this, R.id.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fromLgon = getIntent().getIntExtra("from", -1);
		tv_ask_pwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setData(Uri.parse(HongxianggeApplication.getInstance().getDomain()+"/member/resetpassword.php"));
				i.setAction("android.intent.action.VIEW");
				startActivity(i);
			}
		});
		PushAgent.getInstance(this).onAppStart();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		readConfig();
		super.onStart();
	}

	private void readConfig() {
		String tuid = "";
		String tpwd = "";
		String uid = "";
		String pwd = "";
		SharedPreferences sh = getSharedPreferences(CONFIGNAME, Context.MODE_PRIVATE);
		tuid = sh.getString("uid", null);
		tpwd = sh.getString("upwd", null);

		if (tuid != null && tpwd != null) {
			uid = new String(Base64.decode(tuid, Base64.DEFAULT));
			pwd = new String(Base64.decode(tpwd, Base64.DEFAULT));
			pwd = pwd.substring(0, pwd.length() - 3);
			pwd = new String(Base64.decode(pwd, Base64.DEFAULT));
			et_input_user.setText(uid);
			et_input_pwd.setText(pwd);
			rb_remenber_pwd.setChecked(true);
		}
	}

	public static final String CONFIGNAME = "uabxyy";

	private void saveConfig(String uid, String upwd) {
		String tuid = Base64.encodeToString(uid.getBytes(), Base64.DEFAULT);
		String tpwd = Base64.encodeToString(upwd.getBytes(), Base64.DEFAULT);
		tpwd = Base64.encodeToString((tpwd + "pwd").getBytes(), Base64.DEFAULT);
		getSharedPreferences(CONFIGNAME, Context.MODE_PRIVATE).edit().putString("uid", tuid).putString("upwd", tpwd)
				.commit();
	}

	private void clearConfig() {
		getSharedPreferences(CONFIGNAME, Context.MODE_PRIVATE).edit().putString("uid", null).putString("upwd", null)
				.commit();
	}

	public void onRegister(View v) {
		// TODO Auto-generated method stub
		Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
		i.putExtra("from", 1);
		startActivity(i);
	}

	login log = new login();

	public void onLogin(View v) {
		// TODO Auto-generated method stub
		LoginParams param = new LoginParams();
		String uid = et_input_user.getText().toString().trim();
		String pwd = et_input_pwd.getText().toString().trim();
		if (isE(uid)) {
			com.hxgwx.www.utils.Utils.showLongToast(getApplicationContext(), "用户名不能为空");
			return;
		}

		if (isE(pwd)) {
			com.hxgwx.www.utils.Utils.showLongToast(getApplicationContext(), "密码不能为空");
			return;
		}

		param.setUserid(uid);
		param.setPwd(pwd);
		showCheck();
		log.execute(param);

	}

	HttpUtils http = new HttpUtils();

	private class login extends Asyntask2<LoginParams, String, UserLogin> {
		LoginParams login;

		@Override
		protected UserLogin doInbackProgres(LoginParams... params) {
			// TODO Auto-generated method stub
			login = params[0];
			Map<String, Object> param = null;
			String url = application.getDomain() + application.getHttpUrl().get("login");
			param = com.hxgwx.www.utils.Utils.getValueMap(login);
			try {
				String res = http.doPost(url, param, application.getAppkey());

				if (res == null) {
					updateProgress("未知错误，请检测网络连接是否正常");
					return null;
				}

				UserLogin bean2 = (UserLogin) com.hxgwx.www.utils.Utils.jsonPasreToObject(UserLogin.class, res);
				if (bean2==null) {
					updateProgress("未知错误");
				}
				return bean2;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				updateProgress("未知错误，请检测网络连接是否正常");
			}
			return null;
		}

		@Override
		protected void doProgress(String... p) {
			// TODO Auto-generated method stub
			super.doProgress(p);
			com.hxgwx.www.utils.Utils.showLongToast(getApplicationContext(), p[0]);
		}

		@Override
		protected void doResult(UserLogin res) {
			// TODO Auto-generated method stub
			super.doResult(res);
			progress.dismiss();
			if (res == null)
				return;
			if (res.isStatu()) {
				application.setUser(res);
				com.hxgwx.www.utils.Utils.showLongToast(getApplicationContext(), "登录成功");

				Intent i = new Intent(getApplicationContext(), MainFragmentActivity.class);
				i.putExtra("user", res);
				startActivity(i);
				if (rb_remenber_pwd.isChecked()) {
					saveConfig(login.getUserid(), login.getPwd());
				} else {
					clearConfig();
				}

			} else {
				com.hxgwx.www.utils.Utils.showLongToast(getApplicationContext(),
						"登录失败:" + application.getDescriptionByCode(res.getCode() + ""));
			}

		}

	}

	public void onBack(View v) {

		finish();
	}

	@Override
	public void add() {
		// TODO Auto-generated method stub
		ACTIVITYS.add(this);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeAll() {
		// TODO Auto-generated method stub
		for (int i = 0; i < ACTIVITYS.size(); i++) {
			Activity ac = ACTIVITYS.get(i);
			if (!ac.isDestroyed()) {
				ac.finish();
			}
		}
	}

	@Override
	public boolean isE(String str) {
		// TODO Auto-generated method stub
		return TextUtils.isEmpty(str);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(getLocalClassName());
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(getLocalClassName());
		MobclickAgent.onPause(this);
	}
}
