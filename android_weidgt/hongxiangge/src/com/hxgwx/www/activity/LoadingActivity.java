package com.hxgwx.www.activity;

import com.hxgwx.www.HongxianggeApplication;
import com.hxgwx.www.R;
import com.hxgwx.www.utils.ActivityManagerUtils;
import com.jingzhong.asyntask2.Asyntask2;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;

public class LoadingActivity extends Activity implements ActivityManagerUtils {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		application = (HongxianggeApplication) getApplication();
		add();

		setContentView(R.layout.loading);

		SpotManager.getInstance(this).showSpotAds(this, new SpotDialogListener() {
			@Override
			public void onShowSuccess() {
				Log.i("Youmi", "onShowSuccess");

			}

			@Override
			public void onShowFailed() {
				Log.i("Youmi", "onShowFailed");

			}

			@Override
			public void onSpotClosed() {
				Log.e("sdkDemo", "closed");
			}
		});

		new Thread() {
			public void run() {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				application.setAppk();
				choiceActitivity();
			};
		}.start();
		PushAgent mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
		PushAgent.getInstance(this).onAppStart();
	}

	Login login = new Login();
	private HongxianggeApplication application;

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	public void choiceActitivity() {
		if (HongxianggeApplication.getInstance().isFirst())
			SpotManager.getInstance(LoadingActivity.this).showSpotAds(LoadingActivity.this);

		login.execute();
	}

	private class Login extends Asyntask2<String, Integer, String> {

		@Override
		protected String doInbackProgres(String... params) {
			// TODO Auto-generated method stub
			if (HongxianggeApplication.getInstance().isFirst()) {

				try {
					Thread.sleep(5 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			} else {
				HongxianggeApplication.getInstance().setFirst(true);
				try {
					Thread.sleep(2 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void doResult(String res) {
			// TODO Auto-generated method stub
			super.doResult(res);
			SpotManager.getInstance(LoadingActivity.this).disMiss();
			Intent i = new Intent(getApplicationContext(), MainFragmentActivity.class);
			startActivity(i);
			finish();
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		SpotManager.getInstance(LoadingActivity.this).disMiss();
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
