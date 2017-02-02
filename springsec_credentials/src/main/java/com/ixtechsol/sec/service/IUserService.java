package com.ixtechsol.sec.service;

import java.util.List;

import com.ixtechsol.sec.model.PasswordResetToken;
import com.ixtechsol.sec.model.User;
import com.ixtechsol.sec.model.VerificationToken;
import com.ixtechsol.sec.validation.EmailExistsException;
import com.ixtechsol.sec.validation.UsernameExistsException;

public interface IUserService {

    User registerNewUser(User user) throws EmailExistsException, UsernameExistsException;

    User findUserByEmail(String email);
    
    User findUserByUsername(String username);

    void createPasswordResetTokenForUser(User user, String token);

    PasswordResetToken getPasswordResetToken(String token);

    void changeUserPassword(User user, String password);

    void createVerificationTokenForUser(User user, String token);

    VerificationToken getVerificationToken(String token);

    void saveRegisteredUser(User user);

	Iterable<User> findAll();

	void printAll();

	List<String> getRoleList(User user);

	User findUserById(long id);

	List<User> getAll();

	void deleteUser(User user);

	void adminUpdateRegisteredUser(User user) throws EmailExistsException,UsernameExistsException;

}
