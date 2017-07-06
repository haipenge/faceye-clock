package com.faceye.component.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.faceye.component.security.web.service.ResourceService;
import com.faceye.component.security.web.service.UserService;
import com.faceye.component.security.web.service.impl.AccessDeniedHandlerImpl;

@Configuration
@EnableWebSecurity
// @Order(4)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	private Logger logger = LoggerFactory.getLogger(WebSecurityConfiguration.class);
	@Autowired
	@Qualifier("webUserService")
	private UserService userService = null;
	@Autowired
	@Qualifier("webResourceService")
	private ResourceService resourceService = null;

	@Autowired
	private Md5PasswordEncoder passwordEncoder = null;
	@Autowired
	private ReflectionSaltSource saltSource = null;

	// @Autowired
	// private RoleVoter roleVoter = null;
	// @Autowired
	// private AuthenticatedVoter authenticatedVoter = null;
	@Autowired
	private DaoAuthenticationProvider daoAuthenticationProvider = null;
	@Autowired
	private FilterSecurityInterceptor filterSecurityInterceptor = null;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		////////////////////////////////// 简单基础登陆////////////////////////////////////////
		// csrf().disable().cors().disable()
		http.csrf().disable().cors().disable()
				.addFilterBefore(filterSecurityInterceptor, FilterSecurityInterceptor.class).authorizeRequests()
				.anyRequest().authenticated().and().formLogin().loginPage("/user/login").loginProcessingUrl("/login")
				.permitAll().successHandler(new SimpleUrlAuthenticationSuccessHandler("/"))
				.failureHandler(new SimpleUrlAuthenticationFailureHandler("/user/login?error=failure")).and()
				.exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerImpl())
				.accessDeniedPage("/user/login?error=forbiden").and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout", "GET")).permitAll()
				.logoutSuccessUrl("/user/login?action=logout_success")
				.logoutSuccessHandler(new SimpleUrlLogoutSuccessHandler()).invalidateHttpSession(true)
				.addLogoutHandler(new SecurityContextLogoutHandler()).and().exceptionHandling()
				.accessDeniedHandler(new AccessDeniedHandlerImpl()).accessDeniedPage("/user/login?error=forbiden");
		////////////////////////////////////////////////////////////////////////////////////////////

		// http.authorizeRequests().anyRequest().authenticated().and().formLogin().loginPage("/user/login").permitAll()
		// .successHandler(new SimpleUrlAuthenticationSuccessHandler("/"))
		// .failureHandler(new
		// SimpleUrlAuthenticationFailureHandler("/user/login?error=failure")).and()
		// .httpBasic().and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
		// .cors().and().rememberMe().and()
		// .addFilterBefore(filterSecurityInterceptor,
		// FilterSecurityInterceptor.class);

		// http.httpBasic().and().authorizeRequests().anyRequest().authenticated().and().formLogin()
		// .loginPage("/user/login").permitAll().successHandler(new
		// SimpleUrlAuthenticationSuccessHandler("/"))
		// .failureHandler(new
		// SimpleUrlAuthenticationFailureHandler("/user/login?error=failure")).and()
		// .exceptionHandling().accessDeniedHandler(new
		// AccessDeniedHandlerImpl())
		// .accessDeniedPage("/user/login?error=forbiden").and().logout()
		// .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout",
		// "GET")).permitAll()
		// .logoutSuccessUrl("/user/login?action=logout_success")
		// .logoutSuccessHandler(new
		// SimpleUrlLogoutSuccessHandler()).invalidateHttpSession(true)
		// .addLogoutHandler(new
		// SecurityContextLogoutHandler()).and().exceptionHandling()
		// .accessDeniedHandler(new
		// AccessDeniedHandlerImpl()).accessDeniedPage("/user/login?error=forbiden").and()
		// .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and().cors().and()
		// .addFilterBefore(filterSecurityInterceptor,
		// FilterSecurityInterceptor.class);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/", "/favor.ico", "/static/**", "/shutdown");
		super.configure(web);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder).and()
				.authenticationProvider(daoAuthenticationProvider);
		// authenticationManager=auth.build();
	}

	// @Bean
	// public AuthenticationManager authenticationManager(){
	// List<AuthenticationProvider> providers=new
	// ArrayList<AuthenticationProvider>(0);
	// providers.add(daoAuthenticationProvider);
	// authenticationManager=new ProviderManager(providers);
	// return authenticationManager;
	// }
	// @Bean
	// public DaoAuthenticationProvider daoAuthenticationProvider(){
	// daoAuthenticationProvider = new DaoAuthenticationProvider();
	// daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
	// daoAuthenticationProvider.setSaltSource(saltSource);
	// daoAuthenticationProvider.setUserDetailsService(userService);
	// return daoAuthenticationProvider;
	// }
}
