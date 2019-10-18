package cn.domoe.dbproxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import cn.domoe.ThreadPool.ThreadPool;

/*
 * 一次数据库查询连接（虚拟链路）
 * */
public class Conn {
	private boolean debug = true;
	
	private Socket s = null;
	private Queue<Msg> msgQueue = new LinkedList<>();
	private Queue<String> csqlQueue = new LinkedList<>();
	private long starttimestamp = 0;	//请求开始时间
	private long endtimestamp = 0;		//请求结束时间
	private String ip = "";
	
	private InputStream clientInput = null;
	private OutputStream clientOutput = null;
	
	private Socket dbs = null;
	private InputStream dbInput = null;
	private OutputStream dbOutput = null;
	
	private boolean isclose = false;
	
	private DBFile dbfile = null;
	
	public Conn(Socket s,String ip ,int port,DBFile dbfile) throws IOException {
		this.s = s;	
		this.starttimestamp = System.currentTimeMillis();
		this.ip = s.getInetAddress().toString();
		this.dbfile = dbfile;
		if(debug){
			System.out.println("客户机IP地址："+ip);
			System.out.println("会话启动时间戳："+this.starttimestamp);
			System.out.println("新的连接");
		}
		
		//捕获客户机输入输入流及输入监听
		clientInput = s.getInputStream();
		clientOutput = s.getOutputStream();
		//开启输入流的监听
		ThreadPool.submit(new ClientInputThread());
		
		//建立数据库方向连接
		dbs = new Socket();
		dbs.connect(new InetSocketAddress(ip,port));
		dbInput = dbs.getInputStream();
		dbOutput = dbs.getOutputStream();
		//开启数据库方向输入流的监听
		ThreadPool.submit(new DbInputThread());
	}
	
	class ClientInputThread implements Runnable{

		@Override
		public void run() {
			byte[] buff = new byte[99999];
			int index= 0;
			int hasRead = 0;
			while(!isclose){
				try {
					if((hasRead = clientInput.read(buff))>0) {
						dbOutput.write(buff,0,hasRead);
						byte orgin = 1;
						Conn.this.parse(buff,hasRead,orgin);
					}
				} catch (IOException e) {
					isclose = true;
					if(debug)
					System.out.println("虚拟链路关闭");
				}
			}
		}
		
	}
	
	class DbInputThread implements Runnable{

		@Override
		public void run() {
			byte[] buff = new byte[99999];
			int index= 0;
			int hasRead = 0;
			while(!isclose){
				try {
					if((hasRead = dbInput.read(buff))>0) {
						clientOutput.write(buff,0,hasRead);
						byte origin = 2;
						Conn.this.parse(buff, hasRead, origin);
					}
				} catch (IOException e) {
					isclose = true;
					if(debug)
						System.out.println("虚拟链路关闭");
				}
			}
		}
		
	}
	
	/*
	 * 数据解析
	*origin 1 来自客户端 2 来自数据库
	 * 数据库数据头部分由5个字节构成
	 * 第一位代表数据位长度-1 后几位代表请求类型
	 * */
	
	private void parse(byte[] buff,int length,byte origin) {
		int index = 0;
		while(index < length) {
			int len = buff[index]&0x0FF;
			byte[] code = new byte[5];
			int i = 0;
			for(;i+index<length && i < code.length;i++) {
				code[i] = buff[i+index];
			}
			index += i;
			byte[] data = new byte[len -1];
			int j = 0;
			for(;j+index<length && j < data.length;j++) {
				data[j] = buff[j+index];
			}
			index += j;
			
			//创建一个Msg
			Msg msg = new Msg(code,data,origin,this);
			this.msgQueue.add(msg);
			if(msg.isSQL()) {
				String sql = msg.getSQL();
				this.csqlQueue.offer(sql);
			}
			//msg.print();
		}
	}
	
	/*
	 * 关闭当前连接
	 * */
	public void close() {
		this.endtimestamp = System.currentTimeMillis();
		if(debug){
			System.out.println("关闭一个会话连接");
			System.out.println("会话结束时间戳："+this.endtimestamp);
		}
		//结算消息
		DBData data = new DBData(this.csqlQueue,this.starttimestamp,this.endtimestamp,this.ip);
		dbfile.push(data);
		try {
			this.isclose = true;
			this.s.close();
		} catch (IOException e) {
			if(debug)
			System.out.println("关闭失败");
		}
		try {
			this.dbs.close();
		} catch (IOException e) {
			if(debug)
			System.out.println("关闭失败");
		}
	}
	
}
