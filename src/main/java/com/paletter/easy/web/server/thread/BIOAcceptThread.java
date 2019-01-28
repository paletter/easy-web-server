package com.paletter.easy.web.server.thread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.paletter.easy.web.server.utils.LogUtil;

public class BIOAcceptThread extends Thread {

	private boolean isRun;
	private ServerSocket ss;
	private ExecutorService httpHandlerThreadPool;

	public BIOAcceptThread(ServerSocket ss, int httpHandlerThreadSize) {
		this.ss = ss;
		this.httpHandlerThreadPool = Executors.newFixedThreadPool(httpHandlerThreadSize);
	}
	
	public void run() {
		
		try {
			
			isRun = true;
			
			while(isRun) {
				
				Socket socket = ss.accept();
				
				BIOHttpHandlerThread httpHandlerThread = new BIOHttpHandlerThread(socket);
				httpHandlerThreadPool.execute(httpHandlerThread);
			}
			
			ss.close();
		
		} catch (Exception e) {
			if(!(e instanceof SocketException)) {
				LogUtil.error("", e);
			}
		}
	}
	
	public void interrupt() {
		
		try {
			
			LogUtil.printDebug("Server Interrupt");
			isRun = false;
			ss.close();
		} catch (IOException e) {
			LogUtil.error("", e);
		}
	}
}
