package com.xxss.util;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;

import com.xxss.Application;
import com.xxss.dao.CardService;
import com.xxss.dao.VideoService;
import com.xxss.entity.Card;
public class CardUtil {
	
	public static final int times = 200;
	
	public static List<Card> createCard(double months) { 
		List<Card> list = new ArrayList<Card>();
		String str = "";
		int counts = 0;
		while(counts < times) {
			counts++;
			str = UUID.randomUUID().toString();
			str = str.replaceAll("-", "");
			
			String key = str.substring(0, 16);
			String secret = str.substring(16,str.length());
			Card card = new Card();
			card.setKey(key.toUpperCase());
			card.setSecret(secret.toUpperCase());
			card.setMonths(months);
			card.setAvailable(true);
			list.add(card);
		}
		return list;
	}
	
	
	
	
	
	
	
	
	public static void main(String[] args) throws IOException {
		
		
		
		
	}
	
}
