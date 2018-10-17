package com.paletter.easy.web.server.test;

import com.paletter.easy.web.server.annotation.WebMapping;

public class TestController {

	@WebMapping("/getName")
	public String getName(String id) {
		return id;
	}
	
	@WebMapping("/getName2")
	public String getName2(String id, String name) {
		return id + name ;
	}
}
