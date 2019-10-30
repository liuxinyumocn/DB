package cn.domoe.DIDS;

import java.util.List;

public class EL {

	private Bagging bagging = null;
	public EL(List<Item> eLList, Item[] proFu) {

		//Bagging、朴素贝叶斯
		bagging = new Bagging(eLList,proFu);
		List<Item> baggingResult = bagging.getResult();
		//bagging.print();
	
		//经过N个分类器 元素凡是属于任何一个分类器给出的可信赖结果，都认定为有效
		for(int i =0;i<eLList.size();i++){
			boolean find = false;
			//Bagging
			for(int j =0;j<baggingResult.size();j++){
				if(baggingResult.get(j) == eLList.get(i)){
					find = true;
					break;
				}
			}
			if(find){
				GHT.add(eLList.get(i).t);
				continue;
			}
		}
	}


}
