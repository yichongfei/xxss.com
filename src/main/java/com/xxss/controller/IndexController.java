package com.xxss.controller;

import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxss.dao.AccountService;
import com.xxss.dao.CardService;
import com.xxss.dao.PornStarService;
import com.xxss.dao.VideoService;
import com.xxss.entity.Account;
import com.xxss.entity.Card;
import com.xxss.entity.PornStar;
import com.xxss.entity.Result;
import com.xxss.entity.Video;



@Controller
public class IndexController {
	@Autowired
	private VideoService videoService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private CardService cardService;
	
	@Autowired
	private PornStarService pornStarService;
	
	//缓存
	private List<Video> mostViewVideoList = new ArrayList<Video>();
	
	private List<Video> lolilist = new ArrayList<Video>();
	
	private List<Video> koreanlist = new ArrayList<Video>();
	
	private List<Video> chinalist = new ArrayList<Video>();
	
	private List<Video> teenlist = new ArrayList<Video>();
	
	private List<Video> lamalist = new ArrayList<Video>();
	
	private List<Video> gangjiaolist = new ArrayList<Video>();
	
	private List<Video> zhubolist = new ArrayList<Video>();
	
	@RequestMapping("/")
	public String index(Model model, HttpServletRequest request) {
		
		if(lolilist.size()==0||koreanlist.size()==0||chinalist.size()==0||gangjiaolist.size()==0||teenlist.size()==0||lamalist.size()==0) {
			updateIndexCache();
		}
		model.addAttribute("xzhubo", zhubolist);
		model.addAttribute("xloli", lolilist);
		model.addAttribute("xkorean", koreanlist);
		model.addAttribute("xchina", chinalist);
		model.addAttribute("xgangjiao", gangjiaolist);
		model.addAttribute("xteen", teenlist);
		model.addAttribute("xlama", lamalist);
		model.addAttribute("mostview", mostViewVideoList);
		
		return "home-v1";
	}
	
	
	/**
	 * 获取header
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/header")
	public String header(Model model, HttpServletRequest request) {
		return "header";
	}
	
	/**
	 * 加载分类导航条
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/fenleidh")
	public String fenleidh(Model model, HttpServletRequest request) {
		return "fenleidh";
	}
	
	
	/**
	 * 获取手机端header
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/mobileheader")
	public String mobileheader(Model model, HttpServletRequest request) {
		return "mobileheader";
	}
	
	
	/**
	 * 加载footer
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/footer")
	public String footer(Model model, HttpServletRequest request) {
		return "footer";
	}
	
	/**
	 * 加载rightaside
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/rightaside")
	public String rightaside(Model model, HttpServletRequest request) {
		return "rightaside";
	}
	
	
	/**
	 * 跳转到上传视频页
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/submit-post")
	public String submitPost(Model model, HttpServletRequest request) {
		return "submit-post";
	}
	
	/**
	 * 跳转到注册页面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/login-register")
	public String loginRegister(Model model, HttpServletRequest request) {
		return "login-register";
	}
	
	
	/**
	 * 跳转到关于我们界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/about-us")
	public String aboutus(Model model, HttpServletRequest request) {
		return "about-us";
	}
	
	
	
	/**
	 * 跳转到about me
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/profile-about-me")
	public String profileAboutMe(Model model, HttpServletRequest request) {
		return "profile-about-me";
	}
	
	
	/**
	 * 跳转到profile-video
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/profile-video")
	public String profileVideo(Model model, HttpServletRequest request) {
		return "profile-video";
	}
	
	
	
	/**
	 * 跳转到profile-favorite
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/profile-favorite")
	public String profileFavorite(Model model, HttpServletRequest request) {
		return "profile-favorite";
	}
	
	
	/**
	 * 跳转到profile-followers
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/profile-followers")
	public String profileFollowers(Model model, HttpServletRequest request) {
		return "profile-followers";
	}
	
	
	/**
	 * 跳转到profile-comments
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/profile-comments")
	public String profileComments(Model model, HttpServletRequest request) {
		return "profile-comments";
	}
	
	
	/**
	 * 跳转到profile-settings
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/profile-settings")
	public String profileSettings(Model model, HttpServletRequest request) {
		return "profile-settings";
	}
	
	/**
	 * 根据分类查询女优列表
	 * @param category
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping("/pornstar/{country}/{page}")
	public String listPornstar(@PathVariable(value = "country") String country,
			@PathVariable(value = "page") Integer page, Model model) {
		if (country == null) {
			model.addAttribute("name", "japan");
		} else {
			model.addAttribute("name", country);
		}

		Sort sort = new Sort(Direction.DESC, "pornStarName");
		Pageable pageable = new PageRequest(page, 12, sort);
		List<PornStar> list = pornStarService.findBycountry(pageable, country);
		model.addAttribute("pornstars", list);
		model.addAttribute("count", 100);
		model.addAttribute("curpage", page);
		model.addAttribute("category",country);
		return "listPornstar";
	}
	
	
	
	
	
	/**
	 * 根据分类查询
	 * @param category
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping("/listVideo/{category}/{page}")
	public String listVideo(@PathVariable(value = "category") String category,
			@PathVariable(value = "page") Integer page, Model model) {
		if (category == null) {
			model.addAttribute("name", "china");
		} else {
			model.addAttribute("name", category);
		}

		Sort sort = new Sort(Direction.DESC, "uploadTime");
		Pageable pageable = new PageRequest(page, 12, sort);
		List<Video> list = videoService.findBycategory(pageable, category);
		model.addAttribute("videos", list);
		model.addAttribute("count", videoService.getCountByCategory(category));
		model.addAttribute("curpage", page);
		model.addAttribute("category",category);
		return "listVideo";
	}
	
	
	/**
	 * 分页查询最新的视频
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping("/listNewVideo/{page}")
	public String listNewVideo(@PathVariable(value = "page") Integer page, Model model) {
		Sort sort = new Sort(Direction.DESC, "uploadTime");
		Pageable pageable = new PageRequest(page, 12, sort);
		Page<Video> list = videoService.findAll(pageable);
		model.addAttribute("videos", list);
		model.addAttribute("count", videoService.getCountRows());
		model.addAttribute("countpage", list.getTotalPages());
		model.addAttribute("curpage", page);
		return "listNewVideo";
	}
	/**
	 * 分页查询播放次数最多的视频
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping("/listHotVideo/{page}")
	public String listHotVideo(@PathVariable(value = "page") Integer page, Model model) {
		Sort sort = new Sort(Direction.DESC, "playTimes");
		Pageable pageable = new PageRequest(page, 12, sort);
		Page<Video> list = videoService.findAll(pageable);
		model.addAttribute("count", videoService.getCountRows());
		model.addAttribute("videos", list);
		model.addAttribute("countpage", list.getTotalPages());
		model.addAttribute("curpage", page);
		return "listHotVideo";
	}
	/**
	 * 跳转到观看视频页面
	 * 
	 * @return
	 */
	@RequestMapping("/goVideoPlay")
	public String goVideoPlay(HttpServletRequest request, Model model,
			@RequestParam(value = "id", required = false, defaultValue = "") String id) {
		Video video = videoService.findById(id);
		Sort sort = new Sort(Direction.DESC, "playTimes");
		Random ra =new Random();
		Pageable pageable = new PageRequest(ra.nextInt(10)+1, 6, sort);
		List<Video> list = videoService.findBycategory(pageable, video.getCategory());
		if(mostViewVideoList.size()==0) {
			updateMostViewCache();
		}
		
		model.addAttribute("mostviewVideos",mostViewVideoList);
		model.addAttribute("videos", list);
		model.addAttribute("video", video);
		return "dpvideoplay";
	}
	
	
	
	
	
	
	@RequestMapping("/information")
	public String information(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Account account = (Account) session.getAttribute("account");
		if(account.getEmail()=="游客") {
			return "login";
		}
		
		
		return "information";
	}
	/**
	 * 跳转到充值页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/chongzhi")
	public String chongzhi(HttpServletRequest request) {

		return "chongzhi";

	}

	@RequestMapping("/chongzhivip")
	@ResponseBody
	public Result chongzhivip(String email, String key, String secret, HttpServletRequest request) {
		Result result = new Result();
		Account account = accountService.findByemail(email);
		if (account == null) {
			result.setSuccess(false);
			result.setInformation("账号不存在");
			return result;
		}

		if (!key.equals("") && !secret.equals("")) {
			Card card = cardService.findBykeyWords(key);
			if (card != null && card.getSecret().equals(secret) && card.isAvailable() == true) {
				account.updateVip(card);
				accountService.saveAndFlush(account);
				card.setAvailable(false);
				cardService.saveAndFlush(card);
				result.setSuccess(true);
				result.setObject(account);
				result.setInformation("充值VIP " + card.getMonths() + "个月成功");
				HttpSession session = request.getSession();
				session.setAttribute("account", account);// 更新session中账户信息
				return result;
			}
		}
		result.setSuccess(false);
		result.setInformation("充值失败,请查看卡密是否准确,如有疑问,请联系客服QQ");

		return result;

	}
	/**
	 * 更新最多播放的视频缓存
	 */
	public void updateMostViewCache() {
		Sort sort = new Sort(Direction.DESC, "playTimes");
		Random rand = new Random();
		int page =rand.nextInt(15) , size = 10;
		Pageable pageable = new PageRequest(page, size, sort);
		mostViewVideoList = videoService.findAll(pageable).getContent();
	}
	
	/**
	 * 更新主页缓存
	 */
	public void updateIndexCache() {
		Sort sort = new Sort(Direction.DESC, "uploadTime");
		Random rand = new Random();
		int page =rand.nextInt(5) , size = 12;
		Pageable pageable = new PageRequest(page, size, sort);
		lolilist = videoService.findBycategory(pageable, "x-loli");
		koreanlist = videoService.findBycategory(pageable, "x-korean");
		chinalist = videoService.findBycategory(pageable, "x-china");
		lamalist = videoService.findBycategory(pageable, "x-lama");
		teenlist = videoService.findBycategory(pageable, "x-teen");
		gangjiaolist = videoService.findBycategory(pageable, "x-gangjiao");
		zhubolist = videoService.findBycategory(pageable, "x-zhubo");
	}
}
