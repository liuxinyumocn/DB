package cn.domoe.OPTICS;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class OPTICS2 {

	private Queue<double[]> result = new LinkedList<>();
	private Queue<double[]> order = new LinkedList<>();
	
	private double[][] data = null;
	
	private double s = 4;
	private int MinPts = 16;
	
	private void print(String str) {
		System.out.println(str);
	}
	
	private void print(Queue<double[]> queue) {
		double[][] list = this.queueToArrayUnclear(queue);
		for(int i =0;i<list.length;i++) {
			print(list[i][0]+","+list[i][1]+" | "+list[i][2]);
		}
	}
	
	private void print(List<double[]> list) {
		for(int i =0;i<list.size();i++) {
			print(list.get(i)[0]+","+list.get(i)[1]);
		}
	}
	
	public OPTICS2(double[][] data){
		this.data = data;
		
		//打印原数据
//		for(int i = 0;i<data.length;i++) {
//			System.out.println(data[i][0]+" , "+data[i][1]);
//		}
//		
		this.print(data,0,2);
		
		for(int i = 0;i<data.length;i++) {
			//判断是否在结果队列中
			print("取出一个结果，索引为："+i+"，坐标为"+data[i][0]+","+data[i][1]);
			double[][] result = queueToArrayUnclear(this.result);
			boolean c = false;
			for(int j = 0;j<result.length;j++) {
				if(result[j][0] == data[i][0] && result[j][1] == data[i][1])
				{
					c = true;
					break;
				}
			}
			if(c) {
				print("该点冲突跳过");
				continue;	
			}
			
			print("监测是否是核心");
			if(this.isCore(data[i], s, MinPts)) {
				print("是核心，放入结果队列");
				this.pushResult(data[i]);
				print("目前的结果队列为");
				print(this.result);
				//寻找基于该点的所有密度直达点集合
				List<double[]> list = this.getPoint(data[i], s);
				print("该点的密度直达点集合为：");
				print(list);
				//将所有点放入Order队列中
				this.pushOrder(list);
				print("当前有序队列顺序为");
				print(this.order);
				this.shiftOrder();
			}else {
				print("不是核心下一个");
			}
		}
		
		//打印Result
		double[][] result = queueToArray(this.result);
//		for(int i =0;i<result.length;i++) {
//			System.out.println(result[i][0] + " , " + result[i][1] + " || "+result[i][2] + " | "+result[i][3]);
//		}
		this.print(result,0,3);
	}
	
	private void print(double[][] list,int s ,int e) {
		System.out.println("-------------------");
		for(int i = s;i<e;i++) {
			System.out.println("第"+i+"列：");
			for(int j=0;j<list.length;j++) {
				System.out.println(list[j][i]);
			}
		}
		
	}
	
	//将队列打印成数组，且不影响队列
	private double[][] queueToArrayUnclear(Queue<double[]> queue){
		double[][] list = new double[queue.size()][];
		int index = 0;
		while(true) {
			double[] i = queue.poll();
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
	private double[][] queueToArray(Queue<double[]> queue) {
		double[][] list = new double[queue.size()][];
		int index = 0;
		while(true) {
			double[] i = queue.poll();
			if(i != null) {
				list[index++] = i;
			}else {
				break;
			}
		}
		return list;
	}
	
	//从序列集合中取出新的点
	private void shiftOrder() {
		print("从有序队列取出一个");
		double[] p = order.poll();
		if(p == null) {
			print("有序序列空的");
			return;
		}

		print("取出的点为"+p[0]+p[1]);
		print("判断是否为核心");
		if(this.isCore(p, s, MinPts)) {
			print("是核心，放入结果队列");
			pushResult(p);
			print("目前的结果队列为");
			print(this.result);
			List<double[]> list = getPoint(p,s);
			
			print("该点的密度直达点集合为：");
			print(list);
			
			pushOrder(list);

			print("当前有序队列顺序为");
			print(this.order);
		}else {
			print("不是核心点");
		}
		shiftOrder();
	}
	
	private boolean isCore(double[] p,double s,int MinPts){
		int num = 0;
		double[] diss = new double[MinPts];
		for(int i =0;i<diss.length;i++) {
			diss[i] = 99999;
		}
		
		for(int i =0;i< this.data.length;i++){
			double dis = this.dis(p, this.data[i]);
			if(dis <= s){
				num++;
				//将dis存入diss diss只保留最小的前MinPts个
				int index = 0;
				for(int j = 0;j<diss.length;j++) {
					if(diss[j] > diss[index]) {
						index = j;
					}
				}
				if(diss[index] > dis) {
					diss[index] = dis;
				}
			}
		}

		print("当前可达距离前MinPts为：");
		for(int i = 0;i<diss.length;i++) {
			print(diss[i]+"");
		}
		if(num >= MinPts){ //是核心
			//diss中最大值
			double max = 0;
			for(int i = 0;i<diss.length;i++) {
				if(max < diss[i])
					max = diss[i];
			}
			p[3] = max;
			print("核心距离为"+max);
			return true;
		}
		return false;
	}
	
	private double dis(double[] a ,double[] b){
		double a2 = a[0]-b[0];
		double b2 = a[1]-b[1];
		return Math.sqrt(a2*a2 + b2*b2);
	}
	
	private List<double[]> getPoint(double[] p,double s){
		List<double[]> list = new ArrayList<>();
		double[][] result = this.queueToArrayUnclear(this.result);
		for(int i = 0;i<this.data.length;i++){
			double[] q = this.data[i];
			//不能是结果点
			boolean c = false;
			for(int j = 0;j<result.length;j++) {
				if(result[j] == q) {
					c = true;
					break;
				}
			}
			if(c)
				continue;
			double dis = this.dis(q, p);
			if(dis <= s && q != p){
				boolean update = false;
				if(q[2] > dis) {
					update = true;
					q[2] = dis;
				}
				if(update)
				if(q[2] < p[3])
					q[2] = p[3];
				list.add(q);
			}
		}
		return list;
	}
	
	//放入Order中并重新排序 放入的点不能已经存在于Result中 也不能已经存在于Order中
	private void pushOrder(List<double[]> list){

		double[][] order = this.queueToArrayUnclear(this.order);
		double[][] result = this.queueToArrayUnclear(this.result);
		
		for(int i = 0;i<list.size();i++){
			boolean c = false;
			double[] item = list.get(i);
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
					if(order[j][2] > item[2])
						order[j][2] = item[2];
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
				if(order[i][2] > order[j][2]) {
					double[] a = order[i];
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
	private void pushResult(double[] p) {
		double[][] data = this.queueToArrayUnclear(this.result);
		for(int i =0;i<data.length;i++) {
			if(data[i] == p) {
				return;
			}
		}
		this.result.add(p);
	}
	
	public static void main(String[] args) {
//		double[][] data = new double[50][];
//		for(int i = 0;i<data.length;i++){
//			int a = (int)(Math.random() * 20);
//			int b = (int)(Math.random() * 20);
//			data[i] = new double[]{a,b,9999,9999};
//		}
//		
//		double[][] data = new double[][] {
//			{29,33,9999,9999},{28,33,9999,9999},{28,34,9999,9999},{29,34,9999,9999},{29,35,9999,9999},{28,36,9999,9999},{28,35,9999,9999},{29,36,9999,9999},{30,36,9999,9999},{30,35,9999,9999},{30,34,9999,9999},{30,33,9999,9999},{34,42,9999,9999},{34,43,9999,9999},{34,45,9999,9999},{34,44,9999,9999},{35,42,9999,9999},{35,43,9999,9999},{36,43,9999,9999}
//		};
		double[][] data = new double[][] {
			{19,15,9999,9999},{20,15,9999,9999},{21,15,9999,9999},{19,16,9999,9999},{19,17,9999,9999},{19,18,9999,9999},{19,19,9999,9999},{19,20,9999,9999},{20,19,9999,9999},{20,18,9999,9999},{21,18,9999,9999},{22,18,9999,9999},{23,17,9999,9999},{24,17,9999,9999},{25,17,9999,9999},{25,18,9999,9999},{25,19,9999,9999},{25,20,9999,9999},{24,20,9999,9999},{23,20,9999,9999},{22,20,9999,9999},{22,19,9999,9999},{22,17,9999,9999},{21,17,9999,9999},{20,17,9999,9999},{19,21,9999,9999},{19,22,9999,9999},{19,23,9999,9999},{20,23,9999,9999},{21,23,9999,9999},{21,22,9999,9999},{22,22,9999,9999},{22,21,9999,9999},{21,19,9999,9999},{20,20,9999,9999},{20,21,9999,9999},{20,22,9999,9999},{21,21,9999,9999},{21,20,9999,9999},{18,19,9999,9999},{17,19,9999,9999},{17,20,9999,9999},{17,21,9999,9999},{17,22,9999,9999},{18,22,9999,9999},{18,23,9999,9999},{17,18,9999,9999},{17,17,9999,9999},{16,17,9999,9999},{15,17,9999,9999},{18,17,9999,9999},{18,16,9999,9999},{17,13,9999,9999}
		};
		new OPTICS2(data);
	}

}
