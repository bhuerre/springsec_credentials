package com.ixtechsol.sec.persistence;

import com.ixtechsol.sec.model.SecurityQuestionDefinition;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityQuestionDefinitionRepository extends JpaRepository<SecurityQuestionDefinition, Long> {

}