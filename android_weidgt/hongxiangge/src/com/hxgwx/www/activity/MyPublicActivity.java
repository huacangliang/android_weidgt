package com.hxgwx.www.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hxgwx.www.HongxianggeApplication;
import com.hxgwx.www.R;
import com.hxgwx.www.adapter.MyArcListAdapter;
import com.hxgwx.www.bean.ArticList;
import com.hxgwx.www.bean.ArticModelBase;
import com.hxgwx.www.bean.UserLogin;
import com.hxgwx.www.utils.SystemBarTintManager;
import com.hxgwx.www.web.WebPlus;
import com.hxgwx.www.webimpl.WebPlusImpl;
import com.jingzhong.asyntask2.Asyntask2;
import com.umeng.message.PushAgent;

public class MyPublicActivity extends Activity {
	protected boolean mDoing;
	private PullToRefreshListView plv_my_public;

	/**
	 * @return the plv_my_public
	 */
	public PullToRefreshListView getPlv_my_public() {
		return plv_my_public;
	}

	private HongxianggeApplication application;

	/**
	 * @return the application
	 */
	public HongxianggeApplication getApplication2() {
		return application;
	}

	private MyArcListAdapter myAdapter;

	public void onBack(View v) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_public);
		SystemBarTintManager.init(this);
		page = 1;
		application = (HongxianggeApplication) getApplication();
		user = application.getUser();
		myAdapter = new MyArcListAdapter(this, baseList);
		web = new WebPlusImpl(this);

		initView();
		
		PushAgent.getInstance(this).onAppStart();
	}

	private UserLogin user;
	private ProgressDialog progress;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initView() {
		plv_my_public = (PullToRefreshListView) findViewById(R.id.plv_my_public);
		View emptyView = LinearLayout.inflate(this, R.layout.empty_list, null);
		plv_my_public.setEmptyView(emptyView);
		plv_my_public.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
		plv_my_public.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中……");
		plv_my_public.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新……");
		plv_my_public.getLoadingLayoutProxy(true, false).setReleaseLabel("放开以刷新");
		plv_my_public.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");

		plv_my_public.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				if (mDoing) {
					return;
				}
				mDoing = true;
				plv_my_public.setMode(Mode.PULL_FROM_START);
				page = 1;
				showCheck();
				query.execute(user.getData().getMid());
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				if (mDoing) {
					return;
				}
				mDoing = true;
				page++;
				query.execute(user.getData().getMid());
			}
		});

		baseList.clear();
		plv_my_public.setMode(Mode.BOTH);
		plv_my_public.setAdapter(myAdapter);
		plv_my_public.setRefreshing();
		showCheck();
		query.execute(user.getData().getMid());
	}
	
	private void showCheck() {
		if (progress != null) {
			progress.dismiss();
		}
		View v = LayoutInflater.from(this).inflate(R.layout.layout_load_pb, null);
		progress = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		progress.setCancelable(true);
		progress.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub

			}
		});

		progress.setCanceledOnTouchOutside(true);
		com.jingzhong.asyntask2.utils.Utils.showProgress(progress, "请稍后……", v);

	}

	private WebPlus web;

	private List<ArticModelBase> baseList = new ArrayList<ArticModelBase>();

	private queryMyList query = new queryMyList();

	private int page = 1;

	private int size = 10;

	private class queryMyList extends Asyntask2<String, String, ArticList> {

		@Override
		protected ArticList doInbackProgres(String... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ArticList list = web.myarticle(params[0], page, size);
			if (list == null) {
				updateProgress("未知错误，请检查网络连接");
			}
			return list;
		}

		@Override
		protected void doProgress(String... p) {
			// TODO Auto-generated method stub
			super.doProgress(p);
			com.hxgwx.www.utils.Utils.showLongToast(getApplicationContext(), p[0]);
		}

		@Override
		protected void doResult(ArticList res) {
			// TODO Auto-generated method stub
			super.doResult(res);
			plv_my_public.onRefreshComplete();
			mDoing = false;
			if (progress != null&&progress.isShowing()) {
				progress.dismiss();
			}
			if (res != null) {
				if (res.isStatu()) {
					if (res.getData() == null || res.getData().size() <= 0) {
						if (baseList.size() > 0)
							com.hxgwx.www.utils.Utils.showLongToast(getApplicationContext(), "没有更多数据了");
						plv_my_public.setMode(Mode.PULL_FROM_START);
					} else {
						if (res.getData().size() < size)
							plv_my_public.setMode(Mode.PULL_FROM_START);
						else
							plv_my_public.setMode(Mode.BOTH);
						if (page <= 1)
							baseList.clear();
						baseList.addAll(res.getData());
						myAdapter.notifyDataSetChanged();
					}
				} else {
					updateProgress("获取数据失败：" + application.getDescriptionByCode(res.getCode() + ""));
					com.hxgwx.www.utils.Utils.showLongToast(getApplicationContext(),
							application.getDescriptionByCode(res.getCode() + ""));
					plv_my_public.setMode(Mode.PULL_FROM_START);
				}
			}
		}
	}
}
