package com.ixtechsol.sec.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import com.ixtechsol.sec.model.PasswordResetToken;
import com.ixtechsol.sec.model.Role;
import com.ixtechsol.sec.model.User;
import com.ixtechsol.sec.model.VerificationToken;
import com.ixtechsol.sec.persistence.PasswordResetTokenRepository;
import com.ixtechsol.sec.persistence.UserRepository;
import com.ixtechsol.sec.persistence.VerificationTokenRepository;
import com.ixtechsol.sec.validation.EmailExistsException;
import com.ixtechsol.sec.validation.UsernameExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
class UserService implements IUserService {
Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AsyncBean asyncBean;

    @Override
    public Iterable<User> findAll(){
    	return userRepository.findAll();
    }

    public void printAll(){	
    	asyncBean.asyncCall();
    	return;
    }

    @Override
    public User registerNewUser(final User user) throws EmailExistsException, UsernameExistsException {
        if (emailExist(user.getEmail())) {
            throw new EmailExistsException("There is an account with that email address: " + user.getEmail());
        }
        if (usernameExist(user.getUsername())) {
            throw new UsernameExistsException("There is an account with this username: " + user.getUsername());
        }        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

	@Override
	public User findUserById(long id) {
		return userRepository.findOne(id);
	}
    
    @Override
	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

    @Override
    public User findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }

    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token);
    }


    @Override
    public void changeUserPassword(final User user, final String password) {
    	user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public void createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }

    @Override
    public VerificationToken getVerificationToken(final String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public void adminUpdateRegisteredUser(final User user) throws EmailExistsException, UsernameExistsException {
		User usertmp = userRepository.getOne(user.getId());
		logger.debug("Updating user {}",usertmp.toString());

		//Update password if changed
		if (user.getPassword().length() > 0) {		
			logger.debug("Update password for {} with new value",user.getUsername());
			usertmp.setPassword(passwordEncoder.encode(user.getPassword()));
			usertmp.setPasswordConfirmation(usertmp.getPassword());
		}
		//Transform Role Collection to Role Set
		usertmp.setEnabled(user.getEnabled());
		Set<Role> rolestmp = new HashSet<Role>();
		for (Role role : user.getRoles()) {
			rolestmp.add(role);
		}
		usertmp.setRoles(rolestmp);

		//Update if not found in repo email or username as provided by user  
		if (!(user.getUsername().equals(usertmp.getUsername())) && usernameExist(user.getUsername()) ) {
            throw new UsernameExistsException("There is an account with this username: " + user.getUsername());
		};
		usertmp.setUsername(user.getUsername());
		if (!(user.getEmail().equals(usertmp.getEmail())) && emailExist(user.getEmail())) {
			throw new EmailExistsException("There is an account with this email: " + user.getEmail());
		};
		usertmp.setEmail(user.getEmail());
        userRepository.save(usertmp);
    }

    @Override
    public void saveRegisteredUser(final User user) {
        userRepository.save(user);
    }
    
    @Override
    public List<String> getRoleList(final User user) {
    	List<String> troles = new ArrayList<String>();
    	for (Role role : user.getRoles()) {
    		troles.add(role.getName());
    	}
    	return troles;
    }
    
    private boolean emailExist(final String email) {
        final User user = userRepository.findByEmail(email);
        return user != null;
    }

    private boolean usernameExist(final String username) {
    	final User user = userRepository.findByUsername(username);
    	return user != null;
    }

	@Override
	public List<User> getAll() {
		return userRepository.findAll();
	}

	@Override
	public void deleteUser(User user) {
		userRepository.delete(user);
	}
}
