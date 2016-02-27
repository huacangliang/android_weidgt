package com.hxgwx.www.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hxgwx.www.HongxianggeApplication;
import com.hxgwx.www.R;
import com.hxgwx.www.activity.MainFragmentActivity;
import com.hxgwx.www.activity.SearchActivity;
import com.hxgwx.www.adapter.MainListAdapter;
import com.hxgwx.www.bean.ArticList;
import com.hxgwx.www.bean.ArticModelBase;
import com.hxgwx.www.bean.ArticTypeBaseList;
import com.hxgwx.www.bean.ArticTypeList;
import com.hxgwx.www.view.CustomLinearLayout;
import com.hxgwx.www.web.WebPlus;
import com.hxgwx.www.webimpl.WebPlusImpl;
import com.jingzhong.asyntask2.Asyntask2;
import com.jingzhong.asyntask2.annotation.CreateView;
import com.jingzhong.asyntask2.utils.Utils;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

/**
 * 
 * @todo 主界面 文章列表 ，头部为分类，下面为列表
 * 
 * @author dengzhijiang
 * 
 */
public class MainListFragment
		extends Fragment {

	private HongxianggeApplication application;

	/**
	 * @return the application
	 */
	public HongxianggeApplication getApplication2() {
		return application;
	}

	@CreateView("plv_main_list")
	private PullToRefreshListView plv_main_list;

	/**
	 * @return the plv_main_list
	 */
	public synchronized final PullToRefreshListView getPlv_main_list() {
		return plv_main_list;
	}

	private MainListAdapter	adapter;

	private WebPlus			web;

	/**
	 * @return the adapter
	 */
	public synchronized final MainListAdapter getAdapter() {
		return adapter;
	}

	/**
	 * 排行榜
	 */
	public List<ArticModelBase>	rankList	= new ArrayList<ArticModelBase>();

	/**
	 * 推荐
	 */
	public List<ArticModelBase>	recommList	= new ArrayList<ArticModelBase>();

	private Type					type		= Type.tuijian;

	public enum Type {
		tuijian, paihang, other
	}

	private static int						pageTuiJIan		= 1;

	private static int						pagePaiHang		= 1;

	public static final int					PAGESIZE		= 10;

	private boolean							isDoing			= false;

	private QueryList						query			= new QueryList();

	private MainFragmentActivity			activity;

	public static GetTypeList				list;

	public static final Map<String, String>	TYPES			= new HashMap<String, String>();

	public static List<ArticTypeBaseList>	zhuanlanList	= new ArrayList<ArticTypeBaseList>();

	public static List<ArticTypeBaseList>	typeList;

	public void getTypeList(Type type) {

		if (type == null) {
			Utils.showLongToast(getApplication2(), "未知错误");
			return;
		}

		this.setType(type);
		if (type == Type.other) { return; }
		plv_main_list.onRefreshComplete();
		switch (type) {
		case tuijian:

			if (recommList.size() <= 0) {
				pageTuiJIan = 1;
				showCheck();
				query.execute("");
			}
			else {
				adapter.setBaseList(recommList);
				plv_main_list.setAdapter(adapter);
			}
			break;
		case paihang:
			if (rankList.size() <= 0) {
				pagePaiHang = 1;
				showCheck();
				query.execute("");
			}
			else {
				adapter.setBaseList(rankList);
				plv_main_list.setAdapter(adapter);
			}
			break;
		default:
			break;
		}
	}

	private ProgressDialog progress;

	private void initAd() {
		// TODO Auto-generated method stub
		// 实例化广告条
		new AdView(getActivity(), AdSize.FIT_SCREEN);
	}

	private void showCheck() {
		if (progress != null) {
			progress.show();
		}
		else {
			View v = LayoutInflater.from(getActivity()).inflate(R.layout.layout_load_pb, null);
			progress = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
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

			progress.setCanceledOnTouchOutside(true);
			Utils.showProgress(progress, "请稍后……", v);
		}

	}

	private Handler updateUI = new Handler();

	public class QueryList
			extends Asyntask2<String, Integer, ArticList> {

		@Override
		protected ArticList doInbackProgres(String... params) {
			// TODO Auto-generated method stub
			if (type == null) type = Type.tuijian;

			if (TYPES == null || TYPES.size() <= 0) list.execute("");

			switch (type) {
			case tuijian:
				ArticList rec = web.recommend(pageTuiJIan, PAGESIZE);
				if (rec == null) {
					updateProgress(-1);
					return rec;
				}
				else if (rec.isStatu() && rec.getData() == null) {
					updateProgress(-3);
					return rec;
				}
				else if (rec != null && !rec.isStatu()) {
					updateProgress(-4);
					return rec;
				}
				if (pageTuiJIan <= 1) recommList.clear();
				recommList.addAll(rec.getData());
				return rec;

			case paihang:
				rec = web.ranking(pagePaiHang, PAGESIZE);
				if (rec == null) {
					updateProgress(-2);
					return rec;
				}
				else if (rec.isStatu() && rec.getData() == null) {
					updateProgress(-3);
					return rec;
				}
				else if (rec != null && !rec.isStatu()) {
					updateProgress(-4);
					return rec;
				}
				if (pagePaiHang <= 1) rankList.clear();

				rankList.addAll(rec.getData());
				return rec;
			default:
				break;
			}
			return null;
		}

		@Override
		protected void doProgress(Integer... p) {
			// TODO Auto-generated method stub
			super.doProgress(p);
			if (getActivity() == null) return;
			if (p[0] == -1) com.hxgwx.www.utils.Utils.showShortToast(getActivity(), "获取推荐列表异常");
			else if (p[0] == -2) com.hxgwx.www.utils.Utils.showShortToast(getActivity(), "获取排行榜列表异常");
			else if (p[0] == -3) com.hxgwx.www.utils.Utils.showShortToast(getActivity(), "没有更多数据了");
			else if (p[0] == 4) com.hxgwx.www.utils.Utils.showShortToast(getActivity(), "获取数据异常");
			switch (type) {
			case tuijian:
				pageTuiJIan--;
				break;
			case paihang:
				pagePaiHang--;
				break;
			default:
				break;
			}
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
					com.hxgwx.www.utils.Utils.showLongToast(getActivity(), des);
					return;
				}
				switch (type) {
				case tuijian:
					adapter.setBaseList(recommList);
					if (pageTuiJIan <= 1) {
						if (res.getData() == null || res.getData().size() < PAGESIZE && recommList.size() <= 0)
							plv_main_list.setMode(Mode.PULL_FROM_START);
						else plv_main_list.setMode(Mode.BOTH);
						adapter.notifyDataSetChanged();
						plv_main_list.getRefreshableView().setSelectionAfterHeaderView();
					}
					else {
						adapter.notifyDataSetChanged();
					}
					break;
				case paihang:
					adapter.setBaseList(rankList);
					if (pagePaiHang <= 1) {
						if (res.getData() == null || res.getData().size() < PAGESIZE && rankList.size() <= 0)
							plv_main_list.setMode(Mode.PULL_FROM_START);
						else plv_main_list.setMode(Mode.BOTH);
						adapter.notifyDataSetChanged();
						plv_main_list.getRefreshableView().setSelectionAfterHeaderView();
					}
					else {
						adapter.notifyDataSetChanged();
					}
					break;
				default:
					break;
				}
			}
			else {
				plv_main_list.setMode(Mode.PULL_FROM_START);
			}

		}
	}

	boolean isFirst = true;

	private void init() {
		if (isFirst) {
			isFirst = false;
		}
		else {
			initView();
			getTypeList(type);
			return;
		}
		initAd();
		web = new WebPlusImpl(getActivity());
		application = (HongxianggeApplication) getActivity().getApplication();
		// TODO Auto-generated method stub

		adapter = new MainListAdapter(getActivity(), this.plv_main_list.getRefreshableView(), recommList);
		initView();
		plv_main_list.setAdapter(adapter);
		showCheck();
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	private void initView() {
		list = new GetTypeList();
		View emptyView = LinearLayout.inflate(getActivity(), R.layout.empty_list, null);
		plv_main_list.setEmptyView(emptyView);
		plv_main_list.setMode(Mode.BOTH);
		plv_main_list.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
		plv_main_list.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中……");
		plv_main_list.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新……");
		plv_main_list.getLoadingLayoutProxy(true, false).setReleaseLabel("放开以刷新");
		plv_main_list.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");
		plv_main_list.setMinimumHeight(getActivity().getWindowManager().getDefaultDisplay().getHeight());
		plv_main_list.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				if (isDoing) {
					// plv_main_list.onRefreshComplete();
					return;
				}
				else {
					isDoing = true;

					switch (type) {
					case tuijian:
						pageTuiJIan = 1;
						break;
					case paihang:
						pagePaiHang = 1;
						break;
					default:
						break;
					}
					query.execute("");
				}

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				if (isDoing) {
					// plv_main_list.onRefreshComplete();
					return;
				}
				else {
					switch (type) {
					case tuijian:
						pageTuiJIan++;
						break;
					case paihang:
						pagePaiHang++;
						break;
					default:
						break;
					}
					query.execute("");
					isDoing = true;
				}

			}
		});

		plv_main_list.getRefreshableView().setDivider(null);
		plv_main_list.getRefreshableView().setSelector(android.R.color.transparent);
		activity.getMain_fragment_content().sizeChange = new com.hxgwx.www.view.CustomLinearLayout.SizeChange() {

			@Override
			public void onSizeChanged(int w, int h, int oldw, int oldh) {
				// TODO Auto-generated method stub
				LayoutParams params = (LayoutParams) plv_main_list.getLayoutParams();
				params.height = h;
				plv_main_list.setLayoutParams(params);
			}
		};

		list.execute("");
	}

	private boolean					firstInitData	= true;

	private static ArticTypeList	alllist;

	/**
	 * 
	 * @todo 获取分类列表
	 * 
	 * @author dengzhijiang
	 * 
	 */
	class GetTypeList
			extends Asyntask2<String, Integer, ArticTypeList> {

		@Override
		protected ArticTypeList doInbackProgres(String... params) {
			// TODO Auto-generated method stub
			if (typeList != null && typeList.size() > 0) { return alllist; }
			alllist = web.queryArticType();
			if (alllist == null || alllist.getData() == null) {
				updateProgress(-1);
				cancel();
				return null;
			}

			typeList = alllist.getData();
			zhuanlanList.clear();
			for (int i = 0; typeList != null && i < typeList.size(); i++) {
				ArticTypeBaseList mod = typeList.get(i);

				TYPES.put(mod.getId(), mod.getTypename());
				String reid = mod.getReid();
				if (!TextUtils.isEmpty(reid)) {
					for (int j = 0; j < typeList.size(); j++) {
						if (reid.equals(typeList.get(j).getId())) {
							typeList.remove(j);
						}
					}
				}
			}

			Iterator<ArticTypeBaseList> it = typeList.iterator();
			while (it.hasNext()) {
				ArticTypeBaseList mod = it.next();
				if (mod.getTypename().indexOf("专栏") > 0) {
					zhuanlanList.add(mod);
				}
				if (mod.getIshidden().equals("1") || mod.getIspart().equals("1") || mod.getTypename().equals("专栏作者")
						|| mod.getTypename().equals("会员中心")) {
					it.remove();
					continue;
				}

			}

			return alllist;
		}

		@Override
		protected void doProgress(Integer... p) {
			// TODO Auto-generated method stub
			super.doProgress(p);
		}

		@Override
		protected void doResult(ArticTypeList res) {
			// TODO Auto-generated method stub
			super.doResult(res);
			progress.dismiss();
			if (res == null) {
				com.hxgwx.www.utils.Utils.showShortToast(getActivity(), "获取分类信息失败，请检查网络连接");
				return;
			}

			if (res != null && !res.isStatu()) {
				com.hxgwx.www.utils.Utils.showLongToast(application,
						application.getDescriptionByCode(res.getCode() + ""));
				return;
			}

			adapter.notifyDataSetChanged();
			if (firstInitData) {
				firstInitData = false;
				getTypeList(Type.tuijian);
			}
		}

	}

	View baseView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		baseView = inflater.inflate(R.layout.main_content, null);
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

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}
}
