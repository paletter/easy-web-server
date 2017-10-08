package com.paletter.easy.web.server.sample.html;

import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.net.URL;

import com.paletter.iotool.IOReadTool;
import com.paletter.iotool.IOWriterTool;

public class ResponseSample {

	private RequestSample request;
	private Socket socket;
	
	public ResponseSample(RequestSample request, Socket socket) {
		super();
		this.request = request;
		this.socket = socket;
	}

	public void sendStaticResource() throws Exception {

		String uri = request.getHeader(RequestSample.HEADER_URI);
		
		URL url = getClass().getClassLoader().getResource("static/" + uri.substring(1));
		File f = new File(url.getPath());
		
		String str = "HTTP/1.1 200 OK\r\n";
		str += "Server: Apache-Coyote/1.1\r\n";
		str += "Content-Type: text/html\r\n";
		str += "Content-Length: " + f.length() + "\r\n";
		str += "\r\n";
		
		FileInputStream fis = new FileInputStream(f);

		str += IOReadTool.readContent(fis);
		
		IOWriterTool.writeContent(socket.getOutputStream(), str);
	}
}
