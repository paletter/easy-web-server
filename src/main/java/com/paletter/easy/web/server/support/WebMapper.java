package com.paletter.easy.web.server.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import com.paletter.easy.web.server.annotation.WebMapping;
import com.paletter.easy.web.server.exception.InvokeMethodError;
import com.paletter.easy.web.server.exception.InvokeMethodParamLengthError;

public class WebMapper {

	private Object instance;
	private Method method;
	private WebMapping wm;

	public WebMapper(Object instance, Method method, WebMapping wm) {
		this.instance = instance;
		this.method = method;
		this.wm = wm;
	}

	public Object invoke(Object... params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (params.length != method.getParameterCount()) 
			throw new IllegalAccessError("params.length not equal method's parameter count");
		
		return method.invoke(instance, params);
	}
	
	public Object invoke(String... params) {
		if (params.length != method.getParameterCount()) 
			throw new InvokeMethodParamLengthError("params length not equal method's parameter count.");
		
		try {
			
			Object[] objs = new Object[method.getParameterCount()];
			for (int i = 0; i < method.getParameterCount(); i ++) {
				
				if (method.getParameterTypes()[i].equals(String.class)) {
					objs[i] = params[i];
				} else if (method.getParameterTypes()[i].equals(Integer.class)) {
					objs[i] = Integer.valueOf(params[i]);
				} else if (method.getParameterTypes()[i].equals(Short.class)) {
					objs[i] = Short.valueOf(params[i]);
				} else if (method.getParameterTypes()[i].equals(Long.class)) {
					objs[i] = Long.valueOf(params[i]);
				} else if (method.getParameterTypes()[i].equals(Float.class)) {
					objs[i] = Float.valueOf(params[i]);
				} else if (method.getParameterTypes()[i].equals(Double.class)) {
					objs[i] = Double.valueOf(params[i]);
				} else if (method.getParameterTypes()[i].equals(BigDecimal.class)) {
					objs[i] = new BigDecimal(params[i]);
				} else if (method.getParameterTypes()[i].equals(Boolean.class)) {
					objs[i] = Boolean.valueOf(params[i]);
				}
			}
			
			return method.invoke(instance, objs);
			
		} catch (Throwable e) {
			throw new InvokeMethodError(e);
		}
	}

	public Object getInstance() {
		return instance;
	}

	public Method getMethod() {
		return method;
	}

	public WebMapping getWm() {
		return wm;
	}
}
