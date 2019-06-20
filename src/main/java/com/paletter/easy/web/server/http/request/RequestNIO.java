package com.paletter.easy.web.server.http.request;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.paletter.easy.web.server.utils.LogUtil;

public class RequestNIO extends Request {
	
	private SocketChannel channel;
	
	public RequestNIO(SocketChannel socketChannel) {
		super();
		this.channel = socketChannel;
	}

	public boolean parse() throws Exception {

		if (channel == null || !channel.isConnected() || !channel.isOpen()) return false;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int bufferLenght = 1024;
		ByteBuffer b = ByteBuffer.allocate(bufferLenght);
		
//		System.out.println("*******" + channel.isOpen() + "|" + channel.isConnected());
//		System.out.println("*********Read" + channel.hashCode() + "|" + channel.getRemoteAddress());
		
		int ins = channel.read(b);
		if (ins == 0) return false;
		
//		System.out.println("*********Read2" + channel.hashCode() + "|" + channel.getRemoteAddress() + "|" + ins);
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
//		System.out.println("*********Read3" + channel.hashCode() + "|" + channel.getRemoteAddress() + "|" +reqContent);
		LogUtil.printDebug("# Request Content: " + reqContent);
		
		parseHeader(reqContent);
		parseBody(reqContent);
		parseParameters();
		
		return isParseSucc;
	}
}
