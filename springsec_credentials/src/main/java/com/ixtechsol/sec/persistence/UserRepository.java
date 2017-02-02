package com.ixtechsol.sec.persistence;

import com.ixtechsol.sec.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

	User findByUsername(String username);

}
