package com.ixtechsol.sec.persistence;

import java.util.List;

import com.ixtechsol.sec.model.SecurityQuestion;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityQuestionRepository extends JpaRepository<SecurityQuestion, Long> {

	SecurityQuestion findByQuestionDefinitionIdAndUserIdAndAnswer(Long questionDefinitionId, Long userId, String answer);
	SecurityQuestion findQuestionByUserId(Long userId);

}