package com.xxss.util;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.xxss.dao.VideoService;
import com.xxss.entity.Video;

public class IndexCache {
	private VideoService videoService ;
	
	public IndexCache(VideoService vs) {
		this.videoService =vs;
	}

	public static List<Video> newVideoPicList ;
	
	public static List<Video> MostViewedPicList;
	
	public static List<Video> ChinaViewPicList;
	
	public static List<Video> AmericaViewPicList;
	
	
	public  void updateCache() {
		Sort sort = new Sort(Direction.DESC, "uploadTime");
		int page = 0, size = 8;
		Pageable pageable = new PageRequest(page, size, sort);
		newVideoPicList=videoService.findAll(pageable).getContent();
		
		sort = new Sort(Direction.DESC, "playTimes");
		page = 0; size = 8;
		pageable = new PageRequest(page, size, sort);
		MostViewedPicList = videoService.findAll(pageable).getContent();
		
		
		sort = new Sort(Direction.DESC, "uploadTime");
		page = 0;size = 8;
		pageable = new PageRequest(page, size, sort);
		ChinaViewPicList = videoService.findBycategory(pageable, "china");
		
		
		sort = new Sort(Direction.DESC, "uploadTime");
		page = 0;size = 8;
		pageable = new PageRequest(page, size, sort);
		AmericaViewPicList = videoService.findBycategory(pageable, "america");
		
		
		
		
		
	}
	
	
	
	
}
