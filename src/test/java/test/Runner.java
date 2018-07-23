package test;

import com.paletter.easy.web.server.EasyServer;
import com.paletter.easy.web.server.http.RequestMappingType;

public class Runner {

	public static void main(String[] args) {

		EasyServer server = new EasyServer(8080, RequestMappingType.SCAN_ANNOTATION, "com.paletter.easy.web.server.test");
		server.startup();
	}
}
