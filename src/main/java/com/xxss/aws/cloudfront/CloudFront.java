package com.xxss.aws.cloudfront;

import java.io.File;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import com.amazonaws.services.cloudfront.CloudFrontUrlSigner;
import com.amazonaws.services.cloudfront.util.SignerUtils;
import com.xxss.config.CloudFrontConfig;

public class CloudFront {
	
	
	public static final int MoreThanDate = 60000;
	
	public static String getPreUrl(String s3ObjectKey) {
		String preUrl;
		try {
			preUrl = CloudFrontUrlSigner.getSignedURLWithCannedPolicy(SignerUtils.Protocol.http, CloudFrontConfig.distributionDomain, new File(CloudFrontConfig.privateKeyFilePath), s3ObjectKey, "APKAI37IRC7HLOI6ALDQ", new Date(System.currentTimeMillis()+MoreThanDate));
			return preUrl;
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}	
