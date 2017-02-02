package com.ixtechsol.sec.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ixtechsol.sec.model.Role;
import com.ixtechsol.sec.persistence.RoleRepository;
import com.ixtechsol.sec.validation.RoleExistsException;

@Service
@Transactional
public class RoleService implements IRoleService {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public Role registerNewRole(Role role) throws RoleExistsException {
		if (roleExist(role.getName())) {
			throw new RoleExistsException("There is a role with this name:" + role.getName());
		}
		return roleRepository.save(role);
	}
	
	@Override
	public void updateRole(Role role) {
	 roleRepository.save(role);	
	}
		
	@Override
	public Role findRoleById(long id) {
		return roleRepository.getOne(id);
	}


	@Override
	public Role findRoleByName(String name) {
		return roleRepository.findByName(name);
	}

	@Override
	public Iterable<Role> findAll() {
		return roleRepository.findAll();
	}
	
	@Override
	public List<Role> getAll() {
		return roleRepository.findAll();
	}
	
	@Override
	public void deleteRole(Role role) {
		roleRepository.delete(role);
	}

	private boolean roleExist(final String name) {
		final Role role = roleRepository.findByName(name);
		return role != null;
	}

}
