package com.ixtechsol.test.service;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextDefinition
public class ContextLoad {
	
	final static Logger logger = LoggerFactory.getLogger(ContextLoad.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	public void PrepareDatabase() throws SQLException {
		logger.info("IN EmptyDatabase() for {}",this.jdbcTemplate.getDataSource().getConnection().toString());
		// Empty Tests data from "User","Role","Privilege"
		List<String> tablelist = Arrays.asList("User","Role","Privilege"); 
		for ( String entity : tablelist) {
			if (JdbcTestUtils.countRowsInTable(this.jdbcTemplate,entity)  > 0) {
				logger.info("/tEmptying {} table.",entity);
				JdbcTestUtils.deleteFromTables(this.jdbcTemplate,entity);
			}			
		}
		// Import Security Questions from external scripts if not found
		int rowCount = this.jdbcTemplate.queryForObject("select count(*) from SecurityQuestionDefinition", Integer.class);
		if ( rowCount == 0) {
			logger.info("\tEmptying {} table.","SecurityQuestionDefinition");
			JdbcTestUtils.deleteFromTables(this.jdbcTemplate, "SecurityQuestionDefinition");
			Resource resource = new ClassPathResource("importQuestions.sql");
			ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(resource);
			databasePopulator.execute(this.jdbcTemplate.getDataSource());
			logger.info("/tImported SecurityQuestions from {}","importQuestions.sql");
		}
		rowCount = JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "\tSecurityQuestionDefinition");
		logger.info("SecurityQuestionDefinition has {} rows",rowCount);
		logger.info("OUT PrepareDatabase()");
	}
	
	
	@Test
	public void whenLoadTestApplicationContext_thenSuccess(){
	}
}
