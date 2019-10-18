package cn.domoe.dbproxy;

import java.io.UnsupportedEncodingException;

public class Msg {
	private byte[] code = null;
	private byte[] data = null;
	private byte origin = 1;
	private Conn conn = null;
	private boolean issql = false;
	private String sql = "";
	
	public Msg(byte[] code, byte[] data, byte origin,Conn conn) {
		this.code = code;
		this.data = data;
		this.origin = origin;
		this.conn = conn;
		
		this.issql = false;
		this.sql = "";
		
		//解释标识符
		this.parseHeader();
	}
	
	public boolean isSQL(){
		return this.issql;
	}
	
	public String getSQL(){
		return this.sql;
	}
	
	private void parseHeader() {
		int res = Msg.parseCode(this.code);
		if(res == 0) {
			this.conn.close();
		}else if(res == 1 || res == 2) {
			//发送一个SQL语句
			String msg="编码失败";
			try {
				msg = new String(this.data,0,this.data.length,"UTF-8");
				this.issql = true;
				this.sql= msg;
			} catch (UnsupportedEncodingException e) {
			}
			System.out.println(msg);
			
		}
	}

	private static int parseCode(byte[] code) {
		final byte[][] map = new byte[][]{	//头部位
			new byte[]{0,0,0,1},		//结束通信
			new byte[]{0,0,0,22},		//SQL特征标识
			new byte[]{0,0,0,3},		//SQL特征标识
		};
		for(int i = 0;i<map.length;i++) {
			int same = 0;
			for(int j = 0;j<map[i].length;j++) {
				if(code[j+1] == map[i][j]) 
					same++;
				else
					break;
			}
			if(same >= code.length-1) {
				//匹配成功
				return i;
			}
		}
		return -1;
	}
	
	public void print(){
		System.out.println("---------------------");
		String str = "";

		for(int i = 0 ;i<this.code.length;i++){
			str+= this.code[i] + " ";
		}
		str += "*";
		for(int i = 0 ;i<this.data.length;i++){
			str+= this.data[i] + " ";
		}
		
		String msg="编码失败";
		try {
			msg = new String(this.data,0,this.data.length,"UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		String originMsg = "客户机消息";
		if((int)this.origin == 2)
			originMsg = "数据库消息";
		System.out.println(originMsg);
		System.out.println(str);
		System.out.println(msg);
		System.out.println("---------------------");
	}
	
}
