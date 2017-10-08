package com.paletter.easy.web.server;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import com.paletter.easy.web.server.thread.SelectorThread;

public class Runner {

	public static void main(String[] args) {

		try {
			
			int port = 8080;
			int httpHandlerThreadSize = 100;
			
			ServerSocketChannel server = ServerSocketChannel.open();
			server.socket().bind(new InetSocketAddress(port));
			server.configureBlocking(false);
			System.out.println("Server startup on " + port);
			
			Selector selector = Selector.open();
			server.register(selector, SelectionKey.OP_ACCEPT);
			
			SelectorThread selectorThread = new SelectorThread(selector, httpHandlerThreadSize);
			selectorThread.setName("SelectorThread");
			selectorThread.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
