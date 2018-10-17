package com.paletter.easy.web.server.http;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.paletter.easy.web.server.constants.AppConstants;

public abstract class Request {
	
	protected Map<String, String> headerMap = new HashMap<String, String>();
	protected String body = "";
	protected boolean isParseSucc = false;
	
	public abstract boolean parse() throws Exception;
	
	protected void parseHeader(String reqContent) {

		try {
			
			String[] headerArr = reqContent.split("\r\n");
			
			headerMap.put(AppConstants.RequestHeader.HEADER_METHOD, headerArr[0].split(" ")[0]);
			
			System.out.println(headerArr[0]);
			
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
			
			System.out.println("# ContentType: " + parseHeaderArr(headerArr, "Content-Type"));
			
			isParseSucc = true;
		} catch (Exception e) {
			isParseSucc = false;
		}
	}
	
	protected String parseHeaderArr(String[] headerArr, String type) {
		
		for(int i = 1; i < headerArr.length; i ++) {
			String headerLine = headerArr[i];
			if(headerLine.startsWith(type)) {
				return headerLine.split(" ")[1].replace("\r\n", "");
			}
		}
		
		return "";
	}
	
	protected void parseBody(String reqContent) {
		
		String[] contentArr = reqContent.split("\r\n");
		if (contentArr.length > 2 && contentArr[contentArr.length - 2].length() == 0) {
			body = contentArr[contentArr.length - 1];
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
			e.printStackTrace();
		}
		return uri;
	}
}
