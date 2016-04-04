package mian;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.jingzhong.asyntask2.listenear.Defaultlistenear2;
import com.jingzhong.asyntask2.utils.HttpUtils2;

public class FileDwonload {

	Object wait = new Object();
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		args = new String[] {
				"",
				"C://迅雷下载/",
				"https://github.com/huacangliang/android_weidgt.git" };
		FileDwonload fileDwonload = new FileDwonload();
		fileDwonload.dwonload(args);
		try {
			synchronized (fileDwonload.wait) {
				fileDwonload.wait.wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scanner input = new Scanner(System.in);
		int re = input.nextInt();
		if (re == 1)
			System.exit(0);
		// System.out.println(test());
	}

	public static String test() {
		String url = "http://192.168.1.107:8080/meibi/queryUserById.html";
		HttpUtils2 utils = new HttpUtils2();
		Map<String, String> requestProperty = new HashMap<String, String>();
		requestProperty.put("Content-type", "text/json");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", "2");

		return utils.syncGetMethod(url, params, requestProperty)
				.string("UTF-8");
	}

	private void dwonload(String[] args) {
		String url = args[2];
		String path = args[1];
		String name = args[0];
		MultiThreadDownLoadUtils utils = new MultiThreadDownLoadUtils();
		utils.download(name, path, url, new Defaultlistenear2() {

			@Override
			public void onDwonloadListener(long progress, long tatol,
					long speed, String speend, String utime, String stime,
					int tid) {
				// TODO Auto-generated method stub
				if (progress >= tatol) {
					return;
				}
				double p = (((double) progress) / ((double) tatol)) * 100;
				String ntime = "";
				long np = tatol - progress;
				String msg = "";
				if (speed <= 0) {
					speed = 1;
					msg = "当前进度：" + DateFormatterUtils.retain2(p) + "% "
							+ speend + "/s 已用时：" + utime
							+ " 约剩余时间：未知               " + " 开始时间：" + stime
							+ " 当前时间：" + format.format(new Date());
				} else {
					long nt = np / speed;
					nt *= 1000;
					ntime = DateFormatterUtils.formatterToTime(nt);
					msg = "当前进度：" + DateFormatterUtils.retain2(p) + "% "
							+ speend + "/s 已用时：" + utime + " 约剩余时间：" + ntime
							+ " 开始时间：" + stime + " 当前时间："
							+ format.format(new Date());
				}

				System.out.println(msg);
			}

			@Override
			public void onSuccess(String path, String url) {
				// TODO Auto-generated method stub
				System.out.println("下载成功！path:" + path + "-->url:" + url);
				synchronized (wait) {
					wait.notify();
				}

			}

			@Override
			public void onError(String url, Exception e, int status) {
				// TODO Auto-generated method stub
				System.out.println("下载失败2");
				synchronized (wait) {
					wait.notify();
				}
			}

			@Override
			public boolean continueDownload(String filePath) {
				// TODO Auto-generated method stub
				// 已经存在该文件
				System.out.println("该文件已下载，是否要覆盖？0覆盖，1取消：");
				Scanner input = new Scanner(System.in);
				int r = input.nextInt();
				switch (r) {
				case 0:
					return true;
				case 1:
					return false;
				default:
					break;
				}
				return true;
			}
		});

	}

}
