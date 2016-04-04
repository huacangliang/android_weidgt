package com.jingzhong.asyntask2.utils;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class Result {

	File		resultFile;

	byte[]		resultByte;

	String		resultStr;

	InputStream	resultInput;

	public String string(String charset) {
		try {
			resultStr = new String(bytes(), charset);
		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultStr;

	}

	public InputStream inputStream() {
		return resultInput;
	}

	public byte[] bytes() {
		return resultByte;

	}

	public File file() {
		return resultFile;
	}

}
