package cn.domoe.MySQL.Chain;

import cn.domoe.MySQL.Chain.Condition.AND;
import cn.domoe.MySQL.Chain.Condition.Condition;
import cn.domoe.MySQL.Chain.Condition.IF;
import cn.domoe.MySQL.Chain.Condition.OR;

public class Where extends Chain {

	/*
	 * 寻找where内结构
	 * 这里按照简单的做
	 * 从i作为字符串的起点，寻找以 order、limit、分号、或字符串末尾的关键词
	 * 
	 * 先简单的判断3种情况
	 * 第一种  A=a
	 * 第二种 A=a and B=b
	 * 第三种 A=a or B=b
	 * */
	private int start = 0;
	private Condition condition = null;
	public Where(String sql, int i) throws Exception {
		super(sql, "where");
		this.start = i;
		this.init();
	}
	
	private void init() throws Exception {
		//从i起找where关键字
		int index = this.sql.indexOf("WHERE", this.start);
		if(index == -1)
			throw new Exception("找不到WHERE条件");
		//然后寻找区间字符串
		int end = 0 ;
		String[] keys = new String[] {
				";","ORDER","LIMIT"
		};
		for(int i = 0;i<keys.length;i++) {
			end = this.sql.indexOf(keys[i],index);
			if(end != -1) {
				break; //找到
			}
		}
		if(end == 0) {
			throw new Exception("找不到WHERE条件2");
		}
		if(end == -1)
			end = this.sql.length();
		String where = this.sql.substring(index+5, end);
		//判断WHERE中是否存在 AND 或者 OR 关键词
		int and = 0,or = 0;
		and = where.indexOf("AND");
		Condition cd = null;
		if(and != -1) {
			cd = new AND(where);
		}else {
			or = where.indexOf("OR");
			if(or != -1) {
				cd = new OR(where);
			}else {
				cd = new IF(where);
			}
		}
		this.condition = cd; 
	}

	@Override
	public void print() {
		System.out.println("该语句的WHERE为：");
		this.condition.print();
	}

	@Override
	public Chain[] nextChain() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return this.condition;
	}

}
