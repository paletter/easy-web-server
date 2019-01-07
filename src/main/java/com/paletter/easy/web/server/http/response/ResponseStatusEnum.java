package com.paletter.easy.web.server.http.response;

public enum ResponseStatusEnum {

	OK(200, "OK"),
	BAD_REQUEST(400, "Bad Request"),
	NOT_FOUND(404, "Not Found");
	
	private ResponseStatusEnum(Integer status, String msg) {
		this.status = status;
		this.msg = msg;
	}

	private final Integer status;
	private final String msg;

	public Integer getStatus() {
		return status;
	}

	public String getMsg() {
		return msg;
	}
	
}
