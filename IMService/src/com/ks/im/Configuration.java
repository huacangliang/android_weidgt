package com.ks.im;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Configuration {
	public static final String SERVERIP="192.168.0.6";
	public static final int SERVERPORT=9010;
	public static final int BUFFERLENGTH=64*1000;
	public static final int RETRYWRITECOUNT=5;
	public static final int PHONENUMLENGTH=11;
	
	public static final int MSGTYPE_LOGIN=0;
	public static final int MSGTYPE_LOGOUT=1;
	public static final int MSGTYPE_MSG=2;
	
	public static final String DEFAULTENCODING="UTF-8";
	
	public static final int MAXCONNECTPOOL=1000;
	
	public static boolean ISRUN=false;
	
	public static final ExecutorService POOL = Executors.newCachedThreadPool();
}
