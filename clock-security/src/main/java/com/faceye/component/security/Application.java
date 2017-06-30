package com.faceye.component.security;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

import com.faceye.component.security.web.util.SecurityUtil;

@EnableAuthorizationServer
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages={"com.faceye"})
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class Application {
	public static void main(String[] args) {
//		ResponseEntity e=null;
//		SpringApplication.run(Application.class, args);
		new SpringApplicationBuilder(Application.class).web(true).run(args);
	}
	@Bean(name = "auditorAware")
	public AuditorAware<String> auditorAware() {
		return ()-> SecurityUtil.getCurrentUserName();
	}
	
}
