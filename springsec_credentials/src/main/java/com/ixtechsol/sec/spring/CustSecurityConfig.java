package com.ixtechsol.sec.spring;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ixtechsol.sec.persistence.UserRepository;
import com.ixtechsol.sec.security.CustomFilterLogging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@EnableWebSecurity
@EnableAsync
public class CustSecurityConfig extends WebSecurityConfigurerAdapter {
	private final static Logger logger = LoggerFactory.getLogger(CustSecurityConfig.class);

	@Autowired
	private Environment env;
    
	@Autowired
    private UserDetailsService userDetailsService;

	@Autowired
	private CustomFilterLogging customFilterLogging;
	
    public CustSecurityConfig() {
    SecurityContextHolder.setStrategyName("MODE_INHERITABLETHREADLOCAL");
    }

    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {// @formatter:off
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(runAsAuthenticationProvider());
    	auth.ldapAuthentication()
          	.userSearchBase(env.getProperty("ldap.usersearchbase"))
          	.userSearchFilter(env.getProperty("ldap.usersearchfilter"))
          	.groupRoleAttribute("cn")
          	.groupSearchBase(env.getProperty("ldap.groupsearchbase"))
          	.groupSearchFilter(env.getProperty("ldap.groupsearchfilter"))
          	.contextSource()
          	.managerDn(env.getProperty("ldap.managerdn"))
          	.managerPassword(env.getProperty("ldap.managerpw"))
          	.url(env.getProperty("ldap.url"));
    } // @formatter:on

    
    @Override
    protected void configure(HttpSecurity http) throws Exception { // @formatter:off
        http
        
				.addFilterBefore(customFilterLogging,AnonymousAuthenticationFilter.class)
				.authorizeRequests() //.antMatchers("/css/**/").permitAll()        
				//.authorizeRequests()
        		//.antMatchers("/secured").access("hasRole('ADMIN')")
        		//.antMatchers("/secured").hasRole("ADMIN")
        		//.antMatchers("/secured").hasIpAddress("192.168.2.4")
        		//.antMatchers("/secured").not().access("hasIpAddress('192.168.2.4')")
        		//.antMatchers("/secured").hasAuthority("ROLE_ADMIN")
        		//.antMatchers("/secured").access("hasIpAddress('192.168.2.4')")
        		//.antMatchers("/secured").anonymous()
        		//.antMatchers("/secured").access("isAnonymous()")
        		//.antMatchers("/secured").access("request.method == 'GET'")
        		//.antMatchers("/secured").access("request.method != 'POST'")
        		//.antMatchers("/secured").access("hasRole('ADMIN') and request.method != 'POST'")
//        		.antMatchers("/secured").access("hasRole('ADMIN') or request.method != 'GET'")        
//        		.antMatchers("POST","/delete/*").hasRole("ADMIN")
                .antMatchers("/signup",
                		"/resources/**",
                        "/resources/css/**",
                        "/user/register",
                        "/registrationConfirm*",
                        "/badUser*",
                        "/forgotPassword*",
                        "/user/resetPassword*",
                        "/user/changePassword*",
                        "/user/savePassword*",
                        "/profile",
                        "/asyncbean",
                        "/js/**",
                        "/css/**",
                        "/public/**",
                        "/").permitAll()
         .antMatchers(
        		 		"/user",
        		 		"/user/",
        				"/user/list",
        				"/role*",
        				"/privileges*"
        		).authenticated()
                        
//                .anyRequest().permitAll()

        .and()
        .formLogin().
            loginPage("/login").permitAll().
            loginProcessingUrl("/doLogin")

        .and()
        .logout().permitAll().logoutUrl("/logout")
        
//        .and()
//        .rememberMe()
//        .tokenValiditySeconds(604800)
//        .key("LssAppKey")
//        //.useSecureCookie(true) //remember-me cookie only created if https connection
//        .rememberMeCookieName("sticky-cookie")
//        .rememberMeParameter("remember")        
        
        .and()
        .csrf().disable()
        ;
    } // @formatter:on
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    }
    
    @Bean
    public AuthenticationProvider runAsAuthenticationProvider() {
        final RunAsImplAuthenticationProvider authProvider = new RunAsImplAuthenticationProvider();
        authProvider.setKey("MyRunAsKey");
        return authProvider;
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

    //Centralize list to manage access to controller methods
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public static class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    	
        @Override
        public MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
            final Map<String, List<ConfigAttribute>> methodMap = new HashMap<>();
            methodMap.put("com.ixtechsol.sec.web.controller.UserController.createForm*", SecurityConfig.createList("ROLE_ADMIN"));
            return new MapBasedMethodSecurityMetadataSource(methodMap);
        }
    }
    
}
