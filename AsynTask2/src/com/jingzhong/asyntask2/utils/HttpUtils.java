package com.jingzhong.asyntask2.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.jingzhong.asyntask2.listenear.Defaultlistenear2;
import com.squareup.okhttp.OkHttpClient;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * 
 * @author dengzhijiang
 * 
 */
public class HttpUtils implements HttpMothed {

	private final static int BUFFER_SIZE = 1024;

	private static String TAG = "HttpUtils";

	private String charset = "UTF-8";

	private final static String BOUNDARY = "------WebKitFormBoundaryUey8ljRiiZqhZHBu";

	private boolean isFinish = false;

	private Result result;

	public final static String POST = "POST";

	public final static String GET = "GET";

	private int timeOut = 120 * 1000;

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public HttpUtils() {
		// TODO Auto-generated constructor stub
		result = new Result();
	}

	/**
	 * 文件下载长度
	 */
	public int fileLenth = 0;

	public static final String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)";

	public static byte[] readBuffer(InputStream in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len = 0;
		byte[] buffer = new byte[1024];
		try {
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			out.flush();
			buffer = out.toByteArray();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return out.toByteArray();
	}

	public static String getWifiName(Context context) {
		WifiManager mWifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifi = mWifi.getConnectionInfo();
		String name = wifi.getSSID();
		if (name != null)
			name = name.replace("\"", "");

		return name;

	}

	public static InetAddress getIpForName(String domain) {
		try {
			InetAddress[] server = InetAddress.getAllByName(domain);
			for (InetAddress inetAddress : server) {
				return inetAddress;
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public static boolean ping(String ip) {

		if (TextUtils.isEmpty(ip)) {
			Log.e(TAG, "ip not null");
			return false;
		} else {

			String line = null;
			try {
				Process pro = Runtime.getRuntime().exec("ping " + ip);
				BufferedReader buf = new BufferedReader(new InputStreamReader(
						pro.getInputStream()));
				while ((line = buf.readLine()) != null)
					System.out.println(line);
			} catch (Exception ex) {
				Log.e(TAG, ex.getMessage());
			}

		}
		return false;

	}

	public static boolean testNet(String ip) {
		InetAddress inet = null;
		try {
			inet = InetAddress.getByName(ip);
			if (inet.getHostAddress() != null) {
				Log.i("testNet", ip);
				return true;
			} else {
				Log.i(TAG, "true");
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * @deprecated 该方法不好用
	 * 
	 * @param context
	 *            ==null
	 * @param path
	 *            !=null
	 * @param params
	 *            ==null
	 * @param method
	 *            ==null
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> exeHttp(String path, Map<String, Object> params,
			String method) throws Exception {
		if (path == null)
			throw new NullPointerException("url null");

		if (params == null)
			params = new HashMap<String, Object>();

		if (method == null)
			method = GET;

		charset = charset == null ? "utf-8" : charset;
		charset = charset.toUpperCase();

		HttpURLConnection conn = null;
		OkHttpClient client = new OkHttpClient();
		URL url = new URL(path);

		InputStream is = null;
		Map<String, Object> res = new HashMap<String, Object>();
		if (method.equalsIgnoreCase("get")) {
			String p = "";

			Set<String> keys = params.keySet();
			Iterator<String> it = keys.iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = params.get(key).toString();
				p += "&" + key + "=" + value;
			}
			if (p.trim().length() > 0)
				p = URLDecoder
						.decode("?" + p.substring(1, p.length()), charset);

			url = new URL(path + p);
			conn = client.open(url);
			conn.setRequestProperty("User-Agent", USER_AGENT);
			if (conn.getResponseCode() != 200) {
				if (onNetListenear != null) {
					onNetListenear.onError(null, conn.getResponseCode());
				}
				throw new Exception("Unexpected HTTP response: "
						+ conn.getResponseCode() + " "
						+ conn.getResponseMessage());
			}
			this.fileLenth = conn.getContentLength();
			is = conn.getInputStream();
			res.put("inputstream", is);
			res.put("length", fileLenth);
			return res;
		} else if (method.equalsIgnoreCase("post")) {
			conn = client.open(url);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod(POST);
			conn.setRequestProperty("User-Agent", USER_AGENT);
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.connect();
			String p = "";
			Set<String> keys = params.keySet();
			Iterator<String> it = keys.iterator();
			OutputStream out = conn.getOutputStream();

			while (it.hasNext()) {
				String key = it.next();
				String value = params.get(key).toString();
				p += key + "=" + value + "&";
			}
			if (p.length() > 0)
				p = p.substring(0, p.length() - 1);

			out.write(p.getBytes("UTF-8"));
			out.flush();
			out.close();
			if (conn.getResponseCode() != 200) {
				if (onNetListenear != null) {
					onNetListenear.onError(null, conn.getResponseCode());
				}
				throw new Exception("Unexpected HTTP response: "
						+ conn.getResponseCode() + " "
						+ conn.getResponseMessage());
			}
			this.fileLenth = conn.getContentLength();
			is = conn.getInputStream();
			res.put("inputstream", is);
			res.put("length", fileLenth);
			return res;
		}

		return null;

	}

	/**
	 * @deprecated 该方法不完整
	 * @param context
	 * @param path
	 * @param params
	 * @param method
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public InputStream exeHttpUploadFile(String path,
			Map<String, Object> params, String method, String charset)
			throws Exception {
		if (path == null)
			throw new NullPointerException("url null");

		if (params == null)
			params = new HashMap<String, Object>();

		if (method == null)
			method = GET;
		charset = charset == null ? "utf-8" : charset;
		charset = charset.toUpperCase();
		this.charset = charset;
		HttpURLConnection conn = null;
		OkHttpClient client = new OkHttpClient();
		URL url = new URL(path);

		InputStream is = null;
		if (method.equalsIgnoreCase("get")) {
			String p = "";

			Set<String> keys = params.keySet();
			Iterator<String> it = keys.iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = params.get(key).toString();
				p += "&" + key + "=" + value;
			}
			if (p.trim().length() > 0)
				p = URLDecoder
						.decode("?" + p.substring(1, p.length()), charset);

			url = new URL(path + p);
			conn = buildConnection(url, client);
			conn.setRequestProperty("User-Agent", USER_AGENT);
			if (conn.getResponseCode() != 200) {
				if (onNetListenear != null) {
					onNetListenear.onError(null, conn.getResponseCode());
				}
				throw new Exception("Unexpected HTTP response: "
						+ conn.getResponseCode() + " "
						+ conn.getResponseMessage());
			}
			this.fileLenth = conn.getContentLength();
			is = conn.getInputStream();
			return is;
		} else if (method.equalsIgnoreCase("post")) {

			conn = buildConnection(url, client);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(true);
			conn.setConnectTimeout(timeOut);
			conn.setRequestMethod(POST);
			conn.setRequestProperty("User-Agent", USER_AGENT);
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Charsert", charset);
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;BOUNDARY=" + BOUNDARY);
			// 指定流的大小，当内容达到这个值的时候就把流输出
			conn.setChunkedStreamingMode(10240);
			conn.connect();
			OutputStream out = new DataOutputStream(conn.getOutputStream());
			Set<String> keys = params.keySet();
			Iterator<String> it = keys.iterator();
			while (it.hasNext()) {
				String key = it.next();
				Object obj = params.get(key);
				if (obj instanceof File) {
					writeFileForPost(key, (File) obj, out);
				} else {
					String value = obj.toString();
					writeStrForPost(key, value, out);
				}

			}
			out.write(("--" + BOUNDARY + "--\r\n").getBytes(charset));
			out.write(("\r\n").getBytes(charset));
			out.flush();
			out.close();

			if (conn.getResponseCode() != 200) {
				if (onNetListenear != null) {
					onNetListenear.onError(null, conn.getResponseCode());
				}
				throw new Exception("Unexpected HTTP response: "
						+ conn.getResponseCode() + " "
						+ conn.getResponseMessage());
			}

			this.fileLenth = conn.getContentLength();
			is = conn.getInputStream();
			return is;

		}

		return null;

	}

	public void getMethod(String url, Map<String, Object> params,
			SSLContext sctx, HostnameVerifier verifier) {
		exe(url, params, GET, null, sctx, verifier, null, false);
	}

	public void postMethod(String url, Map<String, Object> params,
			SSLContext sctx, HostnameVerifier verifier) {
		exe(url, params, POST, null, sctx, verifier, null, false);
	}

	public Result syncGetMethod(String url, Map<String, Object> params,
			SSLContext sctx, HostnameVerifier verifier) {
		isFinish = false;
		exe(url, params, GET, null, sctx, verifier, null, false);
		this.setOnNetListenear(new Defaultlistenear2() {

			@Override
			public void onUploadListener(long progress, long tatol) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Result r) {
				// TODO Auto-generated method stub
				isFinish = true;
			}

			@Override
			public void onDwonloadListener(long progress, long tatol) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Exception e, int status) {
				// TODO Auto-generated method stub
				isFinish = true;
			}

		});
		while (!isFinish) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return result;

	}

	public Result syncPostMethod(String url, Map<String, Object> params,
			SSLContext sctx, HostnameVerifier verifier) {
		isFinish = false;
		exe(url, params, POST, null, sctx, verifier, null, false);
		this.setOnNetListenear(new Defaultlistenear2() {

			@Override
			public void onUploadListener(long progress, long tatol) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Result r) {
				// TODO Auto-generated method stub
				isFinish = true;
			}

			@Override
			public void onDwonloadListener(long progress, long tatol) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Exception e, int status) {
				// TODO Auto-generated method stub
				isFinish = true;
			}

		});
		while (!isFinish) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return result;

	}

	public void getMethod(String url, Map<String, Object> params,
			Map<String, String> requestProperty, String fileSavePath,
			SSLContext sctx, HostnameVerifier verifier) {
		exe(url, params, GET, requestProperty, sctx, verifier, fileSavePath,
				false);
	}

	public void postMethod(String url, Map<String, Object> params,
			Map<String, String> requestProperty, String fileSavePath,
			SSLContext sctx, HostnameVerifier verifier) {
		exe(url, params, POST, requestProperty, sctx, verifier, fileSavePath,
				false);
	}

	public void getMethod(String url, Map<String, Object> params,
			Map<String, String> requestProperty) {
		exe(url, params, GET, requestProperty, null, null, null, false);
	}

	public void postMethod(String url, Map<String, Object> params,
			Map<String, String> requestProperty) {
		exe(url, params, POST, requestProperty, null, null, null, false);
	}

	public Result syncGetMethod(String url, Map<String, Object> params,
			Map<String, String> requestProperty) {
		isFinish = false;
		exe(url, params, GET, requestProperty, null, null, null, false);
		this.setOnNetListenear(new Defaultlistenear2() {

			@Override
			public void onUploadListener(long progress, long tatol) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Result r) {
				// TODO Auto-generated method stub
				isFinish = true;
			}

			@Override
			public void onDwonloadListener(long progress, long tatol) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Exception e, int status) {
				// TODO Auto-generated method stub
				isFinish = true;
			}

		});
		while (!isFinish) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return result;

	}

	public Result syncPostMethod(String url, Map<String, Object> params,
			Map<String, String> requestProperty) {
		isFinish = false;
		exe(url, params, POST, requestProperty, null, null, null, false);
		this.setOnNetListenear(new Defaultlistenear2() {

			@Override
			public void onUploadListener(long progress, long tatol) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Result r) {
				// TODO Auto-generated method stub
				isFinish = true;
			}

			@Override
			public void onDwonloadListener(long progress, long tatol) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Exception e, int status) {
				// TODO Auto-generated method stub
				isFinish = true;
			}

		});
		while (!isFinish) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return result;

	}

	public void getMethod(String url, Map<String, Object> params,
			String fileSavePath, SSLContext sctx, HostnameVerifier verifier) {
		exe(url, params, GET, null, sctx, verifier, fileSavePath, false);
	}

	public void postMethod(String url, Map<String, Object> params,
			String fileSavePath, SSLContext sctx, HostnameVerifier verifier) {
		exe(url, params, POST, null, sctx, verifier, fileSavePath, false);
	}

	public void getMethod(String url, Map<String, Object> params) {
		exe(url, params, GET, null, null, null, null, false);
	}

	public void postMethod(String url, Map<String, Object> params) {
		exe(url, params, POST, null, null, null, null, false);
	}

	public Result syncGetMethod(String url, Map<String, Object> params) {
		isFinish = false;
		exe(url, params, GET, null, null, null, null, false);
		this.setOnNetListenear(new Defaultlistenear2() {

			@Override
			public void onUploadListener(long progress, long tatol) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Result r) {
				// TODO Auto-generated method stub
				isFinish = true;
			}

			@Override
			public void onDwonloadListener(long progress, long tatol) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Exception e, int status) {
				// TODO Auto-generated method stub
				isFinish = true;
			}

		});
		while (!isFinish) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return result;

	}

	public Result syncPostMethod(String url, Map<String, Object> params) {
		isFinish = false;
		exe(url, params, POST, null, null, null, null, false);
		this.setOnNetListenear(new Defaultlistenear2() {

			@Override
			public void onUploadListener(long progress, long tatol) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Result r) {
				// TODO Auto-generated method stub
				isFinish = true;
			}

			@Override
			public void onDwonloadListener(long progress, long tatol) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Exception e, int status) {
				// TODO Auto-generated method stub
				isFinish = true;
			}

		});
		while (!isFinish) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return result;

	}

	public void getMethod(String url, Map<String, Object> params,
			String fileSavePath) {
		exe(url, params, GET, null, null, null, fileSavePath, false);
	}

	public void postMethod(String url, Map<String, Object> params,
			String fileSavePath) {
		exe(url, params, POST, null, null, null, fileSavePath, false);
	}

	public void getMethod(String url, Map<String, Object> params,
			String fileSavePath, Map<String, String> requestProperty) {
		exe(url, params, GET, requestProperty, null, null, fileSavePath, false);
	}

	public void postMethod(String url, Map<String, Object> params,
			String fileSavePath, Map<String, String> requestProperty) {
		exe(url, params, POST, requestProperty, null, null, fileSavePath, false);
	}

	private void exe(final String path, Map<String, Object> params,
			String method, final Map<String, String> requestProperty,
			final SSLContext sctx, final HostnameVerifier verifier,
			final String fileSavePath, final boolean isSync) {
		if (path == null)
			throw new NullPointerException("url null");

		if (params == null)
			params = new HashMap<String, Object>();

		if (method == null)
			method = GET;
		final String methodT = method;
		final Map<String, Object> paramsT = params;
		ThreadService.getInstance().executeThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				http(path, requestProperty, paramsT, methodT, sctx, verifier,
						fileSavePath, isSync);
			}
		});

	}

	private void http(String path, Map<String, String> requestProperty,
			Map<String, Object> params, String method, SSLContext sctx,
			HostnameVerifier verifier, String fileSavePath, boolean isSync) {
		charset = charset == null ? "utf-8" : charset;
		charset = charset.toUpperCase();
		HttpURLConnection conn = null;
		OkHttpClient client = new OkHttpClient();

		URL url = null;
		try {
			url = new URL(path);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		InputStream is = null;
		if (method.equalsIgnoreCase("get")) {
			// get
			String p = "";

			Set<String> keys = params.keySet();
			Iterator<String> it = keys.iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = params.get(key).toString();
				p += "&" + key + "=" + value;
			}
			try {
				if (p.trim().length() > 0)
					p = URLDecoder.decode("?" + p.substring(1, p.length()),
							charset);
				url = new URL(path + p);
				if (sctx != null) {
					conn = buildConnection(url, client, sctx, verifier);
				} else {
					conn = buildConnection(url, client);
				}

				conn.setRequestProperty("User-Agent", USER_AGENT);
				if (requestProperty != null) {
					for (Entry<String, String> e : requestProperty.entrySet()) {
						conn.setRequestProperty(e.getKey(), e.getValue());
					}
				}
				if (conn.getResponseCode() != 200) {
					if (onNetListenear != null) {
						onNetListenear.onError(null, conn.getResponseCode());
					}
					throw new Exception("Unexpected HTTP response: "
							+ conn.getResponseCode() + " "
							+ conn.getResponseMessage());
				}
				this.fileLenth = conn.getContentLength();
				is = conn.getInputStream();
				if (isSync) {
					this.result.resultInput = is;
				} else if (fileSavePath == null) {
					readResponse(is, null);
				} else {
					readResponse(is, new File(fileSavePath));
				}
			} catch (Exception e) {
				if (onNetListenear != null) {
					onNetListenear.onError(e, 0);
				}
			} finally {
				if (conn != null && !isSync) {
					conn.disconnect();
					conn = null;
				}
			}
		} else {
			// post
			if (sctx != null) {
				conn = buildConnection(url, client, sctx, verifier);
			} else {
				conn = buildConnection(url, client);
			}
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(true);
			conn.setConnectTimeout(timeOut);
			conn.setReadTimeout(timeOut);
			try {
				conn.setRequestMethod("POST");
				conn.setRequestProperty("connection", "Keep-Alive");
				conn.setRequestProperty("Charsert", charset);
				conn.setRequestProperty("User-Agent", USER_AGENT);
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;BOUNDARY=" + BOUNDARY);
				if (requestProperty != null) {
					for (Entry<String, String> e : requestProperty.entrySet()) {
						conn.setRequestProperty(e.getKey(), e.getValue());
					}
				}
				// 指定流的大小，当内容达到这个值的时候就把流输出
				conn.setChunkedStreamingMode(10240);
				conn.connect();
				OutputStream out = new DataOutputStream(conn.getOutputStream());

				writeParamsForPost(params, out);

				if (conn.getResponseCode() != 200) {
					if (onNetListenear != null) {
						onNetListenear.onError(null, conn.getResponseCode());
					}
					throw new Exception("Unexpected HTTP response: "
							+ conn.getResponseCode() + " "
							+ conn.getResponseMessage());
				}

				this.fileLenth = conn.getContentLength();
				is = conn.getInputStream();
				if (isSync) {
					this.result.resultInput = is;
				} else if (fileSavePath == null) {
					readResponse(is, null);
				} else {
					readResponse(is, new File(fileSavePath));
				}
				if (onNetListenear != null) {
					onNetListenear.onSuccess(result);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				if (onNetListenear != null) {
					onNetListenear.onError(e, 0);
				}
			} finally {
				if (conn != null && !isSync) {
					conn.disconnect();
					conn = null;
				}
			}

		}
	}

	public InputStream writeParams(String method, Map<String, Object> params,
			String urlStr, Long startPos, OkHttpClient client, SSLContext sctx,
			HostnameVerifier verifier) {
		URL url = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		if (method == null)
			method = GET;

		if (method.equalsIgnoreCase("get")) {
			// get

			if (params == null) {
				try {
					url = new URL(urlStr);
					if (sctx != null) {
						conn = buildConnection(url, client, sctx, verifier);
					} else {
						conn = buildConnection(url, client);
					}
					String property = "bytes=" + startPos + "-";
					conn.setRequestProperty("RANGE", property);
					conn.setRequestProperty("User-Agent", USER_AGENT);
					if (conn.getResponseCode() >= 300) {
						if (onNetListenear != null) {
							onNetListenear
									.onError(null, conn.getResponseCode());
						}
						throw new Exception("Unexpected HTTP response: "
								+ conn.getResponseCode() + " "
								+ conn.getResponseMessage());
					}
					this.fileLenth = conn.getContentLength();
					is = conn.getInputStream();
					return is;
				} catch (Exception e) {
					if (onNetListenear != null) {
						onNetListenear.onError(e, 0);
					}
				}

			} else {
				String p = "";

				Set<String> keys = params.keySet();
				Iterator<String> it = keys.iterator();
				while (it.hasNext()) {
					String key = it.next();
					String value = params.get(key).toString();
					p += "&" + key + "=" + value;
				}
				if (p.trim().length() > 0)
					try {
						p = URLDecoder.decode("?" + p.substring(1, p.length()),
								charset);
						url = new URL(urlStr + p);
						if (sctx != null) {
							conn = buildConnection(url, client, sctx, verifier);
						} else {
							conn = buildConnection(url, client);
						}
						String property = "bytes=" + startPos + "-";
						conn.setRequestProperty("RANGE", property);
						conn.setRequestProperty("User-Agent", USER_AGENT);
						if (conn.getResponseCode() != 200) {
							if (onNetListenear != null) {
								onNetListenear.onError(null,
										conn.getResponseCode());
							}
							throw new Exception("Unexpected HTTP response: "
									+ conn.getResponseCode() + " "
									+ conn.getResponseMessage());
						}
						this.fileLenth = conn.getContentLength();
						is = conn.getInputStream();
						return is;
					} catch (Exception e) {
						if (onNetListenear != null) {
							onNetListenear.onError(e, 0);
						}
					}

			}

		} else {
			// post
			if (sctx != null) {
				conn = buildConnection(url, client, sctx, verifier);
			} else {
				conn = buildConnection(url, client);
			}
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(true);
			conn.setConnectTimeout(timeOut);
			conn.setReadTimeout(timeOut);
			try {
				conn.setRequestMethod("POST");
				conn.setRequestProperty("connection", "Keep-Alive");
				conn.setRequestProperty("Charsert", charset);
				conn.setRequestProperty("User-Agent", USER_AGENT);
				String property = "bytes=" + startPos + "-";
				conn.setRequestProperty("RANGE", property);
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;BOUNDARY=" + BOUNDARY);
				// 指定流的大小，当内容达到这个值的时候就把流输出
				conn.setChunkedStreamingMode(10240);
				conn.connect();
				OutputStream out = new DataOutputStream(conn.getOutputStream());

				writeParamsForPost(params, out);

				if (conn.getResponseCode() != 200) {
					if (onNetListenear != null) {
						onNetListenear.onError(null, conn.getResponseCode());
					}
					throw new Exception("Unexpected HTTP response: "
							+ conn.getResponseCode() + " "
							+ conn.getResponseMessage());
				}

				this.fileLenth = conn.getContentLength();
				return is = conn.getInputStream();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				if (onNetListenear != null) {
					onNetListenear.onError(e, 0);
				}
			}

		}
		return is;
	}

	private void writeParamsForPost(Map<String, Object> params, OutputStream out)
			throws Exception {

		Set<String> keys = params.keySet();
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String key = it.next();
			Object obj = params.get(key);
			if (obj instanceof File) {
				writeFileForPost(key, (File) obj, out);
			} else {
				String value = obj.toString();
				writeStrForPost(key, value, out);
			}

		}
		out.write(("--" + BOUNDARY + "--\r\n").getBytes(charset));
		out.write(("\r\n").getBytes(charset));
		out.flush();
		out.close();
	}

	public void readResponse(InputStream input, File f) {
		if (f != null) {
			readFile(input, f);
		} else {
			result.resultByte = readBuffer(input);
			if (onNetListenear != null) {
				onNetListenear.onSuccess(result);
			}
		}
	}

	private void writeStrForPost(String key, String value, OutputStream out)
			throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data; name=\"" + key + "\"");
		sb.append("\r\n\r\n");
		out.write(sb.toString().getBytes(charset));
		out.write(encode(value + "\r\n", charset));
	}

	private void writeFileForPost(String key, File f, OutputStream out)
			throws Exception {
		StringBuilder sb = new StringBuilder();
		// 添加form属性
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data; name=\"" + key + "\";");
		sb.append("filename=\"" + encodeStr(f.getName(), charset) + "\"\r\n");
		sb.append("Content-Type: " + getContentType(f.getName()) + "\r\n");
		sb.append("\r\n");
		out.write(sb.toString().getBytes(charset));
		writeFile(out, f);
		out.write("\r\n".getBytes(charset));
	}

	public void writeFile(OutputStream out, File f) throws IOException {
		FileInputStream in = new FileInputStream(f);
		byte[] b = new byte[1024];
		int n;
		long tatol = f.length();
		long progress = 0;
		int count = 0;
		while ((n = in.read(b)) != -1) {
			out.write(b, 0, n);
			progress += n;
			if (onNetListenear != null) {
				onNetListenear.onUploadListener(progress, tatol);
			}
			if (count >= 10) {
				count = 0;
				out.flush();
			} else {
				count++;
			}
		}
		in.close();
	}

	public void readFile(InputStream is, File f) {
		try {
			OutputStream os = new FileOutputStream(f);
			long progress = 0;
			long tatol = fileLenth;
			int count = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((count = is.read(buffer)) != -1) {
				os.write(buffer, 0, count);
				progress += count;
				if (onNetListenear != null) {
					onNetListenear.onDwonloadListener(progress, tatol);
				}
			}
			os.flush();
			os.close();
			result.resultFile = new File(f.getAbsolutePath());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
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

	// 把文件转换成字节数组
	public byte[] getBytes(File f) throws Exception {
		FileInputStream in = new FileInputStream(f);
		return Utils.InputToBytes(in);
	}

	// 获取文件的上传类型，图片格式为image/png,image/jpg等。非图片为application/octet-stream
	public String getContentType(String fileName) {
		fileName = Utils.getFileExitName(fileName);
		boolean isImage = false;
		isImage = Utils.isImage(fileName);
		if (isImage) {
			return "image/" + fileName;
		}
		return "application/octet-stream"; // 此行不再细分是否为图片，全部作为application/octet-stream
											// 类型
	}

	public byte[] encode(String res, String charset) {
		return res.getBytes(Charset.forName(charset));

	}

	public String encodeStr(String res, String charset) throws Exception {
		return URLEncoder.encode(res, charset);

	}

	public HttpURLConnection buildConnection(URL url, OkHttpClient client) {
		try {
			if (url.getProtocol().equals("https")) {
				SSLContext sctx = null;
				sctx = SSLContext.getInstance("TLS");
				sctx.init(new KeyManager[0],
						new TrustManager[] { new X509TrustManager() {

							@Override
							public X509Certificate[] getAcceptedIssuers() {
								// TODO Auto-generated method stub
								return null;
							}

							@Override
							public void checkServerTrusted(
									X509Certificate[] chain, String authType)
									throws CertificateException {
								// TODO Auto-generated method stub

							}

							@Override
							public void checkClientTrusted(
									X509Certificate[] chain, String authType)
									throws CertificateException {
								// TODO Auto-generated method stub

							}
						} }, new SecureRandom());
				HttpsURLConnection conn = (HttpsURLConnection) client.open(url);
				conn.setSSLSocketFactory(sctx.getSocketFactory());
				conn.setHostnameVerifier(new HostnameVerifier() {

					@Override
					public boolean verify(String hostname, SSLSession session) {
						// TODO Auto-generated method stub
						// 默认认证通过
						return true;
					}
				});
				return conn;
			} else {
				return client.open(url);
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public HttpURLConnection buildConnection(URL url, OkHttpClient client,
			SSLContext sctx, HostnameVerifier verifier) {
		if (url.getProtocol().equals("https")) {
			HttpsURLConnection conn = (HttpsURLConnection) client.open(url);
			conn.setSSLSocketFactory(sctx.getSocketFactory());
			conn.setHostnameVerifier(verifier);
			return conn;
		} else {
			return client.open(url);
		}
	}

	private OnNetListenear onNetListenear;

	/**
	 * @param onNetListenear
	 *            the onNetListenear to set
	 */
	public void setOnNetListenear(OnNetListenear onNetListenear) {
		this.onNetListenear = onNetListenear;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public interface OnNetListenear {

		/**
		 * 状态码
		 * 
		 * @param status
		 */
		void onError(Exception e, int status);

		void onUploadListener(long progress, long tatol);

		void onDwonloadListener(long progress, long tatol);

		void onDwonloadListener(long progress, long tatol, long speed,
				String speend, String utime, String stime, int threadId);

		void onSuccess(Result r);
	}

}
