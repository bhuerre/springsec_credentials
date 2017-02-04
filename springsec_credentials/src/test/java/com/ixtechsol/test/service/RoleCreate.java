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
import com.ixtechsol.sec.validation.RoleNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextDefinition
public class RoleCreate {
	Logger logger = LoggerFactory.getLogger(RoleCreate.class);
	
	private static final String USER_ROLE = "USER1";
    
	@Autowired
	IRoleService roleService;
	
	@Test
	public void whenCreateRole_thenSuccess() throws RoleExistsException,RoleNotFoundException{
		logger.info("\n");
		logger.info("IN whenCreateRole_thenSuccess()");
		Role role = roleService.findRoleByName(USER_ROLE);
		if (role != null ) {
			roleService.deleteRole(role);
			logger.info("\tDelete role {}",USER_ROLE);
		} 
		role.setName(USER_ROLE);
		roleService.registerNewRole(role);
		logger.info("OUT whenCreateRole_thenSuccess()");
	};
}
