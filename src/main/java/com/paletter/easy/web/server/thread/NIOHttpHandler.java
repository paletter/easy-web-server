package com.paletter.easy.web.server.thread;

import java.nio.channels.SocketChannel;

import com.paletter.easy.web.server.http.request.Request;
import com.paletter.easy.web.server.http.request.RequestNIO;
import com.paletter.easy.web.server.http.response.Response;
import com.paletter.easy.web.server.http.response.writer.ResponseOutputNIO;
import com.paletter.easy.web.server.utils.LogUtil;

public class NIOHttpHandler implements Runnable {

	private SocketChannel socketChannel;

	public NIOHttpHandler(SocketChannel channel) {
		this.socketChannel = channel;
	}
	
	@Override
	public void run() {
		
		try {
			
			Request request = new RequestNIO(socketChannel);
			request.parse();
			
			if(request.isParseSucc()) {
				Response resp = new Response(request, new ResponseOutputNIO(socketChannel));
				resp.response();
			}
			
		} catch (Throwable e) {
			LogUtil.error("NIOHttpHandlerThread error.", e);
		}
	}
}
