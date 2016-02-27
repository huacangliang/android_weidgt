package com.hxgwx.www.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hongxiangge.image.ImageManager2;
import com.hongxiangge.image.ImageManager2.OndwonListenear;
import com.hxgwx.www.HongxianggeApplication;
import com.hxgwx.www.R;
import com.hxgwx.www.activity.ReadActivity;
import com.hxgwx.www.bean.ArticModelBase;
import com.hxgwx.www.fragment.MainListFragment;
import com.hxgwx.www.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainListAdapter
		extends BaseAdapter {

	private List<ArticModelBase> baseList = new ArrayList<ArticModelBase>();

	public List<ArticModelBase> getBaseList() {
		return baseList;
	}

	public void setBaseList(List<ArticModelBase> baseList) {
		this.baseList = baseList;
	}

	private Context					context;

	public Map<String, ImageView>	images;

	private ImageManager2			imageManage;

	private ListView				listView;

	private boolean					scoll		= false;

	private UpdateThread			updateThread;

	HandlerThread					ht;

	List<Holder>					holderList	= new ArrayList<Holder>();

	public class UpdateThread
			extends Thread {

		ImageView	iv			= null;

		String		key			= null;

		boolean		hasUpdate	= false;

		Holder		holder;

		@Override
		public void run() {

			while (true) {

				if (hasUpdate) {
					hasUpdate = false;
					holder = new Holder();
					holder.iv = iv;
					holder.path = key;
					holderList.add(holder);

				}

				if (!scoll) {
					if (holderList.size() > 0) {
						for (int i = holderList.size() - 1; i >= 0; i--) {
							try {
								Thread.sleep(500);
							}
							catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Holder holder = holderList.get(i);
							Message msg = new Message();
							Bundle data = new Bundle();
							data.putSerializable("image", holder);
							msg.setData(data);
							updateIcon.sendMessage(msg);
						}
						holderList.clear();
					}

				}
				try {
					Thread.sleep(100);
				}
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	Handler updateIcon = new Handler() {

		public void handleMessage(android.os.Message msg) {
			Holder holder = (Holder) msg.getData().get("image");
			if (holder != null) {

				fillImageScod(holder.path, holder.iv);

			}

			if (msg.what == 1) {
				notifyDataSetChanged();
			}
		}

	};

	public void fillImageScod(String key, ImageView iv) {
		if (key != null && key.length() > 0) {

			if (iv.getTag().toString().length() > 0 && iv.getTag().toString().equalsIgnoreCase(key)) {
				iv.setBackgroundDrawable(null);

				imageManage.displayImage(iv, key, R.drawable.bg_nopic);

				imageManage.setSetOndwonListenear(new OndwonListenear() {

					@Override
					public void setStateListenear(boolean flag) {
						if (flag) {
							updateIcon.sendEmptyMessage(1);
						}

					}
				});

				// notifyDataSetChanged();
			}
			else {
				// iv.setImageResource(R.drawable.account_headdefault);
			}
		}

	}

	int	defaultHeiaght	= 0;

	int	defaultWidth	= 0;

	@SuppressWarnings("deprecation")
	public void fillImage(String key, ImageView iv) {
		imageManage = ImageManager2.from(context, false);
		if (key != null && key.length() > 0) {
			// 判断tag是为了防止图片错位
			if (iv.getTag().toString().trim().length() > 0 && iv.getTag().toString().equalsIgnoreCase(key)) {
				key = HongxianggeApplication.getInstance().getDomain() + key;
				if (!scoll) {
					Bitmap bit = imageManage.mMemoryCache.get(key);
					if (bit != null) {
						iv.setImageBitmap(bit);
					}
					else {
						iv.setBackgroundDrawable(null);
						imageManage.displayImage(iv, key, -1, defaultWidth, defaultHeiaght);
					}

				}
				else {
					Bitmap bit = imageManage.mMemoryCache.get(key);
					if (bit != null) {
						iv.setImageBitmap(bit);
					}
					else {
						updateThread.hasUpdate = true;
						updateThread.iv = iv;
						updateThread.key = key;
					}
				}

			}
		}
	}

	/**
	 * 必须在设置adapter的时候设置监听，否则会出错
	 */
	public void setOnlistener() {
		if (listView != null) {
			listView.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView arg0, int arg1) {
					if (arg1 == OnScrollListener.SCROLL_STATE_FLING) {
						scoll = true;
						// needTouch = true;
					}
					if (arg1 == OnScrollListener.SCROLL_STATE_IDLE) {
						scoll = false;
						// needTouch = false;
						updateIcon.sendEmptyMessage(1);
					}
				}

				@Override
				public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
					// needTouch=true;

				}
			});
			listView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
					}
					if (arg1.getAction() == MotionEvent.ACTION_MOVE) {
						scoll = true;
					}

					if (arg1.getAction() == MotionEvent.ACTION_UP) {
						scoll = false;
					}

					return false;
				}
			});
		}
	}

	public MainListAdapter(Context context, ListView listView, List<ArticModelBase> baseList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.listView = listView;
		this.baseList = baseList;
		defaultHeiaght = context.getResources().getDimensionPixelSize(R.dimen.dp100);
		defaultWidth = defaultHeiaght;
		updateThread = new UpdateThread();
		updateThread.start();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return baseList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return baseList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.main_content_list_item, null);
			holder = new Holder();
			holder.iv = (ImageView) convertView.findViewById(R.id.book_img);
			holder.title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.descript = (TextView) convertView.findViewById(R.id.tv_descript);

		}
		else {
			holder = (Holder) convertView.getTag();
		}
		final ArticModelBase model = (ArticModelBase) getItem(position);
		holder.title.setText(Html.fromHtml(model.getTitle()));
		model.setDescription(model.getDescription().replace(
				"--<a href=\"http://android.myapp.com/myapp/detail.htm?apkName=com.hxgwx.www\">来自红香阁手机客户端</a>", ""));
		holder.descript.setText(Html.fromHtml("<font size=\"12px\">【" + MainListFragment.TYPES.get(model.getTypeid())
				+ "】</font>\n" + model.getDescription()));
		LinearLayout.LayoutParams lParams = (android.widget.LinearLayout.LayoutParams) holder.iv.getLayoutParams();
		lParams.height = defaultHeiaght;
		lParams.width = defaultWidth;
		// 设置图片
		if (model.getLitpic() != null && model.getLitpic().length() > 0) {

			holder.iv.setTag(model.getLitpic());

			fillImage(model.getLitpic(), holder.iv);
		}
		else {
			holder.iv.setTag("");
			holder.iv.setImageResource(R.drawable.icon_book);

		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.putExtra("arc", model);
				Utils.gotoActivity(context, ReadActivity.class, i);
			}
		});

		convertView.setTag(holder);
		return convertView;
	}

	private class Holder
			implements Serializable {

		/**
		 * 
		 */
		private static final long	serialVersionUID	= 1L;

		ImageView					iv;

		TextView					title;

		TextView					descript;

		String						path;
	}
}
