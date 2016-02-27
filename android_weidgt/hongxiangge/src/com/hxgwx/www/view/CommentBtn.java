package com.hxgwx.www.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hxgwx.www.activity.CommentActivity;
import com.sohu.cyan.android.sdk.api.CyanSdk;
import com.sohu.cyan.android.sdk.util.WidgetUtil;

@SuppressLint({"ViewConstructor"})
public class CommentBtn extends RelativeLayout {
	private ImageButton commentBtn;

	public CommentBtn(Context context) {
		super(context);
		setId(9004);
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
				WidgetUtil.dip2px(context, 35.0F), WidgetUtil.dip2px(context,
						30.0F));
		p.setMargins(WidgetUtil.dip2px(context, 27.5F), 0, 0, 0);
		setLayoutParams(p);
		this.commentBtn = new ImageButton(context);
		RelativeLayout.LayoutParams cmtBtnLp = new RelativeLayout.LayoutParams(
				WidgetUtil.dip2px(context, 30.0F), WidgetUtil.dip2px(context,
						30.0F));

		cmtBtnLp.addRule(10);
		cmtBtnLp.addRule(9);
		this.commentBtn.setAdjustViewBounds(true);
		this.commentBtn.setPadding(WidgetUtil.dip2px(context, 2.0F),
				WidgetUtil.dip2px(context, 2.0F),
				WidgetUtil.dip2px(context, 2.0F),
				WidgetUtil.dip2px(context, 2.0F));

		this.commentBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		this.commentBtn.setLayoutParams(cmtBtnLp);
		this.commentBtn.setImageBitmap(CyanSdk.ico01);

		this.commentBtn.setBackgroundResource(0);
		setPadding(WidgetUtil.dip2px(context, 2.5F), 0,
				WidgetUtil.dip2px(context, 2.5F), 0);
		addView(this.commentBtn);
	}

	public void bindClickListener(final String aid) {
		this.commentBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), CommentActivity.class);
				intent.putExtra("id", Integer.parseInt(aid));
				// 进入评论列表
				v.getContext().startActivity(intent);
			}
		});
	}
}