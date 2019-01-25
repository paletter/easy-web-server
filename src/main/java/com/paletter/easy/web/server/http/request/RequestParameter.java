package com.paletter.easy.web.server.http.request;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.paletter.easy.web.server.utils.StringUtils;

public class RequestParameter {

	private Map<String, String> getMethodParams;
	private Map<String, String> postMethodParams;
	private String jsonBody;
	
	public void addGetMethodParam(String name, String value) {
		if (StringUtils.isEmpty(name) || StringUtils.isEmpty(value)) return;
		
		if (getMethodParams == null) getMethodParams = Maps.newLinkedHashMap();
		getMethodParams.put(name, value);
	}
	
	public void addPostMethodParam(String name, String value) {
		if (StringUtils.isEmpty(name) || StringUtils.isEmpty(value)) return;
		
		if (postMethodParams == null) postMethodParams = Maps.newLinkedHashMap();
		postMethodParams.put(name, value);
	}
	
	public List<String> getGetMethodParamValues() {
		return getMethodParams == null ? Lists.newArrayList() : Lists.newArrayList(getMethodParams.values());
	}
	
	public List<String> getPostMethodParamValues() {
		return getMethodParams == null ? Lists.newArrayList() : Lists.newArrayList(getMethodParams.values());
	}

	public String getJsonBody() {
		return jsonBody;
	}

	public void setJsonBody(String jsonBody) {
		this.jsonBody = jsonBody;
	}
	
}
