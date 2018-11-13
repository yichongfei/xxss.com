package com.xxss.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ZhuanMaUtil {

	public static final String SOURCE = "C:\\115download";

	public static final String TARGET = "C:\\videos";
	
	public static final int SIZE = 70;
	/**
	 * 获取文件夹下的所有文件
	 * @param directoryPath
	 * @param isAddDirectory
	 * @return
	 */
	public static List<String> getAllFile(String directoryPath,boolean isAddDirectory) {
	        List<String> list = new ArrayList<String>();
	        File baseFile = new File(directoryPath);
	        if (baseFile.isFile() || !baseFile.exists()) {
	            return list;
	        }
	        File[] files = baseFile.listFiles();
	        for (File file : files) {
	            if (file.isDirectory()) {
	                if(isAddDirectory){
	                    list.add(file.getAbsolutePath());
	                }
	                list.addAll(getAllFile(file.getAbsolutePath(),isAddDirectory));
	            } else {
	                list.add(file.getAbsolutePath());
	            }
	        }
	        return list;
	    }
	 
	 public static List<String> getAllMp4File(List<String> list){
		 List<String> Mp4List = new ArrayList<String>();
		 File file = null;
		 for (String string : list) {
			if(string.endsWith("mp4")||string.endsWith("mpg")||string.endsWith("avi")
					||string.endsWith("mkv")||string.endsWith("rmvb")||string.endsWith("wmv")
					||string.endsWith("flv")||string.endsWith("mpeg")
					) {
				file = new File(string);
				if(file.length()/1024/1024 > SIZE) {
					Mp4List.add(string);
				}
			}
		}
		 
		 return Mp4List;
	 }
	 
	 private  static void moveTotherFolders(String fileName,String ansPath){
		    String startPath =  fileName ;
		    String endPath = ansPath ;
		    try {
		        File startFile = new File(startPath);
		        File tmpFile = new File(endPath);//获取文件夹路径
		        if(!tmpFile.exists()){//判断文件夹是否创建，没有创建则创建新文件夹
		            tmpFile.mkdirs();
		        }
		        System.out.println(endPath + startFile.getName());
		        if (startFile.renameTo(new File(endPath + startFile.getName()))) {
		            System.out.println("File is moved successful!");
		        } else {
		            System.out.println("File is failed to move!");
		        }
		    } catch (Exception e) {

		    }
		}

	public static void main(String[] args) {
		List<String> allMp4File = getAllMp4File(getAllFile(SOURCE, false));
		for (String string : allMp4File) {
			moveTotherFolders(string, TARGET);
		}
		
	}

}
