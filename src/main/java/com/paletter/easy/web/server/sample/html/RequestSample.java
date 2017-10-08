package com.paletter.easy.web.server.sample.html;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.paletter.iotool.IOReadTool;

public class RequestSample {

	public static String HEADER_METHOD = "Method";
	public static String HEADER_URI = "Uri";
	public static String HEADER_PROTOCOL = "Protocol";
	public static String HEADER_HOST = "Host";
	public static String HEADER_CONNECTION = "Connection";
	public static String HEADER_CACHE_CONTROL = "Cache-Control";
	public static String HEADER_USER_AGENT = "User-Agent";
	public static String HEADER_UPGRADE_INSECURE_REQUESTS = "Upgrade-Insecure-Requests";
	public static String HEADER_ACCEPT = "Accept";
	public static String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	public static String HEADER_ACCEPT_LANGUAGE = "Accept-Language";
	
	private Socket socket;

	private Map<String, String> headerMap;
	
	public RequestSample(Socket socket) {
		super();
		this.socket = socket;
		headerMap = new HashMap<String, String>();
	}

	public void parse() throws Exception {
		
		String reqContent = IOReadTool.readContent(socket.getInputStream());
		System.out.println(reqContent);
		
		parseHeader(reqContent);
	}
	
	protected void parseHeader(String reqContent) {

		String[] headerArr = reqContent.split("\r\n");
		
		headerMap.put(HEADER_METHOD, headerArr[0].split(" ")[0]);
		headerMap.put(HEADER_URI, headerArr[0].split(" ")[1]);
		headerMap.put(HEADER_PROTOCOL, headerArr[0].split(" ")[2].replace("\r\n", ""));
			
		headerMap.put(HEADER_HOST, parseHeaderArr(headerArr, "Host"));
		headerMap.put(HEADER_CONNECTION, parseHeaderArr(headerArr, "Connection"));
		headerMap.put(HEADER_CACHE_CONTROL, parseHeaderArr(headerArr, "Cache-Control"));
		headerMap.put(HEADER_USER_AGENT, parseHeaderArr(headerArr, "User-Agent"));
		headerMap.put(HEADER_UPGRADE_INSECURE_REQUESTS, parseHeaderArr(headerArr, "Upgrade-Insecure-Requests"));
		headerMap.put(HEADER_ACCEPT, parseHeaderArr(headerArr, "Accept"));
		headerMap.put(HEADER_ACCEPT_ENCODING, parseHeaderArr(headerArr, "Accept-Encoding"));
		headerMap.put(HEADER_ACCEPT_LANGUAGE, parseHeaderArr(headerArr, "Accept-Language"));
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
	
	public String getHeader(String type) {
		
		return headerMap.get(type);
	}

	public Socket getSocket() {
		return socket;
	}
	
}
