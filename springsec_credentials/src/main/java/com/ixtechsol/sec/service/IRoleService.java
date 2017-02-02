package com.ixtechsol.sec.service;

import java.util.List;

import com.ixtechsol.sec.model.Role;
import com.ixtechsol.sec.validation.RoleExistsException;
import com.ixtechsol.sec.validation.RoleNotFoundException;

public interface IRoleService {
	Role findRoleById(long id) throws RoleNotFoundException;

	Role registerNewRole(Role role) throws RoleExistsException;
	
	Role findRoleByName(String name);
	
	List<Role> getAll();
	
	Iterable<Role> findAll();

	void updateRole(Role role) throws RoleExistsException,RoleNotFoundException;
	
	void deleteRole(Role role);

	

}
