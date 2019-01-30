package com.paletter.easy.web.server.http.response.handler;

import java.io.File;
import java.io.FileInputStream;

import com.paletter.easy.web.server.config.EWSConfig;
import com.paletter.easy.web.server.constants.AppConstants;
import com.paletter.easy.web.server.http.request.Request;
import com.paletter.easy.web.server.http.response.ResponseStatusEnum;
import com.paletter.easy.web.server.http.response.writer.ResponseOutput;

public class ResponseDownloadHandler extends ResponseAbstractHandler {

	public ResponseDownloadHandler(ResponseOutput printer, Request request) {
		super(printer, request);
	}

	@Override
	public void doResponse() throws Exception {
		
		if (request.getURI() == null) return;
		
		File file = new File(EWSConfig.downloadFilePath + request.getURI().getPath());

		if(!file.exists()) {
			printer.writeNotFoundStatus();
			return;
		}
		
		printer.writeHeader(ResponseStatusEnum.OK, AppConstants.ContentType.APPLICATION_OCTET_STREAM, file.length());
		
		FileInputStream fis = new FileInputStream(file);
		byte[] b = new byte[1024];
		while(fis.read(b) != -1) {
			printer.write(b);
		}
		
		fis.close();
	}
}
