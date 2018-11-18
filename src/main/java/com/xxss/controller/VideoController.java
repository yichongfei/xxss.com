package com.xxss.controller;

import java.util.Calendar;
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
import com.xxss.dao.VideoService;
import com.xxss.entity.Account;
import com.xxss.entity.Result;
import com.xxss.entity.Video;

@Controller
public class VideoController {
	@Autowired
	private VideoService videoService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private CardService cardService;
	
	
	
	//每播放一次记录IP
		private static ConcurrentHashMap<String, Integer> playTimes = new ConcurrentHashMap<String, Integer>();
		
		
		public static long dayEnd = getEndTime();
	
	
	/**
	 * 获取要播放的PRE-URL
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/video/getVideo")
	@ResponseBody
	public Result getVideoUrl(String id,HttpServletRequest request) {
		HttpSession session = request.getSession();
		Account account = (Account) session.getAttribute("account");
		Result result = new Result();
		Video video = videoService.findById(id);
		String preSignedURL = CloudFront.getPreUrl(video.getMp4Key());
		String preVideoUrl = CloudFront.getPreUrl(video.getVideopreview());
		video.setPreUrl(preSignedURL);
		video.setVideopreview(preVideoUrl);
		//会员观看不限制次数
		if (account != null && account.getVipDeadline() > System.currentTimeMillis()) {
			result.setObject(video);
			result.setSuccess(true);
			return result;
		}

		if (System.currentTimeMillis() > dayEnd) {
			resetPlayTimes();// 每天重置播放次数记录数据
		}

		String addr = getIpAddr(request);
		if (playTimes.containsKey(addr)) {
			if (playTimes.get(addr) > S3Config.playTimesForFree) {
				result.setSuccess(false);
				result.setInformation("非会员每天只能播放"+S3Config.playTimesForFree+"个视频,会员可无限观看,请成为我们星辰吧的会员,谢谢您的支持");
				return result;
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
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("http_client_ip");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		// 如果是多级代理，那么取第一个ip为客户ip
		if (ip != null && ip.indexOf(",") != -1) {
			ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
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
	
	
}
