package com.paletter.easy.web.server.sample.html;

import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

	public static void main(String[] args) throws Exception {
		
		ServerSocket ss = new ServerSocket(1001);
		
		Socket s = ss.accept();
		
		System.out.println("Accpet:" + s);
		
		RequestSample r = new RequestSample(s);
		r.parse();
		
		ResponseSample resp = new ResponseSample(r, s);
		
		resp.sendStaticResource();
	}
}
