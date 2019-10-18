package cn.domoe.MySQL.Chain;

public class Field extends Chain{
	
	private String field = null;
	private String[] fields = null;
	
	private int start = 0;
	private int endIndex = 0;
	public Field(String sql, int start) throws Exception {
		super(sql, "field");
		this.start = start;
		this.parse();
	}
	//应该从开始位置起，第一个非空字符到下一个空格
	private void parse() throws Exception {
		//先找到起始位置开始的下一个非空字符
		int index= -1;
		for(int i = this.start;i<this.sql.length();i++) {
			if(!this.sql.substring(i, i+1).equals(" ")) {
				index = i;
				break;
			}
		}
		if(index == -1) {
			//未找到
			throw new Exception("未能找到Field字段");
		}
		int indexNextSpace = this.sql.indexOf(" ", index);
		if(indexNextSpace == -1)
			throw new Exception("未能找到Field字段");
		//找到了
		this.endIndex = indexNextSpace-1;
		String field = this.sql.substring(index, indexNextSpace);
		if(field.equals("*")) {
			this.field = "*";
			return;
		}
		field = field.replace("`", ""); //去除`符号
		this.fields = field.split(","); //分割，
		return;
	}

	/*
	 * Field下一级是表格
	 * */
	public Chain[] nextChain() throws Exception{
		Chain[] cs = new Chain[1];
		cs[0] =  new Table(this.sql,endIndex+1);
		return cs;
	}
	
	@Override
	public void print() {
		String str = "";
		if(this.field == null) {
			for(int i = 0;i<this.fields.length;i++) {
				str += this.fields[i];
				str += "/";
			}
		}else
			str = this.field;
		System.out.println("该语句FIELD为："+str);
	}
	@Override
	public Object getValue() {
		return this.fields;
	}
	
}
