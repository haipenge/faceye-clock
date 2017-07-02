package com.faceye.component.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//@Configuration
//@EnableResourceServer
//@Order(3)
public  class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
	@Autowired
	private DefaultTokenServices tokenServices = null;
	private final static String API_RESOURCE_ID = "api-resource";
	@Autowired
	public  FilterSecurityInterceptor filterSecurityInterceptor = null;


	// @Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(API_RESOURCE_ID);
		resources.tokenServices(tokenServices);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
//		 http.csrf().disable();
		// http.cors().disable();
		 
//		http.authorizeRequests().regexMatchers("/*").authenticated().antMatchers("/index.html*", "/security/user/login*", "/shutdown*").permitAll().and().formLogin().loginPage("/security/user/login").permitAll();
		
		
		http.addFilterBefore(filterSecurityInterceptor, FilterSecurityInterceptor.class).authorizeRequests().anyRequest().authenticated()
		.antMatchers("/index.html*", "/security/user/login*", "/shutdown*").permitAll()
		.and().formLogin().loginPage("/security/user/login").permitAll()
		.failureHandler(new SimpleUrlAuthenticationFailureHandler("/security/user/login?error=failure"))
		.successHandler(new SimpleUrlAuthenticationSuccessHandler("/index.html"))
		.and().logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/security/user/logout","GET")).logoutSuccessUrl("/index.html?action=logout_success")
		.logoutSuccessHandler(new SimpleUrlLogoutSuccessHandler()).invalidateHttpSession(true).addLogoutHandler(new SecurityContextLogoutHandler())
		.and().exceptionHandling().
		accessDeniedHandler(new AccessDeniedHandlerImpl()).accessDeniedPage("/security/user/login?error=forbiden")
		.and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		.and().cors();
		 
		 //http.authorizeRequests().anyRequest().fullyAuthenticated();
		 
		// http.authorizeRequests().anyRequest().authenticated();
//		http.authorizeRequests().regexMatchers("/shutdown*").permitAll().and()
//				.requestMatchers().antMatchers("/api").and().authorizeRequests().antMatchers("/api")
//				.access("#oauth2.hasScope('read')");
		// http.requestMatchers().antMatchers("/oauth/token**").and().
//CorsFilter f=null;
		// http.
		// addFilter(headerAdminFilter).
		// authorizeRequests().
		// regexMatchers("/login.*").permitAll().
		// regexMatchers("/api.*").fullyAuthenticated().
		// regexMatchers("/jolokia.*").hasRole(ADMINISTRATOR).
		// regexMatchers("/appadmin.*").hasRole(ADMINISTRATOR).
		// regexMatchers(".*").fullyAuthenticated().
		// and().
		// formLogin().loginPage("/login").successHandler(new
		// RedirectingAuthenticationSuccessHandler()).
		// and().
		// exceptionHandling().authenticationEntryPoint(new
		// RestAwareAuthenticationEntryPoint("/login"));

	}

}