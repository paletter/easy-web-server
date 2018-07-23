package com.paletter.easy.web.server.util;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

public class WebUtils {

	public static Map<String, String> getRequestParams(String uri) {
		
		Map<String, String> paramsMap = new TreeMap<String, String>();
		
		if (StringUtils.isEmpty(uri) || uri.split("\\?").length < 2) {
			return paramsMap;
		}
		
		String uriParam = uri.split("\\?")[1];
		String[] uriParams = uriParam.split("&");
		for(String param : uriParams) {
			try {
				String key = param.split("=")[0];
				String value = param.split("=")[1];
				paramsMap.put(key, value);
			} catch (Exception e) {
				continue;
			}
		}
		
		return paramsMap;
	}
}
