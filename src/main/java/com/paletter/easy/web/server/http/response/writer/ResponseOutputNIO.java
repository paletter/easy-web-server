package com.paletter.easy.web.server.http.response.writer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.paletter.easy.web.server.config.EWSConfig;

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
	public void print(String str) throws IOException {
		write(str);
	}

	@Override
	public void write(String str) throws IOException {
		if (socketChannel.isOpen()) {
			socketChannel.write(ByteBuffer.wrap(str.getBytes(EWSConfig.responseEncode)));
		}
	}

	@Override
	public void write(byte[] b) throws IOException {
		if (socketChannel.isOpen()) {
			socketChannel.write(ByteBuffer.wrap(b));
		}
	}

	@Override
	public void close() throws IOException {
		socketChannel.close();
	}
}
