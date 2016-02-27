package com.hxgwx.www.fragment;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hxgwx.www.R;
import com.hxgwx.www.activity.MainFragmentActivity;
import com.hxgwx.www.adapter.LocationArcAdapter;
import com.hxgwx.www.bean.ArticModelBase;
import com.hxgwx.www.db.HistoryDBHelper;
import com.hxgwx.www.view.SlidingMenu.OnOpenListener;
import com.jingzhong.asyntask2.utils.Utils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class HistoryFragment extends Fragment
		implements
			OnOpenListener {

	private View baseView;

	private PullToRefreshListView plv_history;

	private Button bt_edit;

	private LocationArcAdapter adapter;

	private List<ArticModelBase> baseList = new ArrayList<ArticModelBase>();

	private HistoryDBHelper db;

	private int limit = 20;

	private int page = 1;

	private boolean isEdit;

	private LinearLayout ll_delete;

	private MainFragmentActivity activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		baseView = inflater.inflate(R.layout.reading_history, null);
		activity = (MainFragmentActivity) getActivity();
		activity.getMenu().setSecondaryOnOpenListner(this);
		init();
		return baseView;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private void init() {
		if (plv_history == null) {
			ll_delete = (LinearLayout) baseView.findViewById(R.id.ll_delete);
			plv_history = (PullToRefreshListView) baseView
					.findViewById(R.id.plv_history);
			adapter = new LocationArcAdapter(baseList, getActivity());
			plv_history.setMode(Mode.PULL_FROM_START);
			View emptyView = LinearLayout.inflate(getActivity(), R.layout.empty_list, null);
			plv_history.setEmptyView(emptyView);
			plv_history.setOnRefreshListener(new OnRefreshListener2() {

				@Override
				public void onPullDownToRefresh(PullToRefreshBase refreshView) {
					// TODO Auto-generated method stub

					page = 1;
					updateUI.sendEmptyMessageDelayed(0x20, 1000);
					try {
						List<ArticModelBase> list = db.createDb()
								.findAll(
										Selector.from(ArticModelBase.class)
												.limit(limit)
												.orderBy("readTime", true));
						baseList.clear();
						if (list != null)
							baseList.addAll(list);
						if (list != null && list.size() == limit) {
							plv_history.setMode(Mode.BOTH);
						} else {
							plv_history.setMode(Mode.PULL_FROM_START);
						}
					} catch (DbException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					adapter.notifyDataSetChanged();
				}

				@Override
				public void onPullUpToRefresh(PullToRefreshBase refreshView) {
					// TODO Auto-generated method stub
					updateUI.sendEmptyMessageDelayed(0x20, 1000);
					try {
						List<ArticModelBase> list = db.createDb().findAll(
								Selector.from(ArticModelBase.class)
										.offset(page * limit).limit(limit)
										.orderBy("readTime", true));
						if (list != null)
							baseList.addAll(list);
						adapter.notifyDataSetChanged();

					} catch (DbException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					page++;
				}
			});
			plv_history.setAdapter(adapter);
		}

		db = HistoryDBHelper.getInstance(getActivity());
		initData();

		bt_edit = (Button) baseView.findViewById(R.id.bt_edit);
		bt_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (bt_edit.getText().toString().equals("±à¼­")) {
					isEdit = true;
					bt_edit.setText("È¡Ïû");
					ll_delete.setVisibility(View.VISIBLE);
					adapter.setEdit(isEdit);
					adapter.notifyDataSetChanged();
				} else {
					bt_edit.setText("±à¼­");
					ll_delete.setVisibility(View.GONE);
					isEdit = false;
					adapter.setEdit(isEdit);
					adapter.notifyDataSetChanged();
				}
			}
		});

		ll_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (adapter.getEditIdList().size() > 0) {
					ll_delete.setVisibility(View.GONE);
					bt_edit.setText("±à¼­");
					for (int i = 0; i < adapter.getEditIdList().size(); i++) {

						try {
							db.createDb().deleteById(ArticModelBase.class,
									adapter.getEditIdList().get(i));
						} catch (DbException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					adapter.getEditIdList().clear();
					isEdit = false;
					adapter.setEdit(isEdit);
					initData();

				} else {
					Utils.showShortToast(getActivity(), "»¹Ã»Ñ¡ÔñÄØ");
				}

			}
		});
	}

	private void initData() {
		try {
			List<ArticModelBase> list = db.createDb().findAll(
					Selector.from(ArticModelBase.class).limit(limit)
							.orderBy("readTime", true));
			if (list == null || list.size() == limit) {
				plv_history.setMode(Mode.BOTH);
			} else {
				plv_history.setMode(Mode.PULL_FROM_START);
			}
			if (list != null) {
				baseList.clear();
				baseList.addAll(list);
				adapter.notifyDataSetChanged();
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Handler updateUI = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x20) {
				plv_history.onRefreshComplete();
			}
		};
	};

	@Override
	public void onOpen() {
		if (this.getView() != null
				&& this.getView().getVisibility() == View.VISIBLE) {
			initData();
		}
	}
}
