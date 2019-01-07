package test;

import com.paletter.easy.web.server.EWSRunner;
import com.paletter.easy.web.server.config.EWSConfig;

public class EWSRunnerTest {

	public static void main(String[] args) {

		EWSConfig.port = 8080;
		EWSConfig.bioMode();
		EWSConfig.resourcesPath = "html/";
		EWSConfig.httpHandlerThreadSize = 1024;
		EWSConfig.annotationMappingMode("com.paletter.easy.web.server.sample");
		
		EWSRunner.run();
	}
}
