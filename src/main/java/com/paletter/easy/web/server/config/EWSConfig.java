package com.paletter.easy.web.server.config;

public class EWSConfig {

	public static Integer port = 8080;
	public static int httpHandlerThreadSize = 100;
//	public static String webMappingScannerPath = "com.paletter.easy.web.server.test";
	public static String webMappingScannerPath = "com.paletter.easy.web.server.test";
//	public static String htmlPath = "static/";
	public static String resourcesPath = "static/";
	public static int logLevel = 2;
	
	/**
	 * <pre>
	 * 1 - BIO
	 * 2 - NIO
	 * </pre>
	 */
	public static int ioMode = 1;
	
	/**
	 * Only download file
	 */
	public static Boolean isFileServer = false;
//	public static String downloadFilePath = "D:\\Zemp";
	public static String downloadFilePath = "";
	
	/**
	 * Do Service Method:
	 * <pre>
	 * 1) By service and method params reflect
	 * 2) By URI path
	 * </pre>
	 */
	public static Boolean isAnnotationMappingService = false;
	
	public static void bioMode() {
		ioMode = 1;
	}
	
	public static void nioMode() {
		ioMode = 2;
	}
	
	public static void annotationMappingMode(String webMappingScannerPath) {
		isAnnotationMappingService = true;
		EWSConfig.webMappingScannerPath = webMappingScannerPath;
	}
}
