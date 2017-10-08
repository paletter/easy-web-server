package com.paletter.easy.web.server.thread;

import java.nio.channels.SocketChannel;

import com.paletter.easy.web.server.http.Request;
import com.paletter.easy.web.server.http.Response;

public class HttpHandlerThread extends Thread {

	private SocketChannel socketChannel;

	public HttpHandlerThread(SocketChannel channel) {
		super();
		this.socketChannel = channel;
	}
	
	public void run() {
		
		try {
			
			Request request = new Request(socketChannel);
			request.parse();
			
			if(request.isParseSucc()) {
				Response resp = new Response(request, socketChannel);
				resp.response();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
