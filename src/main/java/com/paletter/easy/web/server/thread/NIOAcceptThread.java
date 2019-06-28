package com.paletter.easy.web.server.thread;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.paletter.easy.web.server.support.SelectorHandler;
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
		
		new SelectorHandler(selector, new SelectorHandler.Handler() {
			
			@Override
			public boolean doHandle(SelectionKey key, Iterator<SelectionKey> ite) {

				try {
					
					if (key.isAcceptable()) {
	
						// Accept
						
						ite.remove();
						
						ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
						SocketChannel channel = ssc.accept();
						if(channel == null) return false;
						
						channel.configureBlocking(false);
						channel.register(selector, SelectionKey.OP_READ);
					}
					
					if(key.isReadable()) {
						
						// Readable
						
						SocketChannel sc = (SocketChannel) key.channel();
						NIOHttpHandlerThread httpHandler = new NIOHttpHandlerThread(sc);
						httpHandlerThreadPool.execute(httpHandler);
					}
					
				} catch (Throwable e) {
					LogUtil.error("", e);
				}
				
				return false;
			}
		});
	}
}
