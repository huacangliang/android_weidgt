package com.hxgwx.www.activity;

import java.util.ArrayList;
import java.util.List;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hxgwx.www.HongxianggeApplication;
import com.hxgwx.www.R;
import com.hxgwx.www.adapter.ArcListAdapter;
import com.hxgwx.www.bean.ArticList;
import com.hxgwx.www.bean.ArticModelBase;
import com.hxgwx.www.bean.ArticTypeBaseList;
import com.hxgwx.www.utils.SystemBarTintManager;
import com.hxgwx.www.utils.Utils;
import com.hxgwx.www.web.WebPlus;
import com.hxgwx.www.webimpl.WebPlusImpl;
import com.jingzhong.asyntask2.Asyntask2;
import com.jingzhong.asyntask2.annotation.CreateView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

public class ArcTypeListActivity extends Activity {
	@CreateView("tv_title")
	private TextView tv_title;

	private static int page = 1;

	public static final int PAGESIZE = 10;

	private String id;

	private ArticTypeBaseList base;
	@CreateView("plv_arclist")
	private PullToRefreshListView plv_arclist;

	/**
	 * @return the plv_arclist
	 */
	public synchronized final PullToRefreshListView getPlv_arclist() {
		return plv_arclist;
	}

	/**
	 * @param plv_arclist
	 *            the plv_arclist to set
	 */
	public synchronized final void setPlv_arclist(PullToRefreshListView plv_arclist) {
		this.plv_arclist = plv_arclist;
	}

	private boolean complet = true;

	private WebPlus web;

	private HongxianggeApplication application;

	/**
	 * @return the application
	 */
	public HongxianggeApplication getApplication2() {
		return application;
	}

	private final List<ArticModelBase> data = new ArrayList<ArticModelBase>();

	private ArcListAdapter adapter;

	private queryList query;

	private boolean hsQery = false;

	private void initAd() {
		// TODO Auto-generated method stub
		// 实例化广告条
		AdView adView = new AdView(this, AdSize.SIZE_468x60);

		// 获取要嵌入广告条的布局
		LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);

		// 将广告条加入到布局中
		adLayout.addView(adView);
	}

	private View initAd2() {
		// TODO Auto-generated method stub
		// 实例化广告条
		AdView adView = new AdView(this, AdSize.SIZE_468x60);
		return adView;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.arclist);
		SystemBarTintManager.init(this);
		page = 1;
		try {
			com.jingzhong.asyntask2.utils.Utils.injectObject(this, this, R.id.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PushAgent.getInstance(this).onAppStart();
		//initAd();
		base = (ArticTypeBaseList) getIntent().getSerializableExtra("ArticTyp");
		String name = base.getTypename();
		id = base.getId();
		tv_title.setText(name);
		web = new WebPlusImpl(this);
		application = (HongxianggeApplication) getApplication();
		adapter = new ArcListAdapter(this, data);
		query = new queryList();
		init();
	}

	private ProgressDialog progress;

	private void showCheck() {
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void init() {
		View emptyView = LinearLayout.inflate(this, R.layout.empty_list, null);
		plv_arclist.setEmptyView(emptyView);
		plv_arclist.setMode(Mode.BOTH);
		plv_arclist.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
		plv_arclist.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中……");
		plv_arclist.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新……");
		plv_arclist.getLoadingLayoutProxy(true, false).setReleaseLabel("放开以刷新");
		plv_arclist.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");
		plv_arclist.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub

			}
		});
		plv_arclist.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				if (!complet) {
					plv_arclist.onRefreshComplete();
				} else {
					complet = false;
					page = 1;
					query.execute("");
				}

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				if (!complet) {
					plv_arclist.onRefreshComplete();
				} else {
					complet = false;
					page++;
					query.execute("");
				}

			}
		});
		plv_arclist.getRefreshableView().addHeaderView(initAd2());
		plv_arclist.setAdapter(adapter);
		showCheck();
		query.execute("");
	}

	private class queryList extends Asyntask2<String, String, ArticList> {

		@Override
		protected ArticList doInbackProgres(String... params) {
			// TODO Auto-generated method stub
			ArticList list = web.queryArticList(id, page, PAGESIZE);
			if (list == null) {
				updateProgress("未知数错误，请检查网络连接状态");
			}

			return list;
		}

		@Override
		protected void doProgress(String... p) {
			// TODO Auto-generated method stub
			super.doProgress(p);
			Utils.showLongToast(getApplicationContext(), p[0]);
		}

		@Override
		protected void doResult(ArticList res) {
			// TODO Auto-generated method stub
			super.doResult(res);
			plv_arclist.onRefreshComplete();
			complet = true;
			if (progress.isShowing()) {
				progress.dismiss();
			}
			if (res != null) {
				if (res.isStatu() && res.getData() != null) {
					if (page <= 1)
						data.clear();
					data.addAll(res.getData());
					adapter.notifyDataSetChanged();
					hsQery = true;
				} else if (res.isStatu() && res.getData() == null) {
					if (hsQery) {
						Utils.showLongToast(getApplicationContext(), "没有更多数据了");
						page--;
					} else {
						Utils.showLongToast(getApplicationContext(),
								"获取数据失败：" + application.getDescriptionByCode(res.getCode() + ""));
						page--;
					}

				} else {
					Utils.showLongToast(getApplicationContext(),
							"获取数据失败：" + application.getDescriptionByCode(res.getCode() + ""));
					page--;
				}
			} else {
				Utils.showLongToast(getApplicationContext(), "获取数据失败：网络异常");
				page--;
			}
		}

	}

	public void onBack(View v) {
		// TODO Auto-generated method stub
		finish();
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
