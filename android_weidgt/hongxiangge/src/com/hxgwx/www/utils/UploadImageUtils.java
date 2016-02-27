package com.hxgwx.www.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import com.hxgwx.www.HongxianggeApplication;
import com.hxgwx.www.bean.ImageUploadBean;
import com.hxgwx.www.bean.SendARCBodyImages;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

public class UploadImageUtils
		extends AsyncTask<Object, Integer, ImageUploadBean> {

	/**
	 * ���ݷ������ȡͼƬ
	 * 
	 * @param requestCode
	 * @param data
	 */
	public static String doPhoto(int requestCode, Intent data, Context ctx) {
		Uri uri = null;
		String path = null;
		ContentResolver cr = ctx.getContentResolver();
		uri = data.getData();
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = cr.query(uri, proj, null, null, null);
		if (cursor == null) {
			path = uri.getPath();
		}
		else {
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

			cursor.moveToFirst();
			path = cursor.getString(column_index);// ͼƬ�ڵ�·��
		}
		return path;

	}

	private boolean isfinish = false;

	public SendARCBodyImages UploadImage(final SendARCBodyImages path, Context ctx) {
		UploadImageUtils up = new UploadImageUtils();
		isfinish = false;
		path.setUpload(false);
		up.setCallBack(new CallBack() {

			@Override
			public void succes(String url) {
				// TODO Auto-generated method stub
				String text = "";
				text += "<img src=\"" + url + "\"/><br/>";
				path.setKey(path.getKey());
				path.setImage(text);
				isfinish = true;
				path.setUpload(true);
			}

			@Override
			public void filar(String msg) {
				// TODO Auto-generated method stub
				path.setUpload(false);
				isfinish = true;
			}
		});
		up.execute(new Object[] { path.getKey(), ctx });
		while (!isfinish) {
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!path.isUpload()) { return null; }
		return path;

	}

	@SuppressWarnings("deprecation")
	@Override
	protected ImageUploadBean doInBackground(Object... params) {
		// TODO Auto-generated method stub
		// ��һ��ѹ��ͼƬ���浽��ʱĿ¼
		// �ڶ����ϴ�ͼƬ
		// �����������ϴ�ʧ�ܻ�ɹ���ɾ����ʱĿ¼�ļ�
		String path = (String) params[0];
		Context ctx = (Context) params[1];
		File root = new File(path);
		String tPath = Utils.getExtenalPath(ctx, ctx.getPackageName() + "/data/cache/image/");

		Bitmap bit = getBitmapFromPath(path, 320, 400);
		InputStream is = com.jingzhong.asyntask2.utils.Utils.Bitmap2IS(bit);
		try {
			tPath = com.jingzhong.asyntask2.utils.Utils.save(is, tPath, root.getName());
		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (tPath == null) { return null; }
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("appkey", HongxianggeApplication.getInstance().getAppkey());
			param.put("addonfile", new File(tPath));
			param.put("dopost", "saveFromApp");
			param.put("mid", HongxianggeApplication.getInstance().getUser().getData().getMid());
			String res = null;

			InputStream input = new com.jingzhong.asyntask2.utils.HttpUtils().exeHttpUploadFile(null,
					HongxianggeApplication.getInstance().getDomain()
							+ HongxianggeApplication.getInstance().getHttpUrl().get("uploads_add"),
					param, "post", "GBK");
			res = com.jingzhong.asyntask2.utils.Utils.InputToStr(input, "GBK");

			if (res == null) return null;
			Log.d("UploadImageUtils", res);
			ImageUploadBean bean = (ImageUploadBean) Utils.jsonPasreToObject(ImageUploadBean.class, res);
			return bean;
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			new File(tPath).delete();
		}
		return null;
	}

	@Override
	protected void onPostExecute(ImageUploadBean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (result != null && result.isStatu() && result.getCode() == 0) {
			String path = result.getPath();
			if (callBack != null) {
				callBack.succes(path);
			}
		}
		else {
			if (callBack != null) {
				callBack.filar("�ϴ�ʧ�ܣ�������");
			}
		}
	}

	// ͨ���ļ�·����ȡ��bitmap
	public static Bitmap getBitmapFromPath(String path, int w, int h) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// ����Ϊtureֻ��ȡͼƬ��С
		opts.inJustDecodeBounds = true;
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		// ����Ϊ��
		BitmapFactory.decodeFile(path, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		float scaleWidth = 0.f, scaleHeight = 0.f;
		if (width > w || height > h) {
			// ����
			scaleWidth = ((float) width) / w;
			scaleHeight = ((float) height) / h;
		}
		opts.inJustDecodeBounds = false;
		float scale = Math.max(scaleWidth, scaleHeight);
		opts.inSampleSize = (int) scale;
		WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));

		return Bitmap.createScaledBitmap(compressImage(weak.get()), w, h, true);
	}

	// ������ѹ��ͼƬ
	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// ����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��
			baos.reset();// ����baos�����baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// ����ѹ��options%����ѹ��������ݴ�ŵ�baos��
			options -= 10;// ÿ�ζ�����10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// ��ѹ���������baos��ŵ�ByteArrayInputStream��
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// ��ByteArrayInputStream��������ͼƬ
		return bitmap;
	}
	
	// ������ѹ��ͼƬ
		public static Bitmap compressImage(Bitmap image,int size) {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// ����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
			int options = 100;
			while (baos.toByteArray().length / 1024 > size) { // ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��
				baos.reset();// ����baos�����baos
				image.compress(Bitmap.CompressFormat.JPEG, options, baos);// ����ѹ��options%����ѹ��������ݴ�ŵ�baos��
				options -= 10;// ÿ�ζ�����10
			}
			ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// ��ѹ���������baos��ŵ�ByteArrayInputStream��
			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// ��ByteArrayInputStream��������ͼƬ
			return bitmap;
		}

	private CallBack callBack;

	public void setCallBack(CallBack callBack) {
		this.callBack = callBack;
	}

	public interface CallBack {

		void succes(String url);

		void filar(String msg);
	}

}
