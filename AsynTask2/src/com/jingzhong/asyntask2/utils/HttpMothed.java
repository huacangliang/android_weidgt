package com.jingzhong.asyntask2.utils;

import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import android.content.Context;

public interface HttpMothed {

	public void getMethod(String url, Map<String, Object> params, SSLContext sctx, HostnameVerifier verifier);

	public void postMethod(String url, Map<String, Object> params, SSLContext sctx, HostnameVerifier verifier);

	public Result syncGetMethod(String url, Map<String, Object> params, SSLContext sctx, HostnameVerifier verifier);

	public Result syncPostMethod(String url, Map<String, Object> params, SSLContext sctx, HostnameVerifier verifier);

	public void getMethod(String url, Map<String, Object> params, Map<String, String> requestProperty, String fileSavePath, SSLContext sctx, HostnameVerifier verifier);

	public void postMethod(String url, Map<String, Object> params, Map<String, String> requestProperty, String fileSavePath, SSLContext sctx, HostnameVerifier verifier);

	public void getMethod(String url, Map<String, Object> params, Map<String, String> requestProperty);

	public void postMethod(String url, Map<String, Object> params, Map<String, String> requestProperty);

	public Result syncGetMethod(String url, Map<String, Object> params, Map<String, String> requestProperty);

	public Result syncPostMethod(String url, Map<String, Object> params, Map<String, String> requestProperty);

	public void getMethod(String url, Map<String, Object> params, String fileSavePath, SSLContext sctx, HostnameVerifier verifier);

	public void postMethod(String url, Map<String, Object> params, String fileSavePath, SSLContext sctx, HostnameVerifier verifier);

	public void getMethod(String url, Map<String, Object> params);

	public void postMethod(String url, Map<String, Object> params);

	public Result syncGetMethod(String url, Map<String, Object> params);

	public Result syncPostMethod(String url, Map<String, Object> params);

	public void getMethod(String url, Map<String, Object> params, String fileSavePath);

	public void postMethod(String url, Map<String, Object> params, String fileSavePath);

	public void getMethod(String url, Map<String, Object> params, String fileSavePath, Map<String, String> requestProperty);

	public void postMethod(String url, Map<String, Object> params, String fileSavePath, Map<String, String> requestProperty);

}
