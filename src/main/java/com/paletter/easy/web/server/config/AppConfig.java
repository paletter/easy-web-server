package com.paletter.easy.web.server.config;

public class AppConfig {

	public static String webMappingScannerPath = "com.paletter.easy.web.server.test";
	public static String htmlPath = "static/";
	public static String downloadFilePath = "D:\\Zemp";
	public static int httpHandlerThreadSize = 100;
	
	/**
	 * 1 - BIO
	 * 2 - NIO
	 */
	public static int ioMode = 1;
	
	/**
	 * Only download file
	 */
	public static Boolean isFileServer = false;
	
	/**
	 * Do Service Method:
	 * 1) By service and method params reflect
	 * 2) By URI path
	 */
	public static Boolean isAnnotationMappingService = true;
}
