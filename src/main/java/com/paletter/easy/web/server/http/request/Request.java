package com.paletter.easy.web.server.http.request;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.paletter.easy.web.server.constants.AppConstants;
import com.paletter.easy.web.server.utils.LogUtil;
import com.paletter.easy.web.server.utils.StringUtils;

public abstract class Request {
	
	protected Map<String, String> headerMap = new HashMap<String, String>();
	protected String body = "";
	protected boolean isParseSucc = false;
	private RequestParameter requestParam = new RequestParameter();
	
	public abstract boolean parse() throws Exception;
	
	protected void parseHeader(String reqContent) {

		try {
			
			String[] headerArr = reqContent.split("\r\n");
			
			headerMap.put(AppConstants.RequestHeader.HEADER_METHOD, headerArr[0].split(" ")[0]);
			headerMap.put(AppConstants.RequestHeader.HEADER_URI, headerArr[0].split(" ")[1]);
			headerMap.put(AppConstants.RequestHeader.HEADER_PROTOCOL, headerArr[0].split(" ")[2].replace("\r\n", ""));
				
			headerMap.put(AppConstants.RequestHeader.HEADER_HOST, parseHeaderArr(headerArr, "Host"));
			headerMap.put(AppConstants.RequestHeader.HEADER_CONNECTION, parseHeaderArr(headerArr, "Connection"));
			headerMap.put(AppConstants.RequestHeader.HEADER_CACHE_CONTROL, parseHeaderArr(headerArr, "Cache-Control"));
			headerMap.put(AppConstants.RequestHeader.HEADER_USER_AGENT, parseHeaderArr(headerArr, "User-Agent"));
			headerMap.put(AppConstants.RequestHeader.HEADER_UPGRADE_INSECURE_REQUESTS, parseHeaderArr(headerArr, "Upgrade-Insecure-Requests"));
			headerMap.put(AppConstants.RequestHeader.HEADER_ACCEPT, parseHeaderArr(headerArr, "Accept"));
			headerMap.put(AppConstants.RequestHeader.HEADER_ACCEPT_ENCODING, parseHeaderArr(headerArr, "Accept-Encoding"));
			headerMap.put(AppConstants.RequestHeader.HEADER_ACCEPT_LANGUAGE, parseHeaderArr(headerArr, "Accept-Language"));
			headerMap.put(AppConstants.RequestHeader.HEADER_CONTENT_TYPE, parseHeaderArr(headerArr, "Content-Type"));
			
			LogUtil.printDebug("# ContentType: " + parseHeaderArr(headerArr, "Content-Type"));
			
			isParseSucc = true;
		} catch (Exception e) {
			LogUtil.printDebug("request parseHeader error." + e.getMessage());
			isParseSucc = false;
		}
	}
	
	private String parseHeaderArr(String[] headerArr, String type) {
		
		for(int i = 1; i < headerArr.length; i ++) {
			String headerLine = headerArr[i];
			if(headerLine.startsWith(type)) {
				return headerLine.replaceAll(type + ": ", "").replace("\r\n", "");
			}
		}
		
		return "";
	}
	
	protected void parseBody(String reqContent) {
		try {
			
			String[] contentArr = reqContent.split("\r\n");
			if (contentArr.length > 2 && contentArr[contentArr.length - 2].length() == 0) {
				body = contentArr[contentArr.length - 1];
			}
			
			isParseSucc = true;
		} catch (Exception e) {
			isParseSucc = false;
			LogUtil.printDebug("request parseBody error." + e.getMessage());
		}
	}
	
	protected void parseParameters() {
		
		// URI params
		URI uri = getURI();
		if (StringUtils.isNotEmpty(uri.getQuery())) {
			String[] uriParams = uri.getQuery().split("&");
			for(String param : uriParams) {
				requestParam.addGetMethodParam(param.split("=")[0], param.split("=")[1]);
			}
		}
		
		// body param
		String body = getBody();
		if (StringUtils.isNotEmpty(body)) {
			if (body.startsWith("{") && body.endsWith("}")) {
				// json
				requestParam.setJsonBody(body);
			} else {
				// form
				String[] uriParams = body.split("&");
				for(String param : uriParams) {
					requestParam.addPostMethodParam(param.split("=")[0], param.split("=")[1]);
				}
			}
		}
	}
	
	public String getHeader(String type) {
		
		return headerMap.get(type);
	}
	
	public String getBody() {
		
		return body;
	}

	public boolean isParseSucc() {
		return isParseSucc;
	}
	
	public URI getURI() {
		String reqUri = getHeader(AppConstants.RequestHeader.HEADER_URI);
		URI uri = null;
		try {
			uri = new URI(reqUri);
		} catch (URISyntaxException e) {
			LogUtil.error("Request.getURI() error.", e);
		}
		return uri;
	}

	public RequestParameter getRequestParam() {
		return requestParam;
	}
	
}
