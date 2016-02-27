package com.hxgwx.www.activity;

import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hxgwx.www.HongxianggeApplication;
import com.hxgwx.www.R;
import com.hxgwx.www.bean.BaseBean;
import com.hxgwx.www.bean.RegisterParams;
import com.hxgwx.www.http.HttpUtils;
import com.hxgwx.www.utils.ActivityManagerUtils;
import com.hxgwx.www.utils.SystemBarTintManager;
import com.hxgwx.www.utils.Utils;
import com.hxgwx.www.utils.Utils.CheckIsEmptySTR;
import com.jingzhong.asyntask2.Asyntask2;
import com.jingzhong.asyntask2.annotation.CreateView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

public class RegisterActivity extends Activity
		implements
			CheckIsEmptySTR,
			ActivityManagerUtils {

	@CreateView("et_user_name")
	private EditText et_user_name;
	@CreateView("et_user_uname")
	private EditText et_user_uname;
	@CreateView("et_user_pwd")
	private EditText et_user_pwd;
	@CreateView("et_user_ypwd")
	private EditText et_user_ypwd;
	@CreateView("EditText04")
	private EditText EditText04;// email
	@CreateView("radioGroup1")
	private RadioGroup radioGroup1;
	@CreateView("bt_register")
	private Button bt_register;

	private String user_name;
	private String user_uname;
	private String user_pwd;
	private String user_ypwd;
	private String email;
	private String sex;

	private HongxianggeApplication application;

	private register reg = new register();

	private int fromLgon = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		add();
		setContentView(R.layout.register);
		SystemBarTintManager.init(this);
		try {
			com.jingzhong.asyntask2.utils.Utils.injectObject(this, this,
					R.id.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fromLgon = getIntent().getIntExtra("from", -1);
		application = (HongxianggeApplication) getApplication();
		
		PushAgent.getInstance(this).onAppStart();
	}
	
private ProgressDialog progress;
	
	private void showCheck() {
		View v = LayoutInflater.from(this).inflate(R.layout.layout_load_pb,
				null);
		progress = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		progress.setCancelable(true);
		progress.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub

			}
		});

		progress.setCanceledOnTouchOutside(false);
		com.jingzhong.asyntask2.utils.Utils.showProgress(progress, "请稍后……", v);

	}

	public void onRegister(View v) {
		// TODO Auto-generated method stub
		user_name = et_user_name.getText().toString();
		if (isE(user_name)) {
			Utils.showLongToast(this, "用户名不能为空");
			et_user_name.setSelected(true);
			return;
		}
		user_uname = et_user_uname.getText().toString();
		if (isE(user_name)) {
			Utils.showLongToast(this, "笔名不能为空");
			et_user_uname.setSelected(true);
			return;
		}

		user_pwd = et_user_pwd.getText().toString();
		if (isE(user_name)) {
			Utils.showLongToast(this, "密码不能为空");
			et_user_pwd.setSelected(true);
			return;
		}

		user_ypwd = et_user_ypwd.getText().toString();
		if (isE(user_name)) {
			Utils.showLongToast(this, "确认密码不能为空");
			et_user_ypwd.setSelected(true);
			return;
		}

		email = EditText04.getText().toString();
		if (isE(user_name)) {
			Utils.showLongToast(this, "邮箱不能为空");
			et_user_name.setSelected(true);
			return;
		}

		if (!Utils.checkEmail(email)) {
			Utils.showLongToast(this, "邮箱格式不对，例如abc@qq.com");
			et_user_name.setSelected(true);
			return;
		}

		RadioButton rb = (RadioButton) findViewById(radioGroup1
				.getCheckedRadioButtonId());
		sex = rb.getText().toString();
		showCheck();
		reg.execute("");
	}

	private class register extends Asyntask2<String, String, BaseBean> {

		@Override
		protected BaseBean doInbackProgres(String... params) {
			// TODO Auto-generated method stub
			HttpUtils http = new HttpUtils();
			Map<String, Object> param = null;
			String url = application.getDomain()
					+ application.getHttpUrl().get("register");

			RegisterParams bean = new RegisterParams();
			bean.setEmail(email);
			bean.setUname(user_uname);
			bean.setUserid(user_name);
			bean.setUserpwd(user_pwd);
			bean.setUserpwdok(user_ypwd);
			bean.setSex(sex);
			param = Utils.getValueMap(bean);
			// param.put("appkey", application.getAppkey());
			try {
				String res = http.doPost(url, param, application.getAppkey());

				if (res == null) {
					updateProgress("未知错误，请检测网络连接是否正常");
					return null;
				}
				BaseBean bean2 = (BaseBean) Utils.jsonPasreToObject(
						BaseBean.class, res);
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
			Utils.showLongToast(getApplicationContext(), p[0]);
		}

		@Override
		protected void doResult(BaseBean res) {
			// TODO Auto-generated method stub
			super.doResult(res);
			progress.dismiss();
			if (res == null)
				return;
			if (res.isStatu()) {
				Utils.showLongToast(getApplicationContext(), "注册成功");
				if (fromLgon == 2) {
					Intent i = new Intent();
					i.putExtra("from", 1);
					// TODO Auto-generated method stub
					com.hxgwx.www.utils.Utils.gotoActivity(
							getApplicationContext(), LoginActivity.class, i);
				}
				finish();
			} else {
				Utils.showLongToast(getApplicationContext(), "注册失败:"
						+ application.getDescriptionByCode(res.getCode() + ""));
			}
		}
	}

	public void onLogin(View v) {
		// TODO Auto-generated method stub
		if (fromLgon == 1)
			finish();
		else
			startActivity(new Intent(getApplicationContext(),
					LoginActivity.class));
	}

	public void onBack(View v) {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public boolean isE(String str) {
		// TODO Auto-generated method stub
		return TextUtils.isEmpty(str);
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
