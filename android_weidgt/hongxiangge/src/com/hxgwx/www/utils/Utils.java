package com.hxgwx.www.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;

import com.google.android.maps.MapView.LayoutParams;
import com.google.gson.GsonBuilder;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hxgwx.www.HongxianggeApplication;
import com.hxgwx.www.activity.LoginActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Utils {

	public static void showLongToast(Context context, String msg) {
		if (context == null || msg == null) return;
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	public static void showShortToast(Context context, String msg) {
		if (context == null || msg == null) return;
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static String readXMLToString_Config(Context context, String name) {

		try {
			InputStream is = context.getResources().getAssets().open(name);
			byte[] buffer = new byte[1024];
			ByteArrayBuffer bab = new ByteArrayBuffer(1024);

			while (is.read(buffer) != -1) {
				bab.append(buffer, 0, buffer.length);
			}
			is.close();

			String res = new String(bab.toByteArray());
			// buffer=Base64.decode(res.getBytes(),Base64.DEFAULT);
			// res=new String(buffer);

			return res;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void gotoActivity(Context c, Class<?> cs, Intent i) {
		if (i == null) {
			Intent intent = new Intent(c, cs);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			c.startActivity(intent);
		}
		else {
			i.setClass(c, cs);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			c.startActivity(i);
		}

	}

	public interface CheckIsEmptySTR {

		boolean isE(String str);
	}

	public static boolean checkEmail(String str) {
		Pattern pattern = Pattern.compile(
				"^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	public static Object jsonPasreToObject(Class<?> cls, String res) {
		try {
			return new GsonBuilder().create().fromJson(res, cls);
		}
		catch (Exception e) {

		}
		return null;

	}

	/**
	 * 将对象转换成map
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> getValueMap(Object obj) {

		Map<String, Object> map = new HashMap<String, Object>();
		// System.out.println(obj.getClass());
		// 获取f对象对应类中的所有属性域
		Field[] fields = obj.getClass().getDeclaredFields();
		for (int i = 0, len = fields.length; i < len; i++) {
			String varName = fields[i].getName();
			try {
				// 获取原来的访问控制权限
				boolean accessFlag = fields[i].isAccessible();
				// 修改访问控制权限
				fields[i].setAccessible(true);
				// 获取在对象f中属性fields[i]对应的对象中的变量
				Object o = fields[i].get(obj);
				if (o != null) map.put(varName, o);
				// System.out.println("传入的对象中包含一个如下的变量：" + varName + " = " + o);
				// 恢复访问控制权限
				fields[i].setAccessible(accessFlag);
			}
			catch (IllegalArgumentException ex) {
				ex.printStackTrace();
			}
			catch (IllegalAccessException ex) {
				ex.printStackTrace();
			}
		}
		return map;

	}

	/**
	 * 将对象转换成map
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> getValueMapObj(Object obj) {

		Map<String, Object> map = new HashMap<String, Object>();
		// System.out.println(obj.getClass());
		// 获取f对象对应类中的所有属性域
		Field[] fields = obj.getClass().getDeclaredFields();
		for (int i = 0, len = fields.length; i < len; i++) {
			String varName = fields[i].getName();
			try {
				// 获取原来的访问控制权限
				boolean accessFlag = fields[i].isAccessible();
				// 修改访问控制权限
				fields[i].setAccessible(true);
				// 获取在对象f中属性fields[i]对应的对象中的变量
				Object o = fields[i].get(obj);
				if (o != null) map.put(varName, o);
				// System.out.println("传入的对象中包含一个如下的变量：" + varName + " = " + o);
				// 恢复访问控制权限
				fields[i].setAccessible(accessFlag);
			}
			catch (IllegalArgumentException ex) {
				ex.printStackTrace();
			}
			catch (IllegalAccessException ex) {
				ex.printStackTrace();
			}
		}
		return map;

	}

	public static String ConvertEntity(HttpEntity entity) throws Exception {
		return EntityUtils.toString(entity, "gbk");
	}

	public static boolean checkLogin(HongxianggeApplication app) {
		if (app.isLogin) return true;
		else {
			Intent i = new Intent();
			i.putExtra("from", 1);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			gotoActivity(app, LoginActivity.class, i);
		}
		return false;
	}

	public static String getArcTypeByRank(int rank) {
		if (rank == -1) {
			return "未审核";
		}
		else if (rank > -1) {
			return "已审核";
		}
		else {
			return "已删除或不存在";
		}

	}

	/**
	 * 排版文章
	 * 
	 * @param f
	 * @return
	 */
	public static String tyepSetting(String f) {

		char[] cs = new char[f.length()];
		for (int i = 0; i < f.length(); i++) {
			char c = f.charAt(i);
			cs[i] = c;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cs.length; i++) {
			String s = String.valueOf(cs[i]);
			if (!s.equals("\n")) s += "\t";

			sb.append(s);
		}
		return sb.toString();
	}

	/**
	 * 默认排版文章
	 * 
	 * @param f
	 * @return
	 */
	public static String defaultTyepSetting(String f) {

		char[] cs = new char[f.length()];
		for (int i = 0; i < f.length(); i++) {
			char c = f.charAt(i);
			cs[i] = c;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cs.length; i++) {
			String s = String.valueOf(cs[i]);
			if (s.equals("\n")) {
				s = "</br>";
			}
			else if (s.equals("\t") || s.equals(" ")) {
				s = "&nbsp;";
			}

			sb.append(s);
		}
		return sb.toString();
	}

	public static String decodeRes(Context context, String name) {

		try {
			InputStream is = context.getResources().getAssets().open(name);
			ByteArrayBuffer bab = new ByteArrayBuffer(0);
			byte[] b = new byte[is.available()];
			int len = 0;
			while ((len = is.read(b)) != -1) {
				bab.append(b, 0, len);
			}
			b = bab.buffer();

			for (int i = 0; i < b.length; i++) {
				byte tb = (byte) (b[i] - 1);
				b[i] = tb;
			}

			return new String(b);
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static String convertNumToDisply(int count) {
		if (count < 10000) {
			return String.valueOf(count);
		}
		else if (count >= 10000 && count < 10 * 1000) {
			int m = count % 1000;
			if (m == 0) { return count / 1000 + "千"; }
			int q = count - m;
			q = q / 1000;
			return q + "千" + m;
		}
		else if (count >= (10 * 1000) && count < (100 * 1000)) {
			int m = count % 10000;
			if (m == 0) { return count / (1000 * 10) + "万"; }
			int q = count - m;
			q = q / 10000;
			String qs = convertNumToDisply(m);
			return q + "万" + qs;
		}
		else if (count >= (100 * 1000) && count < (1000 * 1000)) {
			int m = count % (100 * 1000);
			if (m == 0) { return count / (1000 * 10) + "万"; }
			int q = count - m;
			q = q / (1000 * 100);
			String qs = convertNumToDisply(m);
			return q + "万" + qs;
		}
		else if (count >= (1000 * 1000) && count < (10000 * 1000)) {
			int m = count % (1000 * 1000);
			if (m == 0) { return count / (1000 * 10) + "万"; }
			int q = count - m;
			q = q / 1000 * 1000;
			String qs = convertNumToDisply(m);
			return q + "万" + qs;
		}
		else if (count >= (10000 * 1000) && count < (100000 * 1000)) {
			int m = count % (1000 * 1000);
			if (m == 0) { return count / (1000 * 10) + "万"; }
			int q = count - m;
			q = q / (1000 * 1000);
			String qs = convertNumToDisply(m);
			return q + "万" + qs;
		}

		return count / 10000.0 + "万";

	}

	public static void showGuide(Activity ac, int res) {
		SharedPreferences shared = ac.getSharedPreferences(ac.getClass().getName(), Context.MODE_PRIVATE);
		if (shared.getBoolean("show" + res, false)) { return; }
		final FrameLayout root = (FrameLayout) ac.getWindow().getDecorView();
		final ImageView iv = new ImageView(ac);
		iv.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		iv.setImageResource(res);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				root.removeView(iv);
			}
		});
		root.addView(iv);
		shared.edit().putBoolean("show" + res, true).apply();
	}

	public static void showGuide(Activity ac, int res, final Opreation opreation) {
		SharedPreferences shared = ac.getSharedPreferences(ac.getClass().getName(), Context.MODE_PRIVATE);
		if (shared.getBoolean("show" + res, false)) { return; }
		final FrameLayout root = (FrameLayout) ac.getWindow().getDecorView();
		final ImageView iv = new ImageView(ac);
		iv.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		iv.setImageResource(res);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				root.removeView(iv);
				if (opreation != null) {
					opreation.dissmis();
				}
			}
		});
		root.addView(iv);
		shared.edit().putBoolean("show" + res, true).apply();
	}

	public interface Opreation {

		void dissmis();
	}

	public static String getSign(Context context) {
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
		Iterator<PackageInfo> iter = apps.iterator();
		while (iter.hasNext()) {
			PackageInfo packageinfo = iter.next();
			String packageName = packageinfo.packageName;
			if (packageName.equals(context.getPackageName())) { return packageinfo.signatures[0].toCharsString(); }
		}
		return null;
	}

	public static void getSingInfo(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
					PackageManager.GET_SIGNATURES);
			Signature[] signs = packageInfo.signatures;
			Signature sign = signs[0];
			parseSignature(sign.toByteArray());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void parseSignature(byte[] signature) {
		try {
			CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) certFactory
					.generateCertificate(new ByteArrayInputStream(signature));
			String pubKey = cert.getPublicKey().toString();
			String signNumber = cert.getSerialNumber().toString();
			System.out.println("signName:" + cert.getSigAlgName());
			System.out.println("pubKey:" + pubKey);
			System.out.println("signNumber:" + signNumber);
			System.out.println("subjectDN:" + cert.getSubjectDN().toString());
		}
		catch (CertificateException e) {
			e.printStackTrace();
		}
	}

	public static String setImageUrl(String text) {

		text = text.replaceAll("src=&quot;", "src=&quot;" + HongxianggeApplication.getInstance().getDomain());
		text = text.replaceAll("&quot;", "\"");

		return text;

	}

	public static String parseNetNbsp(String text) {
		text = text.replaceAll("&nbsp;", "\t");
		return text;

	}

	public static char[] subChars(char[] src, int start, int end) {
		char[] res = new char[end - start];
		for (int i = 0; i < res.length; i++) {
			res[i] = src[start + i];
		}
		return res;

	}

	public static String textChar(char[] src) {

		return String.valueOf(src);
	}

	/**
	 * 如果外部内存不可用，会返回一个内部存储，如果ctx=null则只返回外部路径
	 * 
	 * @param ctx
	 * @param subPath
	 * @return
	 */
	public static String getExtenalPath(Context ctx, String subPath) {
		String path;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
				&& Environment.getExternalStorageDirectory().exists()) {
			path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + subPath;
			if (!new File(path).exists()) {
				new File(path).mkdirs();
			}
			return path;
		}
		else if (ctx == null) {
			return null;
		}
		else {
			path = ctx.getCacheDir().getAbsolutePath() + "/" + subPath;
			if (!new File(path).exists()) {
				new File(path).mkdirs();
			}
			return path;
		}
	}

	public static String getFileExitName(String name) {
		if (name.lastIndexOf(".") > -1) {
			name = name.substring(name.lastIndexOf(".") + 1, name.length());
			if (name.equals("jpg")) {
				name = "jpeg";
			}
		}
		return name;

	}

	/**
	 * 计算listview适应高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(PullToRefreshListView listView, int footerHeight, int headHeight) {
		if (listView == null) { return; }
		ListAdapter listAdapter = listView.getRefreshableView().getAdapter();
		if (listAdapter == null) { return; }
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		totalHeight += footerHeight;
		totalHeight += headHeight;
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getRefreshableView().getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 计算listview适应高度
	 * 
	 * @param listView
	 */
	public static int getListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null) { return 0; }
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) { return 0; }
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		return totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

	}

}
