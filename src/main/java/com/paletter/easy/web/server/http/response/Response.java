package com.paletter.easy.web.server.http.response;

import com.paletter.easy.web.server.config.EWSConfig;
import com.paletter.easy.web.server.constants.AppConstants;
import com.paletter.easy.web.server.http.request.Request;
import com.paletter.easy.web.server.http.response.handler.ResponseDownloadHandler;
import com.paletter.easy.web.server.http.response.handler.ResponseMappingJsonHandler;
import com.paletter.easy.web.server.http.response.handler.ResponseServiceMethodJsonHandler;
import com.paletter.easy.web.server.http.response.handler.ResponseWebResourceHandler;
import com.paletter.easy.web.server.http.response.writer.ResponseOutput;
import com.paletter.easy.web.server.utils.StringUtils;

public class Response {

	private Request request;
	private ResponseOutput printer;
	
	private ResponseServiceMethodJsonHandler responseServiceMethodJsonHandler;
	private ResponseMappingJsonHandler responseMappingJsonHandler;
	private ResponseWebResourceHandler responseWebResourceHandler;
	private ResponseDownloadHandler responseDownloadHandler;
	
	public Response(Request request, ResponseOutput printer) {
		super();
		this.request = request;
		this.printer = printer;
		
		responseServiceMethodJsonHandler = new ResponseServiceMethodJsonHandler(printer, request);
		responseMappingJsonHandler = new ResponseMappingJsonHandler(printer, request);
		responseWebResourceHandler = new ResponseWebResourceHandler(printer, request);
		responseDownloadHandler = new ResponseDownloadHandler(printer, request);
	}

	public void response() throws Exception {
		
		if (EWSConfig.isFileServer) {
			
			responseDownloadHandler.doResponse();
			
		} else {
		
			if(isResourceRequest()) {
				
				responseWebResourceHandler.doResponse();
				
			} else if(isServiceRequest()) {
				
				if (EWSConfig.isAnnotationMappingService) {
					responseMappingJsonHandler.doResponse();
				} else {
					responseServiceMethodJsonHandler.doResponse();
				}
				
			}
		}
		
		printer.close();
	}
	
	private boolean isResourceRequest() {
		String uriPath = request.getURI().getPath();
		return uriPath.split("\\.").length > 1;
	}
	
	private boolean isServiceRequest() {
		String contentType = request.getHeader(AppConstants.RequestHeader.HEADER_CONTENT_TYPE);
		return !isResourceRequest() && 
				(StringUtils.isEmpty(contentType) 
						|| contentType.contains(AppConstants.ContentType.APPLICATION_JSON)
						|| contentType.contains(AppConstants.ContentType.APPLICATION_XML)
						|| contentType.contains(AppConstants.ContentType.APPLICATION_FORM));
	}
	
}
