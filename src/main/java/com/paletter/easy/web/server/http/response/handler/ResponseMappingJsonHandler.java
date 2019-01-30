package com.paletter.easy.web.server.http.response.handler;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.paletter.easy.web.server.constants.AppConstants;
import com.paletter.easy.web.server.http.request.Request;
import com.paletter.easy.web.server.http.response.ResponseStatusEnum;
import com.paletter.easy.web.server.http.response.writer.ResponseOutput;
import com.paletter.easy.web.server.support.WebMapHelper;
import com.paletter.easy.web.server.support.WebMapper;
import com.paletter.easy.web.server.utils.LogUtil;
import com.paletter.easy.web.server.utils.StringUtils;

public class ResponseMappingJsonHandler extends ResponseAbstractHandler {

	public ResponseMappingJsonHandler(ResponseOutput printer, Request request) {
		super(printer, request);
	}

	@Override
	public void doResponse() throws Exception {
		URI uri = request.getURI();
		if (uri == null) return;
		
		String mappingPath = uri.getPath().split("\\.")[0];
		WebMapper webMapper = WebMapHelper.getMapper(mappingPath);
		
		if (webMapper != null) {
			
			String method = request.getHeader(AppConstants.RequestHeader.HEADER_METHOD);
			if(method.equals("GET")) {
				
				List<String> methodParamValues = request.getRequestParam().getGetMethodParamValues();
				String[] methodParamStrValues = new String[methodParamValues.size()];
				methodParamValues.toArray(methodParamStrValues);
				
				Object result = webMapper.invoke(methodParamStrValues);
				
				String respStr = convertToJSONResult(result);
			
				printer.writeHeader(ResponseStatusEnum.OK, AppConstants.ContentType.APPLICATION_JSON);
				
				printer.print(respStr);
				
			} else if(method.equals("POST")) {
				
				String contentType = request.getHeader(AppConstants.RequestHeader.HEADER_CONTENT_TYPE);
				
				if (StringUtils.isNotEmpty(contentType)) {
				
					if (contentType.contains(AppConstants.ContentType.APPLICATION_FORM)) {
						
						List<String> methodParamValues = request.getRequestParam().getPostMethodParamValues();
						String[] methodParamStrValues = new String[methodParamValues.size()];
						methodParamValues.toArray(methodParamStrValues);
						
						Object result = webMapper.invoke(methodParamStrValues);
						
						String respStr = convertToJSONResult(result);
						
						printer.writeHeader(ResponseStatusEnum.OK, AppConstants.ContentType.APPLICATION_JSON, respStr.length());
						
						printer.print(respStr);
						
					} else if (contentType.contains(AppConstants.ContentType.APPLICATION_JSON)) {
	
						Method targetMethod = webMapper.getMethod();
						Parameter[] params = targetMethod.getParameters();
						
						if (params.length == 0) {
							
							Object result = webMapper.invoke();
							String respStr = convertToJSONResult(result);
							
							printer.writeHeader(ResponseStatusEnum.OK, AppConstants.ContentType.APPLICATION_JSON, respStr.length());
							printer.print(respStr);
						}
						
						if (params.length > 0) {
							Parameter param = params[0];
							Object paramObj = JSONObject.parseObject(request.getRequestParam().getJsonBody(), 
									Class.forName(param.getParameterizedType().getTypeName()));
							Object result = webMapper.invoke(paramObj);
							
							String respStr = convertToJSONResult(result);
							
							printer.writeHeader(ResponseStatusEnum.OK, AppConstants.ContentType.APPLICATION_JSON, respStr.length());
							printer.print(respStr);
						}
					}
				}
				
			} else if(method.equals("OPTIONS")) {
				printer.writeHeader(ResponseStatusEnum.OK, AppConstants.ContentType.APPLICATION_JSON);
				printer.print("{}");
			}
		}
	}
}
