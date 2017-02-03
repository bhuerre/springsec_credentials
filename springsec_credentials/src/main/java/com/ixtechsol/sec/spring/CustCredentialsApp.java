package com.ixtechsol.sec.spring;

import com.ixtechsol.sec.model.User;
import com.ixtechsol.sec.persistence.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.ixtechsol.sec")
@EnableJpaRepositories("com.ixtechsol.sec")
@EntityScan("com.ixtechsol.sec.model")
public class CustCredentialsApp {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public Converter<String, User> messageConverter() {
        return new Converter<String, User>() {
            @Override
            public User convert(String id) {
                return userRepository.findOne(Long.valueOf(id));
            }
        };
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(new Class[] { CustCredentialsApp.class, CustSecurityConfig.class, CustWebMvcConfiguration.class }, args);
    }

}
