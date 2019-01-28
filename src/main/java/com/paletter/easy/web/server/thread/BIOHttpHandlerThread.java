package com.paletter.easy.web.server.thread;

import java.net.Socket;
import java.net.SocketException;

import com.paletter.easy.web.server.http.request.Request;
import com.paletter.easy.web.server.http.request.RequestBIO;
import com.paletter.easy.web.server.http.response.Response;
import com.paletter.easy.web.server.http.response.writer.ResponseOutputBIO;
import com.paletter.easy.web.server.utils.LogUtil;

public class BIOHttpHandlerThread extends Thread {

	private Socket socket;
	
	public BIOHttpHandlerThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		
		try {
				
			Request request = new RequestBIO(socket);
			request.parse();
			
			if(request.isParseSucc()) {
				Response resp = new Response(request, new ResponseOutputBIO(socket));
				resp.response();
			}
		
		} catch (Exception e) {
			if(!(e instanceof SocketException)) {
				LogUtil.error("", e);
			}
		}
	}
}
