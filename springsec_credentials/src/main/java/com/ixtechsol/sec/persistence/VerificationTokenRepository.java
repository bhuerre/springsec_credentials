package com.ixtechsol.sec.persistence;

import com.ixtechsol.sec.model.VerificationToken;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);
    VerificationToken findByUserId(Long userId);

}
