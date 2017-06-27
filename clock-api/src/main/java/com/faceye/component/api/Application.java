package com.faceye.component.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = { "com.faceye" })
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
public class Application {
	private static Logger logger=LoggerFactory.getLogger(Application.class);
	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		ApplicationContext ctx=SpringApplication.run(Application.class, args);
//		ApplicationContext ctx = new SpringApplicationBuilder(Application.class).web(true).run(args);
		String[] activeProfiles = ctx.getEnvironment().getActiveProfiles();
		for (String profile : activeProfiles) {
			logger.warn("clock-provider  Spring Boot 使用profile为:{}", profile);
		}
	}

//	@Configuration
//    protected static class RestSecurity extends WebSecurityConfigurerAdapter {
//    	//不需要权限控制的URL
//        @Override
//        public void configure(WebSecurity web) throws Exception {
//            web.ignoring().antMatchers("/info", "/error");
//        }
//    }
}