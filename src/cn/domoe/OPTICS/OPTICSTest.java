package cn.domoe.OPTICS;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class OPTICSTest {

	private Queue<double[]> result = new LinkedList<>();
	private Queue<double[]> order = new LinkedList<>();
	
	private double[][] data = null;
	
	private double s = 1000;
	private int MinPts = 10;
	
	public OPTICSTest(double[][] data){
		this.data = data;
		this.MinPts = data.length-1;

		this.print(data,0,2);
		
		for(int i = 0;i<data.length;i++) {
			//判断是否在结果队列中
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
				continue;	
			}
			
			if(this.isCore(data[i], s, MinPts)) {
				this.pushResult(data[i]);
				//寻找基于该点的所有密度直达点集合
				List<double[]> list = this.getPoint(data[i], s);
				//将所有点放入Order队列中
				this.pushOrder(list);
				this.shiftOrder();
			}else {
			}
		}
		
		//打印Result
		double[][] result = queueToArray(this.result);
//		for(int i =0;i<result.length;i++) {
//			System.out.println(result[i][0] + " , " + result[i][1] + " || "+result[i][2] + " | "+result[i][3]);
//		}
		this.print(result,0,3);
	}
	
	private void print(double[][] list,int s ,int e) { //--------------------
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
		double[] p = order.poll();
		if(p == null) {
			return;
		}

		if(this.isCore(p, s, MinPts)) {
			pushResult(p);
			List<double[]> list = getPoint(p,s);
			pushOrder(list);
		}else {
		}
		shiftOrder();
	}
	
	private boolean isCore(double[] p,double s,int MinPts){
		int num = 0;
		double[] diss = new double[MinPts];
		for(int i =0;i<diss.length;i++) {
			diss[i] = 9999;
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

		if(num >= MinPts){ //是核心
			//diss中最大值
			double max = 0;
			for(int i = 0;i<diss.length;i++) {
				if(max < diss[i])
					max = diss[i];
			}
			p[3] = max;
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
//				if(update)
//				if(q[2] < p[3])
//					q[2] = p[3];
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
		double[][] data2 = new double[][] {
			{21,21,9999,9999},{22,22,9999,9999},{22,23,9999,9999},{22,24,9999,9999},{22,25,9999,9999},{22,26,9999,9999},{21,26,9999,9999},{21,27,9999,9999},{20,27,9999,9999},{20,26,9999,9999},{20,25,9999,9999},{20,24,9999,9999},{20,23,9999,9999},{20,22,9999,9999},{21,22,9999,9999},{22,21,9999,9999},{23,21,9999,9999},{24,21,9999,9999},{25,21,9999,9999},{25,22,9999,9999},{25,23,9999,9999},{24,23,9999,9999},{23,23,9999,9999},{21,24,9999,9999},{19,24,9999,9999},{17,21,9999,9999},{16,21,9999,9999},{15,21,9999,9999},{15,22,9999,9999},{14,22,9999,9999},{17,28,9999,9999},{17,29,9999,9999},{20,29,9999,9999},{21,29,9999,9999},{25,27,9999,9999},{27,20,9999,9999},{27,19,9999,9999},{26,19,9999,9999},{25,18,9999,9999},{24,17,9999,9999},{23,17,9999,9999},{18,17,9999,9999},{35,48,9999,9999},{27,44,9999,9999},{26,44,9999,9999},{26,45,9999,9999},{28,52,9999,9999},{35,50,9999,9999},{34,49,9999,9999},{34,50,9999,9999},{33,49,9999,9999},{32,49,9999,9999},{30,48,9999,9999},{31,48,9999,9999},{24,45,9999,9999},{24,46,9999,9999},{24,47,9999,9999},{25,48,9999,9999},{26,48,9999,9999},{27,50,9999,9999},{34,45,9999,9999},{29,46,9999,9999},{29,49,9999,9999},{31,51,9999,9999},{31,52,9999,9999},{31,53,9999,9999},{32,48,9999,9999},{32,47,9999,9999},{32,44,9999,9999},{31,44,9999,9999},{31,45,9999,9999},{30,46,9999,9999},{30,47,9999,9999},{29,45,9999,9999},{28,45,9999,9999},{28,46,9999,9999},{27,47,9999,9999},{27,48,9999,9999},{26,49,9999,9999},{27,49,9999,9999},{28,50,9999,9999},{28,51,9999,9999},{27,52,9999,9999},{26,52,9999,9999},{24,52,9999,9999},{25,52,9999,9999},{23,52,9999,9999},{21,53,9999,9999},{22,53,9999,9999},{23,53,9999,9999},{24,53,9999,9999},{25,53,9999,9999},{29,51,9999,9999},{29,50,9999,9999},{25,47,9999,9999},{25,49,9999,9999},{25,50,9999,9999},{24,50,9999,9999},{23,50,9999,9999},{22,49,9999,9999},{22,50,9999,9999},{21,49,9999,9999},{21,50,9999,9999},{22,51,9999,9999},{23,51,9999,9999},{24,51,9999,9999},{32,55,9999,9999},{36,36,9999,9999},{36,35,9999,9999},{37,35,9999,9999},{37,34,9999,9999},{40,29,9999,9999},{40,28,9999,9999},{39,24,9999,9999},{33,23,9999,9999},{15,38,9999,9999},{11,46,9999,9999},{11,47,9999,9999},{15,49,9999,9999},{16,42,9999,9999},{14,43,9999,9999},{3,66,9999,9999},{2,66,9999,9999},{2,65,9999,9999},{2,64,9999,9999},{2,63,9999,9999},{1,63,9999,9999},{1,62,9999,9999},{1,64,9999,9999},{1,65,9999,9999},{1,66,9999,9999},{1,67,9999,9999},{1,68,9999,9999},{1,69,9999,9999},{2,69,9999,9999},{3,69,9999,9999},{4,70,9999,9999},{5,70,9999,9999},{6,70,9999,9999},{5,63,9999,9999},{5,64,9999,9999},{5,65,9999,9999},{5,66,9999,9999},{5,67,9999,9999},{5,68,9999,9999},{5,69,9999,9999},{4,71,9999,9999},{4,69,9999,9999},{4,68,9999,9999},{4,67,9999,9999},{6,66,9999,9999},{7,66,9999,9999},{8,65,9999,9999},{9,65,9999,9999},{9,64,9999,9999},{10,64,9999,9999},{5,62,9999,9999},{4,62,9999,9999},{3,63,9999,9999},{0,64,9999,9999},{-2,64,9999,9999},{-1,64,9999,9999},{-3,64,9999,9999},{-4,64,9999,9999},{2,61,9999,9999},{3,62,9999,9999},{4,63,9999,9999},{6,65,9999,9999},{8,67,9999,9999},{8,68,9999,9999},{9,69,9999,9999},{6,69,9999,9999},{6,71,9999,9999},{6,72,9999,9999},{5,72,9999,9999},{5,73,9999,9999},{0,33,9999,9999},{-1,33,9999,9999},{-1,34,9999,9999},{-2,34,9999,9999},{-1,31,9999,9999},{0,31,9999,9999},{1,32,9999,9999},{1,33,9999,9999},{1,34,9999,9999},{0,34,9999,9999},{0,35,9999,9999},{0,36,9999,9999},{-1,36,9999,9999},{-1,37,9999,9999},{-2,37,9999,9999},{-3,37,9999,9999},{-3,36,9999,9999},{-3,35,9999,9999},{-3,34,9999,9999},{-2,33,9999,9999},{-2,32,9999,9999},{-1,32,9999,9999},{1,30,9999,9999},{2,30,9999,9999},{3,30,9999,9999},{3,31,9999,9999},{3,32,9999,9999},{2,32,9999,9999},{0,32,9999,9999},{-2,29,9999,9999},{-2,30,9999,9999},{-2,31,9999,9999},{-1,35,9999,9999},{1,50,9999,9999},{0,50,9999,9999},{0,51,9999,9999},{-1,51,9999,9999},{-2,50,9999,9999},{-2,49,9999,9999},{-2,48,9999,9999},{-2,47,9999,9999},{-2,45,9999,9999},{27,64,9999,9999},{23,71,9999,9999},{10,57,9999,9999},{28,37,9999,9999},{2,23,9999,9999},{-2,66,9999,9999},{0,28,9999,9999},{-1,28,9999,9999},{-2,28,9999,9999},{-4,29,9999,9999},{-5,32,9999,9999},{-4,33,9999,9999},{-4,31,9999,9999},{-4,32,9999,9999},{-3,33,9999,9999},{26,18,9999,9999},{24,18,9999,9999},{24,19,9999,9999},{23,19,9999,9999},{22,20,9999,9999},{21,20,9999,9999},{20,20,9999,9999},{19,20,9999,9999},{19,21,9999,9999},{19,23,9999,9999},{15,24,9999,9999},{18,24,9999,9999},{18,22,9999,9999},{18,25,9999,9999}
			//{9,9,9999,9999},{8,14,9999,9999},{6,20,9999,9999},{8,30,9999,9999},{5,40,9999,9999},{4,50,9999,9999},{6,46,9999,9999},{4,37,9999,9999},{3,25,9999,9999},{3,32,9999,9999},{4,28,9999,9999},{15,14,9999,9999},{17,7,9999,9999},{24,7,9999,9999},{11,19,9999,9999},{11,26,9999,9999},{19,28,9999,9999},{13,38,9999,9999},{13,33,9999,9999},{9,37,9999,9999},{9,34,9999,9999},{12,45,9999,9999},{10,51,9999,9999},{23,52,9999,9999},{25,48,9999,9999},{17,48,9999,9999},{15,51,9999,9999},{23,51,9999,9999},{23,50,9999,9999},{18,43,9999,9999},{24,42,9999,9999},{33,46,9999,9999},{36,48,9999,9999},{29,38,9999,9999},{22,35,9999,9999},{19,36,9999,9999},{28,28,9999,9999},{26,23,9999,9999},{18,22,9999,9999},{18,16,9999,9999},{26,11,9999,9999},{24,17,9999,9999},{22,11,9999,9999},{30,15,9999,9999},{34,13,9999,9999},{34,8,9999,9999},{34,5,9999,9999},{31,26,9999,9999},{32,34,9999,9999},{36,37,9999,9999},{37,41,9999,9999},{49,46,9999,9999},{47,31,9999,9999},{40,35,9999,9999},{44,40,9999,9999},{39,28,9999,9999},{30,19,9999,9999},{36,19,9999,9999},{45,23,9999,9999},{45,13,9999,9999},{39,15,9999,9999},{42,6,9999,9999},{45,6,9999,9999},{52,14,9999,9999},{9,57,9999,9999},{17,62,9999,9999},{40,58,9999,9999},{47,58,9999,9999},{48,63,9999,9999},{32,63,9999,9999},{34,55,9999,9999},{23,56,9999,9999},{22,50,9999,9999},{22,51,9999,9999},{22,52,9999,9999},{24,52,9999,9999},{24,51,9999,9999},{24,50,9999,9999},{24,49,9999,9999},{23,49,9999,9999},{22,49,9999,9999},{21,49,9999,9999},{21,51,9999,9999},{20,51,9999,9999},{20,52,9999,9999},{21,55,9999,9999},{25,54,9999,9999},{25,53,9999,9999},{23,53,9999,9999},{22,53,9999,9999},{21,53,9999,9999},{21,54,9999,9999},{22,54,9999,9999},{23,54,9999,9999},{24,54,9999,9999},{33,14,9999,9999},{33,15,9999,9999},{33,16,9999,9999},{33,17,9999,9999},{33,18,9999,9999},{32,18,9999,9999},{31,18,9999,9999},{30,18,9999,9999},{30,17,9999,9999},{29,17,9999,9999},{29,16,9999,9999},{29,15,9999,9999},{29,14,9999,9999},{30,14,9999,9999},{31,14,9999,9999},{32,14,9999,9999},{32,15,9999,9999},{32,16,9999,9999},{32,17,9999,9999},{31,17,9999,9999},{31,16,9999,9999},{31,15,9999,9999},{30,16,9999,9999},{8,26,9999,9999},{7,26,9999,9999},{6,26,9999,9999},{8,27,9999,9999},{8,28,9999,9999},{8,29,9999,9999},{8,31,9999,9999},{8,32,9999,9999},{7,31,9999,9999},{7,30,9999,9999},{7,29,9999,9999},{7,28,9999,9999},{6,27,9999,9999},{6,28,9999,9999},{6,25,9999,9999},{6,24,9999,9999},{7,24,9999,9999},{7,25,9999,9999},{7,27,9999,9999},{5,28,9999,9999},{5,29,9999,9999},{5,30,9999,9999},{6,30,9999,9999},{6,29,9999,9999},{8,25,9999,9999},{8,24,9999,9999},{5,26,9999,9999},{5,27,9999,9999},{48,48,9999,9999},{47,49,9999,9999},{47,50,9999,9999},{46,49,9999,9999},{46,50,9999,9999},{46,48,9999,9999},{46,47,9999,9999},{46,46,9999,9999},{47,45,9999,9999},{48,45,9999,9999},{48,46,9999,9999},{48,47,9999,9999},{47,48,9999,9999},{47,47,9999,9999},{46,45,9999,9999},{47,44,9999,9999},{48,44,9999,9999},{47,46,9999,9999},{45,45,9999,9999},{45,46,9999,9999},{44,46,9999,9999},{44,47,9999,9999},{44,48,9999,9999},{44,49,9999,9999},{45,49,9999,9999},{45,48,9999,9999},{45,47,9999,9999}
			//{8,31,9999,9999},{8,32,9999,9999},{8,33,9999,9999},{8,34,9999,9999},{8,35,9999,9999},{8,36,9999,9999},{8,37,9999,9999},{9,37,9999,9999},{10,37,9999,9999},{11,37,9999,9999},{11,36,9999,9999},{12,36,9999,9999},{12,35,9999,9999},{12,34,9999,9999},{12,33,9999,9999},{12,32,9999,9999},{12,31,9999,9999},{11,31,9999,9999},{10,31,9999,9999},{9,31,9999,9999},{9,32,9999,9999},{7,35,9999,9999},{7,36,9999,9999},{7,37,9999,9999},{7,38,9999,9999},{8,38,9999,9999},{9,38,9999,9999},{10,36,9999,9999},{10,35,9999,9999},{10,34,9999,9999},{10,33,9999,9999},{9,36,9999,9999},{9,35,9999,9999},{9,34,9999,9999},{9,33,9999,9999},{10,32,9999,9999},{11,34,9999,9999},{31,15,9999,9999},{30,15,9999,9999},{29,15,9999,9999},{28,15,9999,9999},{27,16,9999,9999},{27,17,9999,9999},{27,18,9999,9999},{28,19,9999,9999},{28,20,9999,9999},{29,20,9999,9999},{30,20,9999,9999},{31,20,9999,9999},{32,20,9999,9999},{33,20,9999,9999},{33,19,9999,9999},{33,18,9999,9999},{33,17,9999,9999},{33,16,9999,9999},{32,16,9999,9999},{32,15,9999,9999},{31,14,9999,9999},{30,14,9999,9999},{29,14,9999,9999},{28,14,9999,9999},{27,14,9999,9999},{27,15,9999,9999},{26,15,9999,9999},{26,16,9999,9999},{26,17,9999,9999},{26,18,9999,9999},{26,19,9999,9999},{26,20,9999,9999},{26,21,9999,9999},{27,21,9999,9999},{28,21,9999,9999},{29,21,9999,9999},{30,21,9999,9999},{31,21,9999,9999},{31,19,9999,9999},{32,19,9999,9999},{32,18,9999,9999},{32,17,9999,9999},{31,16,9999,9999},{28,16,9999,9999},{28,17,9999,9999},{28,18,9999,9999},{29,19,9999,9999},{30,19,9999,9999},{27,45,9999,9999},{28,45,9999,9999},{29,45,9999,9999},{30,45,9999,9999},{31,45,9999,9999},{32,45,9999,9999},{33,45,9999,9999},{34,45,9999,9999},{35,46,9999,9999},{36,46,9999,9999},{36,47,9999,9999},{37,48,9999,9999},{38,49,9999,9999},{38,50,9999,9999},{38,51,9999,9999},{39,51,9999,9999},{39,52,9999,9999},{39,53,9999,9999},{39,54,9999,9999},{39,55,9999,9999},{38,56,9999,9999},{37,57,9999,9999},{36,58,9999,9999},{35,58,9999,9999},{34,58,9999,9999},{33,58,9999,9999},{32,58,9999,9999},{31,58,9999,9999},{30,58,9999,9999},{29,58,9999,9999},{29,57,9999,9999},{28,55,9999,9999},{28,56,9999,9999},{28,54,9999,9999},{28,53,9999,9999},{28,52,9999,9999},{28,51,9999,9999},{28,50,9999,9999},{29,49,9999,9999},{29,48,9999,9999},{29,47,9999,9999},{30,46,9999,9999},{35,45,9999,9999},{36,45,9999,9999},{37,45,9999,9999},{38,46,9999,9999},{38,47,9999,9999},{39,47,9999,9999},{39,48,9999,9999},{39,49,9999,9999},{39,50,9999,9999},{38,53,9999,9999},{37,53,9999,9999},{35,53,9999,9999},{36,53,9999,9999},{33,53,9999,9999},{34,53,9999,9999},{32,53,9999,9999},{30,53,9999,9999},{31,53,9999,9999},{29,52,9999,9999},{27,51,9999,9999},{27,50,9999,9999},{27,49,9999,9999},{27,48,9999,9999},{28,48,9999,9999},{30,47,9999,9999},{31,47,9999,9999},{32,47,9999,9999},{33,48,9999,9999},{33,49,9999,9999},{34,49,9999,9999},{34,50,9999,9999},{34,51,9999,9999},{34,52,9999,9999},{34,54,9999,9999},{33,54,9999,9999},{33,55,9999,9999},{32,55,9999,9999},{32,54,9999,9999},{31,51,9999,9999},{31,52,9999,9999},{31,50,9999,9999},{32,49,9999,9999},{32,48,9999,9999},{33,47,9999,9999},{34,47,9999,9999},{34,48,9999,9999},{33,52,9999,9999},{29,53,9999,9999},{29,51,9999,9999},{29,50,9999,9999},{30,49,9999,9999},{31,48,9999,9999},{35,52,9999,9999},{35,54,9999,9999},{35,55,9999,9999},{35,56,9999,9999},{34,57,9999,9999},{33,56,9999,9999},{33,57,9999,9999},{32,52,9999,9999},{32,51,9999,9999},{33,51,9999,9999},{33,50,9999,9999},{35,50,9999,9999},{36,50,9999,9999},{37,50,9999,9999},{37,51,9999,9999},{37,52,9999,9999},{37,54,9999,9999},{37,55,9999,9999},{36,56,9999,9999},{32,57,9999,9999},{30,57,9999,9999},{31,57,9999,9999},{27,57,9999,9999},{28,57,9999,9999},{26,57,9999,9999},{25,57,9999,9999},{25,56,9999,9999},{25,54,9999,9999},{25,55,9999,9999},{25,53,9999,9999},{25,51,9999,9999},{25,52,9999,9999},{26,50,9999,9999},{26,49,9999,9999},{35,47,9999,9999},{37,47,9999,9999},{40,48,9999,9999},{40,49,9999,9999},{40,50,9999,9999},{40,51,9999,9999},{40,52,9999,9999},{40,53,9999,9999},{40,54,9999,9999},{40,55,9999,9999},{38,55,9999,9999},{36,55,9999,9999},{32,50,9999,9999},{35,48,9999,9999},{36,48,9999,9999},{36,49,9999,9999},{31,54,9999,9999},{30,54,9999,9999},{29,54,9999,9999}
		};
		new OPTICSTest(data2);
	}

}