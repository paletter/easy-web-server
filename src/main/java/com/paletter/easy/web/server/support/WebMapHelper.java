package com.paletter.easy.web.server.support;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.paletter.easy.web.server.annotation.WebMapping;
import com.paletter.easy.web.server.utils.FileUtils;

public class WebMapHelper {

	public static Map<String, WebMapper> webMapperFactory = Maps.newHashMap();
	
	@SuppressWarnings("rawtypes")
	public static void scanner(String path) {
		
		try {
			
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			String rootPath = path.replaceAll("\\.", "/");
			Enumeration<URL> urls = classLoader.getResources(rootPath);
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				if (url != null) {
					String protocol = url.getProtocol();
					String pkgPath = url.getPath();
					if (protocol.equals("file")) {
						List<File> fileList = FileUtils.findFiles(pkgPath);
						for (File file : fileList) {
							int subIndex = file.getPath().indexOf(rootPath.replace("/", "\\"));
							String classPath = file.getPath().substring(subIndex).replace("\\", ".");
							classPath = classPath.replace(".class", "");
							
							Class clazz = Class.forName(classPath);
							Object instance = clazz.newInstance();
							for (Method m : clazz.getMethods()) {
								if (m.isAnnotationPresent(WebMapping.class)) {
									WebMapper webMapper = new WebMapper(instance, m);
									webMapperFactory.put(m.getAnnotation(WebMapping.class).value(), webMapper);
								}
							}
						}
					}
				}
			}
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public static WebMapper getMapper(String uri) {
		return webMapperFactory.get(uri);
	}
	
	public static class WebMapper {

		private Object instance;
		private Method method;

		public WebMapper(Object instance, Method method) {
			this.instance = instance;
			this.method = method;
		}
		
		public Object invoke(Object[] params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			return method.invoke(instance, params);
		}

		public Object getInstance() {
			return instance;
		}

		public void setInstance(Object instance) {
			this.instance = instance;
		}

		public Method getMethod() {
			return method;
		}

		public void setMethod(Method method) {
			this.method = method;
		}
		
	}
}
