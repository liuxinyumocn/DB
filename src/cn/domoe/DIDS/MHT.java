package cn.domoe.DIDS;

import java.util.ArrayList;
import java.util.List;

public class MHT {
	private static List<Transaction> lib = new ArrayList<>();
	
	public static void add(Transaction t) {
		lib.add(t);
	}
}
