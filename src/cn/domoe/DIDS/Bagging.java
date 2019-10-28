package cn.domoe.DIDS;

import java.util.List;

public class Bagging {
	
	private Item[][] bag = null;
	List<Item> eLList = null;
	Item[] proFu = null;
	public Bagging(List<Item> eLList, Item[] proFu){
		this.proFu = proFu;
		this.eLList = eLList;
		//有放回的随机抽样
		bag = new Item[10][];
	}
	
	//有放回的随机抽样
	private Item[] gen(){
		
		return null;
	}
	
	
}
