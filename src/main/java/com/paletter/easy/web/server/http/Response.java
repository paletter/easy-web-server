package com.paletter.easy.web.server.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.paletter.easy.web.server.config.GlobalConfig;
import com.paletter.easy.web.server.support.MethodMappingScanner;
import com.paletter.easy.web.server.support.ScanResult;
import com.paletter.easy.web.server.util.WebUtils;

public class Response {

	public static String CONTENT_TYPE_TEXTHTML = "text/html";
	public static String CONTENT_TYPE_TEXTPLAIN = "text/plain";
	public static String CONTENT_TYPE_TEXTCSS = "text/css";
	public static String CONTENT_TYPE_APPLICATIONXML = "application/xml";
	public static String CONTENT_TYPE_APPLICATIONJSON = "application/json";
	public static String CONTENT_TYPE_APPLICATIONOCTETSTREAM = "application/octet-stream";
	public static String CONTENT_TYPE_APPLICATIONXJAVASCRIPT = "application/x-javascript";
	
	private Request request;
	private SocketChannel socketChannel;
	
	public Response(Request request, SocketChannel socketChannel) {
		super();
		this.request = request;
		this.socketChannel = socketChannel;
	}

	public void response() throws Throwable {
		
		String reqUri = request.getHeader(Request.HEADER_URI);
		
		String uri = reqUri.split("\\?")[0];
		
		if(uri.endsWith(".html")) {
			
			System.out.println("## Get Html: " + reqUri);
			writeWebResource(uri, CONTENT_TYPE_TEXTHTML);
			
		} else if(uri.endsWith(".js")) {

			System.out.println("## Get Js: " + reqUri);
			writeWebResource(uri, CONTENT_TYPE_APPLICATIONXJAVASCRIPT);
			
		} else if(uri.endsWith(".css")) {

			System.out.println("## Get Css: " + reqUri);
			writeWebResource(uri, CONTENT_TYPE_TEXTCSS);
			
		} else if(uri.endsWith(".json")) {
			
			System.out.println("## Get Json: " + reqUri); 
			writeJson(reqUri);
			
		} else {
			
			downloadFile(uri);
		};
		
		socketChannel.close();
	}
	
	private void println(String str) throws IOException {
		str += "\r\n";
		socketChannel.write(ByteBuffer.wrap(str.getBytes()));
	}
	
	private void writeNotFoundStatus() throws IOException {
		ResponseStatusEnum status = ResponseStatusEnum.NOT_FOUND;
		String content = status.getMsg();
		writeHeader(status, CONTENT_TYPE_TEXTHTML, content.length());
		
		println(content);
	}
	
	private void writeHeader(ResponseStatusEnum status, String contentType, long length) throws IOException {
		
		println("HTTP/1.1 " + status.getStatus() + " " + status.getMsg() + "");
		println("Server: Apache-Coyote/1.1");
		println("Content-Type: " + contentType);
		println("Content-Length: " + length);
		println("");
	}
	
	private void writeWebResource(String uri, String contentType) throws IOException {

		URL url = getClass().getClassLoader().getResource("static/" + uri.substring(1));
		File file = new File(url.getPath());
		
		if(!file.exists()) {
			writeNotFoundStatus();
			return;
		}
		
		writeHeader(ResponseStatusEnum.OK, contentType, file.length());
		
		FileInputStream fis = new FileInputStream(file);
		byte[] b = new byte[1024];
		while(fis.read(b) != -1) {
			socketChannel.write(ByteBuffer.wrap(b));
		}
		
		fis.close();
	}
	
	private void downloadFile(String uri) throws IOException {

		File file = new File("D:\\Zemp" + uri);

		if(!file.exists()) {
			writeNotFoundStatus();
			return;
		}
		
		writeHeader(ResponseStatusEnum.OK, CONTENT_TYPE_APPLICATIONOCTETSTREAM, file.length());
		
		FileInputStream fis = new FileInputStream(file);
		byte[] b = new byte[1024];
		while(fis.read(b) != -1) {
			socketChannel.write(ByteBuffer.wrap(b));
		}
		
		fis.close();
	}
	
	private void writeJson(String reqUri) throws Throwable {
		
		if (GlobalConfig.REQUEST_MAPPING_TYPE.isCodePath()) {
			writeJsonForCodePath(reqUri);
		}
		
		if (GlobalConfig.REQUEST_MAPPING_TYPE.isAnnotation()) {
			writeJsonForAnnotation();
		}
	}
	
	private void writeJsonForCodePath(String reqUri) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException
		, InvocationTargetException, InstantiationException, IOException {

		String method = request.getHeader(Request.HEADER_METHOD);
		if(method.equals("GET")) {
			
			Map<String, String> paramsMap = WebUtils.getRequestParams(reqUri);
			List<String> methodParamValues = new ArrayList<String>();
			paramsMap.entrySet().forEach(entry -> {
				if(!entry.getKey().equals("service") && !entry.getKey().equals("method")) 
					methodParamValues.add(entry.getValue());
			});
			
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
					
					writeHeader(ResponseStatusEnum.OK, CONTENT_TYPE_APPLICATIONJSON, result.toString().length());
					
					println(result.toString());
				}
			}
		}
	}
	
	private void writeJsonForAnnotation() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {

		String reqMethod = request.getHeader(Request.HEADER_METHOD);
		if(reqMethod.equals("GET")) {
			
			String uri = request.getHeader(Request.HEADER_URI);
			String path = uri.split("\\?")[0].split("\\.")[0];
			
			ScanResult scanResult = MethodMappingScanner.getMethod(path);
			if (scanResult != null) {
				
				Object result = scanResult.getMethod().invoke(scanResult.getInstance());
				
				writeHeader(ResponseStatusEnum.OK, CONTENT_TYPE_APPLICATIONJSON, result.toString().length());
				
				println(result.toString());
			}
		}
	}
}
