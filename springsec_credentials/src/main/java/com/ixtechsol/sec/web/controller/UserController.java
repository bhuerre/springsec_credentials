package com.ixtechsol.sec.web.controller;

//import java.util.ArrayList;
//import java.util.HashMap;
import java.util.List;
import java.util.Locale;
//import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//import com.ixtechsol.sec.service.RoleService;

import com.ixtechsol.sec.model.Role;
import com.ixtechsol.sec.model.User;
import com.ixtechsol.sec.persistence.RoleRepository;
import com.ixtechsol.sec.persistence.UserRepository;
import com.ixtechsol.sec.service.IRoleService;
import com.ixtechsol.sec.service.IUserService;
import com.ixtechsol.sec.validation.EmailExistsException;
import com.ixtechsol.sec.validation.UsernameExistsException;

@Controller
@RequestMapping("/user")
class UserController {
	static Logger logger = LoggerFactory.getLogger(UserController.class);
	static String businessObject = "user"; //used in RedirectAttributes messages
	 
   @Autowired
	private Environment env;

    @Autowired
    private UserRepository userRepository;
        
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private IRoleService roleService;
    
    @Autowired
    private IUserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MessageSource messageSource;

    
    @ModelAttribute("allRoles")
    public List<Role> getAllRoles() {
        return roleService.getAll();
    }
    
    @RequestMapping(value={"","/list"}, method = RequestMethod.GET)
    public String listUser(Model model) {
        List<User> users = userService.getAll();
        model.addAttribute("users", users);
        
        // if there was an error in /add, we do not want to overwrite
        // the existing user object containing the errors.
        if (!model.containsAttribute("user")) {
            logger.debug("Adding User object to model");
            User user = new User();
            model.addAttribute("user", user);
        }
        return "tl/user-list";
    }

    @RequestMapping("{id}")
    public ModelAndView view(@PathVariable("id") final User user) {
        return new ModelAndView("tl/userview", "user", user);
    }

	@RequestMapping(value = "edit/{id}",method= RequestMethod.POST)
	@Secured("ROLE_ADMIN")
    public String editUser(@ModelAttribute User user,
            BindingResult result, RedirectAttributes redirect,
            @RequestParam(value = "action", required = true) String action) {

        logger.debug("IN: User/edit-POST: " + action);

        if (action.equals(messageSource.getMessage("button.action.cancel", null, Locale.US))) {
            String message = messageSource.getMessage("ctrl.message.success.cancel", 
                    new Object[] {"Edit", businessObject, user.getUsername()}, Locale.US);
            redirect.addFlashAttribute("message", message);
        } else if (result.hasErrors()) {
            logger.debug("User-edit error: " + result.toString());
            //redirect.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            redirect.addFlashAttribute("user", user);
            return "redirect:/user/edit/" + user.getId();
        } else if (action.equals(messageSource.getMessage("button.action.save",  null, Locale.US))) {
            logger.debug("User/edit-POST:  " + user.toString());
            try {
                userService.adminUpdateRegisteredUser(user);
                String message = messageSource.getMessage("ctrl.message.success.update", 
                       new Object[] {businessObject, user.getUsername()}, Locale.US);
                redirect.addFlashAttribute("message", message);
            } catch (UsernameExistsException dse) {
                String message = messageSource.getMessage("ctrl.message.error.duplicate", 
                       new Object[] {businessObject, user.getUsername()}, Locale.US);
                redirect.addFlashAttribute("error", message);
                return "redirect:/user/edit/" + user.getId();
            } catch (EmailExistsException e) {
            	return "redirect:/user/edit/" + user.getId();
			}
        }
        return "redirect:/user/list";
    }
    
    
	@RequestMapping(value="/add",method= RequestMethod.POST)
	public String createUser(@Valid @ModelAttribute User user, 
								final BindingResult result, 
								final  HttpServletRequest request, 
								final RedirectAttributes redirect)  {
		logger.debug("IN: User/add-POST");
		if (result.hasErrors()) {
			logger.debug("User-add error : {}",result.toString());
	        redirect.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
	        redirect.addFlashAttribute("user", user);
	        return "redirect:/user/add";
		}
		try {
			userService.registerNewUser(user);
			String message = messageSource.getMessage("ctrl.message.success.add",
					 new Object[] {businessObject, user.getUsername()}, Locale.US);
            redirect.addFlashAttribute("message", message);
            if (user != null) {
            	final String token = UUID.randomUUID().toString();
            	userService.createPasswordResetTokenForUser(user, token);
            	final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            	final SimpleMailMessage email = constructPasswordResetTokenEmail(appUrl, token, user);
            	mailSender.send(email);
            }
            return "redirect:/user/list";

		} catch (UsernameExistsException e) {
            String message = messageSource.getMessage("ctrl.message.error.duplicate", 
                    new Object[] {businessObject, user.getUsername()}, Locale.US);
            redirect.addFlashAttribute("error", message);
            redirect.addFlashAttribute("user", user);
            return "redirect:/user/add";
		} catch (EmailExistsException e) {
            String message = messageSource.getMessage("ctrl.message.error.duplicatemail", 
                    new Object[] {businessObject, user.getEmail()}, Locale.US);
            redirect.addFlashAttribute("error", message);
            redirect.addFlashAttribute("user", user);
            return "redirect:/user/add";
		}
	}
	
    
	@RequestMapping(value = "delete/{id}")
	@Secured("ROLE_ADMIN")
	public String delete(@PathVariable("id") final User user) {
		this.userService.deleteUser(user);
		return "redirect:/user/list";
		
	}

	
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
//    @PreAuthorize("hasUser('CTRL_ROLE_EDIT_GET')")
    public String editUserPage(@PathVariable("id") User user
           , Model model, RedirectAttributes redirectAttrs) {
    	Long id = user.getId();
        logger.debug("IN: User/edit-GET:  ID to query = {}", id);
            if (!model.containsAttribute("user")) {
    		    logger.debug("Adding User object to model");
    		    user = userService.findUserById(id);
    		    logger.debug("User/edit-GET:  " + user.toString());
    		    model.addAttribute("user", user);
    		}
    		return "tl/user-edit";        	
    }
	
    @RequestMapping(value = "/add", method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
//    @PreAuthorize("hasUser('CTRL_ROLE_EDIT_GET')")
    public String addUserPage(Model model) {
        // if there was an error in /add, we do not want to overwrite
        // the existing user object containing the errors.
        if (!model.containsAttribute("user")) {
            logger.debug("Adding User object to model");
            User user = new User();
            model.addAttribute("user", user);
        }
    	return "tl/user-add";        	
    }

        
    // userform

    @RequestMapping(params = "userform", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    //@PreAuthorize("hasIpAddress('192.168.2.0/24') or hasRole('ADMIN')")
    //@PreAuthorize("hasRole('ADMIN')")
//    @Secured("ROLE_ADMIN")
    public String createForm(@ModelAttribute final User user) {
        return "tl/userform";
    }
    
    // NON-API

    private SimpleMailMessage constructPasswordResetTokenEmail(final String contextPath, final String token, final User user) {
        final String url = contextPath + "/user/changePassword?id=" + user.getId() + "&token=" + token;
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Complete registration");
        email.setText("Please open the following URL to complete the registration: \r\n" + url);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
}
