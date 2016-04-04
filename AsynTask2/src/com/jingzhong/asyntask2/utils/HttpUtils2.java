package com.jingzhong.asyntask2.utils;

import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

public class HttpUtils2
		extends HttpUtils
		implements HttpMothed2 {

	@Override
	public void getMethod(String url, Map<String, Object> params, SSLContext sctx, HostnameVerifier verifier) {
		// TODO Auto-generated method stub
		this.getMethod(url, params, sctx, verifier);
	}

	@Override
	public void postMethod(String url, Map<String, Object> params, SSLContext sctx, HostnameVerifier verifier) {
		// TODO Auto-generated method stub
		this.postMethod(url, params, sctx, verifier);
	}

	@Override
	public Result syncGetMethod(String url, Map<String, Object> params, SSLContext sctx, HostnameVerifier verifier) {
		// TODO Auto-generated method stub
		return syncGetMethod(url, params, sctx, verifier);
	}

	@Override
	public Result syncPostMethod(String url, Map<String, Object> params, SSLContext sctx, HostnameVerifier verifier) {
		// TODO Auto-generated method stub
		return syncPostMethod(url, params, sctx, verifier);
	}

	@Override
	public void getMethod(String url, Map<String, Object> params, Map<String, String> requestProperty, String fileSavePath, SSLContext sctx, HostnameVerifier verifier) {
		// TODO Auto-generated method stub
		getMethod(url, params, requestProperty, fileSavePath, sctx, verifier);
	}

	@Override
	public void postMethod(String url, Map<String, Object> params, Map<String, String> requestProperty, String fileSavePath, SSLContext sctx, HostnameVerifier verifier) {
		// TODO Auto-generated method stub
		postMethod(url, params, requestProperty, fileSavePath, sctx, verifier);
	}

	@Override
	public void getMethod(String url, Map<String, Object> params, Map<String, String> requestProperty) {
		// TODO Auto-generated method stub
		getMethod(url, params, requestProperty);
	}

	@Override
	public void postMethod(String url, Map<String, Object> params, Map<String, String> requestProperty) {
		// TODO Auto-generated method stub
		postMethod(url, params, requestProperty);
	}

	@Override
	public Result syncGetMethod(String url, Map<String, Object> params, Map<String, String> requestProperty) {
		// TODO Auto-generated method stub
		return syncGetMethod(url, params, requestProperty);
	}

	@Override
	public Result syncPostMethod(String url, Map<String, Object> params, Map<String, String> requestProperty) {
		// TODO Auto-generated method stub
		return syncPostMethod(url, params, requestProperty);
	}

	@Override
	public void getMethod(String url, Map<String, Object> params, String fileSavePath, SSLContext sctx, HostnameVerifier verifier) {
		// TODO Auto-generated method stub
		getMethod(url, params, fileSavePath, sctx, verifier);
	}

	@Override
	public void postMethod(String url, Map<String, Object> params, String fileSavePath, SSLContext sctx, HostnameVerifier verifier) {
		// TODO Auto-generated method stub
		postMethod(url, params, fileSavePath, sctx, verifier);
	}

	@Override
	public void getMethod(String url, Map<String, Object> params) {
		// TODO Auto-generated method stub
		getMethod(url, params);
	}

	@Override
	public void postMethod(String url, Map<String, Object> params) {
		// TODO Auto-generated method stub
		postMethod(url, params);
	}

	@Override
	public Result syncGetMethod(String url, Map<String, Object> params) {
		// TODO Auto-generated method stub
		return syncGetMethod(url, params);
	}

	@Override
	public Result syncPostMethod(String url, Map<String, Object> params) {
		// TODO Auto-generated method stub
		return syncPostMethod(url, params);
	}

	@Override
	public void getMethod(String url, Map<String, Object> params, String fileSavePath) {
		// TODO Auto-generated method stub
		getMethod(url, params, fileSavePath);
	}

	@Override
	public void postMethod(String url, Map<String, Object> params, String fileSavePath) {
		// TODO Auto-generated method stub
		postMethod(url, params, fileSavePath);
	}

	@Override
	public void getMethod(String url, Map<String, Object> params, String fileSavePath, Map<String, String> requestProperty) {
		// TODO Auto-generated method stub
		getMethod(url, params, fileSavePath, requestProperty);
	}

	@Override
	public void postMethod(String url, Map<String, Object> params, String fileSavePath, Map<String, String> requestProperty) {
		// TODO Auto-generated method stub
		postMethod(url, params, fileSavePath, requestProperty);
	}

}
