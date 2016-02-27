package com.hongxiangge.image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 图片处理
 * 
 * @author dengzhijiang
 * 
 * 
 */
public class ImageManage {

	public static Bitmap getBitmapByInputstream(InputStream is) {
		if (is != null) {
			InputStream isO = is;
			Bitmap bit = null;
			try {
				bit = BitmapFactory.decodeStream(isO);
				return bit;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (isO != null) {
					try {
						isO.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (bit != null) {
					if (bit.isRecycled()) {
						System.gc();
					}
				}
			}

		}
		return null;

	}

	public final static String[] DRA = new String[4];
	static {
		DRA[0] = "btn_bclean_press.png";
		DRA[1] = "btn_bkafei_press.png";
		DRA[2] = "btn_bporridge_press.png";
		DRA[3] = "btn_bsoup_press.png";
		// try {
		// DRA[3] = PullJsonForMenu.getNewFunction().get(0).getImgUrl();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	/**
	 * 将bitmap转换成inputstream
	 * 
	 * @param bm
	 * @return
	 */
	public static InputStream Bitmap2IS(Bitmap bm) {
		if (bm == null) {
			return null;
		}
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 10, baos);
			InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
			return sbs;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (bm.isRecycled()) {
				bm.recycle();
			}

		}

		return null;
	}

	public static byte[] Bitmap2BT(Bitmap bm) {
		if (bm == null) {
			return null;
		}
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 100, baos);

			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (bm.isRecycled()) {
				bm.recycle();
			}

		}

		return null;
	}

	public static void save(Context context, List<String> paths) {

	}

	public static Bitmap getByAssets(Context context, String imageName,
			Object path) throws IOException {

		if (path == null) {
			AssetManager imageAssets = context.getAssets();
			InputStream input = imageAssets.open(imageName);
			Bitmap res = BitmapFactory.decodeStream(input);
			return res;
		} else {

		}
		return null;
	}

	/**
	 * 正方形获取圆角位图的方法
	 * 
	 * @param bitmap
	 *            需要转化成圆角的位图
	 * @param pixels
	 *            圆角的度数，数值越大，圆角越大
	 * @return 处理后的圆角位图
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 获取圆角位图的方法
	 * 
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap makeRoundCorner(Bitmap bitmap, int pixels) {

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int left = 0, top = 0, right = width, bottom = height;
		float roundPx = (float) ((height / 1) + height * 0.5);
		if (width > height) {
			left = (width - height) / 2;
			top = 0;
			right = left + height;
			bottom = height;
		} else if (height > width) {
			left = 0;
			top = (height - width) / 2;
			right = width;
			bottom = top + width;
			roundPx = (float) ((height / 1) + height * 0.5);
		} else {
			width = width / 3;
			height = height / 3;
			Bitmap output = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			int color = 0xff424242;
			Paint paint = new Paint();
			Rect rect = new Rect(0, 0, width, height);
			RectF rectF = new RectF(rect);
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, pixels, pixels, paint);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			return output;
		}

		Bitmap output = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		int color = 0xff424242;
		Paint paint = new Paint();
		Rect rect = new Rect(left, top, right, bottom);
		RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 
	 * @param x
	 *            图像的宽度
	 * @param y
	 *            图像的高度
	 * @param image
	 *            源图片
	 * @param outerRadiusRat
	 *            圆角的大小
	 * @return 圆角图片
	 */
	public static Bitmap createFramedPhoto(int x, int y, Bitmap image,
			float outerRadiusRat, Context context) {
		Bitmap output = null;
		try {
			// image=comp(image);
			// 根据源文件新建一个darwable对象
			Drawable imageDrawable = new BitmapDrawable(image);

			// 新建一个新的输出图片
			output = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			// 新建一个矩形
			RectF outerRect = new RectF(0, 0, x, y);

			// 产生一个红色的圆角矩形
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setColor(Color.RED);
			canvas.drawRoundRect(outerRect, outerRadiusRat, outerRadiusRat,
					paint);

			// 将源图片绘制到这个圆角矩形上
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
			imageDrawable.setBounds(0, 0, x, y);
			canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
			imageDrawable.draw(canvas);
			canvas.restore();

			return output;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				output.isRecycled();
				System.gc();
			}
		}
		return null;
	}

	// 按比例压缩图片
	private static Bitmap comp(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 100f;// 这里设置高度为800f
		float ww = 100f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	// 按质量压缩图片
	private static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}
}
