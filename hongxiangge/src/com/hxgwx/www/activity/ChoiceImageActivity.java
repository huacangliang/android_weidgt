package com.hxgwx.www.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hxgwx.www.R;
import com.umeng.message.PushAgent;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

public class ChoiceImageActivity
		extends Activity {

	File			camerFile;

	private String	path;

	// 使用系统当前日期加以调整作为照片的名称
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice_image_view);
		camerFile = new File(com.hxgwx.www.utils.Utils.getExtenalPath(this, "/hongxiang/images/"));
		camerFile = new File(camerFile.getAbsolutePath(), getPhotoFileName());
		PushAgent.getInstance(this).onAppStart();
	}

	public void onCamer(View v) {
		// TODO Auto-generated method stub
		Uri imageFileUri = Uri.fromFile(camerFile);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
		startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
	}

	public void onTicket(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		intent.putExtra("return-data", false);
		startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
	}

	public void onCancel(View v) {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.an_pow_out, R.anim.an_pow_in);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_FIRST_USER) {
			doPhoto(requestCode, data);
		}
	}

	/***
	 * 使用照相机拍照获取图片
	 */
	public static final int	SELECT_PIC_BY_TACK_PHOTO	= 1;

	/***
	 * 使用相册中的图片
	 */
	public static final int	SELECT_PIC_BY_PICK_PHOTO	= 0;

	/**
	 * 根据返回码获取图片
	 * 
	 * @param requestCode
	 * @param data
	 */
	private void doPhoto(int requestCode, Intent data) {
		Uri uri = null;
		switch (requestCode) {
		case SELECT_PIC_BY_PICK_PHOTO:
			ContentResolver cr = this.getContentResolver();
			uri = data.getData();
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = cr.query(uri, proj, null, null, null);
			if (cursor == null) {
				path = uri.getPath();
				data.putExtra(IMAGEPATH, path);
				setResult(Activity.RESULT_OK, data);
				finish();
			}
			else {
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

				cursor.moveToFirst();
				path = cursor.getString(column_index);// 图片在的路径
				data.putExtra(IMAGEPATH, path);
				setResult(Activity.RESULT_OK, data);
				finish();
			}

			break;

		case SELECT_PIC_BY_TACK_PHOTO:
			path = camerFile.getAbsolutePath();
			data = new Intent();
			data.putExtra(IMAGEPATH, path);
			setResult(Activity.RESULT_OK, data);
			finish();
			break;
		}

	}

	public static final String IMAGEPATH = "ImagePath";
}
