package com.ixtechsol.sec.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ixtechsol.sec.model.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Privilege findByName(String name);

    @Override
    void delete(Privilege privilege);

}
