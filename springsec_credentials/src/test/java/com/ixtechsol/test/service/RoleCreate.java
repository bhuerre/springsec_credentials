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
public class RoleCreate {
	Logger logger = LoggerFactory.getLogger(RoleCreate.class);
	
	private static final String USER_ROLE = "USER";
    
	@Autowired
	IRoleService roleService;
	
	@Test
	@Transactional
	@Rollback
	public void whenCreateRole_thenSuccess() throws RoleExistsException,RoleNotFoundException{
		logger.info("\n");
		logger.info("IN whenCreateRole_thenSuccess()");
		if (roleService.findRoleByName(USER_ROLE) != null ) {
			roleService.deleteRole(roleService.findRoleByName(USER_ROLE));
			logger.info("\tDelete role {}",USER_ROLE);
		} 
		Role role = new Role(); 
		role.setName(USER_ROLE);
		assert(roleService.findRoleByName(USER_ROLE) == null);
		roleService.registerNewRole(role);
		assert(roleService.findRoleByName(USER_ROLE).getName().equals(USER_ROLE));
		logger.info("OUT whenCreateRole_thenSuccess()");
	};
}
