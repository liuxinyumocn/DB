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
			this.data[i].t = dataset[i];
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
		// TODO Auto-generated method stub
		return 0;
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
//				if(update)
//					if(q.rd < p.rd)
//						q.rd = p.rd;
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
	
}
