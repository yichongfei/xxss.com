package com.xxss.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	
	public static String getDate(SimpleDateFormat sdf) {
		return sdf.format(new Date());
	}
	
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	}
	
}
