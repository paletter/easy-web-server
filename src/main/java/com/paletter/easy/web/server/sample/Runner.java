package com.paletter.easy.web.server.sample;

import com.paletter.easy.web.server.EWSRunner;
import com.paletter.easy.web.server.config.EWSConfig;
import com.paletter.easy.web.server.utils.LogUtil;

public class Runner {

	public static void main(String[] args) {

		LogUtil.setLevel(1);
		
		EWSConfig.port = 8080;
		EWSConfig.nioMode();
		EWSConfig.resourcesPath = "html/";
		EWSConfig.httpHandlerThreadSize = 1024;
		EWSConfig.annotationMappingMode("com.paletter.easy.web.server.sample");
		EWSConfig.logLevel = 1;
		
		EWSRunner.run();
	}
}
