package com.ks.im;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

import com.google.gson.Gson;
import com.jingzhong.asyntask2.utils.HttpMothed2;
import com.jingzhong.asyntask2.utils.HttpUtils2;

public class IMDefaultExcute implements IMPlugin {
	ExecutorService pool = Configuration.POOL;
	private final static java.util.Map<String, NetInfo> CLIENTS = new HashMap<String, NetInfo>(
			1000, 0.1f);
	private final Queue<Message> mMsgQueue = new LinkedList<Message>();
	private byte[] buffer = new byte[Configuration.BUFFERLENGTH];
	DatagramSocket serverDS;
	DatagramPacket dp_receive = new DatagramPacket(buffer,
			Configuration.BUFFERLENGTH);
	private boolean isStop;
	ReceiveTask receiveTask;
	WriteTask writeTask;
	Object wait = new Object();
	ConnManager connManager;

	@Override
	public boolean startUp() throws Exception {
		// TODO Auto-generated method stub
		serverDS = new DatagramSocket(Configuration.SERVERPORT,
				InetAddress.getByName(Configuration.SERVERIP));
		isStop = false;
		Configuration.ISRUN = true;
		connManager = ConnManager.getInstance(this);
		receiveTask = new ReceiveTask();
		writeTask = new WriteTask();
		pool.execute(receiveTask);
		pool.execute(writeTask);
		return false;
	}

	@Override
	public boolean write(Message msg) {
		if (CLIENTS.containsKey(msg.getToPhoneNo())) {
			NetInfo info = CLIENTS.get(msg.getToPhoneNo());
			InetAddress addr = info.getAddr();
			byte[] data = packetData(msg);
			DatagramPacket dp_send = new DatagramPacket(data, data.length,
					addr, info.getPort());
			DatagramSocket serverSendDS = null;
			if (msg.getType() == Configuration.MSGTYPE_LOGIN) {
				serverSendDS=serverDS;
			} else {
				serverSendDS = connManager.get(info.getLocalPort());
			}
			if (serverSendDS == null) {
				if (msg.getType() == Configuration.MSGTYPE_MSG) {
					// 对方下线了，需要保存到业务服务器上
				}
				return false;
			}
			pool.execute(new SendTask(serverSendDS, dp_send, msg));
			return true;
		}
		if (msg.getType() == Configuration.MSGTYPE_MSG) {
			// 说明对方没登陆，需要保存到业务服务器上

		}
		return false;
	}
	
	public void saveMsgToLocal(Message msg){
		String msgs=new Gson().toJson(msg);
		HttpUtils2 http=new HttpUtils2();
		String url = Configuration.SERVERIP+":8080/ksIM-web/msg/add";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("msg", msgs);
		Map<String, String> requestProperty = new HashMap<String, String>();
		requestProperty.put("Content-type", "text/json");
		http.postMethod(url, params, requestProperty);
	}

	private static class SendTask implements Runnable {
		DatagramPacket dp_send;
		Message msg;
		DatagramSocket serverSendDS = null;

		public SendTask(DatagramSocket serverSendDS, DatagramPacket dp_send,
				Message msg) {
			super();
			this.dp_send = dp_send;
			this.msg = msg;
			this.serverSendDS = serverSendDS;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			int count = 0;
			while (count < Configuration.RETRYWRITECOUNT) {

				try {
					System.out.println("向客户端转发："
							+ dp_send.getAddress().getHostAddress() + ":"
							+ dp_send.getPort());
					if (serverSendDS == null) {
						System.out.println("null server");
					}
					serverSendDS.send(dp_send);
					return;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				count++;
			}
		}

		// 对方网络可能不正常，暂时收不到消息，所以消息要保存到业务服务器上，等待对方正常后获取
		// 业务服务器暂时没用
	};

	@Override
	public void read() {
		// TODO Auto-generated method stub
		while (!isStop) {
			try {
				serverDS.receive(dp_receive);
				DatagramPacket t_receive = new DatagramPacket(
						dp_receive.getData(), dp_receive.getLength(),
						dp_receive.getAddress(), dp_receive.getPort());
				System.out.println("收到客户端连接请求，ip:"
						+ dp_receive.getAddress().getHostAddress() + "--端口："
						+ dp_receive.getPort());
				dp_receive.setLength(Configuration.BUFFERLENGTH);
				pool.execute(new ResolveReciveDataTask(t_receive));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class ResolveReciveDataTask implements Runnable {
		DatagramPacket dp_receive;

		public ResolveReciveDataTask(DatagramPacket dp_receive) {
			super();
			this.dp_receive = dp_receive;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			resolveReciveData(dp_receive);
			dp_receive.setLength(Configuration.BUFFERLENGTH);
		}

	}

	private void insertMsg(Message msg) {
		// 如果内存保存数量超过了10000，则保存到数据库
		if (mMsgQueue.size() >= 10000) {
			return;
		}
		mMsgQueue.offer(msg);
		notifyDataChange();
	}

	private Message getMsg() {
		Message msg = mMsgQueue.poll();
		if (msg == null) {
			// 如果内存用光了，就去数据库看看
		}
		return msg;
	}

	private byte[] packetData(Message msg) {
		byte[] sendData = null;

		byte[] types = new byte[] { (byte) msg.getType() };

		byte[] fromPhoneBytes = Utils.packetEn(msg.getFromPhoneNo());

		sendData = Utils.connArr(types, fromPhoneBytes);

		byte[] toPhoneBytes = Utils.packetEn(msg.getToPhoneNo());

		sendData = Utils.connArr(sendData, toPhoneBytes);

		byte[] uid = Utils.packetEn(msg.getUid() + "");
		sendData = Utils.connArr(sendData, uid);

		if (msg.getType() == Configuration.MSGTYPE_MSG) {
			String tm = msg.getTime() + "[+t]" + msg.getFromName() + "[+m]"
					+ msg.getMsg();
			byte[] ms = Utils.packetCH(tm);
			sendData = Utils.connArr(sendData, ms);
		} else if (msg.getMsg() != null) {
			sendData = Utils.connArr(sendData, msg.getMsg().getBytes());
		}
		return sendData;
	}

	private void notifyDataChange() {
		synchronized (wait) {
			wait.notify();
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		isStop = true;
		Configuration.ISRUN = false;
		serverDS.disconnect();
		serverDS = null;
	}

	private class ReceiveTask implements Runnable {

		public ReceiveTask() {
			super();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			read();
		}
	};

	private class WriteTask implements Runnable {

		public WriteTask() {
			super();
		}

		private void waiting() {
			synchronized (wait) {
				try {
					wait.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (!isStop) {

				if (mMsgQueue.size() <= 0) {
					waiting();
				}
				if (mMsgQueue.size() <= 0) {
					continue;
				}

				Message msg = getMsg();
				write(msg);
			}
		}
	}

	/**
	 * 第一节 fromPhone 的长度 第二节 fromPhone 的内容 第三节 toPhone 的长度 第四节 toPhone的内容 第五节
	 * msgType 的内容 最后是消息的json内容，包含 fromName，uid，msg
	 * 
	 * @param dp_receive
	 */
	@Override
	public void resolveReciveData(DatagramPacket dp_receive) {
		if (dp_receive.getLength() <= 0) {
			return;
		}
		byte[] data = dp_receive.getData();
		int index = 0;
		int msgType = data[index];
		index++;
		int fromPhoneNumLen = data[index];
		index++;
		StringBuilder fromPhone = new StringBuilder();
		for (int i = index; i < fromPhoneNumLen + index; i++) {
			int n = data[i];
			if (n < 0) {
				n = n << 0xff;
			}
			char c = (char) n;
			fromPhone.append(c);
		}
		index += fromPhoneNumLen;
		int toPhoneNumLen = data[index];
		index++;
		StringBuilder toPhone = new StringBuilder();
		for (int i = index; i < index + toPhoneNumLen; i++) {
			int n = data[i];
			char c = (char) n;
			toPhone.append(c);
		}
		index += toPhoneNumLen;

		StringBuilder uid = new StringBuilder();
		int iUid = 0;
		int uidLen = data[index];
		index++;
		for (int i = index; i < index + uidLen; i++) {
			int n = data[i];
			char c = (char) n;
			uid.append(c);
		}
		iUid = Integer.valueOf(uid.toString());
		index += uidLen;

		switch (msgType) {
		case Configuration.MSGTYPE_LOGIN:
			DatagramSocket cSocket = null;
			try {
				cSocket = connManager.creatConn();
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			NetInfo info = new NetInfo(dp_receive.getAddress(),
					dp_receive.getPort(), cSocket.getLocalPort());
			CLIENTS.put(toPhone.toString(), info);
			Message msg = new Message();
			msg.setFromPhoneNo(fromPhone.toString());
			msg.setToPhoneNo(toPhone.toString());
			msg.setUid(iUid);
			msg.setMsg("" + cSocket.getLocalPort());
			msg.setType(Configuration.MSGTYPE_LOGIN);
			insertMsg(msg);
			notifyDataChange();
			break;
		case Configuration.MSGTYPE_LOGOUT:
			msg = new Message();
			msg.setFromPhoneNo(fromPhone.toString());
			msg.setToPhoneNo(toPhone.toString());
			msg.setUid(iUid);
			msg.setMsg("logOut ok");
			msg.setType(Configuration.MSGTYPE_LOGOUT);
			insertMsg(msg);
			notifyDataChange();
			CLIENTS.remove(toPhone);
			break;
		case Configuration.MSGTYPE_MSG:
			msg = new Message();
			msg.setFromPhoneNo(fromPhone.toString());
			msg.setToPhoneNo(toPhone.toString());
			msg.setUid(iUid);
			msg.setType(Configuration.MSGTYPE_MSG);
			StringBuilder msgStr = new StringBuilder();
			byte[] btStr = new byte[data.length - index];
			int de = 0;
			for (int i = index; i < data.length; i++) {
				btStr[de] = data[i];
				de++;
			}
			msgStr.append(Utils.dencodingCH(btStr));
			String fromName = null;
			StringBuilder msgContent = new StringBuilder();
			String[] msgArr = null;
			msgArr = msgStr.toString().split("\\[\\+t\\]");
			String time = msgArr[0];
			try {
				Utils.defaultDataFormat.parse("time");
			} catch (Exception e) {
				// TODO: handle exception
				time = null;
			}

			if (time == null)
				msgArr = msgStr.toString().split("\\[\\+m\\]");
			else
				msgArr = msgArr[1].split("\\[\\+m\\]");

			if (msgArr.length >= 2) {
				fromName = msgArr[0];
				for (int i = 1; i < msgArr.length; i++) {
					msgContent.append(msgArr[i]);
				}
			} else {
				fromName = "未知";
				msgContent.append(msgStr);
			}
			msg.setMsg(msgContent.toString());
			msg.setFromName(fromName);
			if (time == null)
				msg.setTime(Utils.defaultDataFormat.format(new Date()));
			else
				msg.setTime(time);

			insertMsg(msg);
			break;
		default:
			break;
		}
	}
}
