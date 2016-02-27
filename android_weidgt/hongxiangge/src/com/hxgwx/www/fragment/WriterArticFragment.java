package com.hxgwx.www.fragment;

import java.util.ArrayList;
import java.util.List;

import com.hxgwx.www.HongxianggeApplication;
import com.hxgwx.www.R;
import com.hxgwx.www.activity.SearchActivity;
import com.hxgwx.www.activity.WriterActivity;
import com.hxgwx.www.bean.SendARCBody;
import com.hxgwx.www.db.ArcMoelDBhelper;
import com.jingzhong.asyntask2.annotation.CreateView;
import com.jingzhong.asyntask2.utils.Utils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

public class WriterArticFragment extends Fragment {
	@CreateView("tv_write_new")
	private TextView tv_write_new;
	@CreateView("tv_write_old")
	private TextView tv_write_old;
	private View baseView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		baseView = inflater.inflate(R.layout.writer_fragment, null);
		try {
			Utils.injectObject(this, baseView, R.id.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tv_write_new.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (MainListFragment.TYPES.size() <= 0) {
					com.hxgwx.www.utils.Utils.showLongToast(getActivity(), "分类信息获取失败，请退出重启试试");
					return;
				}
				if (!com.hxgwx.www.utils.Utils.checkLogin((HongxianggeApplication) getActivity().getApplication())) {
					com.hxgwx.www.utils.Utils.showLongToast(getActivity(), "请登录");
					return;
				}
				com.hxgwx.www.utils.Utils.gotoActivity(getActivity(), WriterActivity.class, null);
			}
		});
		tv_write_old.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (MainListFragment.TYPES.size() <= 0) {
					com.hxgwx.www.utils.Utils.showLongToast(getActivity(), "分类信息获取失败，请退出重启试试");
					return;
				}
				if (!com.hxgwx.www.utils.Utils.checkLogin((HongxianggeApplication) getActivity().getApplication())) {
					com.hxgwx.www.utils.Utils.showLongToast(getActivity(), "请登录");
					return;
				}
				showTitle();

			}
		});
		initAd(baseView);
		return baseView;
	}

	private void initAd(View v) {
		// TODO Auto-generated method stub
		// 实例化广告条
		AdView adView = new AdView(getActivity(), AdSize.FIT_SCREEN);

		// 获取要嵌入广告条的布局
		LinearLayout adLayout = (LinearLayout) v.findViewById(R.id.adLayout);

		// 将广告条加入到布局中
		adLayout.addView(adView);
	}

	private PopupWindow pWindow;

	/**
	 * @return the pWindow
	 */
	public PopupWindow getpWindow() {
		return pWindow;
	}

	private ArcMoelDBhelper adb;

	private typeAdapter aAdapter;

	private void showTitle() {

		if (pWindow != null) {
			if (pWindow.isShowing()) {
				pWindow.dismiss();
			}
			try {
				List<SendARCBody> list = adb.createDb().findAll(Selector.from(SendARCBody.class).where("mid", "=",
						((HongxianggeApplication) getActivity().getApplication()).getUser().getData().getMid()));
				bodyList.clear();
				if (list == null || list.size() <= 0) {
					aAdapter.notifyDataSetChanged();
					Utils.showLongToast(getActivity(), "您没有保存记录");
					com.hxgwx.www.utils.Utils.gotoActivity(getActivity(), WriterActivity.class, null);
					return;
				}
				bodyList.addAll(list);
				aAdapter.notifyDataSetChanged();
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pWindow.showAtLocation(baseView, Gravity.CENTER, 0, 0);
			return;
		}

		aAdapter = new typeAdapter();

		adb = ArcMoelDBhelper.getInstance(getActivity());

		try {
			List<SendARCBody> list = adb.createDb().findAll(Selector.from(SendARCBody.class).where("mid", "=",
					((HongxianggeApplication) getActivity().getApplication()).getUser().getData().getMid()));
			if (list == null || list.size() <= 0) {
				Utils.showLongToast(getActivity(), "您没有保存记录");
				com.hxgwx.www.utils.Utils.gotoActivity(getActivity(), WriterActivity.class, null);
				return;
			}
			bodyList.addAll(list);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pWindow = new PopupWindow();
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
		int mScreenHeight = dm.heightPixels;

		pWindow.setWidth(mScreenWidth);
		pWindow.setHeight(mScreenHeight);

		final View view = LinearLayout.inflate(getActivity(), R.layout.arc_types, null);
		ListView lv = (ListView) view.findViewById(R.id.lv_types);
		pWindow.setContentView(view);
		pWindow.setAnimationStyle(android.R.anim.fade_in);

		pWindow.setOutsideTouchable(false);
		pWindow.setFocusable(false);
		lv.setAdapter(aAdapter);

		pWindow.showAtLocation(baseView, Gravity.CENTER, 0, 0);
	}

	private List<SendARCBody> bodyList = new ArrayList<SendARCBody>();

	private class typeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return bodyList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return bodyList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = LinearLayout.inflate(getActivity(), R.layout.types_item, null);
			TextView tv = (TextView) convertView.findViewById(R.id.tv_type);
			final SendARCBody body = bodyList.get(position);
			if (body==null) {
				return tv;
			}
			tv.setText(body.getTitle());
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					pWindow.dismiss();
					Intent i = getActivity().getIntent();
					i.putExtra("from", 1);
					i.putExtra("body", body);
					com.hxgwx.www.utils.Utils.gotoActivity(getActivity(), WriterActivity.class, i);
				}
			});
			return convertView;
		}

	}

}
