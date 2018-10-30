package com.paletter.easy.web.server.thread;

import java.nio.channels.SocketChannel;

import com.paletter.easy.web.server.http.Request;
import com.paletter.easy.web.server.http.RequestNIO;
import com.paletter.easy.web.server.http.Response;
import com.paletter.easy.web.server.http.ResponseOutputNIO;

public class NIOHttpHandlerThread extends Thread {

	private SocketChannel socketChannel;

	public NIOHttpHandlerThread(SocketChannel channel) {
		this.socketChannel = channel;
	}
	
	public void run() {
		
		try {
			
			Request request = new RequestNIO(socketChannel);
			request.parse();
			
			if(request.isParseSucc()) {
				Response resp = new Response(request, new ResponseOutputNIO(socketChannel));
				resp.response();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
