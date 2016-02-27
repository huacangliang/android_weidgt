package com.hxgwx.www.activity;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hxgwx.www.HongxianggeApplication;
import com.hxgwx.www.R;
import com.hxgwx.www.adapter.MainListAdapter;
import com.hxgwx.www.bean.ArticList;
import com.hxgwx.www.bean.ArticModelBase;
import com.hxgwx.www.web.WebPlus;
import com.hxgwx.www.webimpl.WebPlusImpl;
import com.jingzhong.asyntask2.Asyntask2;
import com.jingzhong.asyntask2.annotation.CreateView;
import com.jingzhong.asyntask2.utils.Utils;
import com.umeng.message.PushAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

public class NewActivity
		extends Activity {

	public static final int PAGESIZE = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_article);
		try {
			Utils.injectObject(this, this, R.id.class);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		web = new WebPlusImpl(this);
		application = (HongxianggeApplication) this.getApplication();
		// TODO Auto-generated method stub

		adapter = new MainListAdapter(this, this.plv_main_list.getRefreshableView(), data);
		plv_main_list.getRefreshableView().addHeaderView(initAd());
		plv_main_list.setAdapter(adapter);

		View emptyView = LinearLayout.inflate(this, R.layout.empty_list, null);
		plv_main_list.setEmptyView(emptyView);
		plv_main_list.setMode(Mode.BOTH);
		plv_main_list.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
		plv_main_list.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中……");
		plv_main_list.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新……");
		plv_main_list.getLoadingLayoutProxy(true, false).setReleaseLabel("放开以刷新");
		plv_main_list.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");

		plv_main_list.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (isDoing) {
					// plv_main_list.onRefreshComplete();
					return;
				}
				else {
					page = 1;
					query.execute(false);
				}

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (isDoing) {
					// plv_main_list.onRefreshComplete();
					return;
				}
				else {
					page++;
					query.execute(false);
					isDoing = true;
				}
			}
		});

		plv_main_list.getRefreshableView().setDivider(null);
		plv_main_list.getRefreshableView().setSelector(android.R.color.transparent);
		query.execute(true);
		
		PushAgent.getInstance(this).onAppStart();
	}
	
	public void onBack(View v) {
		// TODO Auto-generated method stub
		finish();
	}

	private QueryList				query	= new QueryList();

	@CreateView("plv_main_list")
	private PullToRefreshListView	plv_main_list;

	private MainListAdapter			adapter;

	private WebPlus					web;

	public int						page	= 1;

	/**
	 * 排行榜
	 */
	private List<ArticModelBase>	data	= new ArrayList<ArticModelBase>();

	private ProgressDialog			progress;

	private View initAd() {
		// TODO Auto-generated method stub
		// 实例化广告条
		return new AdView(this, AdSize.FIT_SCREEN);
	}

	private Handler					updateUI	= new Handler();

	private HongxianggeApplication	application;

	public boolean					isDoing;

	private void showCheck() {
		if (progress != null) {
			progress.show();
		}
		else {
			View v = LayoutInflater.from(this).inflate(R.layout.layout_load_pb, null);
			progress = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
			progress.setCancelable(true);
			progress.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub

				}
			});

			progress.setOnShowListener(new OnShowListener() {

				@Override
				public void onShow(DialogInterface dialog) {
					// TODO Auto-generated method stub
					updateUI.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (progress.isShowing()) {
								progress.dismiss();
							}
						}
					}, 10 * 1000);
				}
			});

			progress.setCanceledOnTouchOutside(false);
			Utils.showProgress(progress, "请稍后……", v);
		}

	}

	public class QueryList
			extends Asyntask2<Object, String, ArticList> {

		boolean isFirst = false;

		@Override
		protected void doStart() {
			// TODO Auto-generated method stub
			if (isFirst) {
				showCheck();
			}
			super.doStart();
		}

		@Override
		protected ArticList doInbackProgres(Object... params) {
			// TODO Auto-generated method stub
			isFirst = (boolean) params[0];
			ArticList rec = web.newUpdate(page, PAGESIZE);
			if (rec == null) {
				updateProgress("网络异常");
				return rec;
			}
			else if (rec.isStatu() && rec.getData() == null) {
				updateProgress("获取数据异常");
				return rec;
			}
			else if (rec != null && !rec.isStatu()) {
				updateProgress("获取数据异常");
				return rec;
			}
			if (page <= 1) data.clear();

			data.addAll(rec.getData());
			return rec;
		}

		@Override
		protected void doProgress(String... p) {
			// TODO Auto-generated method stub
			super.doProgress(p);

			com.hxgwx.www.utils.Utils.showShortToast(getApplicationContext(), p[0]);
			page--;
		}

		@Override
		protected void doResult(ArticList res) {
			// TODO Auto-generated method stub
			super.doResult(res);

			plv_main_list.onRefreshComplete();
			if (progress.isShowing()) {
				progress.dismiss();
			}
			isDoing = false;

			if (res != null) {
				if (!res.isStatu()) {
					String des = application.getDescriptionByCode(res.getCode() + "");
					com.hxgwx.www.utils.Utils.showLongToast(getApplicationContext(), des);
					return;
				}

				if (page <= 1) {
					if (res.getData() == null || res.getData().size() < PAGESIZE && data.size() <= 0)
						plv_main_list.setMode(Mode.PULL_FROM_START);
					else plv_main_list.setMode(Mode.BOTH);
					adapter.notifyDataSetChanged();
					plv_main_list.getRefreshableView().setSelectionAfterHeaderView();
				}
				else {
					adapter.notifyDataSetChanged();
				}
			}
			else {
				plv_main_list.setMode(Mode.PULL_FROM_START);
			}

		}
	}
}
