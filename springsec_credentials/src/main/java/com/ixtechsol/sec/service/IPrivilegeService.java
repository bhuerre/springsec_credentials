package com.ixtechsol.sec.service;

import java.util.List;

import com.ixtechsol.sec.model.Privilege;
import com.ixtechsol.sec.validation.PrivilegeExistsException;
import com.ixtechsol.sec.validation.PrivilegeNotFoundException;

public interface IPrivilegeService {
	
	Privilege addPrivilege(Privilege privilege) throws PrivilegeExistsException;

	Privilege findPrivilegeByName(String name);
	
	List<Privilege> getAll();
	
	Iterable<Privilege> findAll();

	Privilege updatePrivilege(Privilege privilege)
			throws PrivilegeNotFoundException, PrivilegeExistsException;

	Privilege findPrivilegeById(long id) throws PrivilegeNotFoundException;

	void deletePrivilege(long id) throws PrivilegeNotFoundException;

	void deletePrivilege(Privilege privilege) throws PrivilegeNotFoundException;


}
