package com.hxgwx.www.dwon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import android.content.Context;
import android.os.Environment;
import android.text.format.Formatter;

import com.jingzhong.asyntask2.utils.HttpUtils;

public class DwonLoad extends Thread {
	/**
	 * 开始下载
	 */
	public static final int STARTDWON = 1;
	/**
	 * 下载中
	 */
	public static final int DWONING = 2;
	/**
	 * 下载成功
	 */
	public static final int DWON_OK = 3;
	/**
	 * 下载失败
	 */
	public static final int DWON_ERROR = 4;

	private String path = null;
	private String url = null;
	private int length = 0;
	private Context context;

	private boolean isCancel = false;

	public DwonLoad(String path, String url, Context context) {
		this.path = path;
		this.url = url;
		this.context = context;
	}

	public void dwon() {
		if (path != null) {
			InputStream is = null;
			byte[] data = new byte[1024 * 10];
			int progress = 0;
			try {
				if (setOnDwonListener != null) {
					setOnDwonListener.setOnDwoningStatus(STARTDWON);
				}
				boolean sdCardExist = Environment.getExternalStorageState()
						.equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
				if (sdCardExist) {
					@SuppressWarnings("rawtypes")
					Map map = new HttpUtils().exeHttp(null, url, null, null);
					if (map.get("inputstream") != null)
						is = (InputStream) map.get("inputstream");

					if (is != null) {
						length = Integer.parseInt(map.get("length").toString());
						if (setOnDwonListener != null) {
							setOnDwonListener.setOnDwoningStatus(DWONING);
						}

						FileOutputStream fos = new FileOutputStream(path);
						int len = 0;
						int newlen = 0;
						int dataLen = 1024 * 10;
						int count = 1;

						while ((len = is.read(data, 0, dataLen)) != -1
								&& !isCancel) {
							newlen += len;
							fos.write(data, 0, len);
							count++;
							if (count >= 10) {
								count = 1;
								if (setOnDwonListener != null) {
									setOnDwonListener
											.setOnDwoningProgress(progress = progress >= 100
													? 99
													: progress);
									String dwon = convertKBByByte(newlen,
											context);
									String total = convertKBByByte(length,
											context);
									setOnDwonListener.setOnDwoningProgress(
											dwon, total);
								}

								if (length > 0) {
									progress = (int) ((((double) newlen / length)) * 100);
									Thread.sleep(200);
								} else {
									progress = 0;
								}

							}

						}
						fos.flush();
						fos.close();
						if (isCancel) {
							new File(path).delete();
							return;
						}
						if (setOnDwonListener != null) {
							setOnDwonListener.setOnDwoningProgress(100);
						}
					}
				} else {
					if (setOnDwonListener != null) {
						setOnDwonListener.setOnDwoningStatus(DWON_ERROR);
					}

				}

			} catch (IOException e) {
				if (setOnDwonListener != null) {
					setOnDwonListener.setOnDwoningStatus(DWON_ERROR);
				}
				e.printStackTrace();
			} catch (InterruptedException e) {
				if (setOnDwonListener != null) {
					setOnDwonListener.setOnDwoningStatus(DWON_ERROR);
				}
				e.printStackTrace();
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (!isCancel) {
					if (is == null) {
						if (setOnDwonListener != null) {
							setOnDwonListener.setOnDwoningStatus(DWON_ERROR);
						}

					} else {
						if (setOnDwonListener != null) {
							setOnDwonListener.setOnDwoningStatus(DWON_OK);
						}

					}
				}

				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}
	}

	@Override
	public void run() {
		dwon();
		super.run();
	}

	private OnDwonListener setOnDwonListener;

	public interface OnDwonListener {
		void setOnDwoningProgress(int progress);

		void setOnDwoningProgress(float dwon, float total);

		void setOnDwoningProgress(String dwon, String total);

		void setOnDwoningStatus(int status);
	}

	public void setSetOnDwonListener(OnDwonListener setOnDwonListener) {
		this.setOnDwonListener = setOnDwonListener;
	}

	public static String convertKBByByte(long l, Context context) {
		return Formatter.formatFileSize(context, l);
	}

	/**
	 * @return the isCancel
	 */
	public boolean isCancel() {
		return isCancel;
	}

	/**
	 * @param isCancel
	 *            the isCancel to set
	 */
	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}

}
