package cn.domoe.MySQL.Chain;

import java.util.ArrayList;
import java.util.List;

public class UpdateValue extends Chain{

	private int start = 0;
	private List<String> key = null;
	private List<String> value = null;
	private int endindex = 0;
	
	public UpdateValue(String sql, int i) throws Exception {
		super(sql,"updatevalue");
		this.key = new ArrayList<>();
		this.value = new ArrayList<>();
		start = i;
		this.parse();
	}

	private void parse() throws Exception {
		//寻找Key与Value
		int whereindex = this.sql.indexOf("WHERE",this.start);
		if(whereindex == -1)
			throw new Exception("未找到UpdateValue字段");
		this.endindex = whereindex;
		String value = this.sql.substring(this.start, whereindex).trim();
		String[] kv = value.split(",");
		for(int i = 0;i<kv.length;i++) {
			//等号左边
			int d = kv[i].indexOf("=");
			if(d == -1)
				throw new Exception("未找到UpdateValue字段");
			this.key.add(kv[i].substring(0,d).trim());
			this.value.add(kv[i].substring(d+1).trim());
		}
	}
	
	@Override
	public void print() {
		System.out.println("该语句的Update字段为");
		for(int i = 0;i<this.key.size();i++) {
			System.out.println(this.key.get(i) + ":" + this.value.get(i));
		}
		
	}

	@Override
	public Chain[] nextChain() throws Exception {
		Chain[] chains = new Chain[1];
		chains[0] = new Where(this.sql,this.endindex);
		return chains;
	}

	@Override
	public Object getValue() {
		List<String>[] re = new ArrayList[2];
		re[0] = this.key;
		re[1] = this.value;
		return re;
	}

}
