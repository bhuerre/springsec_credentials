package com.ixtechsol.sec.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.ixtechsol.sec.model.User;
import com.ixtechsol.sec.persistence.UserRepository;


@Component
public class AsyncBean {
	private static Logger logger = LoggerFactory.getLogger(AsyncBean.class);
	
	@Autowired
	UserRepository userRepository;
	
	@Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @Async
    public void asyncCall() {
    		Authentication authenticated  = SecurityContextHolder.getContext().getAuthentication();
    		logger.info("************* Asyncbean START ********************");
    		logger.info("ASYNC : asyncCall() Repo user list prepared by {} with authorities={}",
			authenticated.getName(),
			authenticated.getAuthorities());
    		List<User> users = userRepository.findAll();
    		for (User user : users) {
    			logger.info("User name = {}",user.getUsername());
    		}
    		logger.info("************* Asyncbean END  ********************");
    	}
        
    }

