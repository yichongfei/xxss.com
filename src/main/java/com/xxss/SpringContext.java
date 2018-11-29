package com.xxss;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.xxss.aws.s3.AmazonS3Object;
import com.xxss.dao.CardService;
import com.xxss.dao.VideoService;
import com.xxss.entity.Card;
import com.xxss.entity.Video;
import com.xxss.util.CardUtil;

@Component
@Lazy(false)
public class SpringContext implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

	public static <T> T getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}

	public static <T> T getBean(String name, Class<T> clazz) {
		return getApplicationContext().getBean(name, clazz);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}

	public static void main(String[] args) throws Exception {
		
		//更新S3电影到数据库当中
		  ApplicationContext ctx = SpringApplication.run(Application.class, args);
		  
		  VideoService VideoService= ctx.getBean(VideoService.class);
		  
		  AmazonS3Object S3 = new AmazonS3Object();
		  
		
		  S3.saveVideo2DB(VideoService,"2018-11-28");

		
		
		
		//生成card 存入数据库，并写入文件当中		
//		List<Card> list = CardUtil.createCard(0.25);
//
//		ApplicationContext ctx = SpringApplication.run(Application.class, args);
//
//		CardService CardService = ctx.getBean(CardService.class);
//
//		CardService.save(list);
//
//		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("E:\\card244.txt")));
//
//		for (Card card : list) {
//			bw.write(card.getKey() + "  ");
//			bw.write(card.getSecret() + "\r\n");
//			bw.flush();
//		}
//		
//		bw.close(); 

		
		
	
		
		
		
		
		
		
	}
}
