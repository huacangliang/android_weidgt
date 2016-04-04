package com.jingzhong.asyntask2.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jingzhong.asyntask2.listenear.Defaultlistenear;
import com.squareup.okhttp.OkHttpClient;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.Formatter;

/**
 * 多线程下载工具类
 * 
 * @author dzj
 *
 */
public class MultiThreadDownLoadUtils
		implements Serializable {

	/**
	 * 
	 */
	private static final long				serialVersionUID	= 1L;

	private volatile boolean				start;

	private volatile boolean				stop;

	// 临时保存下载数据地址
	private File							temp;

	// 下载的组数
	private volatile List<DownloadBean>		info				= new ArrayList<MultiThreadDownLoadUtils.DownloadBean>();

	// 待下载队列,先进先出
	private volatile Queue<DownloadBean>	queue				= new LinkedList<MultiThreadDownLoadUtils.DownloadBean>();

	private volatile DownListenear			downListenear;

	// 是否是单线程下载模式
	private volatile boolean				isSingleMode		= false;

	/**
	 * 每块下载大小，默认每块大小为10MB
	 */
	public int								BLOCKSIZE			= 1024 * 1000 * 10;

	/**
	 * 最多几个线程下载，默认10个
	 */
	public static int						MAXTHREADNUM		= 10;

	public volatile long					tatol				= 1;

	private volatile Context				ctx;

	private int								threadId;

	public interface DownListenear
			extends HttpUtils.OnNetListenear {

		void onError(String url, Exception e, int status);

		void onSuccess(String path, String url);

		boolean continueDownload(String filePath);
	}

	public String getFileName(String url) {

		return url.substring(url.lastIndexOf("/") + 1, url.length());

	}

	/**
	 * 可在ui线程修改数据
	 * 
	 * @param threadId
	 * @param fileName
	 * @param filePath
	 * @param url
	 * @param downListenear
	 */
	public void download(int threadId, String fileName, String filePath, String url, DownListenear downListenear) {
		this.downListenear = new Defaultlistenear(downListenear);
		setThreadId(threadId);
		if (TextUtils.isEmpty(fileName)) {
			fileName = getFileName(url);
		}
		temp = new File(filePath, fileName + ".temp");
		info = readDownloadInfo(temp);
		start = true;
		stop = false;
		DonwloadHepler donwloadHepler = new DonwloadHepler();
		donwloadHepler.stime = System.currentTimeMillis();
		ThreadService.getInstance().executeThread(donwloadHepler);
		if (info != null && info.size() > 0) {
			// 继续下载
			continueDownload(fileName, filePath, url);
		}
		else {
			// 重新开始
			if (new File(filePath, fileName).exists()) {
				// 存在该文件，询问是否要重新下载，如果重新下载，原文件将被覆盖掉
				if (downListenear != null
						&& downListenear.continueDownload(new File(filePath, fileName).getAbsolutePath())) {
					new File(filePath, fileName).delete();
					startDownload(fileName, filePath, url);
				}
				else {
					return;
				}
			}
			else {
				// 开始下载
				startDownload(fileName, filePath, url);
			}
		}
	}

	public void download(String fileName, String filePath, String url, DownListenear downListenear) {
		this.downListenear = downListenear;
		if (TextUtils.isEmpty(fileName)) {
			fileName = getFileName(url);
		}
		temp = new File(filePath, fileName + ".temp");
		info = readDownloadInfo(temp);
		start = true;
		stop = false;
		DonwloadHepler donwloadHepler = new DonwloadHepler();
		donwloadHepler.stime = System.currentTimeMillis();
		ThreadService.getInstance().executeThread(donwloadHepler);
		if (info != null && info.size() > 0) {
			// 继续下载
			continueDownload(fileName, filePath, url);
		}
		else {
			// 重新开始
			if (new File(filePath, fileName).exists()) {
				// 存在该文件，询问是否要重新下载，如果重新下载，原文件将被覆盖掉
				if (downListenear != null
						&& downListenear.continueDownload(new File(filePath, fileName).getAbsolutePath())) {
					new File(filePath, fileName).delete();
					startDownload(fileName, filePath, url);
				}
				else {
					return;
				}
			}
			else {
				// 开始下载
				startDownload(fileName, filePath, url);
			}
		}
	}

	public void download(Map<String, Object> params, String method, String fileName, String filePath, String url, DownListenear downListenear) {
		this.downListenear = downListenear;
		if (TextUtils.isEmpty(fileName)) {
			fileName = getFileName(url);
		}
		temp = new File(filePath, fileName + ".temp");
		info = readDownloadInfo(temp);
		start = true;
		stop = false;
		DonwloadHepler donwloadHepler = new DonwloadHepler();
		donwloadHepler.stime = System.currentTimeMillis();
		ThreadService.getInstance().executeThread(donwloadHepler);
		if (info != null && info.size() > 0) {
			// 继续下载
			progress += new File(filePath, fileName).length();
			continueDownload(fileName, filePath, url);
		}
		else {
			// 重新开始
			if (new File(filePath, fileName).exists()) {
				// 存在该文件，询问是否要重新下载，如果重新下载，原文件将被覆盖掉
				if (downListenear != null
						&& downListenear.continueDownload(new File(filePath, fileName).getAbsolutePath())) {
					new File(filePath, fileName).delete();
					startDownload(fileName, filePath, url);
				}
				else {
					return;
				}
			}
			else {
				startDownload(fileName, filePath, url);
			}
		}
	}

	/**
	 * 继续下载
	 * 
	 * @param fileName
	 * @param filePath
	 * @param url
	 */
	private void continueDownload(String fileName, String filePath, String url) {
		getFileSize(url);
		startMultiDownload(url, filePath, fileName);
	}

	/**
	 * 从头下载
	 * 
	 * @param fileName
	 * @param filePath
	 * @param url
	 */
	private void startDownload(String fileName, String filePath, String url) {
		try {
			info = readDownloadInfo(url, filePath, fileName);
			if (info == null || info.size() <= 0) {
				// 开启单线程下载
				startSingleDownload(url, filePath, fileName);
			}
			else {
				writeDownloadInfo(temp, info);
				// 开启多线程下载
				startMultiDownload(url, filePath, fileName);
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void startSingleDownload(String url, String path, String name) {
		BLOCKSIZE = 0;
		isSingleMode = true;
		DownloadBean bean = new DownloadBean(name, path, url, 0, 0);
		bean.start();
	}

	private void startMultiDownload(String url, String path, String name) {
		isSingleMode = false;

		for (int i = 0; i < info.size(); i++) {
			queue.offer(info.get(i));
		}

		/**
		 * 先启动10个线程下载，每下载好一个就从队列中取出继续下载
		 */
		for (int i = 0; i < MAXTHREADNUM && i < info.size(); i++) {
			queue.poll().start();
		}

	}

	public long getFileSize(String url) {

		try {
			HttpURLConnection conn = new HttpUtils().buildConnection(new URL(url), new OkHttpClient());
			setHeader(conn);
			int code;
			code = conn.getResponseCode();
			if (code >= 300) { return 0; }
			int length = conn.getContentLength();
			tatol = length;
			if (tatol == -1) {
				tatol = 1;
			}
			return tatol;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;

	}

	public static final int	diskNotFree		= -1;

	public static final int	diskCanNotWrite	= -2;

	public static final int	diskCanNotRead	= -3;

	/**
	 * 获取下载信息
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private List<DownloadBean> readDownloadInfo(String url, String path, String name) throws IOException {
		if (!Environment.getExternalStorageDirectory().canRead()) {
			if (downListenear != null) {
				downListenear.onError(url, new Exception("内存不能读取数据！"), diskCanNotRead);
				return null;
			}
		}

		if (!Environment.getExternalStorageDirectory().canWrite()) {
			if (downListenear != null) {
				downListenear.onError(url, new Exception("内存不能写入数据！"), diskCanNotWrite);
				return null;
			}
		}

		List<DownloadBean> t = new ArrayList<MultiThreadDownLoadUtils.DownloadBean>();
		HttpURLConnection conn = new HttpUtils().buildConnection(new URL(url), new OkHttpClient());
		setHeader(conn);
		int code = conn.getResponseCode();
		if (code >= 300) {
			if (downListenear != null) {
				downListenear.onError(url, null, code);
			}
		}
		else {
			int length = conn.getContentLength();
			if (length > Environment.getExternalStorageDirectory().getFreeSpace()) {
				if (downListenear != null) {

					downListenear.onError(url, new Exception("内存不足！"), diskNotFree);
					return null;
				}
			}
			tatol = length;
			if (length == -1 || length == 0) {
				// 开启单线程下载模式
				return null;
			}
			else {
				int blockNum = 0;
				// 组建多线程下载模块
				if (length < BLOCKSIZE) {
					BLOCKSIZE = length / MAXTHREADNUM;
					blockNum = length / BLOCKSIZE;
				}
				else {
					blockNum = length / BLOCKSIZE;
					if (blockNum < MAXTHREADNUM) {
						BLOCKSIZE = length / MAXTHREADNUM;
						blockNum = MAXTHREADNUM;
					}
				}

				for (int i = 0; i < blockNum; i++) {
					if (i == blockNum - 1) {
						DownloadBean bean = new DownloadBean(name, path, url, BLOCKSIZE * i, length);
						bean.setThreadId(i);
						t.add(bean);
						break;
					}
					else {
						DownloadBean bean = new DownloadBean(name, path, url, BLOCKSIZE * i, BLOCKSIZE * (i + 1));
						bean.setThreadId(i);
						t.add(bean);
					}

				}
				return t;
			}
		}
		return null;

	}

	public void setHeader(URLConnection conn) {
		conn.setRequestProperty("User-Agent",
				"Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.3) Gecko/2008092510 Ubuntu/8.04 (hardy) Firefox/3.0.3");
		conn.setRequestProperty("Accept-Language", "en-us,en;q=0.7,zh-cn;q=0.3");
		conn.setRequestProperty("Accept-Encoding", "utf-8");
		conn.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		conn.setRequestProperty("Keep-Alive", "300");
		conn.setRequestProperty("connnection", "keep-alive");
		conn.setRequestProperty("If-Modified-Since", "Fri, 02 Jan 2009 17:00:05 GMT");
		conn.setRequestProperty("If-None-Match", "\"1261d8-4290-df64d224\"");
		conn.setRequestProperty("Cache-conntrol", "max-age=0");
		conn.setRequestProperty("Referer", "http://www.baidu.com");
	}

	public MultiThreadDownLoadUtils(Context ctx) {
		super();
		this.ctx = ctx;
	}

	/**
	 * 读取下载过的信息
	 * 
	 * @param path
	 * @return
	 */
	@SuppressWarnings("resource")
	private synchronized List<DownloadBean> readDownloadInfo(File path) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String t = null;
			while ((t = br.readLine()) != null) {
				sb.append(t);
			}
			progress = 0;
			String[] spliStr = sb.toString().split("-->");
			String list = spliStr[0];
			String pString = spliStr[1];
			progress = Integer.parseInt(pString);
			Type type = new TypeToken<List<DownloadBean>>() {
			}.getType();
			List<DownloadBean> lt = gson.fromJson(list, type);
			br = null;
			List<DownloadBean> te = new ArrayList<MultiThreadDownLoadUtils.DownloadBean>();

			for (int i = 0; i < lt.size(); i++) {
				DownloadBean bena = new DownloadBean(lt.get(i).getFileName(), lt.get(i).getFilePath(),
						lt.get(i).getUrl(), lt.get(i).getStartPos(), lt.get(i).getEndPos());
				bena.setProgress(lt.get(i).getProgress());
				bena.setThreadId(i);
				te.add(bena);
			}
			lt.clear();
			lt = null;
			return te;
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	Gson gson = new Gson();

	private synchronized void writeDownloadInfo(File path, List<DownloadBean> info) {
		Type type = new TypeToken<List<DownloadBean>>() {
		}.getType();
		String json = gson.toJson(info, type);
		json += "-->" + progress;
		try {
			FileOutputStream fos = new FileOutputStream(path);
			fos.write(json.getBytes());
			fos.flush();
			fos.close();
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private synchronized void deleteDownloadInfo(File path, DownloadBean info) {
		if (this.info.contains(info)) {
			this.info.remove(info);
			writeDownloadInfo(path, this.info);
		}
	}

	/**
	 * @author dzj
	 *
	 */
	public class DownloadBean
			implements Serializable, Cloneable {

		/**
		 * 
		 */
		private static final long	serialVersionUID	= -8698693295863668909L;

		private int					threadId;

		private String				fileName;

		private String				filePath;

		private String				url;

		private long				startPos;

		private long				endPos;

		private long				progress;

		private Map<String, Object>	params;

		private SSLContext			sctx;

		public SSLContext getSctx() {
			return sctx;
		}

		public void setSctx(SSLContext sctx) {
			this.sctx = sctx;
		}

		public HostnameVerifier getVerifier() {
			return verifier;
		}

		public void setVerifier(HostnameVerifier verifier) {
			this.verifier = verifier;
		}

		private HostnameVerifier verifier;

		public Map<String, Object> getParams() {
			return params;
		}

		public void setParams(Map<String, Object> params) {
			this.params = params;
		}

		public String getMethod() {
			return method;
		}

		public void setMethod(String method) {
			this.method = method;
		}

		private String method;

		public DownloadBean() {
			// TODO Auto-generated constructor stub
		}

		public DownloadBean(String fileName, String filePath, String url, long startPos, long endPos) {
			this.fileName = fileName;
			this.filePath = filePath;
			this.url = url;
			this.startPos = startPos;
			this.endPos = endPos;
		}

		public DownloadBean(String fileName,
				String filePath,
				String url,
				long startPos,
				long endPos,
				Map<String, Object> params,
				String method) {
			this.fileName = fileName;
			this.filePath = filePath;
			this.url = url;
			this.startPos = startPos;
			this.endPos = endPos;
			this.params = params;
			this.method = method;
		}

		public DownloadBean(String fileName,
				String filePath,
				String url,
				long startPos,
				long endPos,
				Map<String, Object> params,
				String method,
				SSLContext sctx,
				HostnameVerifier verifier) {
			this.fileName = fileName;
			this.filePath = filePath;
			this.url = url;
			this.startPos = startPos;
			this.endPos = endPos;
			this.params = params;
			this.method = method;
			this.sctx = sctx;
			this.verifier = verifier;
		}

		public void start() {
			new DownloadThread(this).start();
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public long getStartPos() {
			return startPos;
		}

		public void setStartPos(long startPos) {
			this.startPos = startPos;
		}

		public long getEndPos() {
			return endPos;
		}

		public void setEndPos(long endPos) {
			this.endPos = endPos;
		}

		@Override
		protected Object clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(bos);
				oos.writeObject(this);
				oos.flush();
				oos.close();
				ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
				ObjectInputStream ois = new ObjectInputStream(bis);
				Object o = ois.readObject();
				ois.close();
				return o;
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return super.clone();
		}

		public long getProgress() {
			return progress;
		}

		public void setProgress(long progress) {
			this.progress = progress;
		}

		public int getThreadId() {
			return threadId;
		}

		public void setThreadId(int threadId) {
			this.threadId = threadId;
		}

	}

	volatile long progress = 0;

	public class DonwloadHepler
			implements Runnable {

		long				stime;

		long				utime;

		long				oldProgress;

		long				speend;

		SimpleDateFormat	format	= new SimpleDateFormat("yy-MM-dd HH:mm:ss");

		StringBuilder		sb		= new StringBuilder();

		Date				sd		= new Date();

		@Override
		public void run() {
			// TODO Auto-generated method stub
			oldProgress = 0;
			while (start) {

				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (has_download_ok) {
					break;
				}
				sb.delete(0, sb.length());
				if (oldProgress == 0) {
					speend = progress;
					oldProgress = progress;
					continue;
				}
				else {
					speend = (progress - oldProgress);
					oldProgress = progress;
				}

				sb.append(Formatter.formatFileSize(ctx, speend));
				utime = System.currentTimeMillis() - stime;

				if (downListenear != null) {
					sd.setTime(stime);
					if (progress > tatol) {
						progress = tatol;
					}
					downListenear.onDwonloadListener(progress, tatol, speend,sb.toString(),
							DateFormatterUtils.formatterToTime(utime), format.format(sd),0);
				}

			}
		}

	}

	/**
	 * 下载原理，如果下载好了，就把当前信息从下载信息中删除
	 * 
	 * @author dzj
	 *
	 */
	public class DownloadThread
			implements Runnable {

		DownloadBean bean;

		public DownloadThread(DownloadBean bean) {
			this.bean = bean;
		}

		public void start() {
			ThreadService.getInstance().executeThread(this);
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (bean.getMethod() == null) {
				defualtWork();
			}
		}

		private void defualtWork() {
			// 默认get下载
			HttpUtils http = new HttpUtils();
			InputStream is = http.writeParams("get", null, bean.getUrl(), bean.getStartPos(), new OkHttpClient(), null,
					null);
			SaveFile save = null;

			try {
				save = new SaveFile(new File(bean.filePath, bean.getFileName()).getAbsolutePath(), bean.getStartPos());
				byte[] buffer = new byte[1024 * 10];
				int reandInt = 0;
				if (BLOCKSIZE == 0) {

					while ((reandInt = is.read(buffer)) != -1 && !isStop()) {
						save.write(buffer, 0, reandInt);
						progress += reandInt;
					}
					if (downListenear != null && isSingleMode && !isStop()) {
						start = false;
						stop = true;
						downListenear.onSuccess(new File(bean.getFilePath(), bean.getFileName()).getAbsolutePath(),
								bean.getUrl());
						return;
					}
				}
				else {

					while (bean.getStartPos() < bean.getEndPos() && !isStop()) {
						reandInt = is.read(buffer);
						if (reandInt < 0) {
							has_download_ok = true;
							break;
						}
						bean.setProgress(bean.getProgress() + reandInt);
						bean.setStartPos(bean.getStartPos() + reandInt);
						progress += reandInt;
						save.write(buffer, 0, reandInt);
					}

					if (queue.size() > 0) {
						if (!isStop()) {
							deleteDownloadInfo(temp, bean);
							queue.poll().start();
						}
					}
					else {
						if (!isStop()) {
							reandInt = is.read(buffer);
							if (reandInt < 0) {
								has_download_ok = true;
							}
							if (downListenear != null && has_download_ok) {
								if (temp.exists()) {
									temp.delete();
								}
								downListenear.onSuccess(
										new File(bean.getFilePath(), bean.getFileName()).getAbsolutePath(),
										bean.getUrl());
							}
						}
					}
				}
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if (downListenear != null && isSingleMode) {
					downListenear.onError(bean.getUrl(), e, 0);
				}
			}
			finally {
				if (save != null) {
					try {
						save.close();
					}
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (!has_download_ok) {
					writeDownloadInfo(temp, info);
				}
			}
		}
	}

	volatile boolean has_download_ok = false;

	/**
	 * 随机写入数据
	 * 
	 * @author dzj
	 *
	 */
	public class SaveFile {

		private RandomAccessFile rafile;

		public SaveFile(String path, long pos)
				throws Exception {
			rafile = new RandomAccessFile(path, "rw");
			rafile.seek(pos);
		}

		/**
		 * <b>function:</b> 同步方法写入文件
		 * 
		 * @author hoojo
		 * @createDate 2011-9-26 下午12:21:22
		 * @param buff
		 *            缓冲数组
		 * @param start
		 *            起始位置
		 * @param length
		 *            长度
		 * @return
		 */
		public synchronized int write(byte[] buff, int start, int length) {
			int i = -1;
			try {
				rafile.write(buff, start, length);
				i = length;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			return i;
		}

		public void close() throws IOException {
			if (rafile != null) {
				rafile.close();
			}
		}
	}

	public boolean isStart() {
		return start;
	}

	public boolean isStop() {
		return stop;
	}

	public void stop() {
		this.stop = true;
	}

	public List<DownloadBean> getInfo() {
		return info;
	}

	public void setInfo(List<DownloadBean> info) {
		this.info = info;
	}

	public DownListenear getDownListenear() {
		return downListenear;
	}

	public void setDownListenear(DownListenear downListenear) {
		this.downListenear = downListenear;
	}

	public int getThreadId() {
		return threadId;
	}

	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}
}
