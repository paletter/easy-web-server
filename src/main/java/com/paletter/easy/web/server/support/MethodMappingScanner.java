package com.paletter.easy.web.server.support;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import com.google.common.collect.Maps;
import com.paletter.easy.web.server.annotation.Controller;
import com.paletter.easy.web.server.annotation.MappingPath;
import com.paletter.easy.web.server.config.GlobalConfig;

public class MethodMappingScanner {

	public static Map<String, ScanResult> mappingFactory = Maps.newHashMap();
	
	public static void scan() {
		
		Reflections reflection = new Reflections(GlobalConfig.MAPPING_SCAN_BASIC_PATH);
		Set<Class<?>> classes = reflection.getTypesAnnotatedWith(Controller.class);
		Map<String, Object> tmpClassInstanceMap = Maps.newHashMap();
		classes.forEach(clazz -> {
			
			try {
				
				Object instance = null;
				if (tmpClassInstanceMap.containsKey(clazz.getName())) {
					instance = clazz.newInstance();
				} else {
					instance = clazz.newInstance();
					tmpClassInstanceMap.put(clazz.getName(), instance);
				}
				
				for (Method method : clazz.getMethods()) {
					if (method.isAnnotationPresent(MappingPath.class)) {
						String mappingName = method.getAnnotation(MappingPath.class).name();
						if (!mappingName.startsWith("/")) {
							mappingName = mappingName.concat("/");
						}
						
						ScanResult scanResult = new ScanResult(instance, method);
						mappingFactory.put(mappingName, scanResult);
					}
				}
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public static ScanResult getMethod(String mapping) {
		
		return mappingFactory.get(mapping);
	}
}
