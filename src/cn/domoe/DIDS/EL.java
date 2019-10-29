package cn.domoe.DIDS;

import java.util.List;

public class EL {

	private Bagging bagging = null;
	public EL(List<Item> eLList, Item[] proFu) {

		//Bagging、朴素贝叶斯
		bagging = new Bagging(eLList,proFu);
		
		bagging.print();
	}


}
