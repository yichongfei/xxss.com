package com.xxss.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.omg.CORBA.INTERNAL;

import com.amazonaws.util.Md5Utils;
import com.xxss.entity.Pay;
import com.xxss.entity.PayArgs;

public class PayUtil {
	//商户号
	public static final String merchant = "TC18120218066";
	
	//回调通知函数
	public static final String notifyurl = "http://52.88.80.214:9999/chongzhi/result";
	
	public static final String backurl = "http://52.88.80.214:9999/chongzhi";
	
	public static final String md5key = "385258d9fb9242444232d2fccfa3514e";
	
	
	public static final Map<String,Integer> vipType = new HashMap<String,Integer>();
	
	static {
		vipType.put("1", 10);
		vipType.put("3", 10);
		vipType.put("6", 10);
		vipType.put("12", 10);
	}
	
	
	public static PayArgs getPayArgs(Pay pay) {
		PayArgs payargs = new PayArgs();
		payargs.setBackurl(backurl);
		payargs.setCustomno(pay.getId());
		payargs.setMerchant(merchant);
		payargs.setMoney(vipType.get(pay.getVipType()));
		payargs.setNotifyurl(notifyurl);
		payargs.setQrtype(pay.getQyType());
		payargs.setRisklevel("");
		payargs.setSendtime(pay.getSendTime());
		payargs.setSign(getMD5Sign(pay));
		
		return payargs;
	}
	
	public static String getMD5Sign(Pay pay) {
		StringBuilder builder = new StringBuilder();
		builder.append("merchant=").append(merchant).append("&")
				.append("qrtype=").append(pay.getQyType()).append("&")
				.append("customno=").append(pay.getId()).append("&")
				.append("money=").append(vipType.get(pay.getVipType())).append("&")
				.append("sendtime=").append(pay.getSendTime()).append("&")
				.append("notifyurl=").append(notifyurl).append("&")
				.append("backurl=").append(backurl).append("&")
				.append("risklevel=1").append(md5key);
				
		System.out.println(builder.toString());
		System.out.println(MD5(builder.toString()));
		return MD5Utils.stringMD5(builder.toString());
	}
	public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
	/**
     * 生成32位md5码
     * @param password
     * @return
     */
    public static String md5Password(String password) {

        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }

            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

    }
	public static void main(String[] args) {
		 System.out.println(System.currentTimeMillis()/1000l);
	}
}
