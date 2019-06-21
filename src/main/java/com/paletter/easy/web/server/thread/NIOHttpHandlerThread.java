package com.paletter.easy.web.server.thread;

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

public class NIOHttpHandlerThread implements Runnable {

	private SocketChannel socketChannel;

	public NIOHttpHandlerThread(SocketChannel channel) {
		this.socketChannel = channel;
	}
	
	@Override
	public void run() {
		
		ResponseOutputNIO printer = new ResponseOutputNIO(socketChannel);
		Request request = null;
		
		try {
			
			request = new RequestNIO(socketChannel);
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
			
		}
	}
	
	private void printer(ResponseOutputNIO printer, ResponseStatusEnum status, String contentType, String msg) {
		
		try {
			
			printer.writeHeader(status, contentType);
			printer.print(msg);
			printer.close();
			
		} catch (Throwable e) {
			LogUtil.error("", e);
		}
	}
}
