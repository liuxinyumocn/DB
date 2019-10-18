package cn.domoe.Factor;

import java.util.ArrayList;
import java.util.List;

import cn.domoe.DataMining.Session;

public class Factor {
	
	public int key = 0; //影响因子标识
	public String valueStr = null; //对应值
	public int valueInt = 0;
	
	public Factor(int key, String value) {
		this.key = key;
		this.valueStr = value;
	}
	
	public Factor(int key, int value){
		this.key = key;
		this.valueStr = Integer.toString(value);
	}

	public static List<Factor> parse(Session session) {
		List<Factor> list = new ArrayList<>();
		Factor[] fs = null;
		fs = Time.Parse(session);	//1x 2x
		push(list,fs);
		fs = Address.Parse(session); //0x
		push(list,fs);
		fs = SQLsOrder.Parse(session); //3x
		push(list,fs);
		fs = Chain.Parse(session);	//4x
		push(list,fs);
		
		return list;
	}
	
	private static void push(List<Factor> list,Factor[] fs) {
		for(int i = 0;i<fs.length;i++) {
			list.add(fs[i]);
		}
	}

	public void print() {
		System.out.println(this.key+"->"+this.valueStr);
	}
}
