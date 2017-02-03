package com.ixtechsol.test.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ixtechsol.sec.model.Role;
import com.ixtechsol.sec.service.IRoleService;
import com.ixtechsol.sec.validation.RoleExistsException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextDefinition
public class RoleCreate {
	Logger logger = LoggerFactory.getLogger(RoleCreate.class);
	
	private static final String USER_ROLE = "USER";
    
	@Autowired
	IRoleService roleService;
	
	@Test
	public void whenCreateRole_thenSuccess() throws RoleExistsException{
		logger.info("\n");
		logger.info("IN whenCreateRole_thenSuccess()");
		Role role = new Role(USER_ROLE);
		roleService.registerNewRole(role);
		System.out.println(roleService.findRoleByName(USER_ROLE).equals(role));
	};
}