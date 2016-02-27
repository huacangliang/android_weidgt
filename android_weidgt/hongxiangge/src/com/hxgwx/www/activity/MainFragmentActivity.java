package com.hxgwx.www.activity;

import com.hxgwx.www.HongxianggeApplication;
import com.hxgwx.www.R;
import com.hxgwx.www.R.color;
import com.hxgwx.www.fragment.HistoryFragment;
import com.hxgwx.www.fragment.MainListFragment;
import com.hxgwx.www.fragment.MainListFragment.Type;
import com.hxgwx.www.fragment.SearchFragment;
import com.hxgwx.www.fragment.UserFragment;
import com.hxgwx.www.fragment.WriterArticFragment;
import com.hxgwx.www.utils.ActivityManagerUtils;
import com.hxgwx.www.utils.SystemBarTintManager;
import com.hxgwx.www.utils.Utils.Opreation;
import com.hxgwx.www.view.CustomLinearLayout;
import com.hxgwx.www.view.SlidingMenu;
import com.jingzhong.asyntask2.annotation.CreateView;
import com.jingzhong.asyntask2.utils.Utils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateConfig;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainFragmentActivity
		extends Activity
		implements ActivityManagerUtils {

	/**
	 * 头部title
	 */
	@CreateView("tv_title")
	private TextView			tv_title;

	/**
	 * 个人中心
	 */
	@CreateView("iv_mycenter")
	private ImageView			iv_mycenter;

	/**
	 * 浏览历史
	 */
	@CreateView("iv_history")
	private ImageView			iv_history;

	/**
	 * 内容主体
	 */
	@CreateView("main_fragment_content")
	private CustomLinearLayout	main_fragment_content;

	/**
	 * @return the main_fragment_content
	 */
	public CustomLinearLayout getMain_fragment_content() {
		return main_fragment_content;
	}

	/**
	 * 推荐
	 */
	@CreateView("ll_tb_tuijian")
	private LinearLayout					ll_tb_tuijian;

	/**
	 * 推荐
	 */
	@CreateView("tv_tuijian")
	private TextView						tv_tuijian;

	/**
	 * 排行
	 */
	@CreateView("tv_paihang")
	private TextView						tv_paihang;

	/**
	 * 排行
	 */
	@CreateView("ll_tb_paihang")
	private LinearLayout					ll_tb_paihang;

	/**
	 * 搜索
	 */
	@CreateView("ll_tb_sousuo")
	private LinearLayout					ll_tb_sousuo;

	/**
	 * 发表文章
	 */
	@CreateView("ll_tb_fabiao")
	private LinearLayout					ll_tb_fabiao;

	@CreateView("tv_sousuo")
	private TextView						tv_sousuo;

	@CreateView("tv_fabiao")
	private TextView						tv_fabiao;

	/**
	 * 主体列表
	 */
	private MainListFragment				mianList;

	private SearchFragment					search;

	private WriterArticFragment				writer;

	private UserFragment					userFragmentl;

	private HistoryFragment					historyFragment;

	/**
	 * fragment管理器
	 */
	private FragmentManager					manager;

	private com.hxgwx.www.view.SlidingMenu	menu;

	private HongxianggeApplication			application;

	private PopupWindow						pop;

	/**
	 * @return the menu
	 */
	public synchronized final com.hxgwx.www.view.SlidingMenu getMenu() {
		return menu;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		setIntent(intent);
		super.onNewIntent(intent);
	}

	public void onBack(View v) {
		getMenu().showContent();
	}

	public void onSearch(View view) {
		Utils.gotoActivity(this, SearchActivity.class, null);
	}

	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setApplication((HongxianggeApplication) getApplication());
		add();
		setContentView(R.layout.main_fragment);
		SystemBarTintManager.init(this);
		try {
			Utils.injectObject(this, this, R.id.class);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		initView();

		UpdateConfig.setDebug(false);

		UmengUpdateAgent.update(this);

		MobclickAgent.updateOnlineConfig(this);

		com.hxgwx.www.utils.Utils.showGuide(this, R.drawable.hxg_index_user, new Opreation() {

			@Override
			public void dissmis() {
				com.hxgwx.www.utils.Utils.showGuide(MainFragmentActivity.this, R.drawable.hxg_index_history,
						new Opreation() {

					@Override
					public void dissmis() {
						com.hxgwx.www.utils.Utils.showGuide(MainFragmentActivity.this, R.drawable.hxg_index_types);
					}
				});

			}
		});

		PushAgent.getInstance(this).onAppStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add("退出");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getTitle().toString().equals("退出")) {
			application.setUser(null);
			closeAll();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void initView() {
		manager = getFragmentManager();
		mianList = new MainListFragment();
		search = new SearchFragment();
		writer = new WriterArticFragment();
		manager.beginTransaction().replace(main_fragment_content.getId(), mianList).commit();
		clearOnclick();
		tv_tuijian.setTextColor(Color.parseColor("#44c1c1"));
		setOnclick();
		initSlidinmenu();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	private void initSlidinmenu() {
		menu = new SlidingMenu(this);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度

		menu.setMode(SlidingMenu.LEFT_RIGHT); // 设置为左菜单
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setBehindOffset(0);// 设置菜单宽度
		menu.setShadowDrawable(R.drawable.shadow);// 设置阴影图片
		menu.setShadowWidth(mScreenWidth / 40);// 设置阴影宽度
		menu.setFadeDegree(0.35f);// SlidingMenu滑动时的渐变程度
		menu.setMenu(R.layout.user_center);
		if (userFragmentl == null) {
			userFragmentl = new UserFragment();
		}

		if (historyFragment == null) {
			historyFragment = new HistoryFragment();
		}

		manager.beginTransaction().replace(R.id.user_center, userFragmentl).commit();

		menu.setSecondaryMenu(R.layout.reading_history);
		manager.beginTransaction().replace(R.id.reading_history, historyFragment).commit();
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
	}

	int count = 0;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		if (pop != null && pop.isShowing()) {
			pop.dismiss();
			return;
		}

		if (menu.isMenuShowing()) {
			menu.showContent();
		}
		else {
			if (writer.getpWindow() != null && writer.getpWindow().isShowing()) {
				writer.getpWindow().dismiss();
				return;
			}
			if (count == 0) {
				count++;
				Utils.showShortToast(getApplicationContext(), "再按一次返回桌面");
				ll_tb_tuijian.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						count = 0;
					}
				}, 2 * 1000);
				return;
			}

			super.onBackPressed();
		}
	}

	private void setOnclick() {
		// TODO Auto-generated method stub
		ll_tb_tuijian.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mianList.setType(Type.tuijian);
				if (!mianList.isVisible())
					manager.beginTransaction().replace(main_fragment_content.getId(), mianList).commit();
				else mianList.getTypeList(Type.tuijian);
				clearOnclick();
				tv_tuijian.setTextColor(Color.parseColor("#44c1c1"));

			}
		});

		ll_tb_paihang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mianList.setType(Type.paihang);
				if (!mianList.isVisible()) {
					manager.beginTransaction().replace(main_fragment_content.getId(), mianList).commit();
					mianList.getTypeList(Type.paihang);
				}
				else mianList.getTypeList(Type.paihang);
				clearOnclick();
				tv_paihang.setTextColor(Color.parseColor("#44c1c1"));
			}
		});

		ll_tb_sousuo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearOnclick();
				tv_sousuo.setTextColor(Color.parseColor("#44c1c1"));
				if (!search.isVisible())
					manager.beginTransaction().replace(main_fragment_content.getId(), search).commit();
			}
		});

		ll_tb_fabiao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearOnclick();
				tv_fabiao.setTextColor(Color.parseColor("#44c1c1"));
				if (!writer.isVisible())
					manager.beginTransaction().replace(main_fragment_content.getId(), writer).commit();
			}
		});

		iv_mycenter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				menu.showMenu();
			}
		});

		iv_history.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				menu.showSecondaryMenu();
			}
		});
	}

	/**
	 * 清除所有选中状态
	 */
	private void clearOnclick() {
		tv_tuijian.setTextColor(color.Black);
		tv_paihang.setTextColor(color.Black);
		tv_sousuo.setTextColor(color.Black);
		tv_fabiao.setTextColor(color.Black);
	}

	@Override
	public void add() {
		// TODO Auto-generated method stub
		closeAll();
		ACTIVITYS.add(this);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeAll() {
		// TODO Auto-generated method stub
		for (int i = 0; i < ACTIVITYS.size(); i++) {
			Activity ac = ACTIVITYS.get(i);
			ac.finish();
		}
		userFragmentl = null;
		historyFragment = null;
	}

	/**
	 * @return the application
	 */
	public HongxianggeApplication getApplication2() {
		return application;
	}

	/**
	 * @param application
	 *            the application to set
	 */
	public void setApplication(HongxianggeApplication application) {
		this.application = application;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(getLocalClassName());
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(getLocalClassName());
		MobclickAgent.onPause(this);
	}

	/**
	 * @return the pop
	 */
	public PopupWindow getPop() {
		return pop;
	}

	/**
	 * @param pop
	 *            the pop to set
	 */
	public void setPop(PopupWindow pop) {
		this.pop = pop;
	}
}
