package com.paletter.easy.web.server;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import javax.sql.rowset.serial.SerialException;

import org.apache.commons.lang.StringUtils;

import com.paletter.easy.web.server.config.GlobalConfig;
import com.paletter.easy.web.server.http.RequestMappingType;
import com.paletter.easy.web.server.support.MethodMappingScanner;
import com.paletter.easy.web.server.thread.SelectorThread;

public class EasyServer {

	private int port;
	
	public EasyServer(int port) {
		this.port = port;
		GlobalConfig.REQUEST_MAPPING_TYPE = RequestMappingType.SCAN_ANNOTATION;
	}
	
	public EasyServer(int port, RequestMappingType requestMappingType) {
		this.port = port;
		GlobalConfig.REQUEST_MAPPING_TYPE = requestMappingType;
		if (requestMappingType == null) GlobalConfig.REQUEST_MAPPING_TYPE = RequestMappingType.SCAN_ANNOTATION;
	}
	
	public EasyServer(int port, RequestMappingType requestMappingType, String scanBasicPath) {
		this.port = port;
		GlobalConfig.REQUEST_MAPPING_TYPE = requestMappingType;
		if (requestMappingType == null) GlobalConfig.REQUEST_MAPPING_TYPE = RequestMappingType.SCAN_ANNOTATION;
		GlobalConfig.MAPPING_SCAN_BASIC_PATH = scanBasicPath;
	}
	
	public void startup() {

		
		try {
			
			// Check
			checkStartup();
			
			// Before
			if (GlobalConfig.REQUEST_MAPPING_TYPE.isAnnotation()) {
				MethodMappingScanner.scan();
			}
			
			// Start
			int httpHandlerThreadSize = 100;
			
			ServerSocketChannel server = ServerSocketChannel.open();
			server.socket().bind(new InetSocketAddress(port));
			server.configureBlocking(false);
			System.out.println("Server startup on " + port);
			
			Selector selector = Selector.open();
			server.register(selector, SelectionKey.OP_ACCEPT);
			
			SelectorThread selectorThread = new SelectorThread(selector, httpHandlerThreadSize);
			selectorThread.setName("SelectorThread");
			selectorThread.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void checkStartup() throws Exception {
		
		if (GlobalConfig.REQUEST_MAPPING_TYPE.isAnnotation() && StringUtils.isEmpty(GlobalConfig.MAPPING_SCAN_BASIC_PATH)) {
			throw new SerialException("MAPPING_SCAN_BASIC_PATH is null");
		}
	}
	
	public void setMappingScanBasicPath(String path) {
		
	}
}
