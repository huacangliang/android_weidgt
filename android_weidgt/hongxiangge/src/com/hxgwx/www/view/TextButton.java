package com.hxgwx.www.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hxgwx.www.HongxianggeApplication;
import com.hxgwx.www.activity.LoginActivity;
import com.hxgwx.www.activity.PostCommentActivity;
import com.sohu.cyan.android.sdk.util.WidgetUtil;

@SuppressLint({"ViewConstructor"})
public class TextButton extends Button {
	@SuppressWarnings("deprecation")
	public TextButton(Context context) {
		super(context);
		setId(9002);
		setBackgroundDrawable(new ShapeDrawable(new backGroudShape()));
		setText("我来说两句");
		setTextColor(Color.rgb(Integer.parseInt("DA", 16),
				Integer.parseInt("DA", 16), Integer.parseInt("DA", 16)));
		setGravity(19);
		LinearLayout.LayoutParams etLp = new LinearLayout.LayoutParams(0,
				WidgetUtil.dip2px(context, 30.0F), 2.0F);
		setLayoutParams(etLp);
		setTextSize(16.0F);
		setPadding(WidgetUtil.dip2px(context, 10.0F), 0,
				WidgetUtil.dip2px(context, 5.0F), 0);
	}

	public void bindCLickListener(final String aid) {
		setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!HongxianggeApplication.getInstance().isLogin) {
					Intent intent = new Intent(getContext(),
							LoginActivity.class);
					getContext().startActivity(intent);
					Toast.makeText(getContext(), "请登录", 1000).show();
					return;
				}
				Intent intent = new Intent(getContext(),
						PostCommentActivity.class);
				intent.putExtra("id", Integer.parseInt(aid));
				intent.putExtra("title", HongxianggeApplication.getInstance()
						.getUser().getData().getUname()
						+ "发表的评论");
				intent.putExtra("type", 0);
				// 触发发布评论按钮
				getContext().startActivity(intent);
			}
		});
	}

	private class backGroudShape extends Shape {
		private backGroudShape() {
		}

		public void draw(Canvas canvas, Paint paint) {
			int borderWidth = WidgetUtil.dip2px(TextButton.this.getContext(),
					1.0F);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(borderWidth);
			paint.setColor(Color.rgb(Integer.parseInt("DA", 16),
					Integer.parseInt("DA", 16), Integer.parseInt("DA", 16)));

			Rect rect = new Rect(borderWidth, borderWidth, canvas.getWidth()
					- borderWidth, canvas.getHeight() - borderWidth);

			RectF rectf = new RectF(rect);
			canvas.drawRoundRect(rectf, borderWidth, borderWidth, paint);

			paint.setStyle(Paint.Style.FILL);
			paint.setColor(-1);
			Rect rectBack = new Rect(borderWidth * 2, borderWidth * 2,
					canvas.getWidth() - borderWidth * 2, canvas.getHeight()
							- borderWidth * 2);

			RectF rectfBack = new RectF(rectBack);
			canvas.drawRoundRect(rectfBack, borderWidth, borderWidth, paint);
		}
	}
}