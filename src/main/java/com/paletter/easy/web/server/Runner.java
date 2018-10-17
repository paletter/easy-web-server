package com.paletter.easy.web.server;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import com.paletter.easy.web.server.config.AppConfig;
import com.paletter.easy.web.server.support.WebMapHelper;
import com.paletter.easy.web.server.thread.BIOAcceptThread;
import com.paletter.easy.web.server.thread.NIOAcceptThread;

public class Runner {

	public static void main(String[] args) {

		try {
			
			int port = 8080;
			
			if (AppConfig.isAnnotationMappingService) {
				WebMapHelper.scanner(AppConfig.webMappingScannerPath);
			}
			
			// BIO
			if (AppConfig.ioMode == 1) {
				
				ServerSocket server = new ServerSocket(port);
				System.out.println("Server startup on " + port);
				
				BIOAcceptThread accept = new BIOAcceptThread(server, AppConfig.httpHandlerThreadSize);
				accept.start();
			}
			
			// NIO
			if (AppConfig.ioMode == 2) {
				
				ServerSocketChannel server = ServerSocketChannel.open();
				server.socket().bind(new InetSocketAddress(port));
				server.configureBlocking(false);
				System.out.println("Server startup on " + port);
				
				Selector selector = Selector.open();
				server.register(selector, SelectionKey.OP_ACCEPT);
				
				NIOAcceptThread accpetThread = new NIOAcceptThread(selector, AppConfig.httpHandlerThreadSize);
				accpetThread.setName("SelectorThread");
				accpetThread.start();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
