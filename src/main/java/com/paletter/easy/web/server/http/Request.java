package com.paletter.easy.web.server.http;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

public class Request {

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
	
	private SocketChannel channel;
	private Map<String, String> headerMap;
	private boolean isParseSucc;
	
	public Request(SocketChannel socketChannel) {
		super();
		this.channel = socketChannel;
		headerMap = new HashMap<String, String>();
		this.isParseSucc = false;
	}

	public boolean parse() throws Exception {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int bufferLenght = 1024;
		ByteBuffer b = ByteBuffer.allocate(bufferLenght);
		int ins = channel.read(b);
		if(ins == -1) {
			isParseSucc = false;
			return isParseSucc;
		}
		
		baos.write(b.array(), 0, ins);
		while(ins == bufferLenght) {
			ins = channel.read(b);
			if(ins == -1) break;
			baos.write(b.array(), 0, ins);
		}
		
		String reqContent = baos.toString();
		
		parseHeader(reqContent);
		
		return isParseSucc;
	}
	
	protected void parseHeader(String reqContent) {

		try {
			
			String[] headerArr = reqContent.split("\r\n");
			
			headerMap.put(HEADER_METHOD, headerArr[0].split(" ")[0]);
			
			System.out.println(headerArr[0]);
			
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
	
	public String getHeader(String type) {
		
		return headerMap.get(type);
	}

	public boolean isParseSucc() {
		return isParseSucc;
	}
}
