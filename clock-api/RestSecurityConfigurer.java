package com.faceye.component.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//@Configuration
public class RestSecurityConfigurer extends WebSecurityConfigurerAdapter {
	//不需要权限控制的URL
    @Override
    public void configure(WebSecurity web) throws Exception {
    	super.getHttp().cors().disable();
    	super.getHttp().csrf().disable();
    	super.getHttp().authorizeRequests().regexMatchers("/index").permitAll();
        web.ignoring().antMatchers("/index", "/error","/info");
    }
}
