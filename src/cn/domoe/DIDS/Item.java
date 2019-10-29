package cn.domoe.DIDS;

public class Item {
	public Item(Transaction transaction) {
		this.t = transaction;
	}
	public Transaction t = null;
	public double cd = 99999;
	public double rd = 99999;
	public double dis = 99999;
	
	//Bagging 分类器
	public int T = 0;	//未知行为
	public double P = 0.5; //概率
	public int soc = 0;
}
