package com.paletter.easy.web.server.thread;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.paletter.easy.web.server.support.NIOHttpHandler;
import com.paletter.easy.web.server.support.SelectorHandler;

public class NIOReadThread implements Runnable {

	private Selector selector;

	public NIOReadThread(Selector selector) {
		super();
		this.selector = selector;
	}

	@Override
	public void run() {
		
		new SelectorHandler(selector, new SelectorHandler.Handler() {
			
			@Override
			public boolean doHandle(SelectionKey key, Iterator<SelectionKey> ite) {

				if(key.isReadable()) {
					
					// Readable
					
					SocketChannel sc = (SocketChannel) key.channel();
					NIOHttpHandler httpHandler = new NIOHttpHandler(sc);
					httpHandler.doHandle();
					
					return !ite.hasNext();
				}
				
				return false;
			}
		});
	}
}
