package com.hxgwx.www.view;

import com.sohu.cyan.android.sdk.api.CyanSdk;
import com.sohu.cyan.android.sdk.util.WidgetUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.LinearLayout;

public class ToolBarLayout extends LinearLayout {
	Paint mPaint = new Paint();

	public ToolBarLayout(Context context) {
		super(context);
		setFocusable(true);
		setFocusableInTouchMode(true);
		setPadding(WidgetUtil.dip2px(context, 15.0F),
				WidgetUtil.dip2px(context, 12.5F),
				WidgetUtil.dip2px(context, 15.0F),
				WidgetUtil.dip2px(context, 12.5F));

		setBackgroundColor(CyanSdk.config.ui.toolbar_bg);
	}

	protected void onDraw(Canvas canvas) {
		int borderWidth = WidgetUtil.dip2px(getContext(), 1.0F);
		this.mPaint.setStyle(Paint.Style.STROKE);
		this.mPaint.setStrokeWidth(borderWidth);
		this.mPaint.setColor(CyanSdk.config.ui.toolbar_border);
		super.onDraw(canvas);
		canvas.drawLine(0.0F, 0.0F, getWidth() - 1, 0.0F, this.mPaint);
	}

	public void init(final String aid, int count) {

		TextButton txtbtn = (TextButton) ToolBarLayout.this.findViewById(9002);
		CountView cv = (CountView) ToolBarLayout.this.findViewById(9001);
		CommentBtn commentBtn = (CommentBtn) ToolBarLayout.this
				.findViewById(9004);
		
		String cst = "";
		
		if(count<10000)
			cst=count+"";
		else
			cst="1Íò+";
		
		cv.setText(cst);
		txtbtn.bindCLickListener(aid);
		commentBtn.bindClickListener(aid);
	}
}