package cn.domoe.MySQL.Chain;

public class UpdateTable extends Chain{

	
	private int start = 0;
	private int endIndex = 0;
	private String table = null;
	
	public UpdateTable(String sql ,int startIndex) throws Exception {
		super(sql,"table");
		this.start = startIndex;
		this.parse();
	}
	
	private void parse() throws Exception {
		//位于SET后
		int setindex = 0;
		setindex = this.sql.indexOf("SET", this.start);
		if(setindex == -1)
			throw new Exception("未能找到Table字段");
		String table = this.sql.substring(this.start,setindex).trim();
		//判断中间是否有空白
		int space = table.indexOf(" ");
		if(space != -1)
			throw new Exception("未能找到Table字段2");
		this.table = table;
		this.endIndex = setindex + 3;
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		System.out.println("该语句的Table字段为"+this.table);
	}

	@Override
	public Chain[] nextChain() throws Exception {
		//UpdateValue
		Chain[] chains = new Chain[1];
		UpdateValue updateValue = new UpdateValue(this.sql,this.endIndex+1);
		chains[0] = updateValue;
		return chains;
	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return this.table;
	} 
}
