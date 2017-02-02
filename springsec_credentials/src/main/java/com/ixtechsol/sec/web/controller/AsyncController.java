package com.ixtechsol.sec.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ixtechsol.sec.model.User;
import com.ixtechsol.sec.persistence.UserRepository;
import com.ixtechsol.sec.service.AsyncBean;
import com.ixtechsol.sec.service.IUserService;

@Controller
@RequestMapping("/asyncbean")
public class AsyncController {
	private final static Logger logger = LoggerFactory.getLogger(UserController.class);
	
//	@Autowired 
//	AsyncBean asyncBean;

	@Autowired
	IUserService userService;
	
 
	@Secured({"ROLE_USER","ROLE_ADMIN"})
    @RequestMapping
    public String list() {
		logger.info("In AsyncPrint Controller");
//		asyncBean.asyncCall();
		userService.printAll();
        return "redirect:/user";
    }

}
