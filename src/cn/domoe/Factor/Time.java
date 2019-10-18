package cn.domoe.Factor;

import java.util.ArrayList;
import java.util.List;

import cn.domoe.DataMining.Session;

//从起始及结束时间的 每日的时间段、星期、几号角度  占用 1x  2x
public class Time {
	public static Factor[] Parse(Session session) {
		List<Factor> arr = new ArrayList<>();
		int am8 = 3600*8;
		long todayStart = (session.starttimestamp/1000-am8)%(3600*24);
		long todayEnd = (session.endtimestamp/1000-am8)%(3600*24);
		//起始时间的今日秒数 10
		arr.add(new Factor(10,Long.toString(todayStart)));
		//上下午  11  0是上午，1是下午
		int m = 0;
		if(todayStart > 3600*12)
			m = 1;
		arr.add(new Factor(11,Integer.toString(m)));
		//星期
		
		//日
		
		//结束时间的今日秒数 20
		arr.add(new Factor(20,Long.toString(todayEnd)));
		//上下午 
		m = 0;
		if(todayEnd > 3600*12)
			m = 1;
		arr.add(new Factor(21,Integer.toString(m)));
		//星期
	
		//日
		
		Factor[] fs = new Factor[arr.size()];
		for(int i = 0;i<arr.size();i++) {
			fs[i] = arr.get(i);
		}
		return fs;
	}
	
	private static Factor time() {
		
		return null;
	}
}
