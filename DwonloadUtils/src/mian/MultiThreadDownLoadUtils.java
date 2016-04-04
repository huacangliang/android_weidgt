package mian;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jingzhong.asyntask2.utils.HttpUtils;
import com.jingzhong.asyntask2.utils.HttpUtils2;
import com.jingzhong.asyntask2.utils.MultiThreadDownLoadUtils.DownListenear;
import com.jingzhong.asyntask2.utils.TextUtils;
import com.jingzhong.asyntask2.utils.ThreadService;
import com.squareup.okhttp.OkHttpClient;

/**
 * 多线程下载工具类
 * 
 * @author dzj
 * 
 */
public class MultiThreadDownLoadUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private volatile boolean start;

	private volatile boolean stop;

	// 临时保存下载数据地址
	private File temp;

	// 下载的组数
	private volatile List<DownloadBean> info = new ArrayList<MultiThreadDownLoadUtils.DownloadBean>();

	// 待下载队列,先进先出
	private volatile Queue<DownloadBean> queue = new LinkedList<MultiThreadDownLoadUtils.DownloadBean>();

	private volatile DownListenear downListenear;

	// 是否是单线程下载模式
	private volatile boolean isSingleMode = false;

	/**
	 * 每块下载大小，默认每块大小为10MB
	 */
	public long BLOCKSIZE = 1024 * 1000 * 10;

	/**
	 * 最多几个线程下载，默认10个
	 */
	public final static int MAXTHREADNUM = 10;

	public volatile long tatol = 1;

	private int threadId;

	private DwonloadGuardThread dwonloadGuardThread;

	public String[] getFileNameAndUrl(String url) {
		String[] data = new String[2];
		String name = "";
		HttpUtils http = new HttpUtils();
		URL uri = null;
		try {
			uri = new URL(url);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpURLConnection conn = http.buildConnection(uri, new OkHttpClient());
		try {
			int code = conn.getResponseCode();
			if (code == 200) {
				String connUrl = conn.getURL().toString();
				if (!connUrl.equals(url)) {
					return getRuleFileName(connUrl);
				}
				String att = conn.getHeaderField("Content-Disposition");
				if (TextUtils.isEmpty(att)) {
					return getRuleFileName(url);
				}
				try {
					name = att
							.substring(att.indexOf("filename="), att.length());
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (TextUtils.isEmpty(name)) {
					return getRuleFileName(url);
				}
				data[0] = url;
				data[1] = name;
				return data;
			} else if (code == 302) {
				url = conn.getHeaderField("Location");
				return getFileNameAndUrl(url);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	public String[] getRuleFileName(String url) {
		String[] data = new String[2];
		String name = url.substring(url.lastIndexOf("/") + 1, url.length());
		if (name.indexOf(".") > -1 && name.indexOf("?") == -1) {
			data[0] = url;
			data[1] = name;
			return data;
		}
		name = name.substring(0, name.indexOf("?"));
		if (name.indexOf(".") > -1) {
			data[0] = url;
			data[1] = name;
			return data;
		}
		return data;
	}

	private String fileName;

	private String filePath;

	private String url;

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

	/**
	 * 
	 * @param threadId
	 * @param fileName
	 * @param filePath
	 * @param url
	 * @param downListenear
	 */
	public void download(int threadId, String fileName, String filePath,
			String url, DownListenear downListenear) {
		this.downListenear = downListenear;
		setThreadId(threadId);
		errorCout = 0;
		String[] data = getFileNameAndUrl(url);
		if (data.length <= 0) {
			new Exception("获取下载信息失败");
		}
		url = data[0];

		if (TextUtils.isEmpty(fileName)) {
			fileName = data[1];
		}
		temp = new File(filePath, fileName + ".data");
		info = readDownloadInfo(temp);
		start = true;
		stop = false;
		if (info != null && info.size() > 0) {
			// 继续下载
			continueDownload(fileName, filePath, url);
		} else {
			// 重新开始
			if (new File(filePath, fileName).exists()) {
				// 存在该文件，询问是否要重新下载，如果重新下载，原文件将被覆盖掉
				if (downListenear != null
						&& downListenear.continueDownload(new File(filePath,
								fileName).getAbsolutePath())) {
					new File(filePath, fileName).delete();
					startDownload(fileName, filePath, url);
				} else {
					return;
				}
			} else {
				// 开始下载
				startDownload(fileName, filePath, url);
			}
		}
	}

	public void download(
			String fileName,
			String filePath,
			String url,
			com.jingzhong.asyntask2.utils.MultiThreadDownLoadUtils.DownListenear downListenear) {
		this.downListenear = downListenear;
		String[] data = getFileNameAndUrl(url);
		if (data.length <= 0 || data[0] == null) {
			throw new RuntimeException("获取下载信息失败");
		}
		url = data[0];

		if (TextUtils.isEmpty(fileName)) {
			fileName = data[1];
		}
		errorCout = 0;
		setFileName(fileName);
		setFilePath(filePath);
		setUrl(url);
		temp = new File(filePath, fileName + ".data");
		info = readDownloadInfo(temp);
		start = true;
		stop = false;
		if (info != null && info.size() > 0) {
			// 继续下载
			continueDownload(fileName, filePath, url);
		} else {
			// 重新开始
			if (new File(filePath, fileName).exists()) {
				// 存在该文件，询问是否要重新下载，如果重新下载，原文件将被覆盖掉
				if (downListenear != null
						&& downListenear.continueDownload(new File(filePath,
								fileName).getAbsolutePath())) {
					new File(filePath, fileName).delete();
					startDownload(fileName, filePath, url);
				} else {
					return;
				}
			} else {
				// 开始下载
				startDownload(fileName, filePath, url);
			}
		}
	}

	public void download(String method, String fileName, String filePath,
			String url, DownListenear downListenear) {
		this.downListenear = downListenear;

		String[] data = getFileNameAndUrl(url);
		if (data.length <= 0) {
			new Exception("获取下载信息失败");
		}
		url = data[0];

		if (TextUtils.isEmpty(fileName)) {
			fileName = data[1];
		}

		errorCout = 0;
		temp = new File(filePath, fileName + ".data");
		info = readDownloadInfo(temp);
		start = true;
		stop = false;

		if (info != null && info.size() > 0) {
			// 继续下载
			progress += new File(filePath, fileName).length();
			continueDownload(fileName, filePath, url);
		} else {
			// 重新开始
			if (new File(filePath, fileName).exists()) {
				// 存在该文件，询问是否要重新下载，如果重新下载，原文件将被覆盖掉
				if (downListenear != null
						&& downListenear.continueDownload(new File(filePath,
								fileName).getAbsolutePath())) {
					new File(filePath, fileName).delete();
					startDownload(fileName, filePath, url);
				} else {
					return;
				}
			} else {
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
			} else {
				writeDownloadInfo(temp, info);
				// 开启多线程下载
				startMultiDownload(url, filePath, fileName);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void startSingleDownload(String url, String path, String name) {
		BLOCKSIZE = 0;
		isSingleMode = true;
		dwonloadGuardThread = new DwonloadGuardThread();
		DonwloadHepler donwloadHepler = new DonwloadHepler();
		ThreadService.getInstance().executeThread(dwonloadGuardThread);
		donwloadHepler.stime = System.currentTimeMillis();
		ThreadService.getInstance().executeThread(donwloadHepler);
		DownloadBean bean = new DownloadBean(0, 0);
		bean.start();
	}

	private void startMultiDownload(String url, String path, String name) {
		DonwloadHepler donwloadHepler = new DonwloadHepler();
		dwonloadGuardThread = new DwonloadGuardThread();
		ThreadService.getInstance().executeThread(dwonloadGuardThread);
		donwloadHepler.stime = System.currentTimeMillis();
		ThreadService.getInstance().executeThread(donwloadHepler);
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
			HttpURLConnection conn = new HttpUtils().buildConnection(new URL(
					url), new OkHttpClient());
			setHeader(conn);
			int code;
			code = conn.getResponseCode();
			if (code >= 300) {
				return 0;
			}
			long length = conn.getContentLength();
			length = length == -1 ? conn.getContentLengthLong() : length;
			tatol = length;
			return tatol;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (downListenear != null) {
				start = false;
				stop = true;
				downListenear.onError(url, new Exception("下载错误，网络获取失败"), -1);
			}
		}

		return 0;

	}

	public static final int diskNotFree = -1;

	public static final int diskCanNotWrite = -2;

	public static final int diskCanNotRead = -3;

	/**
	 * 获取下载信息
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private List<DownloadBean> readDownloadInfo(String url, String path,
			String name) throws IOException {

		List<DownloadBean> t = new ArrayList<MultiThreadDownLoadUtils.DownloadBean>();
		HttpURLConnection conn = new HttpUtils().buildConnection(new URL(url),
				new OkHttpClient());
		setHeader(conn);
		int code = conn.getResponseCode();
		if (code >= 300) {
			if (downListenear != null) {
				start = false;
				stop = true;
				downListenear.onError(url, new Exception("下载错误，网络获取失败"), code);
			}
		} else {
			long length = conn.getContentLength();
			length = length == -1 ? conn.getContentLengthLong() : length;
			tatol = length;
			if (length == -1 || length == 0) {
				// 开启单线程下载模式
				return null;
			} else {
				int blockNum = 0;
				// 组建多线程下载模块
				if (length < BLOCKSIZE) {
					BLOCKSIZE = (length / MAXTHREADNUM);
					blockNum = (int) (length / BLOCKSIZE);
				} else {
					blockNum = (int) (length / BLOCKSIZE);
					if (blockNum < MAXTHREADNUM) {
						blockNum = MAXTHREADNUM;
						BLOCKSIZE = (length / MAXTHREADNUM);
					}
				}

				for (int i = 0; i <= blockNum; i++) {
					if (i == blockNum) {
						if (BLOCKSIZE * i + 1 >= length) {
							break;
						}
						DownloadBean bean = new DownloadBean(BLOCKSIZE * i + 1,
								length);
						bean.setThreadId(i);
						t.add(bean);
						break;
					} else {
						DownloadBean bean = new DownloadBean(BLOCKSIZE * i,
								BLOCKSIZE * (i + 1));
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
		conn.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.3) Gecko/2008092510 Ubuntu/8.04 (hardy) Firefox/3.0.3");
		conn.setRequestProperty("Accept-Language", "en-us,en;q=0.7,zh-cn;q=0.3");
		conn.setRequestProperty("Accept-Encoding", "utf-8");
		conn.setRequestProperty("Accept-Charset",
				"ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		conn.setRequestProperty("Keep-Alive", "300");
		conn.setRequestProperty("connnection", "keep-alive");
		conn.setRequestProperty("If-Modified-Since",
				"Fri, 02 Jan 2009 17:00:05 GMT");
		conn.setRequestProperty("If-None-Match", "\"1261d8-4290-df64d224\"");
		conn.setRequestProperty("Cache-conntrol", "max-age=0");
		conn.setRequestProperty("Referer", "http://www.baidu.com");
	}

	public MultiThreadDownLoadUtils() {
		super();
	}

	/**
	 * 读取下载过的信息
	 * 
	 * @param path
	 * @return
	 */
	private synchronized List<DownloadBean> readDownloadInfo(File path) {
		if (!path.exists()) {
			return null;
		}
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
				DownloadBean bena = new DownloadBean(lt.get(i).getStartPos(),
						lt.get(i).getEndPos());
				bena.setProgress(lt.get(i).getProgress());
				bena.setThreadId(i);
				te.add(bena);
			}
			lt.clear();
			lt = null;
			return te;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	Gson gson = new Gson();

	private synchronized void writeDownloadInfo(File path,
			List<DownloadBean> info) {
		if (has_download_ok) {
			return;
		}
		Type type = new TypeToken<List<DownloadBean>>() {
		}.getType();
		String json = gson.toJson(info, type);
		json += "-->" + progress;
		try {
			FileOutputStream fos = new FileOutputStream(path);
			fos.write(json.getBytes());
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private synchronized void deleteDownloadInfo(File path, DownloadBean info) {
		if (this.info.contains(info)) {
			this.info.remove(info);
		}
	}

	/**
	 * @author dzj
	 * 
	 */
	public class DownloadBean implements Serializable, Cloneable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8698693295863668909L;

		private int threadId;

		private long startPos;

		private long endPos;

		private long progress;

		public DownloadBean() {
			// TODO Auto-generated constructor stub
		}

		public DownloadBean(long startPos, long endPos) {
			this.startPos = startPos;
			this.endPos = endPos;
		}

		public void start() {
			new DownloadThread(this).start();
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

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return new Gson().toJson(this);
		}
	}

	volatile long progress = 0;

	public class DonwloadHepler implements Runnable {

		long stime;

		long utime;

		long oldProgress;

		long speend;

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		StringBuilder sb = new StringBuilder();

		Date sd = new Date();

		public void run() {
			// TODO Auto-generated method stub
			oldProgress = 0;
			while (true) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				sb.delete(0, sb.length());
				if (oldProgress == 0) {
					speend = progress;
					oldProgress = progress;
				} else {
					speend = (progress - oldProgress);
					oldProgress = progress;
				}
				if (progress >= tatol) {
					downListenear.onSuccess(new File(getFilePath(),
							getFileName()).getAbsolutePath(), getUrl());
					stop = true;
					start = false;
					has_download_ok = true;
					return;
				}

				sb.append(DateFormatterUtils.getDataSize(speend));
				utime = System.currentTimeMillis() - stime;

				if (downListenear != null) {
					sd.setTime(stime);
					downListenear.onDwonloadListener(progress, tatol, speend,
							sb.toString(),
							DateFormatterUtils.formatterToTime(utime),
							format.format(sd), 0);
				}
				if (!start) {
					if (progress >= tatol || has_download_ok) {
						downListenear.onSuccess(new File(getFilePath(),
								getFileName()).getAbsolutePath(), getUrl());
						has_download_ok = true;
					}
					return;
				}
			}
		}

	}

	public class DwonloadGuardThread implements Runnable {
		boolean over = false;

		public void run() {
			// TODO Auto-generated method stub
			while (!over) {

				if (has_download_ok) {
					if (temp.exists()) {
						temp.delete();
					}
					break;
				}
				if (info != null && temp != null && info.size() > 0) {
					writeDownloadInfo(temp, info);
				}

				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	private Lock lock = new ReentrantLock();// 锁对象

	/**
	 * 下载原理，如果下载好了，就把当前信息从下载信息中删除
	 * 
	 * @author dzj
	 * 
	 */
	public class DownloadThread implements Runnable {

		DownloadBean bean;

		DownloadBean clone;

		DownloadThread thiz;

		public DownloadThread(DownloadBean bean) {
			this.bean = bean;
			try {
				clone = (DownloadBean) bean.clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void start() {
			ThreadService.getInstance().executeThread(this);
		}

		public void run() {
			// TODO Auto-generated method stub

			defualtWork();

		}

		private void defualtWork() {
			// 默认get下载
			if (bean.getStartPos() >= bean.getEndPos()
					|| bean.getStartPos() >= tatol) {

				return;
			}
			HttpUtils http = new HttpUtils();
			InputStream is = http.writeParams("get", null, getUrl(),
					bean.getStartPos(), new OkHttpClient(), null, null);

			SaveFile save = null;

			try {
				if (is == null) {
					throw new Exception("意外终止的错误");
				}
				save = new SaveFile(
						new File(filePath, getFileName()).getAbsolutePath(),
						bean.getStartPos());

				int readInt = 0;
				if (BLOCKSIZE == 0) {
					byte[] buffer = new byte[1024 * 10];
					while ((readInt = is.read(buffer)) != -1 && !isStop()) {
						save.write(buffer, 0, readInt);
						progress += readInt;
					}
					if (downListenear != null && isSingleMode && !isStop()) {
						lock.lock();
						try {
							has_download_ok = true;
						} finally {
							lock.unlock();
						}

						start = false;
						stop = true;

						return;
					}
				} else {
					long needLen = bean.getEndPos() - bean.getStartPos();
					byte[] buffer = new byte[(int) needLen];
					while (!isStop() && (readInt = is.read(buffer)) != -1) {
						if (readInt == 0) {
							throw new Exception("意外终止的错误");
						}
						bean.setProgress(bean.getProgress() + readInt);
						progress += readInt;
						save.write(buffer, 0, readInt);
						if (bean.getProgress() >= needLen) {
							break;
						}
					}

					if (queue.size() > 0) {
						if (!isStop()) {
							deleteDownloadInfo(temp, bean);
							queue.poll().start();
						}
					} else {
						// 在这判定是否结束
						if (progress >= tatol) {
							lock.lock();
							try {
								has_download_ok = true;
							} finally {
								lock.unlock();
							}

							if (downListenear != null && has_download_ok) {
								start = false;
								stop = true;
							}
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// System.out.println(e.getMessage());
				// 不要抛出错误，应该重新下载该块
				// if (downListenear != null) {
				// start = false;
				// stop = true;
				// downListenear.onError(bean.getUrl(), e, 0);
				// }
				synchronized (bean) {
					if (clone != null && errorCout < 3 && !stop) {
						errorCout++;
						info.remove(bean);
						progress -= bean.progress;
						thiz = new DownloadThread(clone);
						bean = null;
						has_download_ok = false;
						info.add(clone);
						writeDownloadInfo(temp, info);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						thiz.start();
					} else {
						start = false;
						stop = true;
						dwonloadGuardThread.over = true;
						if (downListenear != null) {
							downListenear.onError(getUrl(), e, 0);
						}
					}
				}

			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (save != null) {
					try {
						save.close();
					} catch (IOException e) {
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

	// 如果发生错误，就重试三次
	volatile int errorCout = 0;

	volatile boolean has_download_ok = false;

	/**
	 * 随机写入数据
	 * 
	 * @author dzj
	 * 
	 */
	public class SaveFile {

		private RandomAccessFile rafile;

		public SaveFile(String path, long pos) throws Exception {
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
			} catch (IOException e) {
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
