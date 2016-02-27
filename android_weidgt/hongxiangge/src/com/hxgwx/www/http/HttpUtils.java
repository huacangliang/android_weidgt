package com.hxgwx.www.http;

import java.io.InputStream;
import java.util.Map;

public class HttpUtils {

	/**
	 * 
	 * 
	 * @param url
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String doPost(String url, Map<String, Object> param, boolean isImage) {
		InputStream input = null;
		try {
			input = new com.jingzhong.asyntask2.utils.HttpUtils().exeHttpUploadFile(null, url, param, "post", "gbk");
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return com.jingzhong.asyntask2.utils.Utils.InputToStr(input, "GBK");
	}

	/**
	 * 
	 * 
	 * @param url
	 * @param param
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public String doPost(String url, Map<String, Object> param, String key) {
		// Log.i("doPost", url);
		if (key == null) { return null; }
		param.put("appkey", key);
		InputStream input = null;
		try {
			input = new com.jingzhong.asyntask2.utils.HttpUtils().exeHttpUploadFile(null, url, param, "post", "GBK");
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return com.jingzhong.asyntask2.utils.Utils.InputToStr(input, "GBK");
	}
}
