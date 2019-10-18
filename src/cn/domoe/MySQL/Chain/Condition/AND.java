package cn.domoe.MySQL.Chain.Condition;

public class AND extends Condition{

	private Condition left = null;
	private Condition right = null;
	
	public AND(String where) throws Exception {
		
		int p1 = 0;
		p1 = where.indexOf("AND");
		if(p1 == -1)
			throw new Exception("未能识别的AND条件句");
		this.left = new IF(where.substring(0, p1));
		this.right = new IF(where.substring(p1+3, where.length()));
	}

	@Override
	public void print() {
		this.left.print();
		System.out.println("AND");
		this.right.print();
	}

}
