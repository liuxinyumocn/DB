package cn.domoe.dbproxy;

import java.io.IOException;
import java.util.Scanner;

public class DBProxy {
	
	public DBProxy() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("基于TCP/IP协议数据库中间件日志系统 By proxy.domoe.cn");
//		System.out.println("真实数据库地址：");
//		String ip = scanner.nextLine();
//		System.out.println("真实数据库端口号：");
//		int port = Integer.parseInt(scanner.nextLine());
//		System.out.println("数据库型号(目前仅支持MySQL)：");
//		String db = scanner.nextLine();
//		if(!db.toLowerCase().equals("mysql")) {
//			System.out.println("目前暂不支持该数据库");
//			return;
//		}
//		System.out.println("虚拟数据库端口号：");
//		int vport = Integer.parseInt(scanner.nextLine());
		//启动端口监听
//		this.launch(ip, port,"mysql",vport);
		this.launch("192.168.1.103", 3306, "mysql", 3306);
	}

	private void launch(String ip,int port,String db,int vport) {
		try {
			System.out.println("服务启动中...");
			new Server(ip,port,vport);
			System.out.println("启动成功！等待客户接入");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("服务启动失败！");
		}
	}
	
}
