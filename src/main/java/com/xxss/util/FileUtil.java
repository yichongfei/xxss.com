package com.xxss.util;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.xxss.dao.*;

public class FileUtil {
	private static FileUtil fUtil = new FileUtil();
	@Autowired
	private static  VideoService VideoService ;


	public static final String SOURCES = "F:\\videos";

	static {
		new Thread() {
			
		}.start();
	}

	public boolean checkDirNotEmpty() {
		String[] fileList = fUtil.getFileList(SOURCES);
		if (fileList.length > 0) {
			return true;
		}
		return false;

	}

	public String[] getFileList(File file) {
		if (file.isDirectory()) {
			return file.list();
		}
		return null;
	}

	public String[] getFileList(String filePath) {
		File file = new File(filePath);
		return getFileList(file);
	}

	public static void main(String[] args) {
		
	}
}
