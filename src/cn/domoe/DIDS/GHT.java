package cn.domoe.DIDS;

import java.util.ArrayList;
import java.util.List;

public class GHT {
	private static List<Transaction> lib = new ArrayList<>();
	
	public static void add(Transaction t) {
		lib.add(t);
	}

	public static void print(){
		//可信赖的点集合为：
		System.out.println("可信赖的点集合为：");
		for(int i = 0;i<lib.size();i++){
			System.out.println("Point:("+lib.get(i).x+","+lib.get(i).y+")");
		}
	}
}
