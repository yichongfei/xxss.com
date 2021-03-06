package com.xxss.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.xxss.aws.cloudfront.CloudFront;
import com.xxss.aws.s3.AmazonS3Object;
import com.xxss.config.S3Config;
import com.xxss.dao.AccountService;
import com.xxss.dao.CardService;
import com.xxss.dao.PayService;
import com.xxss.dao.PornStarService;
import com.xxss.dao.VideoService;
import com.xxss.entity.Account;
import com.xxss.entity.Pay;
import com.xxss.entity.PornStar;
import com.xxss.entity.Result;
import com.xxss.entity.Video;

@Controller
public class VideoController {
	@Autowired
	private  VideoService videoService;

	@Autowired
	private  AccountService accountService;

	@Autowired
	private  CardService cardService;
	
	@Autowired
	private PayService payService;
	
	@Autowired
	private PornStarService pornStarService;
	
	//每播放一次记录IP
	private static ConcurrentHashMap<String, Integer> playTimes = new ConcurrentHashMap<String, Integer>();
	
	private static ConcurrentHashMap<String, Video> videoCache = new ConcurrentHashMap<String,Video>();
	
	private static ConcurrentHashMap<String, PornStar> pornStarCache = new ConcurrentHashMap<String,PornStar>();
	
	public static ConcurrentHashMap<String, Long> accountCache = new ConcurrentHashMap<String,Long>();
	
	public static long dayEnd = getEndTime();
	
	
		
		
	/**
	 * 获取预览VIDEO	
	 */
	@RequestMapping("/video/getPreVideo")
	@ResponseBody
	public String getPreVideoUrl(String id,HttpServletRequest request) {
		//如果缓存为空,则更新缓存
		if(videoCache.size()==0||pornStarCache.size()==0) {
			getAllVideo();
		}
		
		if(videoCache.get(id)!=null) {
			String preVideoUrl = CloudFront.getPreUrl(videoCache.get(id).getVideopreview());
			return preVideoUrl;
		}else {
			String preVideoUrl = CloudFront.getPreUrl(pornStarCache.get(id).getPreviewUrl());
			return preVideoUrl;
		}
		
		
	}
		
	
	/**
	 * 获取要播放的PRE-URL
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/video/getVideo")
	@ResponseBody
	public Result getVideoUrl(String id,HttpServletRequest request) {
		//如果account缓存是空,则更新
		if(accountCache.size()==0) {
			getAllAccount();
		}
		String addr = getIpAddr(request);
		
		
		HttpSession session = request.getSession();
		Account account = (Account) session.getAttribute("account");
		Result result = new Result();
		Video video = videoService.findById(id);
		String preSignedURL = CloudFront.getPreUrl(video.getMp4Key());
		video.setPreUrl(preSignedURL);
		//会员观看不限制次数
		if (account != null &&account.getEmail()!="游客"&& accountCache.get(account.getEmail()) > System.currentTimeMillis()) {
			result.setObject(video);
			result.setSuccess(true);
			return result;
		}

		if (System.currentTimeMillis() > dayEnd) {
			resetPlayTimes();// 每天重置播放次数记录数据
		}

		
		if (playTimes.containsKey(addr)) {
			
			//如果是游客,每天只能播放一个
			if(account.getEmail()=="游客") {
				if (playTimes.get(addr) > S3Config.playTimesWithOutAccount) {
					result.setSuccess(false);
					result.setInformation("亲爱的您还没有注册,注册之后可以继续免费观看,谢谢您的合作,游客每天只能免费观看"+S3Config.playTimesWithOutAccount+"个视频哦!");
					return result;
				}
			}else {
				if (playTimes.get(addr) > S3Config.playTimesForFree) {
					result.setSuccess(false);
					result.setInformation("亲爱的,免费用户只能每天观看"+S3Config.playTimesForFree+"个视频,本站良心小站,赶紧成为我们的VIP,每天无限制观看,海量电影,尽情体验");
					return result;
				}
			}
			Integer times = playTimes.get(addr) + 1;
			playTimes.put(addr, times);
		} else {
			playTimes.put(addr, 1);
		}
		result.setSuccess(true);
		result.setObject(video);
		return result;
		
	}
	
	
	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0
				|| "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if (ip.equals("127.0.0.1")||ip.equals("0:0:0:0:0:0:0:1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ip = inet.getHostAddress();

				// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
				if (ip != null && ip.length() > 15) { // "***.***.***.***".length()
														// = 15
					if (ip.indexOf(",") > 0) {
						ip = ip.substring(0, ip.indexOf(","));
					}
				}
			}
		}
		if (ip != null && ip.length() > 15) { // "***.***.***.***".length()
												// = 15
			if (ip.indexOf(",") > 0) {
				ip = ip.substring(0, ip.indexOf(","));
			}
		}
		return ip;
	}

	private static void resetPlayTimes() {
		dayEnd = getEndTime();
		playTimes.clear();
	}
	
	private static long getStartTime() {
		Calendar todayStart = Calendar.getInstance();
		todayStart.set(Calendar.HOUR, 0);
		todayStart.set(Calendar.MINUTE, 0);
		todayStart.set(Calendar.SECOND, 0);
		todayStart.set(Calendar.MILLISECOND, 0);
		return todayStart.getTimeInMillis();
	}

	private static long getEndTime() {
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.set(Calendar.HOUR, 23);
		todayEnd.set(Calendar.MINUTE, 59);
		todayEnd.set(Calendar.SECOND, 59);
		todayEnd.set(Calendar.MILLISECOND, 999);
		return todayEnd.getTimeInMillis();
	}
	@RequestMapping("/video/updataPlayTimes")
	@ResponseBody
	public void updataPlayTimes(String id) {
		Video video = videoService.findById(id);
		if(video !=null) {
			video.updateIncreasePlayTimes();
			videoService.saveAndFlush(video);
		}
	}
	
	
	/**
	 * 根据video名字查找title
	 */
	@RequestMapping("/video/search/{page}")
	public String searcheVideo(String title,Model model,@PathVariable(value = "page") Integer page) {
		Sort sort = new Sort(Direction.DESC, "playTimes");
		Pageable pageable = new PageRequest(page, 12, sort);
		Page<Video> list = videoService.findBytitleLike("%"+title+"%",pageable);
		int count =videoService.getCountByTitleLike("%"+title+"%");
		
		model.addAttribute("count", count);
		model.addAttribute("videos", list);
		model.addAttribute("countpage", list.getTotalPages());
		model.addAttribute("curpage", page);
		model.addAttribute("search",title);
		return "listVideoSearch";
	}
	
	
	
	
	
	/**
	 * 将所有的视频文件放入缓存
	 */
	private  void getAllVideo() {
		List<Video> AllVideo = videoService.findAll();
		for (Video video : AllVideo) {
			videoCache.put(video.getId(), video);
		}
		List<PornStar> allPornstar = pornStarService.findAll();
		for (PornStar pornStar : allPornstar) {
			pornStarCache.put(pornStar.getId(), pornStar);
		}
	}
	
	/**
	 * 将所有的账号存入缓存当中
	 */
	private void getAllAccount() {
		List<Account> findAll = accountService.findAll();
		for (Account account : findAll) {
			accountCache.put(account.getEmail(), account.getVipDeadline());
		}
	}
	
	
	/**
	 * 充值的回调函数,放这里了
	 * @param merchant
	 * @param qrtype
	 * @param customno
	 * @param sendtime
	 * @param orderno
	 * @param money
	 * @param paytime
	 * @param state
	 * @param sign
	 * @param request
	 */
	@RequestMapping("/chongzhi/result")
	@ResponseBody
	public void chongzhivipResult(String merchant,String qrtype,String customno,String sendtime,String orderno,String money,String paytime,String state,String sign, HttpServletRequest request) {
		
		System.out.println("merchant"+":"+merchant);
		System.out.println("qrtype"+":"+qrtype);
		System.out.println("customno"+":"+customno);
		System.out.println("sendtime"+":"+sendtime);
		System.out.println("orderno"+":"+orderno);
		System.out.println("money"+":"+money);
		System.out.println("paytime"+":"+paytime);
		System.out.println("state"+":"+state);
		System.out.println("sign"+":"+sign);
		
		Pay pay = payService.findByid(customno);
		Account account = accountService.findByemail(pay.getEmail());
		account.updateVip(pay);
		//数据库内容更新
		accountService.saveAndFlush(account);
		//更新缓存
		accountCache.put(account.getEmail(), account.getVipDeadline());
		
		System.out.println("更新成功");
	}
	
	
	
	
	
	public static void main(String[] args) {
		System.out.println(CloudFront.getPreUrl("Adriana Chechik/xvideos.mp4"));
		
	}
}
