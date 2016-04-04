package com.jingzhong.asyntask2.listenear;

import com.jingzhong.asyntask2.utils.MultiThreadDownLoadUtils.DownListenear;
import com.jingzhong.asyntask2.utils.Result;

public class Defaultlistenear2 implements DownListenear {

	public Defaultlistenear2() {

	}

	@Override
	public void onError(Exception e, int status) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUploadListener(long progress, long tatol) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDwonloadListener(long progress, long tatol) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(Result r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(String url, Exception e, int status) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(String path, String url) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean continueDownload(String filePath) {
		// TODO Auto-generated method stub

		return false;
	}

	@Override
	public void onDwonloadListener(long progress, long tatol, long speed, String speend, String utime, String stime, int threadId) {
		// TODO Auto-generated method stub

	}

}
