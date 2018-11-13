package com.xxss.aws.s3;

import java.util.List;

import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.Region;

public class AmazonS3Bucket {
	private static final Regions region = Regions.US_WEST_2;

	/**
	 * 获取bucket
	 * 
	 * @param bucket_name
	 * @return
	 */
	public static Bucket getBucket(String bucket_name) {
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region.getName())
				.withCredentials(new ProfileCredentialsProvider()).build();

		Bucket named_bucket = null;
		List<Bucket> buckets = s3.listBuckets();
		for (Bucket b : buckets) {
			if (b.getName().equals(bucket_name)) {
				named_bucket = b;
			}
		}
		return named_bucket;
	}

	/**
	 * 创建一个bucket
	 * 
	 * @param bucket_name
	 * @return
	 */
	public static Bucket createBucket(String bucket_name) {
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region.getName())
				.withCredentials(new ProfileCredentialsProvider()).build();
		Bucket b = null;
		if (s3.doesBucketExist(bucket_name)) {
			System.out.format("Bucket %s already exists.\n", bucket_name);
			b = getBucket(bucket_name);
		} else {
			try {
				b = s3.createBucket(bucket_name);
			} catch (AmazonS3Exception e) {
				System.err.println(e.getErrorMessage());
			}
		}
		return b;
	}
	/**
	 * 获取所有Bucket
	 * @param bucketNmae
	 * @return
	 */
	public static List<Bucket> listBucket(String bucketNmae) {
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region.getName())
				.withCredentials(new ProfileCredentialsProvider()).build();
		return s3.listBuckets();
		
	}

	
	
	
	
	public static void main(String[] args) {
		Bucket bucket = getBucket("kobeisgood");
		System.out.println(bucket.toString());
	}

}
