package com.store.common.repository.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PolicyConditions;
import com.store.common.data.alioss.AliOSSConstant;
import com.store.common.data.alioss.AliOssConf;
import com.store.common.repository.AliOssRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class AliOssRepositoryImpl implements AliOssRepository {

    @Inject
    private AliOssConf aliOssConf;

    @Override
    public Map<String, String> getOssPolicy(String dir) {
        String host = "http://" + aliOssConf.getBucket() + "." + aliOssConf.getEndpoint();
        OSSClient client = new OSSClient(aliOssConf.getEndpoint(), aliOssConf.getAccessId(), aliOssConf.getAccessKey());
        try {
            long expireTime = AliOSSConstant.OSS_EXPIRE;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, AliOSSConstant.POLICY_MIN, AliOSSConstant.POLICY_MAX);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, aliOssConf.getDir() + dir);

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            Map<String, String> respMap = new HashMap<String, String>();
            respMap.put("accessid", aliOssConf.getAccessId());
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", aliOssConf.getDir() + dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            return respMap;
        } catch (Exception e) {
            log.error("获取阿里云OSS签名错误：{}", e);
            return null;
        }
    }

    @Override
    public int uploadFile(String sourcePath, String filename) {
        try {
            OSSClient client = new OSSClient(aliOssConf.getEndpoint(), aliOssConf.getAccessId(), aliOssConf.getAccessKey());
            File file = new File(sourcePath);
            InputStream content = new FileInputStream(file);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.length());
            client.putObject(aliOssConf.getBucket(), aliOssConf.getDir() + filename, content, metadata);
        } catch (Exception e) {
            log.error("阿里云上传文件错误：{}", e);
            return -1;
        }
        return 1;
    }

    @Override
    public int deleteFile(String picPath) {
        try {
            OSSClient client = new OSSClient(aliOssConf.getEndpoint(), aliOssConf.getAccessId(), aliOssConf.getAccessKey());
            // 从https:// 后面开始
            int index = picPath.indexOf("/", 8);
            String filename = picPath.substring(index + 1);
            client.deleteObject(aliOssConf.getBucket(), filename);
        } catch (Exception e) {
            log.error("阿里云删除文件错误：{}", e);
            return -1;
        }
        return 1;
    }
}