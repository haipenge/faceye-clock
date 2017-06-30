package com.faceye.component.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import com.faceye.component.security.web.service.ResourceService;
import com.faceye.component.security.web.service.UserService;
import com.faceye.component.security.web.service.impl.SecurityAccessDecisionManager;

@Configuration
@EnableWebSecurity
//@Order(3)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	private Logger logger=LoggerFactory.getLogger(WebSecurityConfiguration.class);
	@Autowired
	@Qualifier("webUserService")
	private UserService userService = null;
	@Autowired
	@Qualifier("webResourceService")
	private ResourceService resourceService=null;
	
	@Autowired
	private AuthenticationManager authenticationManager=null;
	
	private AccessDecisionManager accessDecisionManager=null;
	@Autowired
	private Md5PasswordEncoder passwordEncoder = null;
	@Autowired
	private ReflectionSaltSource saltSource = null;
	
	@Autowired
	private RoleVoter roleVoter=null;
	@Autowired
	private AuthenticatedVoter authenticatedVoter=null;
	@Autowired
	private DaoAuthenticationProvider daoAuthenticationProvider=null;
	
	private FilterSecurityInterceptor filterSecurityInterceptor=null;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
        http.cors().disable().csrf().disable().addFilterBefore(filterSecurityInterceptor(), FilterSecurityInterceptor.class);
	}

//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers("/favor.ico");
//		super.configure(web);
//	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder).and().authenticationProvider(daoAuthenticationProvider);
//		authenticationManager=auth.build();
	}

	
	@Bean
	public FilterSecurityInterceptor filterSecurityInterceptor(){
		 filterSecurityInterceptor = new FilterSecurityInterceptor();
		 filterSecurityInterceptor.setSecurityMetadataSource(resourceService);
		 filterSecurityInterceptor.setAuthenticationManager(authenticationManager);
		 filterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());
		 return filterSecurityInterceptor;
	}
	
	@Bean
	public AccessDecisionManager accessDecisionManager(){
		List<AccessDecisionVoter<? extends Object>> voters=new ArrayList<AccessDecisionVoter<? extends Object>>();
		voters.add(roleVoter);
		voters.add(authenticatedVoter);
		accessDecisionManager=new SecurityAccessDecisionManager(voters);
		return accessDecisionManager;
	}
	
//	@Bean
//	public AuthenticationManager authenticationManager(){
//		List<AuthenticationProvider> providers=new ArrayList<AuthenticationProvider>(0);
//		providers.add(daoAuthenticationProvider);
//		authenticationManager=new ProviderManager(providers);
//		return authenticationManager;
//	}
//	@Bean
//	public DaoAuthenticationProvider daoAuthenticationProvider(){
//		daoAuthenticationProvider = new DaoAuthenticationProvider();
//		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
//		daoAuthenticationProvider.setSaltSource(saltSource);
//		daoAuthenticationProvider.setUserDetailsService(userService);
//		return daoAuthenticationProvider;
//	}
}
