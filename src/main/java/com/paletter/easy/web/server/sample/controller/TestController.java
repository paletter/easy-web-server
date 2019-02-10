package com.paletter.easy.web.server.sample.controller;

import com.paletter.easy.web.server.annotation.WebMapping;
import com.paletter.easy.web.server.sample.entity.User;

public class TestController {

	@WebMapping("/getName")
	public String getName(String id) {
		return id;
	}
	
	@WebMapping("/getName2")
	public String getName2(String id, String name) {
		return id + name ;
	}
	
	@WebMapping("/getName3")
	public String getName3(User user) {
		return user.getName();
	}
	
	@WebMapping("/getName4")
	public int getName4() {
		return 1;
	}
	
	@WebMapping("/getName5")
	public boolean getName5() {
		return false;
	}
	
	@WebMapping("/getJson")
	public User getName() {
		User u = new User();
		u.setId(1);
		u.setName("fangbo");
		u.setContent("结果为6和2。这个方法判断的是String串的字符长度，但是Oracle数据库中却是以字节来判断varchar2类型数据长度（如： 字段定义为varchar2(64)，则存入该字段的字符串的字节长度不得超过64）。如果String串为纯英文，那么一个英文字母是一个字符，长度为 1，占1个字节，不会出错，但如果String串中包含中文，一个中文汉字也是一个字符，长度为1，但是却占多个字节（具体占几个字节跟使用的编码有 关），如果数据中包含中文，数据的长度就很有可能会超过数据库中对应字段的长度限制");
//		u.setContent("jsGrid is a lightweight client-side data grid control based on jQuery. It supports basic grid operations like inserting, filtering, editing, deleting, paging, and sorting. jsGrid is flexible and allows to customize its appearance and components.");
		return u;
	}
}
