package com.ks.im;

import java.net.DatagramPacket;

public interface IMPlugin {
	boolean startUp() throws Exception;

	boolean write(Message msg);

	void read();

	void stop();

	void resolveReciveData(DatagramPacket dp_receive);
}
