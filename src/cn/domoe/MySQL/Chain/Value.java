package cn.domoe.MySQL.Chain;

public class Value extends Chain{
	private String value = "";
	private String[] values = null;
	private int start = 0;
	public Value(String sql, int i) throws Exception {
		super(sql, "value");
		this.start = i;
		this.parse();
	}
	
	//寻找Value后面的字段集合
	private void parse() throws Exception {
		int valueindex =  this.sql.indexOf("VALUES", this.start);
		if(valueindex == -1)
			throw new Exception("未找到该语句Value字段");
		int left = 0,right = 0;
		for(int i = valueindex ; i < this.sql.length();i++) {
			if(left == 0) {
				//查左括号
				if(this.sql.substring(i, i+1).equals("(")) {
					left = i;
				}
			}else {
				if(this.sql.substring(i,i+1).equals(")")) {
					right = i;
					break;
				}
			}
		}
		if(right == 0)
			throw new Exception("未找到该语句的Value字段");
		this.value = this.sql.substring(left+1, right).trim();
		this.values = this.value.split(",");
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		System.out.println("该语句的Value内容为"+this.value);
	}

	@Override
	public Chain[] nextChain() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return this.values;
	}

}
