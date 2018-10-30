package com.paletter.easy.web.server.http;

import java.io.IOException;

import com.paletter.easy.web.server.constants.AppConstants;
import com.paletter.easy.web.server.utils.StringUtils;

public abstract class ResponseOutput {

	public void writeHeader(ResponseStatusEnum status, String contentType, long length) throws IOException {
		println("HTTP/1.1 " + status.getStatus() + " " + status.getMsg() + "");
		println("Server: Apache-Coyote/1.1");
		
		if (StringUtils.isNotEmpty(contentType))
			println("Content-Type: " + contentType);
		
		if (length > 0)
			println("Content-Length: " + length);
		
		println("");
	}
	
	public void writeNotFoundStatus() throws IOException {
		ResponseStatusEnum status = ResponseStatusEnum.NOT_FOUND;
		String content = status.getMsg();
		writeHeader(status, AppConstants.ContentType.TEXT_HTML, content.length());
		
		println(content);
	}
	
	public abstract void println(String str) throws IOException;
	public abstract void write(byte[] b) throws IOException;
	public abstract void write(String str) throws IOException;
	public abstract void close() throws IOException;
}
