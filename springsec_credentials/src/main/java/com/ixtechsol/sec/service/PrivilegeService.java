package com.ixtechsol.sec.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ixtechsol.sec.model.Privilege;
import com.ixtechsol.sec.persistence.PrivilegeRepository;
import com.ixtechsol.sec.validation.PrivilegeExistsException;
import com.ixtechsol.sec.validation.PrivilegeNotFoundException;
import com.ixtechsol.sec.validation.RoleNotFoundException;

@Service
@Transactional
public class PrivilegeService implements IPrivilegeService {
	
	@Autowired
	private PrivilegeRepository privilegeRepository;
	
	@Override
	public Privilege addPrivilege(Privilege privilege) throws PrivilegeExistsException {
		if (privilegeExist(privilege.getName())) {
			throw new PrivilegeExistsException("There is a privilege with this name:" + privilege.getName());
		}
		return privilegeRepository.save(privilege);
	}
	
	@Override
	public Privilege updatePrivilege(Privilege privilege) throws PrivilegeNotFoundException,PrivilegeExistsException {
		return privilegeRepository.save(privilege);
	}

	@Override
	public Privilege findPrivilegeByName(String name) {
		return privilegeRepository.findByName(name);
	}

	@Override
	public Iterable<Privilege> findAll() {
		return privilegeRepository.findAll();
	}
	
	@Override
	public List<Privilege> getAll() {
		return privilegeRepository.findAll();
	}

	private boolean privilegeExist(final String name) {
		final Privilege privilege = privilegeRepository.findByName(name);
		return privilege != null;
	}

	@Override
	public Privilege findPrivilegeById(long id) {
		return privilegeRepository.getOne(id);
	}
	
	@Override
	public void deletePrivilege(long id) throws PrivilegeNotFoundException {
		privilegeRepository.delete(id);
	}

}
