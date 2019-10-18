package cn.domoe.Factor;

import java.util.ArrayList;
import java.util.List;

import cn.domoe.DataMining.Session;
import cn.domoe.MySQL.MySQL;
import cn.domoe.MySQL.Chain.Chain;

//负责解析多条SQL语句之间的关系 3x
public class SQLsOrder {
	
	public static Factor[] Parse(Session session) {
		List<Factor> arr = new ArrayList<>();
		List<MySQL> sql = session.sql;
		
		//30 总条数
		arr.add(new Factor(30,sql.size()));
		
		//31 Action顺序，CURD使用0-4代表
		String curd ="";
		for(int i = 0;i<sql.size();i++){
			List<Chain> chains = sql.get(i).getChains();
			for(int j = 0;j<chains.size();j++){
				String el = chains.get(j).getEl();
				//System.out.println(el);
				boolean find = false;
				if(el.equals("action")){
					find = true;
					int action = (int)chains.get(j).getValue();
					curd += action;
					find = true;
					break;
				}
				if(find)
					break;
			}
		}
		if(!curd.equals(""))
			arr.add(new Factor(31,curd));
		
		Factor[] fs = new Factor[arr.size()];
		for(int i = 0;i<arr.size();i++) {
			fs[i] = arr.get(i);
		}
		return fs ;
	}
	
}
