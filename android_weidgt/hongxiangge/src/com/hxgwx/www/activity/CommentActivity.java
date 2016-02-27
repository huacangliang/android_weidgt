package com.hxgwx.www.activity;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hxgwx.www.HongxianggeApplication;
import com.hxgwx.www.R;
import com.hxgwx.www.adapter.CommentAdapter;
import com.hxgwx.www.bean.FeedbackTopBase;
import com.hxgwx.www.utils.ActivityManagerUtils;
import com.hxgwx.www.utils.SystemBarTintManager;
import com.hxgwx.www.web.WebPlus;
import com.hxgwx.www.webimpl.WebPlusImpl;
import com.jingzhong.asyntask2.Asyntask2;
import com.jingzhong.asyntask2.annotation.CreateView;
import com.jingzhong.asyntask2.utils.Utils;
import com.umeng.message.PushAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

public class CommentActivity extends Activity implements ActivityManagerUtils {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment);
		SystemBarTintManager.init(this);
		vid = new Veriable();

		aid = getIntent().getIntExtra("id", 0);

		init();
		add();
		
		PushAgent.getInstance(this).onAppStart();
	}

	private int page = 1;
	private int size = 20;

	private int aid;

	Veriable vid;
	private WebPlus web;
	private boolean complet;

	class Veriable {
		public Veriable() {
			// TODO Auto-generated constructor stub
			try {
				Utils.injectObject(this, CommentActivity.this, R.id.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@CreateView("adLayout")
		LinearLayout adLayout;
		@CreateView("plv_comm_list")
		PullToRefreshListView plv_comm_list;
		@CreateView("et_speak")
		EditText et_speak;
	}

	private void init() {
		initAd();
		web = new WebPlusImpl(this);
		initData();

		vid.et_speak.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!HongxianggeApplication.getInstance().isLogin) {
					Intent intent = new Intent(CommentActivity.this,
							LoginActivity.class);
					startActivity(intent);
					Toast.makeText(CommentActivity.this, "请登录", 1000).show();
					return;
				}
				Intent intent = new Intent(CommentActivity.this,
						PostCommentActivity.class);
				intent.putExtra("id", aid);
				intent.putExtra("title", HongxianggeApplication.getInstance()
						.getUser().getData().getUname()
						+ "发表的评论");
				intent.putExtra("type", 0);
				// 触发发布评论按钮
				startActivity(intent);
				finish();
			}
		});
	}

	private void initAd() {
		// TODO Auto-generated method stub
		// 实例化广告条
		AdView adView = new AdView(this, AdSize.SIZE_468x60);

		// 获取要嵌入广告条的布局
		// 将广告条加入到布局中
		vid.adLayout.addView(adView);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void initData() {
		complet = false;
		adapter = new CommentAdapter(data, this);
		View empty = LinearLayout.inflate(this, R.layout.empty, null);
		vid.plv_comm_list.setEmptyView(empty);
		vid.plv_comm_list.setMode(Mode.BOTH);
		vid.plv_comm_list.getLoadingLayoutProxy(false, true).setPullLabel(
				"上拉加载更多");
		vid.plv_comm_list.getLoadingLayoutProxy(false, true)
				.setRefreshingLabel("加载中……");
		vid.plv_comm_list.getLoadingLayoutProxy(true, false)
				.setRefreshingLabel("正在刷新……");
		vid.plv_comm_list.getLoadingLayoutProxy(true, false).setReleaseLabel(
				"放开以刷新");
		vid.plv_comm_list.getLoadingLayoutProxy(false, true).setReleaseLabel(
				"放开加载更多");

		vid.plv_comm_list.setRefreshing();

		vid.plv_comm_list.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				if (!complet) {
					vid.plv_comm_list.onRefreshComplete();
				} else {
					complet = false;
					page = 1;
					query.execute(true);
				}

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				if (!complet) {
					vid.plv_comm_list.onRefreshComplete();
				} else {
					complet = false;
					page++;
					query.execute(false);
				}

			}
		});

		vid.plv_comm_list.setAdapter(adapter);

		query.execute(true);
	}

	CommentAdapter adapter;

	Query query = new Query();

	private List<Object> data = new ArrayList<Object>();

	class Query extends Asyntask2<Boolean, String, Boolean> {
		int code;
		@Override
		protected Boolean doInbackProgres(Boolean... params) {
			// TODO Auto-generated method stub
			FeedbackTopBase base = web.queryFeedback(aid, page, size);
			if (base != null) {
				if (base.isStatu()) {
					if (params[0]) {
						data.clear();
					}
					if (base.getData() != null) {
						data.addAll(base.getData());
					} else {
						// 没有更多数据
						if (!params[0])
							updateProgress("没有更多了");
					}
					return true;
				} else {
					code = base.getCode();
					return false;
				}
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
			complet = true;
			vid.plv_comm_list.onRefreshComplete();
			if (res == null) {
				Utils.showShortToast(getApplicationContext(), "未知错误");
				return;
			}

			if (res) {
				adapter.notifyDataSetChanged();
			} else {
				String msg = HongxianggeApplication.getInstance()
						.getDescriptionByCode(code + "");
				Utils.showShortToast(getApplicationContext(), msg);
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
