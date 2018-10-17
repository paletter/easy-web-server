package com.paletter.easy.web.server.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

	public static List<File> findFiles(String path) {
		
		List<File> fileList = new ArrayList<File>();
		File file = new File(path);
		if (file.exists()) {
			
			if (file.isFile()) {
				fileList.add(file);
			}
			
			if (file.isDirectory()) {
				for (String fileName : file.list()) {
					fileList.addAll(findFiles(path + "/" + fileName));
				}
			}
		}
		
		return fileList;
	}
}
