package com.paletter.easy.web.server.support;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import com.paletter.easy.web.server.constants.AppConstants;
import com.paletter.easy.web.server.exception.InvokeMethodError;
import com.paletter.easy.web.server.exception.InvokeMethodParamLengthError;
import com.paletter.easy.web.server.http.request.Request;
import com.paletter.easy.web.server.http.request.RequestNIO;
import com.paletter.easy.web.server.http.response.Response;
import com.paletter.easy.web.server.http.response.ResponseStatusEnum;
import com.paletter.easy.web.server.http.response.writer.ResponseOutputNIO;
import com.paletter.easy.web.server.utils.LogUtil;

public class NIOHttpHandler {

	private SocketChannel socketChannel;

	public NIOHttpHandler(SocketChannel channel) {
		this.socketChannel = channel;
	}
	
	public void doHandle() {
		
		ResponseOutputNIO printer = new ResponseOutputNIO(socketChannel);
		
		try {
			
			Request request = new RequestNIO(socketChannel);
			request.parse();
			
			if(request.isParseSucc()) {
				Response resp = new Response(request, printer);
				resp.response();
			}
		
		} catch (InvokeMethodParamLengthError e) {
			
			printer(printer, ResponseStatusEnum.BAD_REQUEST, AppConstants.ContentType.TEXT_HTML, "<h4>400</h4>" + e.getMessage());
			
		} catch (InvokeMethodError e) {
			
			printer(printer, ResponseStatusEnum.BAD_REQUEST, AppConstants.ContentType.TEXT_HTML, "<h4>400</h4>" + e.getMessage());
			
		} catch (Throwable e) {
			
			LogUtil.error("NIOHttpHandler error.", e);
			
		} finally {
			
			try {
				printer.close();
			} catch (IOException e) {
			}
		}
	}
	
	private void printer(ResponseOutputNIO printer, ResponseStatusEnum status, String contentType, String msg) {
		
		try {
			
			printer.writeHeader(status, contentType);
			printer.print(msg);
			
		} catch (Throwable e) {
			LogUtil.error("", e);
		}
	}
}
