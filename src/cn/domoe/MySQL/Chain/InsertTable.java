package cn.domoe.MySQL.Chain;

public class InsertTable extends Chain{



	private int start = 0;
	private int endIndex = 0;
	private String table = null;
	
	public InsertTable(String sql , int startIndex) throws Exception {
		super(sql,"table");
		this.start = startIndex;
		this.parse();
	}
	
	//解析TABLE 下一个非空字段必须是FROM 且 FROM 之后存在空格并有内容
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
			throw new Exception("未能找到INTO字段");
		}
		//判断该字符起是否是FROM
		int indexFrom = this.sql.indexOf("INTO", index);
		if(indexFrom != index)
			throw new Exception("未能找到INTO字段2");
		//FROM字段下一个位置需要是空格 且空格后有内容，直到下一个空格或者字符串尾部或者分号
		indexFrom+=4;
		if(!this.sql.substring(indexFrom, indexFrom+1).equals(" ")) {
			throw new Exception("未能找到TABLE字段");
		}
		//寻找从indexFROM开始的下一个空格或者分号或者字符串最末尾
		boolean find = false;
		int end = 0;
		for(end = ++indexFrom;end<this.sql.length();end++) {
			String ch = this.sql.substring(end, end+1);
			if(ch.equals(";") || ch.equals(" ")) {
				//找到了
				find = true;
				end--;
				break;
			}
		}
		if(!find) {
			end = this.sql.length()-1;
		}
		this.endIndex = end;
		this.table = this.sql.substring(indexFrom, end+1).trim().replace("`", "");
		return;
	}

	@Override
	public void print() {
		System.out.println("该语句TABLE为："+this.table);
		
	}

	/*
	 * 寻找下一个节点
	 * Where
	 * Limit
	 * Order
	 * */
	public Chain[] nextChain() throws Exception {
		Chain[] chains = new Chain[3];
		int finished = 0;
		try{
			InsertField field = new InsertField(this.sql,endIndex+1);
			chains[finished++] = field;
		}catch(Exception e){
			//没有Field
			//e.printStackTrace();
		}
		try{
			Value value = new Value(this.sql,endIndex+1);
			chains[finished++] = value;
		}catch(Exception e){
			//没有Limit
		}
		Chain[] cs = new Chain[finished];
		for(int i = 0; i< cs.length;i++){
			cs[i] = chains[i];
		}
		return cs;
	}

	@Override
	public Object getValue() {
		return this.table;
	}
}
