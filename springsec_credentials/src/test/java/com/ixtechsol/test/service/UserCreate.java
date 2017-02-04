package com.ixtechsol.test.service;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ixtechsol.sec.model.Privilege;
import com.ixtechsol.sec.model.Role;
import com.ixtechsol.sec.model.User;
import com.ixtechsol.sec.service.IPrivilegeService;
import com.ixtechsol.sec.service.IRoleService;
import com.ixtechsol.sec.service.IUserService;
import com.ixtechsol.sec.service.RoleService;
import com.ixtechsol.sec.spring.CustCredentialsApp;
import com.ixtechsol.sec.validation.PrivilegeExistsException;
import com.ixtechsol.sec.validation.RoleExistsException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextDefinition
public class UserCreate {
	
	Logger logger = LoggerFactory.getLogger(UserCreate.class);
	
	private static final String USER_ROLE = "USER";
	private static final String USER_NAME ="Joe";
	private static final String USER_EMAIL = "bhuerre@aol.com";
	private static final String USER_PASS = "pass";
	     
	@Autowired
	IRoleService roleService;

	@Autowired
	IUserService userService;

	@Test
	public void whenCreateUser_thenSuccess() throws PrivilegeExistsException, RoleExistsException{
		logger.info("\n");
		logger.info("IN whenCreateUser_thenSuccess()");
		Role role = roleService.findRoleByName(USER_ROLE);
		Set<Role> roles = new HashSet<Role>();
		if (role == null) {
			role = new Role(USER_ROLE);
			roleService.registerNewRole(role);
			logger.info("\tRole created {}",role);
		}
		roles.add(role);
		
		User user = new User(USER_NAME,USER_EMAIL,USER_PASS,true);
		user.setRoles(roles);
		assert(userService.findUserByUsername(USER_NAME) == null);
		userService.saveRegisteredUser(user);
		assert(userService.findUserByUsername(USER_NAME).getUsername().equals(USER_NAME));
		logger.info("\tUser {} created",user.getUsername());
		logger.info("OUT whenCreateUser_thenSuccess()\n");
	};	
}
