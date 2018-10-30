package com.paletter.easy.web.server.http;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.paletter.easy.web.server.constants.AppConstants;
import com.paletter.easy.web.server.utils.StringUtils;

public abstract class ResponseAbstractHandler {

	protected Request request;
	protected ResponseOutput printer;
	
	public ResponseAbstractHandler(ResponseOutput printer, Request request) {
		this.printer = printer;
		this.request = request;
	}

	public abstract void doResponse() throws Exception;

	protected void writeHeader(ResponseStatusEnum status, String contentType, long length) throws IOException {
		printer.writeHeader(status, contentType, length);
	}
	
	protected void writeNotFoundStatus() throws IOException {
		printer.writeNotFoundStatus();
	}
	
	protected void println(String str) throws IOException {
		printer.println(str);
	}
	
	protected List<String> parseMethodGETParamValues() {
		URI uri = request.getURI();
		List<String> methodParamValues = new ArrayList<String>();
		if (StringUtils.isNotEmpty(uri.getQuery())) {
			String[] uriParams = uri.getQuery().split("&");
			for(String param : uriParams) {
				String value = param.split("=")[1];
				methodParamValues.add(value);
			}
		}
		return methodParamValues;
	}
	
	protected List<String> parseMethodPOSTParamValues() {
		String body = request.getBody();
		String contentType = request.getHeader(AppConstants.RequestHeader.HEADER_CONTENT_TYPE);
		List<String> methodParamValues = new ArrayList<String>();
		if (StringUtils.isNotEmpty(contentType) && contentType.equals(AppConstants.ContentType.APPLICATION_FORM)) {
			if (StringUtils.isNotEmpty(body)) {
				String[] uriParams = body.split("&");
				for(String param : uriParams) {
					String value = param.split("=")[1];
					methodParamValues.add(value);
				}
			}
		}
		return methodParamValues;
	}
}
