package com.ks.im;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;

import com.alibaba.fastjson.JSON;

public class Test {
	static int count = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// IMPlugin imService = new IMDefaultExcute();
		// try {
		// imService.startUp();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		for (int i = 0; i < 1; i++) {
			Client cli = new Test.Client();
			try {
				cli.login();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static class Client {
		DatagramPacket dp_receive;
		DatagramPacket dp_send;
		DatagramSocket ds = null;
		InetAddress loc;
		boolean isRun;
		int sPort = 0;

		public void login() throws IOException {
			Message msg = new Message();
			msg.setFromName("dengxx");
			msg.setFromPhoneNo("1575*******");
			msg.setToPhoneNo("15757*******");
			msg.setUid(10000);
			msg.setType(Configuration.MSGTYPE_LOGIN);
			byte[] sendData = packetData(msg);

			byte[] buf = new byte[Configuration.BUFFERLENGTH];

			loc = InetAddress.getByName(Configuration.SERVERIP);
			dp_send = new DatagramPacket(sendData, sendData.length, loc,
					Configuration.SERVERPORT);
			dp_receive = new DatagramPacket(buf, Configuration.BUFFERLENGTH);
			int cp = 4550;
			while (true) {
				try {
					ds = new DatagramSocket(cp, InetAddress.getLocalHost());
					break;
				} catch (Exception e) {
					// TODO: handle exception
					cp++;
				}
			}
			isRun = true;
			ds.setSoTimeout(5000);
			new Thread(reTask).start();
			ds.send(dp_send);
		}

		Runnable reTask = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (isRun) {
					try {
						ds.receive(dp_receive);
						if (!dp_receive.getAddress().equals(loc)) {
							throw new IOException(
									"Received packet from an umknown source");
						}
						DatagramPacket t_receive = new DatagramPacket(
								dp_receive.getData(), dp_receive.getLength(),
								dp_receive.getAddress(), dp_receive.getPort());
						dp_receive.setLength(Configuration.BUFFERLENGTH);
						resolveReciveData(t_receive);
					} catch (InterruptedIOException e) {
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};

		public void sendMsg(DatagramSocket ds, int port) throws IOException {
			Message msg = new Message();
			msg.setFromName("dengxx");
			msg.setFromPhoneNo("15757*******");
			msg.setToPhoneNo("157571*******");
			msg.setUid(10000);
			msg.setMsg("齉" + count);
			msg.setType(Configuration.MSGTYPE_MSG);
			byte[] sendData = packetData(msg);
			count++;
			DatagramPacket dp_send = new DatagramPacket(sendData,
					sendData.length, loc, port);
			ds.send(dp_send);
		}

		public void logOut(DatagramSocket ds, int port) throws IOException {
			Message msg = new Message();
			msg.setFromName("dengxx");
			msg.setFromPhoneNo("15757*******");
			msg.setToPhoneNo("157571*******");
			msg.setUid(10000);
			msg.setType(Configuration.MSGTYPE_LOGOUT);
			byte[] sendData = packetData(msg);
			DatagramPacket dp_send = new DatagramPacket(sendData,
					sendData.length, loc, port);
			ds.send(dp_send);
		}

		public void logOutOk(DatagramSocket ds, int port) throws IOException {
			byte[] sendData = new byte[] { -1 };
			DatagramPacket dp_send = new DatagramPacket(sendData,
					sendData.length, loc, port);
			ds.send(dp_send);
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
				String msgStr = msg.getFromName() + "[+m]" + msg.getMsg();
				byte[] mgs = null;
				mgs = Utils.packetCH(msgStr);
				sendData = Utils.connArr(sendData, mgs);
			}
			return sendData;
		}

		private void resolveReciveData(DatagramPacket dp_receive) {
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
				System.out.println("我登陆成功了");
				byte[] btStr = new byte[data.length - index];
				int de = 0;
				for (int i = index; i < data.length; i++) {
					btStr[de] = data[i];
					de++;
				}
				int port = 0;
				try {
					String portStr = new String(btStr,
							Configuration.DEFAULTENCODING);
					port = Integer.valueOf(portStr.trim());
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				sPort = port;
				reConnMsgServer();
				try {
					sendMsg(ds, port);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case Configuration.MSGTYPE_LOGOUT:
				System.out.println("我退出成功了");
				try {
					logOutOk(ds, sPort);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				isRun = false;
				break;
			case Configuration.MSGTYPE_MSG:
				Message msg = new Message();
				msg.setFromPhoneNo(fromPhone.toString());
				msg.setToPhoneNo(toPhone.toString());
				msg.setUid(iUid);
				StringBuilder msgStr = new StringBuilder();
				de = 0;
				byte[] dm = new byte[data.length - index];
				for (int i = index; i < data.length; i++) {
					dm[de] = data[i];
					de++;
				}
				msgStr.append(Utils.dencodingCH(dm));
				String fromName = null;
				StringBuilder msgContent = new StringBuilder();
				String[] msgArr = msgStr.toString().split("\\[\\+t\\]");
				String time = msgArr[0];
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
				msg.setFromName(fromName);
				msg.setMsg(msgContent.toString());
				msg.setTime(time);
				msg.setType(Configuration.MSGTYPE_MSG);
				String str = JSON.toJSONString(msg);
				System.out.println("我收到消息了,端口：" + dp_receive.getPort()
						+ "，消息内容:" + str);
				try {
					logOut(ds, sPort);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				break;
			}

		}

		private void reConnMsgServer() {
			Configuration.POOL.execute(reConn);
		}

		Runnable reConn = new Runnable() {
			public void run() {
				while (isRun) {
					// 检查心跳包，每隔60s检查一次
					byte[] sendData = new byte[] { 0 };
					DatagramPacket dp_send = new DatagramPacket(sendData,
							sendData.length, loc, sPort);
					try {
						ds.send(dp_send);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						Thread.sleep(6 * 10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		};
	}

}
