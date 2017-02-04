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
public class PrivilegeDelete {
	Logger logger = LoggerFactory.getLogger(PrivilegeDelete.class);
	
	private static final String USER_ROLE = "USER";
	private static final String USER_PRIVILEGE = "CTRL_READ";
    
	@Autowired
	IRoleService roleService;
	
	@Autowired
	IPrivilegeService privilegeService;
	
	@Test
	@Transactional
	@Rollback
	public void whenDeletingPrivilege_thenSuccess() throws RoleExistsException, PrivilegeExistsException, PrivilegeNotFoundException{
		logger.info("\n");
		logger.info("IN whenDeletingRole_thenSuccess()");
		Role role = roleService.findRoleByName(USER_ROLE);
		Set<Role> roles = new HashSet<Role>();
		if (role == null) {
			role = new Role(USER_ROLE);
			roleService.registerNewRole(role);
			logger.info("\tRole created {}",role);
		}
		roles.add(role);
		
		Privilege privilege = privilegeService.findPrivilegeByName(USER_PRIVILEGE);
		if (privilege == null) {
			privilege = new Privilege(USER_PRIVILEGE);
			privilege.setRoles(roles);
			privilegeService.addPrivilege(privilege);
			logger.info("\tPrivilege created {}",privilege);
			privilege = privilegeService.findPrivilegeByName(USER_PRIVILEGE);
			logger.info("Privilege confirmed created");
		}
		assert(privilegeService.findPrivilegeByName(USER_PRIVILEGE).getName().equals(USER_PRIVILEGE));
		privilegeService.deletePrivilege(privilege.getId());
		assert(privilegeService.findPrivilegeByName(USER_PRIVILEGE) == null);
		logger.info("Delete privilege {}", privilege.getName());
		roleService.deleteRole(role);
		logger.info("\tDelete role {}",role.getName());
		logger.info("OUT whenDeletePrivilege_thenSuccess()\n");
	};
}
