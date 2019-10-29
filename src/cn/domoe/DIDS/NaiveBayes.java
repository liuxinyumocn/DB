package cn.domoe.DIDS;

public class NaiveBayes {

	double r = 3;	//兼容半径
	private Item[] data = null;
	private Item[] proFu = null;
	//朴素贝叶斯 弱分类器
	public NaiveBayes(Item[] items, Item[] proFu ,double r) {
		this.r = r;
		this.data = items;
		this.proFu = proFu;
		this.init();
	}

	public Item[] getResult(){
		return this.data;
	}
	
	public NaiveBayes(Item[] items, Item[] proFu) {
		this.data = items;
		this.proFu = proFu;
		this.init();
	}
	
	private void init(){
		this.checkT();
		//求出每个点的P(T=1|XY)的概率
		for(int i = 0;i<this.data.length;i++){
			double p = this.P(this.data[i]);
			this.data[i].P = p;
		}
	}
	
	//根据ProFu给出每一个点是否是T=Genuine
	private void checkT(){
		double l = Math.sqrt(r * r);
		int Tnum = 0;
		for(int i = 0;i<this.data.length;i++){
			this.data[i].P = 0.5;
			//判断一点是否是属于容错范围内的用户配置文件
			boolean c = false;
			for(int j=0;j<this.proFu.length;j++){
				double dis = this.dis(this.data[i], this.proFu[j]);
				if(dis <= l){
					c = true;
					this.data[i].T = 1;
					Tnum++;
					break;
				}
			}
			if(!c){
				this.data[i].T = 0;
			}
		}
		this.Tnum = Tnum;
	}
	private int Tnum = 0;
	
	private double dis(Item item, Item item2) {
		return Math.sqrt((item.t.x - item2.t.x)*(item.t.x - item2.t.x) + (item.t.y - item2.t.y)*(item.t.y - item2.t.y));
	}
	
	/*
	 P(T=Genuine|X,Y) = P(X|T=G) * P(Y|T=G) * P(T=G) / ( P(X)*P(Y) )
	 求出T=Genuinue在点坐标XY的概率
	 * */
	private double P(Item item){
		double p1 = P1(item) * P2(item) * (this.Tnum*1.0/this.data.length);
		int xnum = 0,ynum = 0;
		for(int i = 0;i<this.data.length;i++){
			if(Math.abs(this.data[i].t.x - item.t.x) <= this.r){
				xnum++;
			}
			if(Math.abs(this.data[i].t.y - item.t.y) <= this.r){
				ynum++;
			}
		}
		double p2 = (xnum*1.0/this.data.length)*(ynum*1.0/this.data.length);
		return p1/p2;
	}
	
	//P(item.X|T=1)的概率
	private double P1(Item item){
		int num = 0;
		for(int i = 0;i<this.data.length;i++){
			double l = Math.abs(item.t.x - this.data[i].t.x);
			if(l <= this.r)
				num++;
		}
		if(this.Tnum == 0)
			return 0;
		return num*1.0/this.Tnum;
	}
	//P(item.Y|T=1)的概率
	private double P2(Item item){
		int num = 0;
		for(int i = 0;i<this.data.length;i++){
			double l = Math.abs(item.t.y - this.data[i].t.y);
			if(l <= this.r)
				num++;
		}
		if(this.Tnum == 0)
			return 0;
		return num/this.Tnum;
	}
}
