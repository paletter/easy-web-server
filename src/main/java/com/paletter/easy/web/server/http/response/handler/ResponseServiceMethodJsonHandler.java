package com.paletter.easy.web.server.http.response.handler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.paletter.easy.web.server.constants.AppConstants;
import com.paletter.easy.web.server.http.request.Request;
import com.paletter.easy.web.server.http.response.ResponseStatusEnum;
import com.paletter.easy.web.server.http.response.writer.ResponseOutput;

public class ResponseServiceMethodJsonHandler extends ResponseAbstractHandler {

	public ResponseServiceMethodJsonHandler(ResponseOutput printer, Request request) {
		super(printer, request);
	}

	@Override
	public void doResponse() throws Exception {
		String reqUri = request.getHeader(AppConstants.RequestHeader.HEADER_URI);
		String method = request.getHeader(AppConstants.RequestHeader.HEADER_METHOD);
		if(method.equals("GET")) {
			
			String uriParam = reqUri.split("\\?")[1];
			String[] uriParams = uriParam.split("&");
			Map<String, String> paramsMap = new TreeMap<String, String>();
			List<String> methodParamValues = new ArrayList<String>();
			for(String param : uriParams) {
				try {
					String key = param.split("=")[0];
					String value = param.split("=")[1];
					paramsMap.put(key, value);
					if(!key.equals("service") && !key.equals("method")) methodParamValues.add(value);
				} catch (Exception e) {
					continue;
				}
			}
			
			String serviceParam = paramsMap.get("service");
			String methodParam = paramsMap.get("method");
			
			Class<?> serviceClass = Class.forName(serviceParam);
			Method[] methods = serviceClass.getMethods();
			for(Method m : methods) {
				if(m.getName().equals(methodParam)) {
					paramsMap.remove("service");
					paramsMap.remove("method");
					Object result = methodParamValues.size() > 0 ? 
							m.invoke(serviceClass.newInstance(), methodParamValues.toArray()) 
							: m.invoke(serviceClass.newInstance());
					
					String respStr = convertToJSONResult(result);
							
					printer.writeHeader(ResponseStatusEnum.OK, AppConstants.ContentType.APPLICATION_JSON, respStr.length());
					
					printer.print(respStr);
				}
			}
		}
	}

}
