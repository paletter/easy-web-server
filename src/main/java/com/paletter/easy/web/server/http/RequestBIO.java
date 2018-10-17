package com.paletter.easy.web.server.http;

import java.net.Socket;

import com.paletter.iotool.IOReadTool;

public class RequestBIO extends Request {
	
	private Socket socket;
	
	public RequestBIO(Socket socket) {
		super();
		this.socket = socket;
	}

	public boolean parse() throws Exception {
		
		String reqContent = IOReadTool.readContent(socket.getInputStream());
		
		System.out.println("# Request Content: " + reqContent);
		
		parseHeader(reqContent);
		parseBody(reqContent);
		
		return isParseSucc;
	}
}
