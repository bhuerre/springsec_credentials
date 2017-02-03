package com.ixtechsol.test.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ixtechsol.sec.spring.CustCredentialsApp;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringApplicationConfiguration(CustCredentialsApp.class)
//@WebAppConfiguration
@IntegrationTest({
	"server.port=8082",
	"spring.jpa.hibernate.ddl-auto=create",
	"spring.datasource.url=jdbc:mysql://localhost/splogindbtst?createDatabaseIfnotexist=true"
	})
public @interface ContextDefinition {};
