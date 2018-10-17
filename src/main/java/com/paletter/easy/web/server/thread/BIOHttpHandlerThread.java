package com.paletter.easy.web.server.thread;

import java.net.Socket;
import java.net.SocketException;

import com.paletter.easy.web.server.http.Request;
import com.paletter.easy.web.server.http.RequestBIO;
import com.paletter.easy.web.server.http.Response;
import com.paletter.easy.web.server.http.ResponsePrinterBIO;

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
				Response resp = new Response(request, new ResponsePrinterBIO(socket));
				resp.response();
			}
		
		} catch (Exception e) {
			if(!(e instanceof SocketException)) {
				System.out.println("Server error.");
				e.printStackTrace();
			}
		}
	}
}
