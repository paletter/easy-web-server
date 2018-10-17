package com.paletter.easy.web.server.thread;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
						
						SelectionKey key = ite.next();
						
						if(key.isAcceptable()) {

							// Accept
							
							ite.remove();
							
							ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
							SocketChannel channel = ssc.accept();
							if(channel == null) continue;
							System.out.println("Accpet: " + channel);
							acceptCnt ++;
							
							channel.configureBlocking(false);
							channel.register(selector, SelectionKey.OP_READ);
						}
						
						if(key.isReadable()) {
							
							// Readable
							
							SocketChannel sc = (SocketChannel) key.channel();
							NIOHttpHandlerThread httpHandlerThread = new NIOHttpHandlerThread(sc);
							System.out.println("# HttpHandlerThread-" + (acceptCnt));
							httpHandlerThread.setName("HttpHandlerThread-" + acceptCnt);
							httpHandlerThreadPool.execute(httpHandlerThread);
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
