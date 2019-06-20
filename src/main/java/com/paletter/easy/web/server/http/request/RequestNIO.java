package com.paletter.easy.web.server.http.request;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
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
		
		int ins = 0;
		try {
			ins = channel.read(b);
		} catch (Throwable e2) {
			if (!(e2 instanceof ClosedChannelException)) throw e2;
		}
		
		if(ins <= 0) return false;
		
		baos.write(b.array(), 0, ins);
		while(ins == bufferLenght) {
			ins = channel.read(b);
			if(ins == -1) break;
			baos.write(b.array(), 0, ins);
		}
		
		String reqContent = baos.toString();
		
		LogUtil.printDebug("# Request Content: " + reqContent);
		
		parseHeader(reqContent);
		parseBody(reqContent);
		parseParameters();
		
		return isParseSucc;
	}
}
