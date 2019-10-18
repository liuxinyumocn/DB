package cn.domoe.MySQL.Chain;

public class Action extends Chain{
	private String action = null;
	private int actionIndex = 0;
	private int endIndex = 0;

	/*
	 * 监测CURD
	 * 只从第一个字符开始监测
	 * 若存在不符合项则直接停止解析
	 * */
	public Action(String sql) throws Exception {
		super(sql,"action");
		this.parse();
	}
	
	//自动根据当前识别情况访问下一个读取节点
	public Chain[] nextChain() throws Exception {
		Chain[] cs = new Chain[1];
		switch(this.actionIndex) {
		case 0://查询
			//正常应该先解析聚合函数
			cs[0] = new Field(this.sql,endIndex+1);
			return cs;
		case 1://更新
			cs[0] = new UpdateTable(this.sql,endIndex+1);
			return cs;
		case 2://插入
			cs[0] = new InsertTable(this.sql,endIndex+1);
			return cs;
		case 3://删除
			cs[0] = new Table(this.sql,endIndex+1);
			return cs;
		}
		return null;
	}
	
	private void parse() throws Exception {
		for(int i = 0;i<this.actionKeywordmap.length;i++) {
			int index = this.sql.indexOf(this.actionKeywordmap[i], 0);
			if(index == 0) {
				this.action = this.actionKeywordmap[i];
				this.actionIndex = i;
				this.endIndex = this.actionKeywordmap[i].length()-1;
				return;
			}
		}
		throw new Exception("未能监测到CURD行为");
	}

	private static String[] actionKeywordmap = new String[]{
		"SELECT",
		"UPDATE",
		"INSERT",
		"DELETE"
	};

	@Override
	public void print() {
		System.out.println("该语句CURD为："+this.action);
	}

	@Override
	public Object getValue() {
		return this.actionIndex;
	}
	
}
