package com.ixtechsol.sec.model;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.ixtechsol.sec.model.Role;
import com.ixtechsol.sec.validation.PasswordMatches;
@Entity
@PasswordMatches
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{error.user.username.null}")
    @NotEmpty(message = "{error.user.username.empty}")
    @Size(max = 50, message = "{error.user.username.max}")
    @Column(name = "username", length = 50)
    private String username;
    
    @Email
    @NotNull(message = "{error.user.email.null}")
    @NotEmpty(message = "{error.user.email.empty}")
    @Size(max = 50, message = "{error.user.email.max}")
    private String email;

    @NotNull(message = "{error.user.password.null}")
    @NotEmpty(message = "{error.user.password.empty}")
    @Size(max = 255, message = "{error.user.password.max}")
    @Column(name = "password", length = 255)
    private String password;

    @Transient
    @NotEmpty(message = "{error.user.passwordconfirmation.empty}")
    private String passwordConfirmation;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column
    private Calendar created = Calendar.getInstance();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", 
    	joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), 
    	inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;

     // Added Hibernate @OnetoOne to allow cascade operations on related entities
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL )
    private SecurityQuestion securityQuestion;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL )
    private VerificationToken verificationToken;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL )
    private PasswordResetToken passwordResetToken;
    
    public User() {
        super();
        this.enabled = false;
    }

//    public User(String username, String email, String password, String role, Boolean enabled) {
    public User(String username, String email, String password, Boolean enabled) {

    	this.username = username;
    	this.email = email;
    	this.password = password;
//    	if (role == "ADMIN") {
//    		this.role = "ADMIN";
//    	} else {
//    		this.role = "USER";
//    	}
    	if (enabled) {
    		this.enabled = true;
    	} else {
    		this.enabled = false;
    	}    			
    }   
    
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getCreated() {
        return this.created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
    
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(final String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(final Boolean enabled) {
        this.enabled = enabled;
    }

    public Collection<Role> getRoles() {
		return roles;
	}
    
    public List<String> getListRoles() {
    	return getRoles().stream().map(r -> r.getName()).collect(Collectors.toList());
    }

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
    public String toString() {
        return "User{" + "id=" + id + '\'' + ", username='" + username + '\'' + ", email='" + email + '\'' + ", password='" + password + '\'' + ", passwordConfirmation='" + passwordConfirmation + '\'' + ", created=" + created + ", enabled=" + enabled + '}';
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
		result = prime * result	+ ((password == null) ? 0 : password.hashCode());
		result = prime * result	+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (enabled == null) {
			if (other.enabled != null)
				return false;
		} else if (!enabled.equals(other.enabled))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}