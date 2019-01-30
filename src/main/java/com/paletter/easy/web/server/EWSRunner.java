package com.paletter.easy.web.server;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import com.paletter.easy.web.server.config.EWSConfig;
import com.paletter.easy.web.server.support.WebMapHelper;
import com.paletter.easy.web.server.thread.BIOAcceptThread;
import com.paletter.easy.web.server.thread.NIOAcceptThread;
import com.paletter.easy.web.server.utils.LogUtil;
import com.paletter.easy.web.server.utils.StringUtils;

public class EWSRunner {

	public static void run() {

		try {
			
			int port = EWSConfig.port;
			
			LogUtil.setLevel(EWSConfig.logLevel);
			
			if (EWSConfig.isAnnotationMappingService) {
				
				if (StringUtils.isEmpty(EWSConfig.webMappingScannerPath)) {
					LogUtil.printDebug("EWSConfig.webMappingScannerPath null.");
					return;
				}
				
				WebMapHelper.scanner(EWSConfig.webMappingScannerPath);
			}
			
			// BIO
			if (EWSConfig.ioMode == 1) {
				
				ServerSocket server = new ServerSocket(port);
				LogUtil.print("Server startup on " + port);
				
				BIOAcceptThread accept = new BIOAcceptThread(server, EWSConfig.httpHandlerThreadSize);
				accept.start();
			}
			
			// NIO
			if (EWSConfig.ioMode == 2) {
				
				ServerSocketChannel server = ServerSocketChannel.open();
				server.socket().bind(new InetSocketAddress(port));
				server.configureBlocking(false);
				LogUtil.print("Server startup on " + port);
				
				Selector selector = Selector.open();
				server.register(selector, SelectionKey.OP_ACCEPT);
				
				NIOAcceptThread accpetThread = new NIOAcceptThread(selector, EWSConfig.httpHandlerThreadSize);
				accpetThread.setName("SelectorThread");
				accpetThread.start();
			}

		} catch (Exception e) {
			LogUtil.error("", e);
		}
	}
}
