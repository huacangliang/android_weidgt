package com.hxgwx.www.activity;

import java.io.File;

import com.hxgwx.www.HongxianggeApplication;
import com.hxgwx.www.R;
import com.hxgwx.www.bean.CheckVersionBean;
import com.hxgwx.www.dwon.DwonLoad;
import com.hxgwx.www.dwon.DwonLoad.OnDwonListener;
import com.hxgwx.www.utils.ActivityManagerUtils;
import com.hxgwx.www.utils.SystemBarTintManager;
import com.hxgwx.www.web.WebPlus;
import com.hxgwx.www.webimpl.WebPlusImpl;
import com.jingzhong.asyntask2.Asyntask2;
import com.jingzhong.asyntask2.utils.Utils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends Activity implements ActivityManagerUtils {
	private Button bt_check;
	private CheckVersionBean bean;
	private HongxianggeApplication application;
	private AlertDialog.Builder dialog;
	private ProgressDialog progress;
	private ProgressDialog dwonProgress;
	private TextView tv_version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		add();
		setContentView(R.layout.about);
		SystemBarTintManager.init(this);
		web = new WebPlusImpl(this);
		bt_check = (Button) findViewById(R.id.bt_check);
		application = (HongxianggeApplication) getApplication();
		bt_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//showCheck();
				umUpdate();
			}
		});
		String name = getCodeName();
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_version.setText(name);
		PushAgent.getInstance(this).onAppStart();
	}

	private void showUpdate() {
		dialog = new Builder(this, AlertDialog.THEME_HOLO_LIGHT);
		final AlertDialog alDialog = dialog.create();

		View v = LayoutInflater.from(this).inflate(R.layout.update_version, null);
		TextView dec = (TextView) v.findViewById(R.id.tv_dec);
		dec.setText(bean.getDescrption() + "\n版本：" + bean.getVersion());
		Button update = (Button) v.findViewById(R.id.bt_update);
		Button cancel = (Button) v.findViewById(R.id.bt_cancel);
		update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alDialog.dismiss();
				showDwon();
				dwonApk();

			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alDialog.dismiss();
			}
		});
		alDialog.show();
		alDialog.setContentView(v);

	}

	Query query = new Query();

	private void showCheck() {
		View v = LayoutInflater.from(this).inflate(R.layout.layout_load_pb, null);
		progress = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		progress.setCancelable(true);
		progress.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub

				//query.cancel();

			}
		});
		progress.setCanceledOnTouchOutside(true);
		//query.execute(getCode());
		Utils.showProgress(progress, "请稍后……", v);

	}

	@SuppressWarnings("deprecation")
	private void showDwon() {
		// if (progress == null) {
		View v = LayoutInflater.from(this).inflate(R.layout.layout_load_pb, null);
		dwonProgress = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		dwonProgress.setCancelable(false);
		// }

		dwonProgress.setButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dwonProgress.dismiss();
			}
		});

		dwonProgress.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub

				if (dwon != null && !dwon.isCancel()) {
					dwon.setCancel(true);

				}

			}
		});
		Utils.showProgress(dwonProgress, "请稍后……", v);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (dwon != null && !dwon.isCancel()) {
			progress.cancel();
			dwon.setCancel(true);
		}
	}

	private String getCode() {
		PackageManager packageManager = getPackageManager();
		PackageInfo packageInfo = null;
		try {
			packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return packageInfo.versionCode + "";
	}

	private String getCodeName() {
		PackageManager packageManager = getPackageManager();
		PackageInfo packageInfo = null;
		try {
			packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return packageInfo.versionName;
	}

	public class Query extends Asyntask2<String, String, CheckVersionBean> {

		@Override
		protected CheckVersionBean doInbackProgres(String... params) {
			// TODO Auto-generated method stub

			CheckVersionBean tem = web.checkVersion(params[0]);
			if (tem == null) {
				updateProgress("未知错误，请检测网络连接是否稳定");
			}
			return tem;
		}

		@Override
		protected void doProgress(String... p) {
			// TODO Auto-generated method stub
			super.doProgress(p);

			com.hxgwx.www.utils.Utils.showLongToast(getApplicationContext(), p[0]);

		}

		@Override
		protected void doResult(CheckVersionBean res) {
			// TODO Auto-generated method stub
			super.doResult(res);
			Utils.cancelProgress(progress);
			if (res != null) {
				if (!res.isStatu()) {
					String des = application.getDescriptionByCode(res.getCode() + "");
					com.hxgwx.www.utils.Utils.showLongToast(getApplicationContext(), des);
					return;
				}
				if (res.getData() != null) {
					bean = res.getData().get(0);
					showUpdate();
				} else {
					com.hxgwx.www.utils.Utils.showLongToast(getApplicationContext(), "已是最新版本");
				}
			}

		}
	}

	private WebPlus web;

	private Handler updateDwon = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x64) {
				String dwon = msg.getData().getString("dwon");
				String tatol = msg.getData().getString("total");
				dwonProgress.setMessage(dwon + "/" + tatol);
			}
			if (msg.what == 0x10) {
				dwonProgress.dismiss();
				installApk(path);
			}
			if (msg.what == 0x12) {
				dwonProgress.dismiss();
				Utils.showLongToast(getApplicationContext(), "下载出错");
			}
		};
	};

	DwonLoad dwon;
	private String path;

	private void dwonApk() {
		
//		String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.hxgwx/";
//		dir = Utils.checkFilePath(getPackageName() + ".apk", this, "com.hxgwx");
//		dwon = new DwonLoad(dir, application.getDomain() + bean.getPath(), this);
//		path = dir;
//		dwon.setSetOnDwonListener(new OnDwonListener() {
//
//			@Override
//			public void setOnDwoningStatus(int status) {
//				// TODO Auto-generated method stub
//				if (status == DwonLoad.DWON_ERROR) {
//					updateDwon.sendEmptyMessage(0x12);
//				}
//			}
//
//			@Override
//			public void setOnDwoningProgress(int progress) {
//				// TODO Auto-generated method stub
//				if (progress == 100) {
//					updateDwon.sendEmptyMessage(0x10);
//				}
//			}
//
//			@Override
//			public void setOnDwoningProgress(float dwon, float total) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void setOnDwoningProgress(String dwon, String total) {
//				Message msg = new Message();
//				// TODO Auto-generated method stub
//				msg.what = 0x64;
//				Bundle data = new Bundle();
//				data.putString("dwon", dwon);
//				data.putString("total", total);
//				msg.setData(data);
//				updateDwon.sendMessage(msg);
//			}
//		});
//		dwon.start();
	}

	private void umUpdate() {
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
		    @Override
		    public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
		    	Utils.cancelProgress(progress);
		    	UmengUpdateAgent.setUpdateListener(null);
		    	UmengUpdateAgent.setUpdateAutoPopup(true);
		        switch (updateStatus) {
		        case UpdateStatus.Yes: // has update
		            UmengUpdateAgent.showUpdateDialog(AboutActivity.this, updateInfo);
		            break;
		        case UpdateStatus.No: // has no update
		        	bt_check.setText("已是最新版本");
		            Toast.makeText(AboutActivity.this, "没有检测到新版本", Toast.LENGTH_SHORT).show();
		            break;
		        case UpdateStatus.NoneWifi: // none wifi
		            Toast.makeText(AboutActivity.this, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
		            break;
		        case UpdateStatus.Timeout: // time out
		            Toast.makeText(AboutActivity.this, "获取版本信息超时", Toast.LENGTH_SHORT).show();
		            break;
		        }
		    }
		});
		showCheck();
		UmengUpdateAgent.forceUpdate(this);
	}

	private void installApk(String path) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	
	public void onLook(View view){
		Intent i = new Intent();
		i.setData(Uri.parse(HongxianggeApplication.getInstance().getDomain()));
		i.setAction("android.intent.action.VIEW");
		startActivity(i);
	}

	public void onBack(View v) {
		// TODO Auto-generated method stub

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
