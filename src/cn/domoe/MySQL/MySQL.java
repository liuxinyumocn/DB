package cn.domoe.MySQL;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import cn.domoe.MySQL.Chain.Action;
import cn.domoe.MySQL.Chain.Chain;

public class MySQL {
	
	private String sql = "";
	
	private List<Chain> chains = new ArrayList<>();
	
	public MySQL(String sql){
		this.sql = sql.trim();
		this.init();
	}
	
	
	
	//获取当前语句的CURD行为
	public List<Chain> getChains(){
		return this.chains;
		
	}
	
	private void init(){
		try {
			Queue<Chain> queue = new LinkedList<>();
			Chain c = new Action(this.sql);
			queue.add(c);
			while(true) {
				Chain next = queue.poll();
				if(next == null)
					break;
				chains.add(next);
				Chain[] cs = next.nextChain();
				if(cs == null)
					continue;
				for(int i = 0;i<cs.length;i++){
					queue.add(cs[i]);
				}
			}
		}catch(Exception e) {
			//e.printStackTrace();
		}
		for(int i = 0;i<this.chains.size();i++) {
			this.chains.get(i).print();
		}
	}
	
	public static void main(String[] args) {
		String[] sqls = new String[] {
			"SELECT * FROM users1 WHERE username='wc' AND password='123'",
			"INSERT INTO Persons ('aaaa','bbbbb') VALUES ('Gates', 'Bill', 'Xuanwumen 10', 'Beijing')",
			"DELETE FROM Person WHERE LastName = 'Wilson'",
			"UPDATE Person SET Address = 'Zhongshan 23', City = 'Nanjing' WHERE LastName = 'Wilson'"
		};
		for(int i = 0 ; i< sqls.length;i++) {
			System.out.println("正在解析的SQL语句为："+sqls[i]);
			new MySQL(sqls[i]);
			System.out.println("----------> END <------------");
		}
	}
	
}
