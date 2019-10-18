package cn.domoe.MySQL.Chain;

public class InsertField  extends Chain{

	private String insertFiled = "";
	private String[] insertFileds = null;
	private int start = 0;
	public InsertField(String sql, int i) throws Exception {
		super(sql, "field");
		// TODO Auto-generated constructor stub
		this.start = i;
		this.parse();
	}
	
	//从起始位置到Value关键词中间的括号内所有字段
	private void parse() throws Exception {
		int valueindex = this.sql.indexOf("VALUES", this.start);
		if(valueindex == -1)
			throw new Exception("未找到INSERT FILED部分");
		//从起始位置找左括号，到valueindex找到右括号
		int left = 0 , right = 0;
		for(int i = this.start;i<valueindex;i++) {
			if(left == 0 ) {
				//找左
				if(this.sql.substring(i, i+1).equals("(")) {
					left = i;
				}
			}else {
				//找右
				if(this.sql.substring(i, i+1).equals(")")) {
					right = i;
				}
			}
		}
		if(right == 0)
			throw new Exception("未找到INSERT FILED部分2");
		this.insertFiled = this.sql.substring(left+1, right).trim();
		this.insertFileds = this.insertFiled.split(",");
	}

	@Override
	public void print() {
		System.out.println("该语句的InsertFiled为："+this.insertFiled);
	}

	@Override
	public Chain[] nextChain() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue() {
		return this.insertFileds;
	}

}
