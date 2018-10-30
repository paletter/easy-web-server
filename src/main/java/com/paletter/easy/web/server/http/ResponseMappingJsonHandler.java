package com.paletter.easy.web.server.http;

import java.net.URI;
import java.util.List;

import com.paletter.easy.web.server.constants.AppConstants;
import com.paletter.easy.web.server.support.WebMapHelper;
import com.paletter.easy.web.server.support.WebMapHelper.WebMapper;

public class ResponseMappingJsonHandler extends ResponseAbstractHandler {

	public ResponseMappingJsonHandler(ResponseOutput printer, Request request) {
		super(printer, request);
	}

	@Override
	public void doResponse() throws Exception {
		URI uri = request.getURI();
		String mappingPath = uri.getPath().split("\\.")[0];
		WebMapper webMapper = WebMapHelper.getMapper(mappingPath);
		
		if (webMapper != null) {
			
			String method = request.getHeader(AppConstants.RequestHeader.HEADER_METHOD);
			if(method.equals("GET")) {
				
				List<String> methodParamValues = parseMethodGETParamValues();
				
				Object result = webMapper.invoke(methodParamValues.toArray());
				
				writeHeader(ResponseStatusEnum.OK, AppConstants.ContentType.APPLICATION_JSON, result.toString().length());
				
				println(result.toString());
				
			} else if(method.equals("POST")) {
				
				List<String> methodParamValues = parseMethodPOSTParamValues();
				
				Object result = webMapper.invoke(methodParamValues.toArray());
				
				writeHeader(ResponseStatusEnum.OK, AppConstants.ContentType.APPLICATION_JSON, result.toString().length());
				
				println(result.toString());
			}
		}
	}
}
