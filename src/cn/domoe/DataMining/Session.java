package cn.domoe.DataMining;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import cn.domoe.Factor.Factor;
import cn.domoe.MySQL.MySQL;
import cn.domoe.dbproxy.DBData;

/*
 * 会话对象
 * 
 * */
public class Session {

	public long starttimestamp = 0;
	public long endtimestamp = 0;
	public String ip = "127.0.0.1";
	
	public List<MySQL> sql = null;
	
	public List<Factor> factor = null;
	
	public Session(){
		this.sql = new ArrayList<>();
		this.factor = new ArrayList<>();
	}
	
	
	public Session(DBData dbdata) {
		this.sql = new ArrayList<>();
		this.factor = new ArrayList<>();

		this.parse(dbdata);
	}
	
	private void parse(DBData dbdata) {
		this.endtimestamp = dbdata.endtimestamp;
		this.starttimestamp = dbdata.starttimestamp;
		this.ip = dbdata.ip;
		Queue<String> q = dbdata.csqlQueue;
		while(true) {
			String sql = q.poll();
			if(sql == null)
				break;
			try{
				MySQL mysql = new MySQL(sql);
				this.sql.add(mysql);
			}catch(Exception e) {
				
			}
		}
		
		this.parseFactor();
	}
	
	//开始解析影响因子
	private void parseFactor() {
		this.factor = Factor.parse(this);
	}


	public void print() {
		System.out.println("打印所有影响因子");
		if(this.factor != null)
			for(int i = 0;i < this.factor.size();i++){
				this.factor.get(i).print();
			}	
	}
}
