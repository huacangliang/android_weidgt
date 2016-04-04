package com.ks.im;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConnManager implements Serializable {

	private IMPlugin im;

	private Map<Integer, DatagramSocket> cacheMap = new HashMap<Integer, DatagramSocket>(
			Configuration.MAXCONNECTPOOL);

	ReadWriteLock rwLock = new ReentrantReadWriteLock();
	/**
	 * 
	 */
	private static final long serialVersionUID = 6298953920411818505L;

	private ConnManager() {
		super();
	}

	public DatagramSocket creatConn() throws SocketException,
			UnknownHostException {
		DatagramSocket socket = new DatagramSocket(0,
				InetAddress.getByName(Configuration.SERVERIP));
		Integer key = socket.getLocalPort();
		cacheMap.put(key, socket);
		CheckState checkState = new CheckState();
		checkState.im = im;
		checkState.socket = socket;
		checkState.connManager=this;
		Configuration.POOL.execute(checkState);
		return socket;
	}

	private static class CheckState implements Runnable {
		DatagramSocket socket;
		private IMPlugin im;
		private byte[] buffer = new byte[Configuration.BUFFERLENGTH];
		DatagramPacket dp_receive = new DatagramPacket(buffer,
				Configuration.BUFFERLENGTH);
		ConnManager connManager;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (Configuration.ISRUN) {
				boolean isResponse = false;
				try {
					socket.receive(dp_receive);
					isResponse = true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (!Configuration.ISRUN) {
					break;
				}
				if (isResponse) {
					byte[] data = dp_receive.getData();
					if (data[0] == 0) {
						// 是心跳包，略过
					} else if (data[0] != -1) {
						im.resolveReciveData(dp_receive);
					} else {
						socket.close();
						connManager.release(socket);
						socket = null;
						System.gc();
						return;
					}
				}
				dp_receive.setLength(Configuration.BUFFERLENGTH);
			}
		}

	}

	public DatagramSocket get(int port) {
		// TODO Auto-generated method stub
		if (port != 0) {
			return cacheMap.get(port);
		}
		return null;
	}

	public void release(DatagramSocket socket) {
		Integer port = socket.getLocalPort();
		cacheMap.remove(port);
	}

	public static ConnManager getInstance(IMPlugin im) {
		Sub.instance.im = im;
		return Sub.instance;
	}

	public static class ConnInfo {
		private DatagramSocket socket;
		private boolean state;
	}

	private static class Sub {
		private static ConnManager instance;
		static {
			if (instance == null)
				instance = new ConnManager();
		}
	}
}
