package com.hxgwx.www.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.hxgwx.www.HongxianggeApplication;
import com.hxgwx.www.R;
import com.hxgwx.www.bean.FeedbackBase;
import com.hxgwx.www.utils.ActivityManagerUtils;
import com.hxgwx.www.utils.SystemBarTintManager;
import com.hxgwx.www.web.WebPlus;
import com.hxgwx.www.webimpl.WebPlusImpl;
import com.jingzhong.asyntask2.Asyntask2;
import com.jingzhong.asyntask2.annotation.CreateView;
import com.jingzhong.asyntask2.utils.Utils;
import com.umeng.message.PushAgent;

public class PostCommentActivity extends Activity
		implements
			ActivityManagerUtils {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_comment);
		SystemBarTintManager.init(this);
		add();
		vid = new veriable();
		title = getIntent().getStringExtra("title");
		id = getIntent().getIntExtra("id", 0) + "";
		type = getIntent().getIntExtra("type", 0);

		vid.submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				messge = vid.content.getText().toString();
				if (TextUtils.isEmpty(messge.trim())) {
					return;
				}
				if(messge.length()>500){
					Utils.showShortToast(getApplicationContext(), "字数太长，系统限定在500字以内");
					return;
				}
				if (progress == null) {
					progress = new ProgressDialog(PostCommentActivity.this,
							ProgressDialog.THEME_HOLO_LIGHT);
				}
				View vw = LayoutInflater.from(PostCommentActivity.this)
						.inflate(R.layout.layout_load_pb, null);
				Utils.showProgress(progress, "请稍后……", vw);
				task.execute("");
			}
		});

		web = new WebPlusImpl(this);
		
		PushAgent.getInstance(this).onAppStart();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Utils.cancelProgress(progress);
		super.onDestroy();
	}

	private WebPlus web;

	String title = "";

	String id = "";

	String messge = "";

	/**
	 * 0为评论，1为回复
	 */
	int type = 0;

	veriable vid;

	class veriable {
		public veriable() {
			// TODO Auto-generated constructor stub
			try {
				Utils.injectObject(this, PostCommentActivity.this, R.id.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@CreateView("content")
		EditText content;
		@CreateView("attache")
		Button attache;
		@CreateView("submit")
		Button submit;
	}

	private ProgressDialog progress;

	Task task = new Task();

	class Task extends Asyntask2<String, String, Boolean> {

		@Override
		protected Boolean doInbackProgres(String... params) {
			// TODO Auto-generated method stub
			FeedbackBase base = null;
			switch (type) {
				case 0 :
					base = web.sendFeedback(Integer.valueOf(id),
							HongxianggeApplication.getInstance().getUser()
									.getData().getUname(), title,
							HongxianggeApplication.getInstance().getUser()
									.getData().getUserid(), messge, "mobile",
							HongxianggeApplication.getInstance().getUser()
									.getData().getFace());
					if (base != null) {
						if (base.isStatu()) {
							return true;
						} else {
							String p = HongxianggeApplication.getInstance()
									.getDescriptionByCode(base.getCode() + "");
							updateProgress(p);
							return false;
						}
					}
					break;
				case 1 :
					base = web.sendReply(Integer.valueOf(id),
							HongxianggeApplication.getInstance().getUser()
									.getData().getUserid(),
							HongxianggeApplication.getInstance().getUser()
									.getData().getUname(), title, messge);
					if (base != null) {
						if (base.isStatu()) {
							return true;
						} else {
							String p = HongxianggeApplication.getInstance()
									.getDescriptionByCode(base.getCode() + "");
							updateProgress(p);
							return false;
						}
					}
					break;

				default :
					break;
			}

			return null;
		}
		@Override
		protected void doProgress(String... p) {
			// TODO Auto-generated method stub
			super.doProgress(p);
			Utils.showShortToast(getApplicationContext(), p[0]);
		}

		@Override
		protected void doResult(Boolean res) {
			// TODO Auto-generated method stub
			super.doResult(res);
			Utils.cancelProgress(progress);
			if (res == null) {
				Utils.showShortToast(getApplicationContext(), "发送失败");
			} else {
				if (res == true) {
					switch (type) {
						case 0 :
							Utils.showShortToast(getApplicationContext(),
									"发送成功");
							Intent intent = new Intent(
									PostCommentActivity.this,
									CommentActivity.class);
							intent.putExtra("id", Integer.parseInt(id));
							// 进入评论列表
							startActivity(intent);
							finish();
							break;
						case 1 :
							Utils.showShortToast(getApplicationContext(),
									"发送成功");
							intent = new Intent(PostCommentActivity.this,
									ReplyActivity.class);
							intent.putExtra("id", Integer.parseInt(id));
							// 进入评论列表
							startActivity(intent);
							finish();
							break;
						default :
							break;
					}

				}
			}
		}

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

	public void onBack(View v) {
		finish();
	}

}
