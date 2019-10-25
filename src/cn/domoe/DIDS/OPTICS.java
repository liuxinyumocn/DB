package cn.domoe.DIDS;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class OPTICS {
	private Queue<Item> result = new LinkedList<>();
	private Queue<Item> order = new LinkedList<>();
	//private Transaction[] data = null;
	private Item[] data = null;
	private double r = 4;
	private int MinPts = 16;
	
	public Item[] getResult(){
		Item[] res = this.queueToArrayUnclear(this.result);
		return res;
	}
	
	public OPTICS(Transaction[] dataset,double r,int MinPts) {
		this.data = new Item[dataset.length];
		for(int i =0;i<data.length;i++){
			this.data[i] = new Item(dataset[i]);
		}
		this.r = r;
		this.MinPts = MinPts;
		for(int i =0;i<data.length;i++){
			Item[] result = queueToArrayUnclear(this.result);
			boolean c = false;
			for(int j = 0;j<result.length;j++){
				if(result[j] == data[i]){
					c = true;
					break;
				}
			}
			if(c){
				continue;
			}
			if(this.isCore(data[i],r,MinPts)){
				this.pushResult(data[i]);
				List<Item> list = this.getPoint(data[i],r);
				this.pushOrder(list);
				this.shiftOrder();
			}
		}
		
	}

	//将队列打印成数组，且不影响队列
	private Item[] queueToArrayUnclear(Queue<Item> queue){
		Item[] list = new Item[queue.size()];
		int index = 0;
		while(true) {
			Item i = queue.poll();
			if(i != null) {
				list[index++] = i;
			}else {
				break;
			}
		}
		for(int i = 0;i<list.length;i++) {
			queue.add(list[i]);
		}
		return list;
	}
		
		//将队列打印成数组，原队列不可用
	private Item[] queueToArray(Queue<Item> queue) {
		Item[] list = new Item[queue.size()];
		int index = 0;
		while(true) {
			Item i = queue.poll();
			if(i != null) {
				list[index++] = i;
			}else {
				break;
			}
		}
		return list;
	}
	
	private void shiftOrder(){
		Item p = order.poll();
		if(p == null){
			return;
		}
		if(this.isCore(p, r, MinPts)){
			pushResult(p);
			List<Item> list = getPoint(p,r);
			pushOrder(list);
		}
		shiftOrder();
	}
	
	
	private boolean isCore(Item item, double r, int MinPts) {
		int num = 0;
		double[] diss = new double[MinPts];
		for(int i = 0;i<diss.length;i++){
			diss[i] = 99999;
		}
		for(int i = 0;i<this.data.length;i++){
			double dis = this.dis(item,this.data[i]);
			if(dis <= r){
				num++;
				int index = 0;
				for(int j = 0;j<diss.length;j++){
					if(diss[j] > diss[index]){
						index = j;
					}
				}
				if(diss[index] > dis){
					diss[index] = dis;
				}
			}
		}
		if(num >= MinPts){
			double max = 0;
			for(int i = 0;i<diss.length;i++){
				if(max < diss[i])
					max = diss[i];
			}
			item.cd = max;
			return true;
		}
		return false;
	}

	private double dis(Item item, Item item2) {
		return Math.sqrt((item.t.x - item2.t.x)*(item.t.x - item2.t.x) + (item.t.y - item2.t.y)*(item.t.y - item2.t.y));
	}
	
	private List<Item> getPoint(Item p ,double r){
		List<Item> list = new ArrayList<>();
		Item[] result = this.queueToArrayUnclear(this.result);
		for(int i = 0;i<this.data.length;i++){
			Item q = this.data[i];
			boolean c = false;
			for(int j = 0;j<result.length;j++){
				if(result[j] == q){
					c = true;
					break;
				}
			}
			if(c)
				continue;
			double dis = this.dis(q, p);
			if(dis <= r && q!= p){
				boolean update = false;
				if(q.rd > dis){
					update = true;
					q.rd = dis;
				}
				if(update)
					if(q.rd < p.cd)
						q.rd = p.cd;
				list.add(q);
			}
		}
		return list;
	}
	//放入Order中并重新排序 放入的点不能已经存在于Result中 也不能已经存在于Order中
	private void pushOrder(List<Item> list){

		Item[] order = this.queueToArrayUnclear(this.order);
		Item[] result = this.queueToArrayUnclear(this.result);
		
		for(int i = 0;i<list.size();i++){
			boolean c = false;
			Item item = list.get(i);
			for(int j =0;j<result.length;j++) {
				if(result[j] == item) {
					c = true;
					break;
				}
			}
			if(c)
				continue;
			for(int j =0;j<order.length;j++){
				if(order[j] == item){
					c = true;
					if(order[j].rd > item.rd)
						order[j].rd = item.rd;
					break;
				}
			}
			if(!c) 
				this.order.add(item);			
		}
		//重新排序
		order =  this.queueToArray(this.order);
		for(int i = 0;i<order.length;i++) {
			for(int j = i+1;j<order.length;j++) {
				if(order[i].rd > order[j].rd) {
					Item a = order[i];
					order[i] = order[j];
					order[j] = a;
				}		
			}
		}
		//放回queue
		for(int i = 0;i<order.length;i++) {
			this.order.add(order[i]);
		}
	}

	//去重复加入Result
	private void pushResult(Item p) {
		Item[] data = this.queueToArrayUnclear(this.result);
		for(int i =0;i<data.length;i++) {
			if(data[i] == p) {
				return;
			}
		}
		this.result.add(p);
	}
	
	/*
	 * 计算两个元素之间的可达距离
	 * */
	private double calRD(Item k,Item q,double r,int MinPts){
		double cdq = q.cd;
		double dis = this.dis(q, k);
		//System.out.println("RD: "+dis + " | " + q.cd + " | " + k.cd);
		if(cdq > dis)
			return cdq;
		return dis;
	}
	/*
	 * 计算元素本地可达距离
	 * */
	private double calLRD(Item k,Item[] data,int MinPts){
		Item[] Nk = this.N(k, data, MinPts);
		//System.out.println("| "+k.t.x+","+k.t.y+" | "+Nk.length+" | "+ Nk[0].t.x+","+Nk[0].t.y);
		double fz = Nk.length;
		double fm = 0;
		for(int i=0;i<Nk.length;i++) {
			fm += this.calRD(k, Nk[i],9999,MinPts);
		}
		//System.out.println("fm:"+fm);
		return fz/fm;
	}
	
	/*
	 * 计算OF
	 * */
	public double calOF(Item k,Item[] data,int MinPts){
		Item[] Nk = this.N(k, data, MinPts);
		
		int fm = Nk.length;
		double fz = 0;
		
		double lrdk = this.calLRD(k, data, MinPts);
		//System.out.println("LRD: "+lrdk);
		for(int i =0;i<Nk.length;i++) {
			double lrdo = this.calLRD(Nk[i], data, MinPts);
			//System.out.println("LRD2: "+lrdo);
			fz += lrdo/lrdk;
		}
		return fz/fm;
	}
	
	/*
	 * 获取一个集合，该集合为对象k相对于其他元素由近到远的m个元素集合，返回值长度 <= m 不含k
	 * NminPts(k)
	 * */
	private Item[] N(Item k,Item[] data,int m){
		int index = 0;
		Item[] diss = new Item[data.length - 1  > m - 1 ? m - 1 : data.length-1];
		for(int i = 0;i<data.length;i++){
			if(data[i] == k)
				continue;
			data[i].dis = this.dis(k, data[i]);
			if(index < diss.length)
				diss[index++] = data[i];
			else{
				if(diss[index-1].dis > data[i].dis){
					diss[index-1] = data[i];
				}
			}
			//排序diss
			for(int j =0;j<index;j++ ){
				for(int t = j+1;t<index;t++){
					if(diss[j].dis > diss[t].dis){
						Item a = diss[j];
						diss[j] = diss[t];
						diss[t] = a;
					}
				}
			}
		}
		return diss;
	}
	
}
