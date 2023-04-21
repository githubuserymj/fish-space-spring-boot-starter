package com.fish.space.starter;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.fish.space.starter.oss.config.OssAutoConfiguration;
import com.fish.space.starter.oss.config.OssProperties;
import com.fish.space.starter.oss.service.OssTemplate;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;

class Tests {


    @Test
    void testOss() {
        OssProperties ossProperties = new OssProperties();
        ossProperties.setAccessKey("COs1TYGBnywmZBn9");
        ossProperties.setSecretKey("VB3ZLdjlACazmdCm8JBchFdesolID38B");
        ossProperties.setEndpoint("http://127.0.0.1:9000");
        OssAutoConfiguration ossAutoConfiguration = new OssAutoConfiguration();
        AmazonS3 amazonS3 = ossAutoConfiguration.ossClient(ossProperties);
        OssTemplate ossTemplate = ossAutoConfiguration.ossTemplate(amazonS3);
        ossTemplate.createBucket("oss02");
        try {
            ossTemplate.putObject("oss02", "图片", new FileInputStream(new File("C:\\Users\\yumingjun.HIK\\Pictures\\11111111111.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        S3Object s3Object = ossTemplate.getObject("oss02", "图片.png");
    }

}
