package com.hxgwx.www.activity;

import java.util.ArrayList;
import java.util.List;

import com.hxgwx.www.HongxianggeApplication;
import com.hxgwx.www.R;
import com.hxgwx.www.adapter.SearchListAdapter;
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
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ListView;

public class SearchActivity
		extends Activity {

	@CreateView("et_search")
	private EditText				et_search;

	@CreateView("lv_search_mian")
	private ListView				lv_search_mian;

	String							q;

	private HongxianggeApplication	application;

	public ListView getLv_search_mian() {
		return lv_search_mian;
	}

	public void setLv_search_mian(ListView lv_search_mian) {
		this.lv_search_mian = lv_search_mian;
	}

	public HongxianggeApplication getApplication2() {
		return application;
	}

	public void setApplication(HongxianggeApplication application) {
		this.application = application;
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

	private SearchListAdapter		adapter;

	private WebPlus					web;

	private List<ArticModelBase>	baseList	= new ArrayList<ArticModelBase>();

	private queryList				query		= new queryList();

	private class queryList
			extends Asyntask2<String, String, ArticList> {

		@Override
		protected ArticList doInbackProgres(String... params) {
			// TODO Auto-generated method stub
			ArticList list = web.search(q);
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
			if (progress.isShowing()) {
				progress.dismiss();
			}
			if (res != null) {
				if (res.isStatu()) {
					if (res.getData() == null || res.getData().size() <= 0) {
						updateProgress("没有检索到数据");
					}
					else {
						baseList.addAll(res.getData());
						adapter.notifyDataSetChanged();
					}
				}
				else {
					updateProgress("获取数据失败：" + application.getDescriptionByCode(res.getCode() + ""));
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serch_page);
		try {
			Utils.injectObject(this, this, R.id.class);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		init();
		
		PushAgent.getInstance(this).onAppStart();
	}

	private void init() {
		// TODO Auto-generated method stub
		application = (HongxianggeApplication) this.getApplication();
		adapter = new SearchListAdapter(this, this, baseList);
		web = new WebPlusImpl(this);

		lv_search_mian.setAdapter(adapter);
		et_search.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODOAuto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					search();
					return true;
				}
				return false;
			}
		});
	}

	private void search() {
		if (!TextUtils.isEmpty(et_search.getText())) {
			baseList.clear();
			adapter.notifyDataSetChanged();
			q = et_search.getText().toString().trim();
			showCheck();
			query.execute("");
			Utils.hideInputMethod(getApplicationContext(), getWindow().getDecorView());
			et_search.setText("");
		}
	}

	public void onSearch(View v) {
		search();
	}

	public void onBack(View view) {
		onBackPressed();
	}
}
