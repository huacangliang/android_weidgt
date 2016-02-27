package com.hxgwx.www.adapter;

import java.util.List;

import com.hongxiangge.image.ImageManager2;
import com.hxgwx.www.R;
import com.hxgwx.www.bean.SendARCBodyImages;
import com.jingzhong.asyntask2.utils.Utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImagesAdapter extends BaseAdapter {
	List<SendARCBodyImages> source;
	Context ctx;

	public ImagesAdapter(List<SendARCBodyImages> source, Context ctx) {
		// TODO Auto-generated constructor stub
		this.source = source;
		this.ctx = ctx;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return source.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return source.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView iv = null;
		if (convertView == null) {
			iv = new ImageView(ctx);
			convertView = iv;
			LayoutParams lparams = new AbsListView.LayoutParams((int) Utils.pixelToDp(ctx, 100),
					(int) Utils.pixelToDp(ctx, 100));
			iv.setLayoutParams(lparams);
		} else {
			iv = (ImageView) convertView;
		}
		SendARCBodyImages path = (SendARCBodyImages) getItem(position);
		ImageManager2.from(ctx, false).displayImage(iv, path.getKey(), R.drawable.bg_nopic);

		return iv;
	}

}
