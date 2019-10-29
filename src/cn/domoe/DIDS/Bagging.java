package cn.domoe.DIDS;

import java.util.List;

public class Bagging {
	
	private int N = 20;
	
	private Item[][] bag = null;
	List<Item> eLList = null;
	Item[] proFu = null;
	public Bagging(List<Item> eLList, Item[] proFu){
		this.proFu = proFu;
		this.eLList = eLList;
		//有放回的随机抽样
		bag = new Item[N][];
		for(int i =0;i<N;i++){
			bag[i] = this.gen();
		}
		//生成N组
		NaiveBayes[] NBs = new NaiveBayes[N];
		//朴素贝叶斯分类
		//初始化投票数据
		for(int i = 0;i<this.eLList.size();i++){
			this.eLList.get(i).soc = 0;
		}
		for(int i = 0;i<N;i++){
			NBs[i] = new NaiveBayes(bag[i],proFu);
			Item[] res = NBs[i].getResult();
			//每次分类完对各个元素进行投票
			Item[] fi = new Item[res.length];
			int fiindex = 0;
			for(int j =0;j<res.length;j++){
				//投票时元素不可重复
				boolean c = false;
				for(int m =0;m<fiindex;m++){
					if(fi[m] == res[j]){
						c = true;
						break;
					}
				}
				if(!c){
					res[j].soc += (res[j].P > 0.5 ? 1 : -1);
				}
			}
		}
		//完成基于Bagging 朴素贝叶斯的分类
		//soc > 0的为可信赖的
	}
	
	//有放回的随机抽样
	private Item[] gen(){
		//抽样次数为额LList.size
		int len = eLList.size();
		Item[] set = new Item[len];
		for(int i =0 ;i<len;i++){
			double random = Math.random();
			if(random == 1){
				set[i] = eLList.get(len-1);
			}else{
				set[i] = eLList.get((int)(random*len));
			}
		}
		return set;
	}

	public void print() {
		//打印分数
		for(int i = 0;i<this.eLList.size();i++){
			Item item = this.eLList.get(i);
			System.out.println("Bagging ("+item.t.x+","+item.t.y+") 投票结果："+item.soc);
		}
	}
	
	
}
