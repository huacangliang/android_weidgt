package com.jingzhong.asyntask2.listenear;

import com.jingzhong.asyntask2.utils.MultiThreadDownLoadUtils.DownListenear;
import com.jingzhong.asyntask2.utils.Result;

import android.os.Handler;

public class Defaultlistenear implements DownListenear {

	DownListenear listenear;

	public Defaultlistenear(DownListenear listenear) {
		this.listenear = listenear;
		handler = new Handler();
	}

	Handler handler;

	@Override
	public void onError(final Exception e, final int status) {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				listenear.onError(e, status);
			}
		});
	}

	@Override
	public void onUploadListener(final long progress, final long tatol) {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				listenear.onUploadListener(progress, tatol);
			}
		});
	}

	@Override
	public void onDwonloadListener(final long progress, final long tatol) {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				listenear.onDwonloadListener(progress, tatol);
			}
		});
	}

	@Override
	public void onSuccess(final Result r) {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				listenear.onSuccess(r);
			}
		});
	}

	@Override
	public void onError(final String url, final Exception e, final int status) {
		// TODO Auto-generated method stub

		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				listenear.onError(url, e, status);
			}
		});
	}

	@Override
	public void onSuccess(final String path, final String url) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				listenear.onSuccess(path, url);
			}
		});
	}

	@Override
	public boolean continueDownload(final String filePath) {
		// TODO Auto-generated method stub

		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				listenear.continueDownload(filePath);
			}
		});
		return false;
	}

	@Override
	public void onDwonloadListener(final long progress, final long tatol, final long speed, final String speend, final String utime, final String stime, final int threadId) {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				listenear.onDwonloadListener(progress, tatol, speed, speend, utime, stime, threadId);
			}
		});
	}

}
