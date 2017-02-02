package com.ixtechsol.sec.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ixtechsol.sec.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
    

    @Override
    void delete(Role role);

}
