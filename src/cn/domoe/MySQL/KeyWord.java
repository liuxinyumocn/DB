package cn.domoe.MySQL;

/*
 * 关键词检索类
 * */
public class KeyWord {
	
	private static String[] actionKeywordmap = new String[]{
		"SELECT",
		"UPDATE",
		"INSERT",
		"DELETE"
	};
	
	public static void scanAction(String str,int start){
		for(int i = 0;i<KeyWord.actionKeywordmap.length;i++){
			str.indexOf(str, start);
		}
	}
	
	public static void main(String[] args){
		String sql = "SELECT * FROM `test` WHERE (`id` = '3');";
	}
}
