package com.paletter.easy.web.server.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import com.paletter.easy.web.server.config.AppConfig;
import com.paletter.easy.web.server.constants.AppConstants;

public class ResponseWebResourceHandler extends ResponseAbstractHandler {

	public ResponseWebResourceHandler(ResponsePrinter printer, Request request) {
		super(printer, request);
	}

	@Override
	public void doResponse() throws Exception {

		String path = request.getURI().getPath();
		
		String contentType = null;
		
		if(path.endsWith(".html")) {
			contentType = AppConstants.ContentType.TEXT_HTML;
		} else if(path.endsWith(".js")) {
			contentType = AppConstants.ContentType.APPLICATION_XJAVASCRIPT;
		} else if(path.endsWith(".css")) {
			contentType = AppConstants.ContentType.TEXT_CSS;
		} else if(path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".gif") || path.endsWith(".ico")) {
			contentType = AppConstants.ContentType.IMG + path.split("\\.")[1];
		}
		
		writeWebResource(path, contentType);
	}
	
	private void writeWebResource(String uri, String contentType) throws IOException {

		URL url = getClass().getClassLoader().getResource(AppConfig.htmlPath + uri.substring(1));
		if (url == null) {
			writeNotFoundStatus();
			return;
		}
		
		File file = new File(url.getPath());
		
		if(!file.exists()) {
			writeNotFoundStatus();
			return;
		}
		
		writeHeader(ResponseStatusEnum.OK, contentType, file.length());
		
		FileInputStream fis = new FileInputStream(file);
		byte[] b = new byte[1024];
		while(fis.read(b) != -1) {
			printer.write(b);
		}
		
		fis.close();
	}

}
