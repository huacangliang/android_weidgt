package com.jingzhong.asyntask2.ui;

import com.jingzhong.asyntask2.Asyntask2;
import com.jingzhong.asyntask2.R;
import com.jingzhong.asyntask2.annotation.CreateView;
import com.jingzhong.asyntask2.listenear.Defaultlistenear2;
import com.jingzhong.asyntask2.utils.MultiThreadDownLoadUtils;
import com.jingzhong.asyntask2.utils.ThreadService;
import com.jingzhong.asyntask2.utils.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * @author dengzhijiang
 * 
 */
public class MainActivity extends Activity {

	@CreateView(value = "progressBar1")
	private ProgressBar	progressBar1;

	@CreateView(value = "progressBar2")
	private ProgressBar	progressBar2;

	@CreateView(value = "progressBar3")
	private ProgressBar	progressBar3;

	@CreateView(value = "progressBar4")
	private ProgressBar	progressBar4;

	@CreateView(value = "tv1")
	private TextView	tv1;

	@CreateView(value = "tv2")
	private TextView	tv2;

	@CreateView(value = "tv3")
	private TextView	tv3;

	@CreateView(value = "tv4")
	private TextView	tv4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			Utils.injectObject(this, this, R.id.class);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		progressBar1.setOnClickListener(clickListener);
		progressBar2.setOnClickListener(clickListener);
		progressBar3.setOnClickListener(clickListener);
		progressBar4.setOnClickListener(clickListener);

	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// switch (v.getId()) {
			// case R.id.progressBar1:
			//
			// break;
			//
			// case R.id.progressBar2:
			//
			// break;
			//
			// case R.id.progressBar3:
			//
			// break;
			//
			// case R.id.progressBar4:
			//
			// break;
			//
			// default:
			// break;
			// }

		}
	};

	protected void onStart() {
		ThreadService.getInstance().clear();
		download = new MultiDownload();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				download.execute();
			}
		}, 1000);
		super.onStart();
	};

	MultiDownload	download;

	ProgressDialog	pDialog;

	private void showProgressAlert() {
		// TODO Auto-generated method stub
		if (pDialog != null && pDialog.isShowing()) { return; }
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(true);
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.setMessage("下载中……");
		pDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				download.cancel();
			}
		});
		pDialog.show();
	}

	public class MultiDownload extends Asyntask2<String, String, String> {

		boolean						isfnish	= false;

		MultiThreadDownLoadUtils	utils;

		String						result;

		@Override
		protected void doStart() {
			// TODO Auto-generated method stub
			super.doStart();
			showProgressAlert();
		}

		@Override
		protected String doInbackProgres(String... params) {
			// TODO Auto-generated method stub
			utils = new MultiThreadDownLoadUtils(getApplicationContext());
			utils.download("", Environment.getExternalStorageDirectory()
				.getAbsolutePath(), "http://218.108.192.209/1Q2W3E4R5T6Y7U8I9O0P1Z2X3C4V5B/4.down.119g.com/4/705DD392AD982C7CA0CA28A8DE774D39E6230A20/DXC%e9%87%87%e9%9b%86%e5%95%86%e4%b8%9a%e7%89%88%20vip2.5%20(milu_pick).rar", new Defaultlistenear2() {

					@Override
					public void onSuccess(String path, String url) {
						// TODO Auto-generated method stub
						result = path + ":" + url;
						isfnish = true;
					}

					@Override
					public void onError(String url, Exception e, int status) {
						// TODO Auto-generated method stub
						isfnish = true;
						if (e != null) {
							e.printStackTrace();
						}
					}

					@Override
					public boolean continueDownload(String filePath) {
						// TODO Auto-generated method stub
						return true;
					}

					@Override
					public void onDwonloadListener(long progress, long tatol, long s, String speend, String utime, String stime, int tid) {
						// TODO Auto-generated method stub
						double p = (((double) progress) / ((double) tatol)) * 100;
						String msg = "当前进度：" + Utils
							.retain2(p) + "% " + speend + "kb/s 已用时：" + utime + " 开始时间：" + stime;
						Log.d("onDwonloadListener", msg);
						updateProgress(msg);
					}
				});
			while (!isfnish) {
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			return result;
		}

		@Override
		protected void doProgress(String... p) {
			// TODO Auto-generated method stub
			super.doProgress(p);
			pDialog.setMessage(p[0]);
		}

		@Override
		public void cancel() {
			// TODO Auto-generated method stub
			utils.stop();
		}

		@Override
		protected void doResult(String res) {
			// TODO Auto-generated method stub
			super.doResult(res);
			if (res == null) {
				pDialog.setMessage("下载失败");
				pDialog.setProgress(100);
			}
			else {
				pDialog.setMessage("下载成功：" + res);
				pDialog.setProgress(100);
			}
		}
	}
}
