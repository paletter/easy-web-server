package com.paletter.easy.web.server.sample.html;

import java.net.Socket;

import com.paletter.iotool.IOWriterTool;

public class Client {

	public static void main(String[] args) throws Exception {
		
		Socket s = new Socket("127.0.0.1", 1001);
		IOWriterTool.printlnWithFlush(s.getOutputStream(), "C");
		
		while(true) {
			
		}
	}
}
