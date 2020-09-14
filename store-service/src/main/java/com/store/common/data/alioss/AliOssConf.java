package com.store.common.data.alioss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="oss")
public class AliOssConf {
	//账户
	private String accessId;
	
	//秘钥（不可公开）
	private String accessKey;
	
	private String bucket;
	
	private String endpoint;
	
	//用户的图片地址
	private String dir;
}