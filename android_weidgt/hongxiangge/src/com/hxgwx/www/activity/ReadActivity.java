package com.hxgwx.www.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.hxgwx.www.HongxianggeApplication;
import com.hxgwx.www.R;
import com.hxgwx.www.bean.ArticBody;
import com.hxgwx.www.bean.ArticModelBase;
import com.hxgwx.www.bean.FeedbackBase;
import com.hxgwx.www.db.BookMarkDBHelper;
import com.hxgwx.www.db.HistoryDBHelper;
import com.hxgwx.www.fragment.MainListFragment;
import com.hxgwx.www.utils.ActivityManagerUtils;
import com.hxgwx.www.utils.SystemBarTintManager;
import com.hxgwx.www.view.CommentBtn;
import com.hxgwx.www.view.CountView;
import com.hxgwx.www.view.TextButton;
import com.hxgwx.www.view.ToolBarLayout;
import com.hxgwx.www.web.WebPlus;
import com.hxgwx.www.webimpl.WebPlusImpl;
import com.jingzhong.asyntask2.Asyntask2;
import com.jingzhong.asyntask2.annotation.CreateView;
import com.jingzhong.asyntask2.utils.Utils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

public class ReadActivity
		extends Activity
		implements ActivityManagerUtils {

	@CreateView("tv_artic_author")
	private TextView				tv_artic_author;

	@CreateView("tv_artic_time")
	private TextView				tv_artic_time;

	@CreateView("tv_click")
	private TextView				tv_click;

	@CreateView("tv_title")
	private TextView				tv_title;

	@CreateView("tv_set")
	private TextView				tv_set;

	@CreateView("ll_data")
	private LinearLayout			ll_data;

	@CreateView("ll_set")
	private LinearLayout			ll_set;

	@CreateView("tv_content_title")
	private TextView				tv_content_title;

	@CreateView("tv_type")
	private TextView				tv_type;

	@CreateView("bt_add_book")
	private Button					bt_add_book;

	@CreateView("bt_setting_font")
	private Button					bt_setting_font;

	@CreateView("bt_choice_bg")
	private Button					bt_choice_bg;

	@CreateView("sb_font_change")
	private SeekBar					sb_font_change;

	@CreateView("bt_choice_fullscreen")
	private Button					bt_choice_fullscreen;

	@CreateView("sc_content")
	private PullToRefreshScrollView	sc_content;

	private WebPlus					web;

	private HongxianggeApplication	application;

	WebSettings						set;

	private ProgressDialog			progress;

	private void showFull() {
		ll_set.setVisibility(View.GONE);
		FullScreenWebViewActivity.start(content, this, bg, fontSize);
	}

	private boolean				waitDouble			= true;

	private static final int	DOUBLE_CLICK_TIME	= 350;						// 两次单击的时间间隔

	OnClickListener				listener			= new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (waitDouble == true) {
				waitDouble = false;
				Thread thread = new Thread() {

					@Override
					public void run() {
						try {
							sleep(DOUBLE_CLICK_TIME);
							if (waitDouble == false) {
								waitDouble = true;
								singleClick();
							}
						}
						catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
				thread.start();
			}
			else {
				waitDouble = true;
				doubleClick();
			}
		}
	};

	// 单击响应事件
	private void singleClick() {

	}

	// 双击响应事件
	private void doubleClick() {
		showFull();
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

	ArticBody		rec;

	public WebView	wv_content;

	public class Query
			extends Asyntask2<String, String, ArticBody> {

		// 评论数
		private int count;

		@Override
		protected ArticBody doInbackProgres(String... params) {
			// TODO Auto-generated method stub

			rec = web.queryArtic(model.getId() + "");
			if (rec == null) {
				updateProgress("未知错误，请检测网络连接是否稳定");
			}
			// 接口获取评论数
			FeedbackBase res = web.queryFeedbackCount(model.getId());
			if (res != null) {
				if (res.isStatu()) {
					count = res.getCount();
				}
				else {
					String msg = HongxianggeApplication.getInstance().getDescriptionByCode(res.getCode() + "");
					updateProgress(msg);
				}
			}
			return rec;
		}

		@Override
		protected void doProgress(String... p) {
			// TODO Auto-generated method stub
			super.doProgress(p);

			com.hxgwx.www.utils.Utils.showLongToast(getApplicationContext(), p[0]);

		}

		@Override
		protected void doResult(ArticBody res) {
			// TODO Auto-generated method stub
			super.doResult(res);
			if (progress.isShowing()) {
				progress.dismiss();
			}
			sc_content.onRefreshComplete();
			if (res != null) {
				if (!res.isStatu()) {
					String des = application.getDescriptionByCode(res.getCode() + "");
					com.hxgwx.www.utils.Utils.showLongToast(getApplicationContext(), des);
					return;
				}
				if (res.getData() == null || res.getData().size() == 0) {
					com.hxgwx.www.utils.Utils.showLongToast(getApplicationContext(), "文章不存在");
					return;
				}
				String text = res.getData().get(0).getBody();
				text = com.hxgwx.www.utils.Utils.setImageUrl(text);
				text = com.hxgwx.www.utils.Utils.parseNetNbsp(text);
				text = text.replace(
						"--<a href=\"http://android.myapp.com/myapp/detail.htm?apkName=com.hxgwx.www\">来自红香阁手机客户端</a>",
						"");
				res.getData().get(0).setBody(text);
				if (wv_content != null) {
					wv_content.loadData(text, "text/html; charset=UTF-8", null);
					content = text;
					addCommentToolbar(model.getId() + "", count);
				}
			}

		}
	}

	private String content;

	private LinearLayout getToolBar(String aid, int count) {
		ToolBarLayout toolbarll = new ToolBarLayout(this);
		TextButton textBtn = new TextButton(this);
		toolbarll.addView(textBtn);

		RelativeLayout buttonGroup = new RelativeLayout(this);
		buttonGroup.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));

		CommentBtn commentBtn = new CommentBtn(this);
		CountView countText = new CountView(this);
		commentBtn.addView(countText);
		buttonGroup.addView(commentBtn);
		toolbarll.addView(buttonGroup);
		toolbarll.init(aid, count);
		return toolbarll;
	}

	public void addCommentToolbar(String aid, int count) {

		LinearLayout toolbar = getToolBar(aid, count);
		ViewGroup rootView = (ViewGroup) ((ViewGroup) this.findViewById(16908290)).getChildAt(0);

		if ((rootView instanceof RelativeLayout)) {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -2);

			params.addRule(12);
			params.addRule(9);
			rootView.addView(toolbar, params);
		}

		if ((rootView instanceof LinearLayout)) {
			toolbar.setGravity(80);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);

			rootView.addView(toolbar, params);
		}
	}

	String			comment	= "";

	private String	status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		add();
		setContentView(R.layout.artic_read);
		SystemBarTintManager.init(this);
		try {
			Utils.injectObject(this, this, R.id.class);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		application = (HongxianggeApplication) getApplication();
		web = new WebPlusImpl(this);
		model = (ArticModelBase) getIntent().getSerializableExtra("arc");
		model.setReadTime(new Date());
		init();
		db = HistoryDBHelper.getInstance(this);
		bDb = BookMarkDBHelper.getInstance(this);
		status = com.hxgwx.www.utils.Utils.getArcTypeByRank(model.getArcrank());
		if (status.equals("已审核")) {
			setReadHistory();
		}
		com.hxgwx.www.utils.Utils.showGuide(this, R.drawable.hxg_index_full_reader);
		
		PushAgent.getInstance(this).onAppStart();
	}

	private void setReadHistory() {

		try {

			if (db.createDb().findById(ArticModelBase.class, model.getId()) != null) {
				db.createDb().deleteById(ArticModelBase.class, model.getId());
			}
			else if (db.createDb().findFirst(Selector.from(ArticModelBase.class).where("title", "=", model.getTitle())
					.and("writer", "=", model.getWriter())) != null) {
				db.createDb().delete(ArticModelBase.class,
						WhereBuilder.b("title", "=", model.getTitle()).and("writer", "=", model.getWriter()));
			}
			db.createDb().save(model);
		}
		catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initAd() {
		// TODO Auto-generated method stub
		// 实例化广告条
		adView = new AdView(this, AdSize.SIZE_468x60);

		// 获取要嵌入广告条的布局
		adLayout = (LinearLayout) findViewById(R.id.adLayout);

		// 将广告条加入到布局中
		adLayout.addView(adView);

	}

	LinearLayout			adLayout;

	AdView					adView;

	HistoryDBHelper			db;

	BookMarkDBHelper		bDb;

	private ArticModelBase	model;

	private PopupWindow		pop;

	private List<Integer>	colorList	= new ArrayList<Integer>();

	@SuppressWarnings("deprecation")
	private void showPop() {
		colorList.clear();
		colorList.add(Color.BLUE);
		colorList.add(Color.GRAY);
		colorList.add(Color.GREEN);
		colorList.add(Color.WHITE);
		colorList.add(Color.parseColor("#009688"));
		colorList.add(Color.parseColor("#FFC107"));
		colorList.add(Color.parseColor("#CDDC39"));
		colorList.add(Color.parseColor("#8BC34A"));
		colorList.add(Color.parseColor("#4CAF50"));
		colorList.add(Color.parseColor("#FF5722"));
		colorList.add(Color.parseColor("#795548"));
		colorList.add(Color.parseColor("#9E9E9E"));
		colorList.add(Color.parseColor("#607D8B"));
		pop = new PopupWindow();
		View contentView = LinearLayout.inflate(getApplicationContext(), R.layout.choice_color_bg, null);
		GridView gv = (GridView) contentView.findViewById(R.id.gv_color);
		gv.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				View v = LinearLayout.inflate(getApplicationContext(), R.layout.choice_color_item, null);
				View b = v.findViewById(R.id.v_color_bg);
				final Integer c = colorList.get(position);
				b.setBackgroundColor(c);
				v.setBackgroundColor(Color.DKGRAY);
				v.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						setBg(c);
						pop.dismiss();
					}
				});
				return v;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return colorList.get(position);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return colorList.size();
			}
		});
		pop.setWidth(getWindowManager().getDefaultDisplay().getWidth());
		pop.setHeight(getWindowManager().getDefaultDisplay().getHeight());
		// pop.setBackgroundDrawable(getResources().getDrawable(
		// R.drawable.choice_color_bg));
		pop.setClippingEnabled(true);

		pop.setContentView(contentView);
		pop.showAtLocation(sc_content, Gravity.CENTER | Gravity.CENTER, 0, 0);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (wv_content.canGoBack()) {
				wv_content.goBack();
				return true;
			}
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (rec == null || rec.getData() == null) return super.onKeyDown(keyCode, event);
			// TODO Auto-generated method stub
			if (ll_set.getVisibility() == View.VISIBLE) {
				ll_set.setVisibility(View.GONE);
			}
			else {
				ll_set.setVisibility(View.VISIBLE);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private int fontSize = 0;

	@Override
	protected void onDestroy() {
		if (wv_content != null) {
			wv_content.destroy();
			ll_data.removeView(wv_content);
			wv_content = null;
		}
		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
	private void init() {
		initAd();
		bt_choice_bg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPop();
				ll_set.setVisibility(View.GONE);
			}
		});

		sb_font_change.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

				seekBar.setVisibility(View.GONE);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				fontSize = seekBar.getProgress() * 10;
				set.setTextZoom(fontSize);
			}
		});

		bt_choice_fullscreen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showFull();
			}
		});
		wv_content = new WebView(this);
		ll_data.addView(wv_content);
		set = wv_content.getSettings();
		set.setJavaScriptEnabled(true);
		set.setDefaultTextEncodingName("UTF-8");
		set.setBlockNetworkLoads(true);
		set.setBlockNetworkImage(true);
		set.setSupportZoom(true); // 允许缩放
		set.setAllowContentAccess(true);
		set.setAllowFileAccess(true);
		set.setAppCacheEnabled(true);
		set.setAppCachePath(
				com.hxgwx.www.utils.Utils.getExtenalPath(getApplicationContext(), getPackageName() + "/data/cache"));
		set.setBuiltInZoomControls(true);
		set.setCacheMode(WebSettings.LOAD_DEFAULT);
		set.setDatabaseEnabled(true);
		set.setDatabasePath(
				com.hxgwx.www.utils.Utils.getExtenalPath(getApplicationContext(), getPackageName() + "/data/cache"));
		set.setDisplayZoomControls(false);
		set.setDomStorageEnabled(true);
		set.setSaveFormData(true);
		set.setJavaScriptCanOpenWindowsAutomatically(true);
		if (bg != 0) {
			setBg(bg);
		}
		else {
			bg = Color.WHITE;
		}
		if (fontSize != 0) {
			set.setTextZoom(fontSize);
		}
		else {
			fontSize = set.getTextZoom();
		}
		wv_content.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				set.setBlockNetworkLoads(false);
				set.setBlockNetworkImage(false);
				super.onPageFinished(view, url);
			}
		});

		wv_content.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				ll_set.setVisibility(View.GONE);
				sb_font_change.setVisibility(View.GONE);
				if (event.getAction() == MotionEvent.ACTION_UP) {
					listener.onClick(v);
				}
				return false;
			}
		});

		if (model != null) {
			tv_type.setText(MainListFragment.TYPES.get(model.getTypeid()));
			tv_artic_author.setText(model.getWriter());
			tv_click.setText(model.getClick() + "");
			tv_title.setText(Html.fromHtml(model.getTitle()));
			tv_content_title.setText(Html.fromHtml(model.getTitle()));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dt = format.format(new Date(model.getPubdate() * 1000));
			tv_artic_time.setText(dt);
			tv_set.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (rec == null || rec.getData() == null) return;
					// TODO Auto-generated method stub
					if (ll_set.getVisibility() == View.VISIBLE) {
						ll_set.setVisibility(View.GONE);
					}
					else {
						ll_set.setVisibility(View.VISIBLE);
					}
					sb_font_change.setVisibility(View.GONE);
				}
			});

			showCheck();
			query.execute("");
		}

		bt_add_book.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ll_set.setVisibility(View.GONE);
				// TODO Auto-generated method stub
				if (!status.equals("已审核")) {
					Utils.showLongToast(getApplicationContext(), "文章未审核，不能加入书签");
					return;
				}
				if (com.hxgwx.www.utils.Utils.checkLogin(application)) {
					model.setReadTime(new Date());
					model.setLoginId(application.getUser().getData().getMid());
					try {
						ArticModelBase t = bDb.createDb()
								.findFirst(Selector.from(ArticModelBase.class).where("title", "=", model.getTitle()));
						if (t != null) bDb.createDb().delete(t);

						bDb.createDb().save(model);

						Utils.showLongToast(getApplicationContext(), "加入书签成功");
					}
					catch (DbException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		bt_setting_font.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ll_set.setVisibility(View.GONE);
				if (sb_font_change.getVisibility() == View.VISIBLE) {
					sb_font_change.setVisibility(View.GONE);
				}
				else {
					sb_font_change.setVisibility(View.VISIBLE);
				}
				ll_set.setVisibility(View.GONE);
			}
		});

		sc_content.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// TODO Auto-generated method stub
				query.execute("");
			}
		});
	}

	Query		query	= new Query();

	static int	bg		= 0;

	private void setBg(int color) {
		bg = color;
		sc_content.setBackgroundColor(color);
		wv_content.setBackgroundColor(color);
	}

	public final static String	CSS_WHITE_FONT	= "<style> {p {color:#FFFFFF;}</style>";

	public final static String	CSS_BLANK_FONT	= "<style> {p {color:#000000;}</style>";

	@SuppressWarnings({ "unused", "deprecation" })
	private void setBg(Drawable d) {
		sc_content.setBackgroundDrawable(d);
		wv_content.setBackgroundDrawable(d);
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
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (pop != null && pop.isShowing()) {
			pop.dismiss();
			return;
		}
		if (sb_font_change.getVisibility() == View.VISIBLE) {
			sb_font_change.setVisibility(View.GONE);
		}
		else {
			super.onBackPressed();
		}
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
