package com.paletter.easy.web.server.http;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class RequestNIO extends Request {
	
	private SocketChannel channel;
	
	public RequestNIO(SocketChannel socketChannel) {
		super();
		this.channel = socketChannel;
	}

	public boolean parse() throws Exception {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int bufferLenght = 1024;
		ByteBuffer b = ByteBuffer.allocate(bufferLenght);
		int ins = channel.read(b);
		if(ins == -1) {
			isParseSucc = false;
			return isParseSucc;
		}
		
		baos.write(b.array(), 0, ins);
		while(ins == bufferLenght) {
			ins = channel.read(b);
			if(ins == -1) break;
			baos.write(b.array(), 0, ins);
		}
		
		String reqContent = baos.toString();
		
		System.out.println("# Request Content: " + reqContent);
		
		parseHeader(reqContent);
		parseBody(reqContent);
		
		return isParseSucc;
	}
}
