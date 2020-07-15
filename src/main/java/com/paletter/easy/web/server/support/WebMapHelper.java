package com.paletter.easy.web.server.support;

import java.io.File;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.google.common.collect.Maps;
import com.paletter.easy.web.server.annotation.WebMapping;
import com.paletter.easy.web.server.config.EWSConfig;
import com.paletter.easy.web.server.utils.FileUtils;
import com.paletter.easy.web.server.utils.LogUtil;
import com.paletter.easy.web.server.utils.StringUtils;

public class WebMapHelper {

	public static Map<String, WebMapper> webMapperFactory = Maps.newHashMap();

	public static void main(String[] args) {
		String s = "\\1\\2\\3";
		System.out.println(s.replace("\\", "0"));
	}

	public static void scanner(String path) {
		
		try {
			
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			String rootPath = path.replaceAll("\\.", "/");
			
			LogUtil.printDebug("# rootpath-" + rootPath);
			
			Enumeration<URL> urls = classLoader.getResources(rootPath);
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				if (url != null) {
					String protocol = url.getProtocol();
					String pkgPath = url.getPath();
					
					LogUtil.printDebug("# protocol-" + protocol);
					LogUtil.printDebug("# pkgPath-" + pkgPath);
					
					if (protocol.equals("file")) {
						
						List<File> fileList = FileUtils.findFiles(pkgPath);
						for (File file : fileList) {

							String filePath = file.getPath().replace("\\", "/");
							int subIndex = filePath.indexOf(rootPath);

							String classPath = filePath.substring(subIndex).replace("/", ".");
							classPath = classPath.replace(".class", "");
							
							addWebMapper(classPath);
						}
						
					} else if (protocol.equals("jar")) {
						
						JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
						Enumeration<JarEntry> jarEntrys = jar.entries();
						while (jarEntrys.hasMoreElements()) {
							JarEntry jarEntry = jarEntrys.nextElement();
							
							String name = jarEntry.getName();
							if (name.startsWith("/")) name = name.substring(1);
							
							if (jarEntry.isDirectory() || !name.startsWith(rootPath) || !name.endsWith(".class"))
								continue;
							
							LogUtil.printDebug("# jarPath-" + name);
							
							int subIndex = name.indexOf(rootPath);
							String classPath = name.substring(subIndex).replace("/", ".");
							classPath = classPath.replace(".class", "");
							
							addWebMapper(classPath);
						}
					}
				}
			}
			
		} catch (Throwable e) {
			LogUtil.error("", e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static void addWebMapper(String classPath) {
		try {
			
			Class clazz = Class.forName(classPath);
			Object instance = clazz.newInstance();
			for (Method m : clazz.getMethods()) {
				if (m.isAnnotationPresent(WebMapping.class)) {
					
					WebMapping wm = m.getAnnotation(WebMapping.class);
					
					WebMapper webMapper = new WebMapper(instance, m, wm);
					
					String webPath = null;
					if (EWSConfig.basicWebPath != null 
						&& EWSConfig.basicWebPath.endsWith("/")
						&& wm.value().startsWith("/")) {
						
						webPath = EWSConfig.basicWebPath + wm.value().substring(1);
						
					} else if (EWSConfig.basicWebPath != null
							   && !EWSConfig.basicWebPath.endsWith("/")
							   && !wm.value().startsWith("/")) {
						
						webPath = EWSConfig.basicWebPath + "/" + wm.value();
						
					} else {
						
						webPath = EWSConfig.basicWebPath + wm.value();
					}
					
					if (!webPath.startsWith("/")) webPath = "/" + webPath;
					
					webMapperFactory.put(webPath, webMapper);
				}
			}
		} catch (Exception e) {
			LogUtil.printDebug("Sacanner " + classPath + " error.");
		}
	}
	
	public static WebMapper getMapper(String uri) {
		return webMapperFactory.get(uri);
	}
}
