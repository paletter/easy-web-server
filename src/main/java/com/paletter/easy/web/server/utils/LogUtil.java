package com.paletter.easy.web.server.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LogUtil {

	private static final Logger logger = LogManager.getLogger(LogUtil.class);
	
	private static Integer LEVLE = 2;
	
	public static void setLevel(Integer level) {
		LEVLE = level;
	}
	
	public static void print(String str) {
		logger.info(str);
	}
	
	public static void error(String str) {
		logger.error(str);
	}
	
	public static void error(String str, Throwable e) {
		logger.error(str, e);
	}
	
	public static void printDebug(String str) {
		if (LEVLE < 2) {
			logger.debug(str);
		}
	}
}
