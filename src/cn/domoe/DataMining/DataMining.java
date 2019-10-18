package cn.domoe.DataMining;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.domoe.dbproxy.DBData;

public class DataMining {
	
	private List<Session> list = null;
	
	public DataMining() {
		list = new ArrayList<>();
		System.out.println("数据库行为分析系统By domoe.cn");
		//System.out.println("请提供.dbproxy文件地址");
		File path = new File(".");
		String pathStr = path.getAbsolutePath();
		pathStr = pathStr.substring(0, pathStr.length()-1);
		File file = new File(pathStr+"DBLog/log.dbproxy");
		this.launch(file.getAbsolutePath());
	}
	
	private DBReader reader = null;
	public void launch(String path) {
		try {
			reader = new DBReader(path);
			//循环读取所有的DBData 并打印
			int num = 0;
			while(true) {
				DBData dbdata = reader.nextDBData();
				if(dbdata == null)
				{
					System.out.println("全部读取完成累计 "+num+" 条");
					break;
				}
				num++;
				dbdata.print();
				this.list.add(new Session(dbdata));
			}
			
			//打印所有影响因子链式
			for(int i = 0;i<this.list.size();i++){
				this.list.get(i).print();
			}
			
		} catch (IOException e) {
			System.out.println("启动失败，日志文件不存在。");
		}
	}
	
	
	
}
