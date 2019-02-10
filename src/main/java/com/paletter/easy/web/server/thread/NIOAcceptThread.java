package com.paletter.easy.web.server.thread;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.paletter.easy.web.server.utils.LogUtil;

public class NIOAcceptThread extends Thread {

	private Selector selector;
	private ExecutorService httpHandlerThreadPool;

	public NIOAcceptThread(Selector selector, int httpHandlerThreadSize) {
		super();
		this.selector = selector;
		this.httpHandlerThreadPool = Executors.newFixedThreadPool(httpHandlerThreadSize);
	}

	public void run() {
		
		try {
			
			int acceptCnt = 0;
			while(true) {

				while(selector.select() > 0) {
					
					Iterator<SelectionKey> ite = selector.selectedKeys().iterator();
					
					while(ite.hasNext()) {
						
						try {
								
							SelectionKey key = ite.next();
							
							if (!key.isValid()) continue;
							
							if (key.isAcceptable()) {
	
								// Accept
								
								ite.remove();
								
								ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
								SocketChannel channel = ssc.accept();
								if(channel == null) continue;
								LogUtil.printDebug("Accpet: " + channel);
								acceptCnt ++;
								
								channel.configureBlocking(false);
								
								NIOHttpHandlerThread httpHandlerThread = new NIOHttpHandlerThread(channel);
								LogUtil.printDebug("# HttpHandlerThread-" + (acceptCnt));
								httpHandlerThread.setName("HttpHandlerThread-" + acceptCnt);
								httpHandlerThreadPool.execute(httpHandlerThread);
							}
							
						} catch (Throwable e) {
							LogUtil.error("", e);
						}
					}
				}
			}
			
		} catch (Exception e) {
			LogUtil.error("", e);
		}
	}
}
