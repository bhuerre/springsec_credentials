package com.ixtechsol.test.service;


import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ixtechsol.sec.model.Role;
import com.ixtechsol.sec.service.IRoleService;
import com.ixtechsol.sec.validation.RoleExistsException;
import com.ixtechsol.sec.validation.RoleNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextDefinition
public class RoleExceptionDupe {
	Logger logger = LoggerFactory.getLogger(RoleExceptionDupe.class);
	
	private static final String USER_ROLE = "USER";
    
	@Autowired
	IRoleService roleService;
	
	@Test
	@Transactional
	@Rollback
	public void whenRoleExceptionDupe_thenSuccess() throws RoleExistsException,RoleNotFoundException{
		logger.info("\n");
		logger.info("IN whenRoleExceptionDupe_thenSuccess()");
		Role role = new Role();
		try {
			role.setName(USER_ROLE);
			roleService.registerNewRole(role);
			logger.info("\tAdding first occuren for role = {}",USER_ROLE);
			roleService.registerNewRole(role);
			//Executing Line bellow would mean a fail
			assert(1==2);
		} catch (RoleExistsException e) {
			logger.info("\tERROR while adding 2nd occurence for role = {}",USER_ROLE);
			assert(e.getLocalizedMessage().length()>0);
		}
		logger.info("OUT whenRoleExceptionDupe_thenSuccess()");
	};
}
