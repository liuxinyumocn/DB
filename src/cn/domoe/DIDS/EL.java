package cn.domoe.DIDS;

import java.util.List;

public class EL {

	public EL(List<Item> eLList, Item[] proFu) {

		//Bagging、朴素贝叶斯
		Object res = new Bagging(eLList,proFu);
		
	}


}
