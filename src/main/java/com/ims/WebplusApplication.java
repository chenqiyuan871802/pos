package com.ims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;



/**
 * 
 * 类名:com.toonan.WebplusApplication
 * 描述:spring boot 启动文件
 * 编写者:陈骑元
 * 创建时间:2019年4月26日 下午5:53:26
 * 修改说明:
 */
@EnableConfigurationProperties
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class WebplusApplication extends SpringBootServletInitializer  {
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(WebplusApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(WebplusApplication.class, args);
	}
}