package com.hxgwx.www.adapter;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxgwx.www.R;
import com.hxgwx.www.activity.ReadActivity;
import com.hxgwx.www.bean.ArticModelBase;
import com.jingzhong.asyntask2.utils.Utils;

public class LocationArcAdapter extends BaseAdapter {

	private List<ArticModelBase> baseList;
	private Context context;
	private boolean isEdit;
	private List<Integer> editIdList = new LinkedList<Integer>();

	/**
	 * @return the editIdList
	 */
	public List<Integer> getEditIdList() {
		return editIdList;
	}

	/**
	 * @param editIdList the editIdList to set
	 */
	public void setEditIdList(List<Integer> editIdList) {
		this.editIdList = editIdList;
	}

	public LocationArcAdapter(List<ArticModelBase> baseList, Context context) {
		// TODO Auto-generated constructor stub
		this.baseList = baseList;
		this.context = context;
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

	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = LinearLayout.inflate(context,
					R.layout.read_history_item, null);
			holder.tv_h_author = (TextView) convertView
					.findViewById(R.id.tv_h_author);
			holder.tv_h_name = (TextView) convertView
					.findViewById(R.id.tv_h_name);
			holder.tv_h_time = (TextView) convertView
					.findViewById(R.id.tv_h_time);
			holder.tv_h_des = (TextView) convertView
					.findViewById(R.id.tv_h_des);
			holder.checkBox1 = (CheckBox) convertView
					.findViewById(R.id.checkBox1);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final ArticModelBase bean = baseList.get(position);
		holder.tv_h_author.setText(Html.fromHtml(bean.getWriter()));
		holder.tv_h_name.setText(Html.fromHtml(bean.getTitle()));
		holder.tv_h_time.setText(format.format(bean.getReadTime()));
		holder.tv_h_des.setText(Html.fromHtml(bean.getDescription()));
		if (isEdit)
			holder.checkBox1.setVisibility(View.VISIBLE);
		else{
			holder.checkBox1.setChecked(false);
			holder.checkBox1.setVisibility(View.GONE);
		}
		
		holder.checkBox1
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						bean.setChecked(isChecked);
						if (isChecked) {
							editIdList.add(bean.getId());
						} else {
							editIdList.remove(Integer.valueOf(bean.getId()));
						}
					}
				});
		holder.checkBox1.setChecked(bean.isChecked());
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				// TODO Auto-generated method stub
				i.putExtra("arc", bean);
				Utils.gotoActivity(context, ReadActivity.class, i);
			}
		});
		return convertView;
	}

	/**
	 * @return the isEdit
	 */
	public boolean isEdit() {
		return isEdit;
	}

	/**
	 * @param isEdit
	 *            the isEdit to set
	 */
	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}

	private class Holder {
		TextView tv_h_time;
		TextView tv_h_name;
		TextView tv_h_author;
		TextView tv_h_des;
		CheckBox checkBox1;
	}

}
