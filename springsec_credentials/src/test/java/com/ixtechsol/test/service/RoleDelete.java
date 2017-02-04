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
public class RoleDelete {
	Logger logger = LoggerFactory.getLogger(RoleDelete.class);
	
	private static final String USER_ROLE = "USER";
    
	@Autowired
	IRoleService roleService;
	
	@Test
	public void whenDeletingRole_thenSuccess() throws RoleExistsException{
		logger.info("\n");
		logger.info("IN whenDeletingRole_thenSuccess()");
		Role role = roleService.findRoleByName(USER_ROLE);
		if (role == null) {
			role = new Role(USER_ROLE);
			roleService.registerNewRole(role);
		}
		logger.info("\tDelete role {}",role.getName());
		assert(roleService.findRoleByName(USER_ROLE).getName().equals(USER_ROLE));
		roleService.deleteRole(role);
		assert(roleService.findRoleByName(USER_ROLE) == null);
		logger.info("OUT whenDeletingRole_thenSuccess()");
	};
}
