package com.ixtechsol.sec.security;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.ixtechsol.sec.model.Role;
import com.ixtechsol.sec.model.User;
import com.ixtechsol.sec.persistence.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LssUserDetailsService implements UserDetailsService {
	
	final static Logger logger = LoggerFactory.getLogger(LssUserDetailsService.class);
	
    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }
        if (logger.isDebugEnabled()) {
        	Collection<? extends GrantedAuthority> authorities = getAuthorities(user.getRoles());
        	
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getEnabled(), true, true, true, getAuthorities(user.getRoles()));
    }
    
   
    private Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
    	
    	logger.debug("Login Authorities  {} ",roles.stream().flatMap(role -> role.getPrivileges().stream())
    			.map( p -> new SimpleGrantedAuthority(p.getName()))
    			.collect(Collectors.toList()).toString());

		return roles.stream().flatMap(role -> role.getPrivileges().stream()).map( p -> new SimpleGrantedAuthority(p.getName())).collect(Collectors.toList());
    	
    }

}