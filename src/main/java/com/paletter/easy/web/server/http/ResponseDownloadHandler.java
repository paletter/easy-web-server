package com.paletter.easy.web.server.http;

import java.io.File;
import java.io.FileInputStream;

import com.paletter.easy.web.server.config.AppConfig;
import com.paletter.easy.web.server.constants.AppConstants;

public class ResponseDownloadHandler extends ResponseAbstractHandler {

	public ResponseDownloadHandler(ResponsePrinter printer, Request request) {
		super(printer, request);
	}

	@Override
	public void doResponse() throws Exception {
		
		File file = new File(AppConfig.downloadFilePath + request.getURI().getPath());

		if(!file.exists()) {
			writeNotFoundStatus();
			return;
		}
		
		writeHeader(ResponseStatusEnum.OK, AppConstants.ContentType.APPLICATION_OCTET_STREAM, file.length());
		
		FileInputStream fis = new FileInputStream(file);
		byte[] b = new byte[1024];
		while(fis.read(b) != -1) {
			printer.write(b);
		}
		
		fis.close();
	}
}
