package com.paletter.easy.web.server.http;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ResponseOutputNIO extends ResponseOutput {

	private SocketChannel socketChannel;
	
	public ResponseOutputNIO(SocketChannel socketChannel) {
		super();
		this.socketChannel = socketChannel;
	}

	@Override
	public void println(String str) throws IOException {
		str += "\r\n";
		write(str);
	}

	@Override
	public void write(String str) throws IOException {
		socketChannel.write(ByteBuffer.wrap(str.getBytes()));
	}

	@Override
	public void write(byte[] b) throws IOException {
		socketChannel.write(ByteBuffer.wrap(b));
	}

	@Override
	public void close() throws IOException {
		socketChannel.close();
	}
}
