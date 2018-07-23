package com.paletter.easy.web.server.http;

public enum RequestMappingType {

	/**
	 * Mapping by paramete service & method.
	 * e.g:
	 * request http://url?service=com.paletter.easy.web.server.test.TestService&method=login
	 */
	CODE_PATH(1),
	
	/**
	 * Mapping by request path.
	 * e.g:
	 * request http://url/test/login
	 * Mapping to method which @MappingPath(name="/test/login")
	 */
	SCAN_ANNOTATION(2);
	
	private Integer type;
	
	private RequestMappingType(Integer type) {
		this.type = type;
	}
	
	public boolean isCodePath() {
		return type.equals(RequestMappingType.CODE_PATH.getType());
	}
	
	public boolean isAnnotation() {
		return type.equals(RequestMappingType.SCAN_ANNOTATION.getType());
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
