package com.hxgwx.www.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxgwx.www.R;
import com.hxgwx.www.activity.ReplyActivity;
import com.hxgwx.www.bean.FeedbackSubData;
import com.hxgwx.www.bean.FeedbackTopData;
import com.jingzhong.asyntask2.utils.ViewHolderUtils;

public class CommentAdapter extends BaseAdapter {

	private List<Object> data;
	private Context context;

	public CommentAdapter(List<Object> data, Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Object bean = getItem(position);
		View root = convertView;

		root = LinearLayout.inflate(context, R.layout.comment_item, null);
		TextView tv_uname = (TextView) ViewHolderUtils.getViewById(root,
				R.id.tv_title);

		TextView tv_postdate = (TextView) ViewHolderUtils.getViewById(root,
				R.id.tv_postdate);

		TextView tv_message = (TextView) ViewHolderUtils.getViewById(root,
				R.id.tv_message);
		TextView tv_author = (TextView) ViewHolderUtils.getViewById(root,
				R.id.tv_author);
		TextView tv_reply = (TextView) ViewHolderUtils.getViewById(root,
				R.id.tv_reply);

		if (bean instanceof FeedbackTopData) {
			FeedbackTopData data = (FeedbackTopData) bean;
			tv_uname.setText(data.getArctitle());
			tv_postdate.setText(data.getPostdate());
			tv_author.setText("作者：" + data.getUname());
			tv_message.setText(data.getMessage());
			tv_reply.setText("回复（" + data.getSub_feedback_count() + "）");
			final int id = data.getId();
			final String name = data.getUname();
			tv_reply.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ReplyActivity.class);
					intent.putExtra("id", id);
					intent.putExtra("commentName", name);
					v.getContext().startActivity(intent);
				}
			});
		} else if (bean instanceof FeedbackSubData) {
			FeedbackSubData data = (FeedbackSubData) bean;
			tv_uname.setText(data.getTf_title());
			tv_postdate.setText(data.getPostdate());
			tv_author.setText("作者：" + data.getUname());
			tv_message.setText(data.getMessage());
			tv_reply.setVisibility(View.GONE);
		}
		return root;
	}

}
