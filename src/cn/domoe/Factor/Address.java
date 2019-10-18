package cn.domoe.Factor;

import java.util.ArrayList;
import java.util.List;

import cn.domoe.DataMining.Session;

//分配 0x
public class Address {

	//判断请求源是内网还是外网 具体的值
	public static Factor[] Parse(Session session) {
		List<Factor> arr = new ArrayList<>();
		
		//01 IP地址
		arr.add(new Factor(1,session.ip));
		
		Factor[] fs = new Factor[arr.size()];
		for(int i = 0;i<arr.size();i++) {
			fs[i] = arr.get(i);
		}
		return fs;
	}
	
}
