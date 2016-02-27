package com.jingzhong.asyntask2.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

/**
 * 线程统一入口
 * 
 * @author dengzhijiang
 * 
 */
public class ThreadService {

	public void clear() {
		executorService.shutdown();
		executorService = Executors.newFixedThreadPool(THREADNUM);
	}

	/**
	 * CPU number
	 */
	private int cpuNum = 0;

	class CpuFilter
			implements FileFilter {

		public boolean accept(File pathname) {
			if (Pattern.matches("cpu[0-9]", pathname.getName())) { return true; }
			return false;
		}
	}

	private ThreadService() {
		try {
			File dir = new File("/sys/devices/system/cpu/");
			File[] files = dir.listFiles(new CpuFilter());
			cpuNum = files.length;
		}
		catch (Exception e) {
			cpuNum = 1;
		}

		switch (cpuNum) {
		case 1:
			THREADNUM = 50;
			break;
		case 2:
			THREADNUM = 65;
			break;
		case 4:
			THREADNUM = 70;
			break;
		case 8:
			THREADNUM = 100;
			break;
		case 12:
			THREADNUM = 120;
			break;

		default:
			THREADNUM = 40;
			break;
		}

		executorService = Executors.newFixedThreadPool(THREADNUM);

	}

	private static ThreadService instance;

	public static ThreadService getInstance() {
		if (instance == null) {
			instance = new ThreadService();
		}
		return instance;
	}

	/**
	 * 允许的线程数
	 */
	private int					THREADNUM		= 20;

	private ExecutorService		executorService;


	public void executeThread(Thread run) {
		executorService.execute(run);
	}

	/**
	 * 不需要创建线程
	 * 
	 * @param run
	 */
	public void executeThread(Runnable run) {
		executorService.execute(run);
	}

}
