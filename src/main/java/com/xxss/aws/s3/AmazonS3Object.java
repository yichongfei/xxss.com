package com.xxss.aws.s3;

import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.Grantee;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.xxss.config.S3Config;
import com.xxss.dao.VideoService;
import com.xxss.entity.Video;

public class AmazonS3Object {
	private static final Regions region = Regions.US_WEST_2;
	
	private static AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region.getName())
			.withCredentials(new ProfileCredentialsProvider()).build();
	/**
	 * 获取一个bucket里面的所有对象
	 * 
	 * @param bucketName
	 * @return
	 */
	public static List<S3ObjectSummary> ListObject(String bucketName) {
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region.getName())
				.withCredentials(new ProfileCredentialsProvider()).build();
		ListObjectsV2Result result = s3.listObjectsV2(bucketName);
		return result.getObjectSummaries();
	}

	/**
	 * 获取一个BUCKET指定目录下的对象
	 * 
	 * @param bucketName
	 * @param prefix
	 * @return
	 */
	public static List<S3ObjectSummary> ListObjectPrefix(String bucketName, String prefix) {
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region.getName())
				.withCredentials(new ProfileCredentialsProvider()).build();
		ObjectListing listing = s3.listObjects( bucketName, prefix );
		List<S3ObjectSummary> summaries = listing.getObjectSummaries();

		while (listing.isTruncated()) {
		   listing = s3.listNextBatchOfObjects (listing);
		   summaries.addAll (listing.getObjectSummaries());
		}
		return summaries;
	}

	/**
	 * 上传文件夹中的所有对象
	 * 
	 * @param dir_path
	 * @param bucket_name
	 * @param key_prefix
	 * @param recursive
	 * @param pause
	 */
	public static void uploadDir(String dir_path, String bucket_name, String key_prefix, boolean recursive,
			boolean pause) {
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region.getName())
				.withCredentials(new ProfileCredentialsProvider()).build();
		System.out.println("directory: " + dir_path + (recursive ? " (recursive)" : "") + (pause ? " (pause)" : ""));

		TransferManager xfer_mgr = TransferManagerBuilder.standard().withS3Client(s3).build();
		try {
			MultipleFileUpload xfer = xfer_mgr.uploadDirectory(bucket_name, key_prefix, new File(dir_path), recursive);
			// loop with Transfer.isDone()
			XferMgrProgress.showTransferProgress(xfer);
			// or block with Transfer.waitForCompletion()
			XferMgrProgress.waitForCompletion(xfer);
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
			System.exit(1);
		}
		xfer_mgr.shutdownNow();
	}

	/**
	 * 获取预签名URL
	 * 
	 * @param bucketName
	 * @param objectKey
	 * @return
	 */
	public static String getPreSignedURL(String bucketName, String objectKey) {
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region.getName())
				.withCredentials(new ProfileCredentialsProvider()).build();

		// Set the presigned URL to expire after one hour.
		java.util.Date expiration = new java.util.Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 5;
		expiration.setTime(expTimeMillis);

		// Generate the presigned URL.
		System.out.println("Generating pre-signed URL.");
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey)
				.withMethod(HttpMethod.GET).withExpiration(expiration);
		URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);

		return url.toString();
	}

	public static void uploadFile(File file, String bucket_name, String key_prefix) {
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region.getName())
				.withCredentials(new ProfileCredentialsProvider()).build();
		String key_name = null;
		if (key_prefix != null) {
			key_name = key_prefix + '/' + file.getName();
		} else {
			key_name = file.getName();
		}

		TransferManager xfer_mgr = TransferManagerBuilder.standard().withS3Client(s3).build();
		try {
			Upload xfer = xfer_mgr.upload(bucket_name, key_name, file);
			// loop with Transfer.isDone()
			XferMgrProgress.showTransferProgress(xfer);
			// or block with Transfer.waitForCompletion()
			XferMgrProgress.waitForCompletion(xfer);
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
			System.exit(1);
		}
		xfer_mgr.shutdownNow();
		System.out.println("文件上传完毕,上传成功");
	}
	
	/**
	 * 返回KEY
	 * @param file
	 * @param bucket_name
	 * @param key_prefix
	 */
	public static String uploadFile1(File file, String bucket_name, String key_prefix) {
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region.getName())
				.withCredentials(new ProfileCredentialsProvider()).build();
		String key_name = null;
		if (key_prefix != null) {
			key_name = key_prefix + '/' + file.getName();
		} else {
			key_name = file.getName();
		}

		TransferManager xfer_mgr = TransferManagerBuilder.standard().withS3Client(s3).build();
		try {
			Upload xfer = xfer_mgr.upload(bucket_name, key_name, file);
			// loop with Transfer.isDone()
			XferMgrProgress.showTransferProgress(xfer);
			// or block with Transfer.waitForCompletion()
			XferMgrProgress.waitForCompletion(xfer);
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
			System.exit(1);
		}
		xfer_mgr.shutdownNow();
		System.out.println("文件上传完毕,上传成功");
		return  key_name;
	}

	/**
	 * 同步当天的数据,进入数据库.
	 */
	public static void saveVideo2DB(VideoService VideoService,String time) {
		List<String> list = getPrefix(time);
		for (String prefix : list) {
			List<S3ObjectSummary> listObjectPrefix = ListObjectPrefix(S3Config.VIDEOBUCKET, prefix);
			System.out.println(prefix);
			List<S3ObjectSummary> mp4VideoS3Object = getMp4VideoS3Object(listObjectPrefix);

			for (S3ObjectSummary s3ObjectSummary : mp4VideoS3Object) {
				VideoService.save(getVideo(s3ObjectSummary));
			}

		}

	}

	/**
	 * 获取所有的MP4S3Object
	 * 
	 * @param list
	 * @return
	 */
	public static List<S3ObjectSummary> getMp4VideoS3Object(List<S3ObjectSummary> list) {
		List<S3ObjectSummary> mp4List = new ArrayList<S3ObjectSummary>();
		for (S3ObjectSummary s3ObjectSummary : list) {
			if (s3ObjectSummary.getKey().endsWith("mp4")||s3ObjectSummary.getKey().endsWith("mpg")) {
				mp4List.add(s3ObjectSummary);
			}
		}
		return mp4List;

	}

	/**
	 * 通过KEY 生成一个实例的VIDEO 对象
	 * 
	 * @param S3Object
	 */
	public static Video getVideo(S3ObjectSummary S3Object) {

		String key = S3Object.getKey();
		Video video = new Video();
		video.setId(UUID.randomUUID().toString());
		video.setMp4Key(key);
		video.setVideopreview(getPreviewVideoKey(key));
		video.setPicKey(getPictureKey(key));
		video.setAdmireTimes(0);
		video.setPlayTimes(0);
		video.setTitle(getVideoTitle(key));
		video.setCategory(getVideoCat(key));
		video.setUploadTime(System.currentTimeMillis());
		video.setOwner(S3Config.OWNER);
		return video;
	}

	/**
	 * 获取当天的视频URL前缀
	 * 
	 * @return
	 */
	public static List<String> getPrefix() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String prefixAmerica = "america/" + sdf.format(date);
		String prefixJhina = "china/" + sdf.format(date);
		String prefixJapan = "japan/" + sdf.format(date);
		List<String> list = new ArrayList<String>();
		list.add(prefixJapan);
		list.add(prefixJhina);
		list.add(prefixAmerica);
		return list;
	}
	/**
	 * 获取某天的的视频URL前缀prefix
	 * 
	 * @return 包含america ,china ,japan
	 */
	public static List<String> getPrefix(String time) {
		List<String> list = new ArrayList<String>();
		for (String str : S3Config.KINDS) {
			list.add(str+time);
			
		}
		return list;
	}

	/**
	 * 获取PICTURE的KEY
	 * 
	 * @param key
	 * @return
	 */
	public static String getPictureKey(String key) {
		String[] array = key.split("mp4", 0);
		return array[0] + "1.jpg";
	}
	
	
	/**
	 * 获取previewVideo的KEY
	 * 
	 * @param key
	 * @return
	 */
	public static String getPreviewVideoKey(String key) {
		String[] array = key.split("mp4", 0);
		return array[0] + "/mp4/preview.mp4";
	}

	/**
	 * 获取VIDEO的标题名字
	 * 
	 * @return
	 */
	public static String getVideoTitle(String key) {
		String[] array = key.split("\\/", 0);
		return array[2];
	}

	/**
	 * 获取VIDEO的分类
	 * 
	 * @param args
	 */
	public static String getVideoCat(String key) {
		String[] array = key.split("\\/", 0);
		return array[0];
	}
	
	
	
	
	/**
	 * 根据prefix获取所有的PIC KEY 
	 */
	public static  List<String> getAllPicKeyByPrefix(List<String> list) {
		 List<String> resultList = new ArrayList<String>();
		 for (String prefix : list) {
			 List<S3ObjectSummary> objectSummaries = s3.listObjects(S3Config.VIDEOBUCKET, prefix).getObjectSummaries();
			 for (S3ObjectSummary s3ObjectSummary : objectSummaries) {
				if(s3ObjectSummary.getKey().endsWith("jpg")) {
					resultList.add(s3ObjectSummary.getKey());
				}
			}
		}
		 return resultList;
	}
	
	
	/**
	 * 公开图片
	 * @param key
	 */
	public static void makePublicPic(String key) {
		AccessControlList objectAcl  =s3.getObjectAcl(S3Config.VIDEOBUCKET,key);
		objectAcl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
		s3.setObjectAcl(S3Config.VIDEOBUCKET,key,objectAcl);
	}
	
	/**
	 * 公开图片
	 */
	public static void makePublicPicRun(String time) {
		List<String> prefixList = getPrefix(time);
		List<String> allPicKeyByPrefix = getAllPicKeyByPrefix(prefixList);
		for (String string : allPicKeyByPrefix) {
			makePublicPic(string);
		}
	}
	
	
	
	public static void main(String[] args) throws ParseException {
		String url = getPreSignedURL("talent-video", "japan/2018-07-18/(SOD)高额奖金悬赏!六本木举办的按摩棒承受力大考验 - 无码 成人 A片 全裸 日本 Sex 女优 Sod 一本道 珍藏 (By 分享大队)/mp4/(SOD)高额奖金悬赏!六本木举办的按摩棒承受力大考验 - 无码 成人 A片 全裸 日本 Sex 女优 Sod 一本道 珍藏 (By 分享大队).mp4");
		System.out.println(url);
		//makePublicPicRun("2018-07-08");//公开照片
	}
}