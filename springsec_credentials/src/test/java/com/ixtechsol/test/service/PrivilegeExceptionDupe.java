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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.ixtechsol.sec.model.Privilege;
import com.ixtechsol.sec.model.Role;
import com.ixtechsol.sec.service.IPrivilegeService;
import com.ixtechsol.sec.service.IRoleService;
import com.ixtechsol.sec.service.RoleService;
import com.ixtechsol.sec.spring.CustCredentialsApp;
import com.ixtechsol.sec.validation.PrivilegeExistsException;
import com.ixtechsol.sec.validation.PrivilegeNotFoundException;
import com.ixtechsol.sec.validation.RoleExistsException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextDefinition
public class PrivilegeExceptionDupe {
	
	Logger logger = LoggerFactory.getLogger(PrivilegeExceptionDupe.class);
	
	private static final String USER_ROLE = "USER";
	private static final String USER_PRIVILEGE = "CTRL_READ";
	     
	@Autowired
	IRoleService roleService;

	@Autowired
	IPrivilegeService privilegeService;
	
	@Test
	@Transactional
	@Rollback
	public void whenPrivilegeExceptionDupe_thenSuccess() throws PrivilegeExistsException, RoleExistsException, PrivilegeNotFoundException{
		logger.info("\n");
		logger.info("IN whenPrivilegeExceptionDupe_thenSuccess()");
		
		Privilege privilege = new Privilege();
		privilege = new Privilege(USER_PRIVILEGE);
		try {
			privilegeService.addPrivilege(privilege);
			logger.info("\tPrivilege created {}",USER_PRIVILEGE);
			privilegeService.addPrivilege(privilege);			
			assert (1==2); 
		} catch  (PrivilegeExistsException e) {
			assert(e.getLocalizedMessage().length()>0);
		}
		assert(privilegeService.findPrivilegeByName(USER_PRIVILEGE).getName().equals(USER_PRIVILEGE));
		logger.info("OUT whenPrivilegeExceptionDupe_thenSuccess()\n");
	};
	
}
