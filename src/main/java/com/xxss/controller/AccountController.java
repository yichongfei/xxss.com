package com.xxss.controller;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxss.aws.s3.AmazonS3Object;
import com.xxss.config.AccountConfig;
import com.xxss.config.BBSconfig;
import com.xxss.config.S3Config;
import com.xxss.dao.AccountService;
import com.xxss.dao.CardService;
import com.xxss.dao.PayService;
import com.xxss.dao.VideoService;
import com.xxss.entity.Account;
import com.xxss.entity.Card;
import com.xxss.entity.Pay;
import com.xxss.entity.PayArgs;
import com.xxss.entity.Result;
import com.xxss.util.ImgUtil;
import com.xxss.util.PayUtil;

@Controller
public class AccountController {


	@Autowired
	private VideoService videoService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private CardService cardService;
	
	@Autowired
	private PayService payService;
	
	/**
	 * 跳转到登录界面
	 * 
	 * @param email
	 * @param request
	 * @return
	 */
	@RequestMapping("/goLogin")
	public String goLogin(HttpServletRequest request) {
		
		return "login";

	}
	
	
	
	
	
	
	
	/**
	 * 注册账号
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	@RequestMapping("/account/zhuce")
	@ResponseBody
	public Result register(String email, String password) {
		Result result = new Result();
		if(email.equals("")||email==null) {
			result.setSuccess(false);
			result.setInformation("账号不能为空");
			return result;
		}
		
		Account findByemail = accountService.findByemail(email);
		
		if (findByemail == null) {
			accountService.save(new Account(email, password));
			result.setSuccess(true);
			result.setInformation("注册成功");
			return result;
		} else {
			result.setSuccess(false);
			result.setInformation("账号已存在,请重新注册");
			return result;
		}

	}
	/**
	 * 登录到XXSS
	 * 
	 * @param email
	 *            账号
	 * @param password
	 *            密码
	 * @return
	 */
	@RequestMapping("/account/login")
	@ResponseBody
	public Result loginXXSS(String email, String password, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Account account = accountService.findByemail(email);
		Result result = new Result();
		if (account == null) {
			result.setSuccess(false);
			result.setInformation("登录失败,账号不存在");
			return result;
		} else if (account.getPassword().equals(password)) {
			result.setSuccess(true);
			result.setInformation("登录成功,欢迎回来,祝您度过愉快的旅程");
			result.setObject(account);
			session.setAttribute("account", account);
			return result;
		} else if (!account.getPassword().equals(password)) {
			result.setSuccess(false);
			result.setInformation("登录失败,您的密码错误");
			return result;
		} else {
			return null;
		}

	}
	
	
	
	/**
	 * 退出登录
	 * 
	 * @param email
	 * @param request
	 * @return
	 */
	@RequestMapping("/account/exit")
	public String exit(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("account");
		Account account = new Account();
		account.setEmail("游客");
		session.setAttribute("account", account);
		return "home-v1";

	}
	
	/**
	 * 更新头像
	 * 
	 * @param email
	 * @param request
	 * @return
	 */
	@RequestMapping("/account/updatePic")
	@ResponseBody
	public void updatePic(HttpServletRequest request,String base64url) {
		HttpSession session = request.getSession();
		Account account = (Account) session.getAttribute("account");
		String generateImage = ImgUtil.GenerateImage(base64url);
		String keyname = AmazonS3Object.uploadFile1(new File(generateImage),S3Config.VIDEOBUCKET, BBSconfig.S3BBSPHOTO_PATH);
		account.setPicPath(AccountConfig.S3PATH+keyname);
		accountService.save(account);
	}
	
	

	/**
	 * 更新账号
	 * 
	 * @param email
	 * @param request
	 * @return
	 */
	@RequestMapping("/account/update")
	@ResponseBody
	public void updateAccount(HttpServletRequest request,String password,String name,String description) {
		HttpSession session = request.getSession();
		Account account = (Account) session.getAttribute("account");
		account.setPassword(password);
		account.setName(name);
		account.setDescription(description);
		accountService.save(account);
		session.setAttribute("account", account);
	}
	
	@RequestMapping("/account/chongzhivip")
	@ResponseBody
	public PayArgs chongzhivipByh5(String email,String qrtype,String viptype, HttpServletRequest request) {
		String customno = UUID.randomUUID().toString().replace("-", "").substring(3,10);
		Pay pay = new Pay();
		pay.setId(customno);
		pay.setQyType(qrtype);
		pay.setEmail(email);
		pay.setVipType(viptype);
		pay.setSendTime(System.currentTimeMillis()/1000l);
		pay.setStatus("0");
		payService.save(pay);
		
		PayArgs payArgs = PayUtil.getPayArgs(pay);
		
		
		return payArgs;

	}
	
	
	
	
	
	
}
