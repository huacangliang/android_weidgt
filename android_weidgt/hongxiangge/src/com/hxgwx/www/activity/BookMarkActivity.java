package com.hxgwx.www.activity;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.hxgwx.www.HongxianggeApplication;
import com.hxgwx.www.R;
import com.hxgwx.www.adapter.LocationArcAdapter;
import com.hxgwx.www.bean.ArticModelBase;
import com.hxgwx.www.bean.UserLogin;
import com.hxgwx.www.db.BookMarkDBHelper;
import com.hxgwx.www.utils.SystemBarTintManager;
import com.jingzhong.asyntask2.utils.Utils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.umeng.message.PushAgent;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class BookMarkActivity extends Activity {

	private PullToRefreshListView plv_book;

	private UserLogin user;

	public void onBack(View v) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_bookmark);
		SystemBarTintManager.init(this);
		plv_book = (PullToRefreshListView) findViewById(R.id.plv_book);
		View emptyView = LinearLayout.inflate(this, R.layout.empty_list, null);
		plv_book.setEmptyView(emptyView);
		plv_book.setMode(Mode.BOTH);
		plv_book.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
		plv_book.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中……");
		plv_book.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新……");
		plv_book.getLoadingLayoutProxy(true, false).setReleaseLabel("放开以刷新");
		plv_book.getLoadingLayoutProxy(false, true).setReleaseLabel("放开加载更多");

		user = HongxianggeApplication.getInstance().getUser();

		bDb = BookMarkDBHelper.getInstance(this);

		bAdapter = new LocationArcAdapter(bookMarkList, getApplicationContext());

		initBookMark(user.getData().getMid());
		PushAgent.getInstance(this).onAppStart();
	}

	private List<ArticModelBase> bookMarkList = new ArrayList<ArticModelBase>();

	private int bPage = 1;

	private int bSize = 10;

	private LocationArcAdapter bAdapter;

	private BookMarkDBHelper bDb;

	private Button bt_edit;

	protected boolean isEdit;

	private LinearLayout ll_delete;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initBookMark(final String mid) {
		plv_book.setRefreshing();
		bookMarkList.clear();
		bAdapter = new LocationArcAdapter(bookMarkList, this);
		plv_book.setAdapter(bAdapter);
		try {
			List list = bDb.createDb().findAll(Selector.from(ArticModelBase.class).where("loginId", "=", mid)
					.limit(bSize).orderBy("readTime", true));
			if (list == null || list.size() < bSize) {
				plv_book.setMode(Mode.PULL_FROM_START);
			} else {
				plv_book.setMode(Mode.BOTH);
			}
			if (list != null)
				bookMarkList.addAll(list);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bAdapter.notifyDataSetChanged();
		plv_book.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				if (bDoing) {
					return;
				}
				bDoing = true;
				bPage = 1;
				Message msg = updateListUI.obtainMessage();
				msg.what = 0x66;
				msg.obj = mid;
				updateListUI.sendMessageDelayed(msg, 1000);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				if (bDoing) {
					return;
				}
				bDoing = true;
				Message msg = updateListUI.obtainMessage();
				msg.what = 0x67;
				msg.obj = mid;
				updateListUI.sendMessageDelayed(msg, 1000);
			}
		});
		ll_delete = (LinearLayout) findViewById(R.id.ll_delete);
		bt_edit = (Button) findViewById(R.id.bt_edit);
		bt_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (bt_edit.getText().toString().equals("编辑")) {
					isEdit = true;
					bt_edit.setText("取消");
					ll_delete.setVisibility(View.VISIBLE);
					bAdapter.setEdit(isEdit);
					bAdapter.notifyDataSetChanged();
				} else {
					bt_edit.setText("编辑");
					ll_delete.setVisibility(View.GONE);
					isEdit = false;
					bAdapter.setEdit(isEdit);
					bAdapter.notifyDataSetChanged();
				}
			}
		});

		ll_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (bAdapter.getEditIdList().size() > 0) {
					ll_delete.setVisibility(View.GONE);
					bt_edit.setText("编辑");
					for (int i = 0; i < bAdapter.getEditIdList().size(); i++) {

						try {
							bDb.createDb().deleteById(ArticModelBase.class, bAdapter.getEditIdList().get(i));
						} catch (DbException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					bAdapter.getEditIdList().clear();
					isEdit = false;
					bAdapter.setEdit(isEdit);
					// 重新初始化数据
					bDoing = true;
					bPage = 1;
					Message msg = updateListUI.obtainMessage();
					msg.what = 0x66;
					msg.obj = mid;
					updateListUI.sendMessageDelayed(msg, 1000);
				} else {
					Utils.showShortToast(getApplicationContext(), "还没选择呢");
				}

			}
		});
	}

	private boolean bDoing = false;

	private Handler updateListUI = new Handler() {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public void handleMessage(android.os.Message msg) {
			plv_book.onRefreshComplete();
			String mid = msg.obj.toString();
			if (msg.what == 0x66) {
				// 刷新
				try {
					List list = bDb.createDb().findAll(Selector.from(ArticModelBase.class).where("loginId", "=", mid)
							.limit(bSize).orderBy("readTime", true));

					if (list != null) {
						bookMarkList.clear();
						bookMarkList.addAll(list);
					}
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bAdapter.notifyDataSetChanged();
				bDoing = false;
			}
			if (msg.what == 0x67) {
				// 加载更多
				try {
					List list = bDb.createDb().findAll(Selector.from(ArticModelBase.class).where("loginId", "=", mid)
							.offset(bPage * bSize).limit(bSize).orderBy("readTime", true));
					if (list == null || list.size() < bSize) {
						Utils.showLongToast(BookMarkActivity.this, "没有更多数据了");
					}
					if (list != null)
						bookMarkList.addAll(list);
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bPage++;
				bAdapter.notifyDataSetChanged();
				bDoing = false;
			}

		};
	};
}
