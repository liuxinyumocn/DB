package cn.domoe.DIDS;

public class Transaction {
	
	public Transaction(int a,int b) {
		x =a ;
		y=b;
	}
	
	public int x = 0;
	public int y = 0;
	
	public int u_id = 0;
	
	public int transaction_id = 0;
	
	public int[] table_list = new int[] {3,1,5};
	
	public int[] att_list = new int[] {1,6,2,3};
	
	public int time_slot = 9;
	
	public int loc = 1;
	
	public int time_gap = 4;
	
}
