package cn.domoe.dbproxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import cn.domoe.ThreadPool.ThreadPool;

/*
 * 一种的数据库连接配置
 * */
public class Server {
	private ServerSocket ss = null;
	private String ip = "";
	private int port = 0;
	
	private DBFile dbfile = null;
	
	public Server(String ip ,int port,int vport) throws IOException {
		dbfile = new DBFile(".");
		this.ip = ip;
		this.port = port;
		ss = new ServerSocket(vport);
		ThreadPool.submit(new ServerThread());
	}
	
	class ServerThread implements Runnable{
		@Override
		public void run() {
			while(true) {
				try {
					Socket s = ss.accept();
					ThreadPool.submit(new AcceptThread(s));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class AcceptThread implements Runnable{
		Socket s = null;
		public AcceptThread(Socket s) {
			this.s = s;
		}
		
		@Override
		public void run() {
			try {
				new Conn(s,ip,port,dbfile);
			} catch (IOException e) {
				System.out.println("一次客户连接失效");
			}
		}
		
	}
}
