package cn.domoe.MySQL.Chain.Condition;

public class IF extends Condition{

	private static String[] operators = new String[]{
			"=",
			"!=",
			"<>",
			"like",
			">",
			"<",
			">=",
			"<="
	};
	
	private String key = "";
	private String value = "";
	private String operator = "";
	
	//根据字符串解析IF的三项内容 键 运算符 值
	public IF(String where) throws Exception {
		int p1=0;
		for(int i = 0;i<this.operators.length;i++){
			p1 = where.indexOf(this.operators[i]);
			if(p1 == -1){
				continue;
			}
			//找到了
			this.operator = this.operators[i];
			this.key = where.substring(0, p1).trim();
			this.value = where.substring(p1+this.operators[i].length(), where.length()).trim();
			return;
		}
		//没找到
		throw new Exception("未能识别的Where表达式");
	}

	@Override
	public void print() {
		System.out.println(this.key+this.operator+this.value);
	}

}
