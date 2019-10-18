package cn.domoe.ThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ThreadPool {
	private static ExecutorService threadPool = Executors.newCachedThreadPool() ; //静态线程池全局共享

	public static void submit(Runnable task){
		threadPool.submit(task);
	}
}
