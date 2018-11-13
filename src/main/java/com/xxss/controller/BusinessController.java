package com.xxss.controller;

import java.util.UUID;

import org.hibernate.boot.model.source.spi.EmbeddableMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxss.config.AccountConfig;
import com.xxss.config.BussinessConfig;
import com.xxss.dao.AccountService;
import com.xxss.dao.ArticleService;
import com.xxss.dao.EmailService;
import com.xxss.entity.Account;
import com.xxss.entity.Article;
import com.xxss.entity.Email;
import com.xxss.entity.Result;

@Controller
public class BusinessController {
	@Autowired
	private AccountService accountService;

	@Autowired
	private ArticleService articleservice;
	
	@Autowired
	private EmailService emailService;
	

	/**
	 * 购买 商品
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	@RequestMapping("/bussiness/buy")
	@ResponseBody
	public Result register(String articleid, String accountid) {
		Result result = new Result();
		
		Article article = articleservice.findByid(articleid);
		
		Account buyaccount = accountService.findByid(accountid);
		
		Account sellaccount = accountService.findByid(article.getAccountId());
		
		//自己不能购买自己的商品
		if(buyaccount.getId().equals(sellaccount.getId())) {
			result.setInformation("您不能自己购买自己的商品");
			return result;
		}
		
		
		
		if(buyaccount.getMoney()<article.getPrice()) {
			result.setInformation("您的余额不足,请您充值后再重新购买");
			return result;
		}else {
			buyaccount.setMoney(buyaccount.getMoney()-article.getPrice());
			
			sellaccount.setMoney(sellaccount.getMoney()+article.getPrice());
			
			accountService.save(buyaccount);
			accountService.save(sellaccount);
			
			Email email = new Email();
			
			email.setContext(article.getCostContext());
			
			email.setFromEmail(AccountConfig.MANGER);
			
			email.setTitle(BussinessConfig.BUSINESS_SUCCESS+article.getTitle());
			
			email.setTargetEmail(buyaccount.getEmail());
			
			email.setIsGuanfangEmail("1");
			
			email.setIsRead("0");
			
			email.setId(UUID.randomUUID().toString());
			
			email.setSendTime(System.currentTimeMillis());
			
			emailService.save(email);
			
			result.setSuccess(true);
			
			result.setInformation("恭喜您购买成功,即将跳转");
			
			return result;
		}
		
	}
	
	
}
