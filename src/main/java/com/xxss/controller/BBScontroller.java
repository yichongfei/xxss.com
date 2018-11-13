package com.xxss.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
import org.springframework.web.multipart.MultipartFile;

import com.xxss.aws.s3.AmazonS3Object;
import com.xxss.config.AccountConfig;
import com.xxss.config.BBSconfig;
import com.xxss.config.S3Config;
import com.xxss.dao.AccountService;
import com.xxss.dao.ArticleReplyService;
import com.xxss.dao.ArticleService;
import com.xxss.dao.CardService;
import com.xxss.dao.VideoService;
import com.xxss.entity.Account;
import com.xxss.entity.Article;
import com.xxss.entity.ArticleReply;
import com.xxss.entity.Result;
import com.xxss.util.AccountCache;
import com.xxss.util.ImgUtil;

@Controller
public class BBScontroller {

	public static List<Article> stickList = new ArrayList<Article>();

	@Autowired
	private VideoService videoService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private ArticleService articleservice;

	@Autowired
	private ArticleReplyService articleReplyService;

	/**
	 * 跳转到论坛,普通查询
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/bbs/{page}")
	public String bbs(@PathVariable(value = "page") Integer page, Model model) {
		if (stickList.size() == 0) {
			stickList = articleservice.findByisStick("1");
		}
		Sort sort = new Sort(Direction.DESC, "publishTime");
		Pageable pageable = new PageRequest(page, 10, sort);
		Page<Article> articleList = articleservice.findByisStickLike("0", pageable);

		getArticleName(articleList);
		getArticleName(stickList);

		model.addAttribute("stickList", stickList);
		model.addAttribute("articleList", articleList);
		model.addAttribute("countpage", articleList.getTotalPages());
		model.addAttribute("curpage", page);
		model.addAttribute("pathurl", "/bbs/");

		return "bbs";
	}

	/**
	 * 跳转到精品区
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/bbs/good/{page}")
	public String bbsByGood(@PathVariable(value = "page") Integer page, Model model) {
		if (stickList.size() == 0) {
			stickList = articleservice.findByisStick("1");
		}
		Sort sort = new Sort(Direction.DESC, "publishTime");
		Pageable pageable = new PageRequest(page, 10, sort);
		Page<Article> articleList = articleservice.findByisGoodLike("1", pageable);

		getArticleName(articleList);
		getArticleName(stickList);

		model.addAttribute("stickList", stickList);
		model.addAttribute("articleList", articleList);
		model.addAttribute("countpage", articleList.getTotalPages());
		model.addAttribute("curpage", page);
		model.addAttribute("pathurl", "/bbs/good/");

		return "bbs";
	}

	/**
	 * 打开某一篇文章
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/bbs/article/{page}")
	public String getArticle(@PathVariable(value = "page") Integer page, String id, Model model) {
		Article article = articleservice.findByid(id);
		Account account = accountService.findByid(article.getAccountId());

		Sort sort = new Sort(Direction.ASC, "replyTime");
		Pageable pageable = new PageRequest(page, 10, sort);
		Page<ArticleReply> replylist = articleReplyService.findByarticleid(id, pageable);
		for (ArticleReply articleReply : replylist) {
			articleReply.setAccountname(AccountCache.accountMap.get(articleReply.getAccountid()).getName());
		}

		model.addAttribute("article", article);
		model.addAttribute("account", account);
		model.addAttribute("replylist", replylist);
		model.addAttribute("countpage", replylist.getTotalPages());
		model.addAttribute("curpage", page);

		return "bbsdetail";
	}

	/**
	 * 选择城市,查看性息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/bbs/city/{city}/{page}")
	public String goCityCategory(@PathVariable(value = "city") String city, @PathVariable(value = "page") Integer page,
			Model model) {
		if (stickList.size() == 0) {
			stickList = articleservice.findByisStick("1");
		}
		Sort sort = new Sort(Direction.DESC, "publishTime");
		Pageable pageable = new PageRequest(page, 10, sort);
		Page<Article> articleList = articleservice.findBycityLike(city, pageable);

		getArticleName(articleList);
		getArticleName(stickList);

		model.addAttribute("stickList", stickList);
		model.addAttribute("articleList", articleList);
		model.addAttribute("countpage", articleList.getTotalPages());
		model.addAttribute("curpage", page);
		model.addAttribute("pathurl", "/bbs/city/" + city + "/");

		return "bbs";
	}

	/**
	 * 跳转到论坛发布文章界面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/bbs/goPublishArticle")
	public String goPublishArticle(HttpServletRequest request) {
		return "publishArticle";
	}

	/**
	 * 发布文章:性息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/bbs/publishArticleInformation")
	@ResponseBody
	public void publishArticle(HttpServletRequest request, String context, String accountid, String category,
			String city, String title, String isCost, String price, String costContext) {

		Document html = Jsoup.parse(context);
		Article article = new Article();
		article.setAccountId(accountid);
		article.setCity(city);
		article.setCategory(category);
		article.setContext(html.toString());
		article.setId(UUID.randomUUID().toString());
		article.setPublishTime(System.currentTimeMillis());
		article.setTitle(title);
		article.setIsStick("0");
		article.setIsCost(isCost);
		if (price != null) {
			article.setPrice(Integer.valueOf(price));
		}
		article.setCostContext(costContext);
		articleservice.save(article);
	}

	/**
	 * 上传图片,返回地址
	 */
	@RequestMapping("/bbs/uploadImg")
	@ResponseBody
	public String uploadImg(@RequestParam("file") MultipartFile file) {
		BufferedOutputStream out = null;
		File img = new File(AccountConfig.PHOTO_PATH+"/"+file.getOriginalFilename());
		try {
			out = new BufferedOutputStream(new FileOutputStream(img));
			out.write(file.getBytes());
			out.flush();
			String keyname = AmazonS3Object.uploadFile1( img,S3Config.VIDEOBUCKET,
					BBSconfig.S3BBSPHOTO_PATH);
			keyname = AccountConfig.S3PATH + keyname;
			return keyname;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(out!=null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;

	}

	/**
	 * 回复主题
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/bbs/replyArticle")
	@ResponseBody
	public Result replyArticle(HttpServletRequest request, String context, String accountid, String articleid) {

		Document html = Jsoup.parse(context);
		Elements imgs = html.getElementsByTag("img");
		if (imgs.size() > 0) {
			for (Element element : imgs) {
				String base64 = element.attr("src");
				String generateImage = ImgUtil.GenerateImage(base64);
				String keyname = AmazonS3Object.uploadFile1(new File(generateImage), "talent-xinjiapo",
						BBSconfig.S3BBSPHOTO_PATH);
				keyname = AccountConfig.S3PATH + keyname;
				element.attr("src", keyname);
			}
		}

		Account account = accountService.findByid(accountid);

		ArticleReply reply = new ArticleReply();
		reply.setId(UUID.randomUUID().toString());
		reply.setAccountid(accountid);
		reply.setArticleid(articleid);
		reply.setContext(html.toString());
		reply.setReplyTime(System.currentTimeMillis());
		reply.setPicPath(account.getPicPath());
		reply.setAccountname(account.getName());
		reply.setEmail(account.getEmail());

		articleReplyService.save(reply);

		Result result = new Result();
		result.setInformation("发表回复成功");
		return result;
	}

	/**
	 * 发布文章 :非性息类
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/bbs/article/admire")
	@ResponseBody
	public Article admire(HttpServletRequest request, String id) {

		Article article = articleservice.findByid(id);
		int times = article.getAdmireTimes();
		article.setAdmireTimes(times + 1);
		articleservice.save(article);
		return article;
	}

	/**
	 * 加精文章
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/bbs/article/highlight")
	@ResponseBody
	public Result highlight(HttpServletRequest request, String id) {
		Account account = (Account) request.getSession().getAttribute("account");
		Result result = new Result();
		if (account.getPrivilege() != 1) {
			result.setInformation("您没有这个权限");
			return result;
		}
		Article article = articleservice.findByid(id);

		article.setIsGood("1");
		articleservice.save(article);
		result.setInformation("成功申请为精品帖子");
		return result;
	}

	/**
	 * 置顶
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/bbs/article/top")
	@ResponseBody
	public Result top(HttpServletRequest request, String id) {
		Account account = (Account) request.getSession().getAttribute("account");
		Result result = new Result();
		if (account.getPrivilege() != 1) {
			result.setInformation("您没有这个权限");
			return result;
		}
		Article article = articleservice.findByid(id);

		article.setIsStick("1");
		articleservice.save(article);
		result.setInformation("成功为主题置顶");
		return result;
	}

	/**
	 * 删除主题
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/bbs/article/delete")
	@ResponseBody
	public Result delete(HttpServletRequest request, String id) {
		Account account = (Account) request.getSession().getAttribute("account");
		Result result = new Result();
		if (account.getPrivilege() != 1) {
			result.setInformation("您没有这个权限");
			return result;
		}
		Article article = articleservice.findByid(id);

		articleservice.delete(article);
		result.setInformation("成功删除该主题");
		return result;
	}

	public static List<Article> getArticleName(List<Article> list) {
		for (Article article : list) {
			article.setAccountName(AccountCache.accountMap.get(article.getAccountId()).getName());
		}
		return list;
	}

	public static List<Article> getArticleName(Page<Article> list) {
		List<Article> resultlist = list.getContent();
		for (Article article : resultlist) {
			article.setAccountName(AccountCache.accountMap.get(article.getAccountId()).getName());
		}
		return resultlist;
	}

}
