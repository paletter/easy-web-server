package com.paletter.easy.web.server.http.response.handler;

import com.alibaba.fastjson.JSONObject;
import com.paletter.easy.web.server.http.request.Request;
import com.paletter.easy.web.server.http.response.writer.ResponseOutput;

public abstract class ResponseAbstractHandler {

	protected Request request;
	protected ResponseOutput printer;
	
	public ResponseAbstractHandler(ResponseOutput printer, Request request) {
		this.printer = printer;
		this.request = request;
	}

	public abstract void doResponse() throws Exception;
	
	protected String convertToJSONResult(Object result) {
		if (result == null) return "{}";
		
		String resultStr;
		if (result.getClass().equals(String.class)) {
			resultStr = result.toString();
		} else {
			resultStr = JSONObject.toJSONString(result);
		}
		
		return resultStr;
	}
}
