package cn.domoe.dbproxy;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Queue;

public class DBData {

	public Queue<String> csqlQueue = null;
	public long starttimestamp = 0;
	public long endtimestamp = 0;
	public String ip = "";
	
	public DBData(Queue<String> csqlQueue,long starttimestamp,long endtimestamp,String ip){
		this.csqlQueue = csqlQueue;
		this.starttimestamp = starttimestamp;
		this.endtimestamp = endtimestamp;
		this.ip = ip;
	}
	
	/*
	 * 定义输出的格式
	 * 头部由9个字节构成
	 * 第一个字节表达行数据类型
	 * 后8个字节表达跟随数据长度
	 * 
	 * 头部数据定义：
	 * 1 消息起始 跟随IP地址
	 * 2 消息发生时间
	 * 3 消息结束时间
	 * 4 消息队列中的一条SQL语句
	 * */
	public void output(FileOutputStream fout) throws IOException {
		byte[] header = new byte[5];
		//IP
		byte[] ip = this.ip.getBytes();
		DBData.writeData(1, ip,fout);	//写入数据
		//起始时间戳
		byte[] starttimestamp = DBData.long2Bytes(this.starttimestamp);
		DBData.writeData(2, starttimestamp, fout);
		//终止时间戳
		byte[] endtimestamp = DBData.long2Bytes(this.endtimestamp);
		DBData.writeData(3, endtimestamp, fout);
		//正文
		while(true) {
			String sql = this.csqlQueue.poll();
			if(sql == null)
				break;
			byte[] sqlbyte = sql.getBytes();
			DBData.writeData(4, sqlbyte, fout);
		}
		//写入完成
		byte[] end = new byte[]{0,0,0,0,0,0,0,0,0};
		fout.write(end);
	}
	
	public static void writeData(int type,byte[] data, FileOutputStream fout) throws IOException {
		fout.write(type);
		byte[] len = DBData.long2Bytes(data.length);
		fout.write(len);
		fout.write(data);
	}
	
	/*
	 *  long 与  byte 之间转化 
	 * */
	public static byte[] long2Bytes(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    public static long bytes2Long(byte[] byteNum) {
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (byteNum[ix] & 0xff);
        }
        return num;
    }

	public void print() {
		System.out.println("-------------------");
		System.out.println("IP:"+this.ip);
		System.out.println("开始时间"+this.starttimestamp);
		System.out.println("结束时间"+this.endtimestamp);
		int num = 1;
		String[] arr = null;
		arr = this.csqlQueue.toArray(new String[0]);
		for(int i = 0;i<arr.length;i++) {
			System.out.println("SQL"+(i+1)+":"+arr[i]);
		}
	}
}
