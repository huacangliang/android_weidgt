package com.ks.im;

import java.net.InetAddress;

public class NetInfo {
	private InetAddress addr;
	private int port;
	private int localPort;

	public NetInfo(InetAddress addr, int port, int localPort) {
		super();
		this.addr = addr;
		this.port = port;
		this.localPort = localPort;
	}

	public InetAddress getAddr() {
		return addr;
	}

	public void setAddr(InetAddress addr) {
		this.addr = addr;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getLocalPort() {
		return localPort;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

}
