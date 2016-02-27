package com.hxgwx.www.fragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.hxgwx.www.HongxianggeApplication;
import com.hxgwx.www.R;
import com.hxgwx.www.activity.ArcTypeListActivity;
import com.hxgwx.www.activity.MainFragmentActivity;
import com.hxgwx.www.activity.NewActivity;
import com.hxgwx.www.adapter.MainListAdapter;
import com.hxgwx.www.bean.ArticList;
import com.hxgwx.www.bean.ArticModelBase;
import com.hxgwx.www.bean.ArticTypeBaseList;
import com.hxgwx.www.view.CustomGridView;
import com.hxgwx.www.view.CustomLinearLayout;
import com.hxgwx.www.web.WebPlus;
import com.hxgwx.www.webimpl.WebPlusImpl;
import com.jingzhong.asyntask2.Asyntask2;
import com.jingzhong.asyntask2.annotation.CreateView;
import com.jingzhong.asyntask2.utils.Utils;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

public class SearchFragment
		extends Fragment {

	private View					baseView;

	private HongxianggeApplication	application;

	@CreateView("hs_type_")
	private CustomGridView			hs_type_;

	private MainFragmentActivity	activity;

	@CreateView("ll_main_content")
	private CustomLinearLayout		ll_main_content;

	@CreateView("prs")
	private PullToRefreshScrollView	prs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		baseView = inflater.inflate(R.layout.search, null);
		try {
			Utils.injectObject(this, baseView, R.id.class);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		activity = (MainFragmentActivity) getActivity();
		init();
		return baseView;
	}

	private View initAd(View v) {
		// TODO Auto-generated method stub
		// 实例化广告条
		AdView adView = new AdView(getActivity(), AdSize.FIT_SCREEN);

		// 获取要嵌入广告条的布局
		LinearLayout adLayout = (LinearLayout) v.findViewById(R.id.adLayout);

		// 将广告条加入到布局中
		adLayout.addView(adView);
		return adView;
	}

	@SuppressWarnings("deprecation")
	private void init() {
		if (MainListFragment.typeList != null) {
			application = (HongxianggeApplication) getActivity().getApplication();
			initAd(baseView);
			typeListAdapter adapterType = new typeListAdapter();
			adapterType.fill(MainListFragment.typeList);

			hs_type_.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					ArticTypeBaseList base = (ArticTypeBaseList) parent.getItemAtPosition(position);
					if (base.getTypename().equals("专栏作者")) {
						showZhuanlanListPop();
						return;
					}
					Intent i = new Intent();
					i.putExtra("ArticTyp", base);
					com.hxgwx.www.utils.Utils.gotoActivity(getActivity(), ArcTypeListActivity.class, i);
				}
			});
			hs_type_.setAdapter(adapterType);

			adapterType.notifyDataSetChanged();

			web = new WebPlusImpl(getActivity());
			application = (HongxianggeApplication) getActivity().getApplication();
			// TODO Auto-generated method stub

			adapter = new MainListAdapter(getActivity(), this.plv_main_list.getRefreshableView(), data);
			TextView tvFooter = new TextView(getActivity());
			tvFooter.setText("更多……");
			tvFooter.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,
					getResources().getDimensionPixelSize(R.dimen.dp40)));
			tvFooter.setBackgroundColor(Color.parseColor("#cccccc"));
			tvFooter.setTextColor(Color.WHITE);
			tvFooter.setGravity(Gravity.CENTER);
			tvFooter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Utils.gotoActivity(getActivity(), NewActivity.class, null);
				}
			});
			plv_main_list.getRefreshableView().addFooterView(tvFooter);
			plv_main_list.setAdapter(adapter);

			View emptyView = LinearLayout.inflate(getActivity(), R.layout.empty_list, null);
			plv_main_list.setEmptyView(emptyView);

			plv_main_list.getRefreshableView().setDivider(null);
			plv_main_list.getRefreshableView().setSelector(android.R.color.transparent);
			query.execute();
		}

		prs.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// TODO Auto-generated method stub
				if (MainListFragment.typeList == null) {
					MainListFragment.list.execute();
					new Thread() {

						public void run() {
							while (MainListFragment.typeList != null) {
								ui.sendEmptyMessage(1);
							}
						};
					}.start();
				}
				else {
					query.execute();
				}

			}
		});
		plv_main_list.setMinimumHeight(getActivity().getWindowManager().getDefaultDisplay().getHeight());

	}

	Handler ui = new Handler() {

		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				init();
			}
			else if (msg.what == 2) {
				prs.onRefreshComplete();
			}
		};
	};

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@CreateView("plv_main_list")
	private PullToRefreshListView	plv_main_list;

	private MainListAdapter			adapter;

	private WebPlus					web;

	public int						page	= 1;

	/**
	 * 排行榜
	 */
	private List<ArticModelBase>	data	= new ArrayList<ArticModelBase>();

	PopupWindow						pop;

	public boolean					isDoing;

	private QueryList				query	= new QueryList();

	private void showZhuanlanListPop() {
		pop = new PopupWindow();
		ListView lv = new ListView(activity);
		View root = activity.getWindow().getDecorView();
		pop.setWidth(root.getWidth());
		pop.setHeight(root.getHeight());
		LinearLayout contentView = (LinearLayout) LinearLayout.inflate(activity, R.layout.zhuanlan_layout, null);
		contentView.findViewById(R.id.bt_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
			}
		});
		ZhuanlanAdapter adapter = new ZhuanlanAdapter();

		lv.setVerticalScrollBarEnabled(false);
		lv.setHorizontalScrollBarEnabled(false);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				pop.dismiss();
				Intent i = new Intent();
				ArticTypeBaseList base = (ArticTypeBaseList) parent.getItemAtPosition(position);
				i.putExtra("ArticTyp", base);
				com.hxgwx.www.utils.Utils.gotoActivity(getActivity(), ArcTypeListActivity.class, i);
			}
		});
		contentView.addView(lv);
		pop.setContentView(contentView);
		pop.setOutsideTouchable(true);
		pop.showAtLocation(root, Gravity.CENTER, 0, 0);
		activity.setPop(pop);

	}

	class ZhuanlanAdapter
			extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return MainListFragment.zhuanlanList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return MainListFragment.zhuanlanList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView tv = null;
			if (convertView == null) tv = new TextView(getActivity());
			else tv = (TextView) convertView;

			AbsListView.LayoutParams params = new AbsListView.LayoutParams(-1,
					(int) Utils.pixelToDp(getActivity(), 80));
			tv.setLayoutParams(params);
			tv.setGravity(Gravity.CENTER);
			final ArticTypeBaseList mod = MainListFragment.zhuanlanList.get(position);
			tv.setText(Html.fromHtml(mod.getTypename()));
			return tv;
		}

	}

	private class typeListAdapter
			extends BaseAdapter {

		List<ArticTypeBaseList> typeList = new ArrayList<ArticTypeBaseList>();

		public void fill(List<ArticTypeBaseList> typeList) {
			this.typeList.clear();
			this.typeList.addAll(typeList);
			Iterator<ArticTypeBaseList> it = this.typeList.iterator();
			while (it.hasNext()) {
				ArticTypeBaseList t = it.next();
				if (t.getTypename().indexOf("专栏") >= 0) {
					it.remove();
				}
			}

			ArticTypeBaseList object = new ArticTypeBaseList();
			object.setTypename("专栏作者");
			this.typeList.add(object);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.typeList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return this.typeList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ArticTypeBaseList base = this.typeList.get(position);
			if (convertView == null) {
				TextView tv = null;
				tv = new TextView(application);
				tv.setGravity(Gravity.CENTER);
				tv.setText(base.getTypename());
				tv.setBackgroundResource(R.drawable.type_item_bg);
				return tv;
			}
			TextView tv = (TextView) convertView;
			tv.setText(base.getTypename());
			return tv;
		}

	}

	public static final int PAGESIZE = 10;

	public class QueryList
			extends Asyntask2<String, String, ArticList> {

		@Override
		protected ArticList doInbackProgres(String... params) {
			// TODO Auto-generated method stub
			if (data.size() > 0) {
				ArticList rec = new ArticList();
				rec.setStatu(true);
				return rec;
			}
			ArticList rec = web.newUpdate(page, PAGESIZE);
			if (rec == null) {
				updateProgress("获取数据异常：请检查网络链接是否稳定");
				return rec;
			}
			else if (rec.isStatu() && rec.getData() == null) {
				updateProgress(application.getDescriptionByCode(rec.getCode() + ""));
				return rec;
			}
			else if (rec != null && !rec.isStatu()) {
				updateProgress(application.getDescriptionByCode(rec.getCode() + ""));
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

			com.hxgwx.www.utils.Utils.showShortToast(getActivity(), p[0]);
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void doResult(ArticList res) {
			// TODO Auto-generated method stub
			super.doResult(res);

			ui.sendEmptyMessage(2);
			isDoing = false;

			if (res != null) {
				if (!res.isStatu()) { return; }
				adapter.notifyDataSetChanged();
				com.hxgwx.www.utils.Utils.setListViewHeightBasedOnChildren(plv_main_list, 0,
						getActivity().getResources().getDimensionPixelSize(R.dimen.dp40));
			}
			else {
				if (plv_main_list.getRefreshableView().getAdapter() == null
						|| plv_main_list.getRefreshableView().getAdapter().getCount() == 0) {
					plv_main_list.setMinimumHeight(getActivity().getWindowManager().getDefaultDisplay().getHeight());
				}

			}

		}
	}
}
