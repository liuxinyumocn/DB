package cn.domoe.DataMining;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import cn.domoe.dbproxy.DBData;

public class DBReader {

	private FileInputStream fin = null;
	private String path = "";
	public DBReader(String path) throws IOException {
		this.path = path;
		this.restart();
	}
	
	//重新创建输入流
	public void restart() throws IOException {
		if(fin != null)
			fin.close();
		fin = new FileInputStream(path);
	}
	
	/*
	 * 读取下一条 DBData
	 * 1 消息起始 跟随IP地址
	 * 2 消息发生时间
	 * 3 消息结束时间
	 * 4 消息队列中的一条SQL语句
	 * */
	public DBData nextDBData() throws IOException {
		Queue<String> queue = new LinkedList<>();
		String ip = "";
		long starttimestamp = 0;
		long endtimestamp = 0;
		
		boolean isfind = false;
		while(true) {
			//先读取1,8个字节
			int type = fin.read();
			byte[] len = new byte[8];
			int lenReaderLen = 0;
			if(type == -1  || type == 0) {
				if(!isfind) {
					return null;	
				}
				//数据结尾
				lenReaderLen = fin.read(len);
				return new DBData(queue,starttimestamp,endtimestamp,ip);
			}
			isfind = true;
			lenReaderLen = fin.read(len);
			long length = DBData.bytes2Long(len);
			if(length <= 0) {
				continue;
			}
			byte[] data = new byte[(int)length];
			int dataReaderLen = fin.read(data);
			switch(type) {
				case 1://获取IP
					ip = new String(data,"utf-8");
					continue;
				case 2://消息发生时间
					starttimestamp = DBData.bytes2Long(data);
					continue;
				case 3://消息结束时间
					endtimestamp = DBData.bytes2Long(data);
					continue;
				case 4://SQL
					String sql = new String(data,"utf-8");
					queue.add(sql);
					continue;
			}
			
		}
	}
}
