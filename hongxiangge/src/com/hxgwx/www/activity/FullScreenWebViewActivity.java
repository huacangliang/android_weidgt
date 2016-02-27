package com.hxgwx.www.activity;

import com.hxgwx.www.R;
import com.jingzhong.asyntask2.utils.ThreadService;
import com.umeng.message.PushAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class FullScreenWebViewActivity extends Activity {
	public static String content;
	public static int bg;
	public static int font;

	public static void start(String content, Context ctx, int bg, int font) {
		FullScreenWebViewActivity.content = content;
		FullScreenWebViewActivity.bg = bg;
		FullScreenWebViewActivity.font = font;
		ctx.startActivity(new Intent(ctx, FullScreenWebViewActivity.class));
	}

	private volatile boolean waitDouble = true;
	private static final int DOUBLE_CLICK_TIME = 350; // 两次单击的时间间隔
	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (waitDouble == true) {
				waitDouble = false;
				Thread thread = new Thread() {
					@Override
					public void run() {
						try {
							sleep(DOUBLE_CLICK_TIME);
							if (waitDouble == false) {
								waitDouble = true;
								singleClick();
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
				ThreadService.getInstance().executeThread(thread);
			} else {
				waitDouble = true;
				doubleClick();
			}
		}
	};

	// 单击响应事件
	private void singleClick() {
		
	}

	// 双击响应事件
	private void doubleClick() {
		finish();
	}

	boolean thouch = false;

	@Override
	protected void onDestroy() {
		scrullWebView.destroy();
		scrullWebView=null;
		super.onDestroy();
	}

	WebView scrullWebView;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		scrullWebView = new WebView(this);
		WebSettings set = scrullWebView.getSettings();
		set.setJavaScriptEnabled(true);
		set.setDefaultTextEncodingName("UTF-8");
		set.setBlockNetworkLoads(true);
		set.setBlockNetworkImage(true);
		set.setSupportZoom(true); // 允许缩放
		set.setAllowContentAccess(true);
		set.setAllowFileAccess(true);
		set.setAppCacheEnabled(true);
		set.setAppCachePath(
				com.hxgwx.www.utils.Utils.getExtenalPath(getApplicationContext(), getPackageName() + "/data/cache"));
		set.setBuiltInZoomControls(true);
		set.setCacheMode(WebSettings.LOAD_DEFAULT);
		set.setDatabaseEnabled(true);
		set.setDatabasePath(
				com.hxgwx.www.utils.Utils.getExtenalPath(getApplicationContext(), getPackageName() + "/data/cache"));
		set.setDisplayZoomControls(true);
		set.setDomStorageEnabled(true);
		set.setSaveFormData(true);
		set.setJavaScriptCanOpenWindowsAutomatically(true);
		set.setTextZoom(font);
		scrullWebView.setBackgroundColor(bg);
		if (TextUtils.isEmpty(content)) {
			scrullWebView.loadData("<div>没有获取内容</div>", "text/html; charset=UTF-8", null);
		} else {
			scrullWebView.loadData(content, "text/html; charset=UTF-8", null);
		}
		
		setContentView(scrullWebView);
		com.hxgwx.www.utils.Utils.showGuide(this, R.drawable.hxg_index_full);
		
		PushAgent.getInstance(this).onAppStart();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN)
			thouch = false;

		if (event.getAction() == MotionEvent.ACTION_MOVE)
			thouch = true;

		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (!thouch) {
				listener.onClick(null);
				return true;
			}
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
	
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add("退出全屏模式");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getTitle().toString().equals("退出全屏模式")) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
