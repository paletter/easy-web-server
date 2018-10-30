package com.paletter.easy.web.server.http;

import java.io.IOException;
import java.net.Socket;

import com.paletter.iotool.IOWriterTool;

public class ResponseOutputBIO extends ResponseOutput {

	private Socket socket;
	
	public ResponseOutputBIO(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void println(String str) throws IOException {
		str += "\r\n";
		write(str);
	}
	
	@Override
	public void write(String str) throws IOException {
		IOWriterTool.writeContent(socket.getOutputStream(), str);
	}

	@Override
	public void write(byte[] b) throws IOException {
		socket.getOutputStream().write(b);
		socket.getOutputStream().flush();
	}

	@Override
	public void close() throws IOException {
		socket.close();
	}
}
