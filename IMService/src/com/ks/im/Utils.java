package com.ks.im;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class Utils {
	public static final SimpleDateFormat defaultDataFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 
	 * @param jsonStr
	 *            格式应该如下：aa:bb,bb:cc,dd:ee
	 * @return
	 */
	public static Map<String, Object> parseJSON2Map(String str) {
		Map<String, Object> result;
		result = JSON.parseObject(str,
				new TypeReference<Map<String, Object>>() {
				});
		return result;
	}

	public static byte[] packetEn(String str) {
		byte[] lData = null;
		try {
			lData = str.getBytes(Configuration.DEFAULTENCODING);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] rData = new byte[lData.length + 1];
		rData[0] = (byte) lData.length;
		System.arraycopy(lData, 0, rData, 1, lData.length);
		return rData;
	}

	public static byte[] packetCH(String str) {
		byte[] lData = null;

		for (int i = 0; i < str.length(); i++) {
			int r = str.charAt(i);
			byte[] ex = encoding(r);
			if (lData == null) {
				lData = ex;
			} else {
				lData = connArr(lData, ex);
			}
		}
		return lData;
	}

	public static byte[] encoding(int ch) {
		byte[] bt = new byte[2];
		String hex = Integer.toHexString(ch);
		if (hex.length() <= 2) {
			bt[0] = 0x00;
			bt[1] = (byte) Integer.parseInt(hex, 16);
		} else {
			String c = hex.substring(0, 2);
			bt[0] = (byte) Integer.parseInt(c, 16);
			c = hex.substring(2, hex.length());
			bt[1] = (byte) Integer.parseInt(hex, 16);
		}
		return bt;

	}

	public static String dencodingCH(byte[] bytes) {
		int c = 0;
		String hex = "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			int b = bytes[i];
			if (b < 0)
				b += 256;
			else if (b == 0)
				continue;

			hex += Integer.toHexString(b);

			if ((i + 1) % 2 == 0) {
				c = Integer.parseInt(hex, 16);
				char ch = (char) c;
				sb.append(ch);
				c = 0;
				hex = "";
			}
		}
		return sb.toString();

	}

	public static byte[] connArr(byte[] lData, byte[] rData) {
		byte[] data = new byte[lData.length + rData.length];
		System.arraycopy(lData, 0, data, 0, lData.length);
		System.arraycopy(rData, 0, data, lData.length, rData.length);
		return data;
	}

	public static byte[] connStrArr(byte[] lData, String rData) {
		byte[] nRData = null;
		try {
			nRData = rData.getBytes(Configuration.DEFAULTENCODING);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] data = connArr(lData, nRData);
		return data;
	}
}
