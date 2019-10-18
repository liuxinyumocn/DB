package cn.domoe.dbproxy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import cn.domoe.ThreadPool.ThreadPool;

/*
 * 文件系统
 * */
public class DBFile {
	private File dir = null;
	private File file = null;
	private FileOutputStream fout = null;
	
	private Queue<DBData> dbdataQueue= null;
	
	public DBFile(String path) throws IOException {
		dir = new File(path);
		if(dir.isDirectory()){
			String path2 = dir.getAbsolutePath();
			path2 = path2.substring(0, path2.length()-1);
			file = new File(path2+"DBLog\\log.dbproxy");
		}else{
			this.dir = file;
		}
		file.createNewFile();
		
		this.fout = new FileOutputStream(file.getAbsolutePath());
		
		this.dbdataQueue = new ConcurrentLinkedQueue<>();
	}
	
	public void push(DBData dbdata){
		//dbdata.print();
		this.dbdataQueue.add(dbdata);
		this.save();
	}

	private void save(){
		if(!saveing){
			saveing = true;
			ThreadPool.submit(new SaveThread());
		}
	}
	private boolean saveing = false;
	class SaveThread implements Runnable{
		@Override
		public void run() {
			while(saveing){
				//写入
				DBData dbdata = dbdataQueue.poll();
				if(dbdata == null) {
					saveing = false;
					break;
				}
				try {
					dbdata.output(fout);
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("数据写入失败");
				}
			}
		}
	}
	
}
