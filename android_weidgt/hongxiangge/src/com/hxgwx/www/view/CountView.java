package com.hxgwx.www.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.sohu.cyan.android.sdk.util.WidgetUtil;

public class CountView extends TextView
{
  public CountView(Context context)
  {
    super(context);
    setId(9001);
    setText(String.valueOf(0));
    setBackgroundDrawable(new ShapeDrawable(new RoundCornerRect()));
    setTextSize(10.0F);
    setTextColor(-1);
    setPadding(WidgetUtil.dip2px(context, 1.0F), 0, WidgetUtil.dip2px(context, 1.0F), 0);
    RelativeLayout.LayoutParams countTextLp = new RelativeLayout.LayoutParams(-2, -2);

    countTextLp.addRule(10);
    countTextLp.addRule(11);
    setLayoutParams(countTextLp);
  }
  public class RoundCornerRect extends Shape {
    public RoundCornerRect() {
    }

    public void draw(Canvas canvas, Paint paint) {
      paint.setColor(Color.rgb(Integer.parseInt("ED", 16), Integer.parseInt("54", 16), Integer.parseInt("28", 16)));

      canvas.drawRoundRect(new RectF(0.0F, 0.0F, canvas.getWidth(), canvas.getHeight()), 3.0F, 3.0F, paint);
    }
  }
}