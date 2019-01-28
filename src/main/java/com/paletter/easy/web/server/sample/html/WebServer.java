package com.paletter.easy.web.server.sample.html;

import java.net.ServerSocket;
import java.net.Socket;

import com.paletter.easy.web.server.utils.LogUtil;

public class WebServer {

	public static void main(String[] args) throws Exception {
		
		ServerSocket ss = new ServerSocket(1001);
		
		Socket s = ss.accept();
		
		LogUtil.printDebug("Accpet:" + s);
		
		RequestSample r = new RequestSample(s);
		r.parse();
		
		ResponseSample resp = new ResponseSample(r, s);
		
		resp.sendStaticResource();
	}
}
