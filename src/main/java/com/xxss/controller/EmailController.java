package com.xxss.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxss.config.AccountConfig;
import com.xxss.dao.AccountService;
import com.xxss.dao.EmailService;
import com.xxss.entity.Account;
import com.xxss.entity.Article;
import com.xxss.entity.Email;
import com.xxss.entity.NotReadEmail;
import com.xxss.entity.Result;
import com.xxss.util.AccountCache;

@Controller
public class EmailController {

	@Autowired
	private AccountService accountService;
	@Autowired
	private EmailService emailService;

	
	
	
	/**
	 * 跳转到收件箱
	 *  
	 * @param request
	 * @return
	 */
	@RequestMapping("/email/inbox/{page}")
	public String checkInbox(@PathVariable(value = "page") Integer page, Model model,HttpServletRequest request) {
		HttpSession session = request.getSession();
		Account account  = (Account) session.getAttribute("account");
		if(account.getEmail() == "游客") {
			return "login";
		}
		Sort sort = new Sort(Direction.DESC, "sendTime");
		Pageable pageable = new PageRequest(page, 24, sort);
		Page<Email> pageEmail = emailService.findBytargetEmail(account.getEmail(), pageable);
		
		model.addAttribute("pageEmail",pageEmail);
		
		return "email";
	}
	
	
	/**
	 * 跳转到论坛,普通查询
	 *  
	 * @param request
	 * @return
	 */
	@RequestMapping("/email/sendEmailpage")
	public String startSendEmail(Model model,String fromEmail) {
		
		model.addAttribute("fromEmail",fromEmail);
		
		return "sendEmail";
	}
	
	
	/**
	 * 查看某一封邮件
	 *  
	 * @param request
	 * @return
	 */
	@RequestMapping("/email/emaildetal")
	public String emaildetal(Model model,String id,HttpServletRequest request) {
		
		
		
		Email email = emailService.findByid(id);
		Account account = accountService.findByemail(email.getFromEmail());
		model.addAttribute("email", email);
		model.addAttribute("account",account);
		return "emaildetal";
	}
	/**
	 * 将EMAIL 更新为已读
	 *  
	 * @param request
	 * @return
	 */
	@RequestMapping("/email/updateIsRead")
	public String updateIsRead(Model model,String id,HttpServletRequest request) {
		
		
		
		Email email = emailService.findByid(id);
		email.setIsRead("1");
		emailService.save(email);
		return "emaildetal";
	}
	
	
	
	/**
	 * 跳转到论坛,普通查询
	 *  
	 * @param request
	 * @return
	 */
	@RequestMapping("/email/sendEmail")
	@ResponseBody
	public Result sendEmail(String fromEmail,String targetEmail,String title,String context) {
		Result result = new Result();
		
		Account account = accountService.findByemail(targetEmail);
		
		if(account == null) {
			result.setInformation("您发送的邮箱地址,并不存在,请核对地址是否正确");
			return result;
		}
		
		Email email = new Email();
		email.setFromEmail(fromEmail);
		email.setContext(context);
		email.setId(UUID.randomUUID().toString());
		email.setIsRead("0");
		email.setSendTime(System.currentTimeMillis());
		email.setTargetEmail(targetEmail);
		email.setTitle(title);
		
		if(fromEmail.equals(AccountConfig.MANGER)) {
			email.setIsGuanfangEmail("1");
		}else {
			email.setIsGuanfangEmail("0");
		}
		
		
		emailService.save(email);
		result.setSuccess(true);
		result.setInformation("发送成功");
		
		
		return result;
	}
	
	
	
	/**
	 * 查询所有未读邮件
	 *  
	 * @param request
	 * @return
	 */
	@RequestMapping("/email/getNotReadEmail")
	@ResponseBody
	public void getNotReadEmail(String id ,HttpServletRequest request) {
		
		if(id == null) {
			return ;
		}
		
		List<Email> list = emailService.findBytargetEmail(AccountCache.accountMap.get(id).getEmail());
		
		if(list.size()==0) {
			return;
		}
		
		
		List<NotReadEmail> result = new ArrayList<NotReadEmail>();
		for (Email email : list) {
			if(!email.getIsRead().equals("1")) {
				NotReadEmail notReadEmail =new NotReadEmail(email);
				result.add(notReadEmail);
			}
		}
		
		HttpSession session =request.getSession();
		session.setAttribute("NotReadEmaillist", result);
		
	}
	
	
	
	
	
}
