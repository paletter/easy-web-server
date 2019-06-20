package com.paletter.easy.web.server.http.response.writer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.paletter.easy.web.server.config.EWSConfig;
import com.paletter.easy.web.server.utils.LogUtil;

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
	public void write(String str) {
		if (socketChannel.isOpen()) {
			try {
				socketChannel.write(ByteBuffer.wrap(str.getBytes(EWSConfig.responseEncode)));
				System.out.println("#############write" +  socketChannel.hashCode() + "|" + str);
			} catch (UnsupportedEncodingException e) {
				LogUtil.error("ResponseOutputNIO write error." + e.getMessage());
			} catch (IOException e) {
				LogUtil.error("ResponseOutputNIO write error." + e.getMessage());
			}
		}
	}

	@Override
	public void write(byte[] b) {
		if (socketChannel.isOpen()) {
			try {
				ByteBuffer wb = ByteBuffer.allocateDirect(1024);
				wb.put(b);
				wb.flip();
				socketChannel.write(wb);
				System.out.println("#############write" +  socketChannel.hashCode() + "|" + new String(b));
			} catch (IOException e) {
				LogUtil.error("ResponseOutputNIO write error." + e.getMessage());
			}
		}
	}

	@Override
	public void close() throws IOException {
		socketChannel.close();
		System.out.println("#############close" +  socketChannel.hashCode());
	}
}
