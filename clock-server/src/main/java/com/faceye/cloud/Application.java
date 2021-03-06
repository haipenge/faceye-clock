package com.faceye.cloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ApplicationContext;

@EnableEurekaServer
@SpringBootApplication
public class Application {
	private static Logger logger = LoggerFactory.getLogger(Application.class);
	public static void main(String[] args) {
		ApplicationContext ctx = new SpringApplicationBuilder(Application.class).web(true).run(args);
		String[] activeProfiles = ctx.getEnvironment().getActiveProfiles();
		for (String profile : activeProfiles) {
			logger.warn("clock-server  Spring Boot 使用profile为:{}", profile);
		}
	}
}