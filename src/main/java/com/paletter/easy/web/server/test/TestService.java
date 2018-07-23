package com.paletter.easy.web.server.test;

import com.paletter.easy.web.server.annotation.Controller;
import com.paletter.easy.web.server.annotation.MappingPath;

@Controller
public class TestService {

	public String login(String param) {
		return "{result:" + param + "}";
	}
	
	@MappingPath(name="/loginByMapping")
	public String loginByMapping() {
		return "{result:}";
	}
}
