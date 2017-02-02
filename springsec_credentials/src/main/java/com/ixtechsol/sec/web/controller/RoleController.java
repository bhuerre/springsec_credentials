package com.ixtechsol.sec.web.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ixtechsol.sec.model.Privilege;
import com.ixtechsol.sec.model.Role;
import com.ixtechsol.sec.model.User;
import com.ixtechsol.sec.persistence.RoleRepository;
import com.ixtechsol.sec.service.IPrivilegeService;
import com.ixtechsol.sec.service.IRoleService;
import com.ixtechsol.sec.validation.RoleExistsException;
import com.ixtechsol.sec.validation.RoleNotFoundException;

@Controller
@RequestMapping("/role")
public class RoleController {
	
	private final static Logger logger = LoggerFactory.getLogger(RoleController.class);
	private final static String businessObject = "role"; //used in RedirectAttributes messages 
	
	@Autowired
	IRoleService roleService;
    
	@Autowired
    private MessageSource messageSource;
	

  @ModelAttribute("roles")
  public List<Role> getAllRoles() {
      return roleService.getAll();
  }
	@RequestMapping(value={"/","/list"}, method = RequestMethod.GET)
	public ModelAndView list(ModelMap model) {
		logger.debug("IN: Role/list-GET");
		
		List<Role> roles = roleService.getAll();
		model.addAttribute("roles",roles);
		//If there was an error in /add, we keep the existing value
		if (!model.containsAttribute("role")) {
			logger.debug("Adding Role object to model");
			Role role = new Role();
			model.addAttribute("role",role);
		}
		return new ModelAndView("tl/role-list",model);
	}
	
	@RequestMapping("{id}")
	public ModelAndView view(@PathVariable("id") final Role role) {
		return new ModelAndView("tl/roleview","role",role);
	}
	
	@RequestMapping(value="/add",method= RequestMethod.POST)
	public String create(@Valid @ModelAttribute Role role, 
								final BindingResult result, 
								final  HttpServletRequest request, 
								final RedirectAttributes redirect) {
		logger.debug("IN: Role/add-POST");
		if (result.hasErrors()) {
			logger.debug("Role-add error : {}",result.toString());
	        redirect.addFlashAttribute("org.springframework.validation.BindingResult.role", result);
	        redirect.addFlashAttribute("role", role);
	        return "redirect:/role/list";
		}
		try {
			roleService.registerNewRole(role);
			String message = messageSource.getMessage("ctrl.message.success.add",
					 new Object[] {businessObject, role.getName()}, Locale.US);
            redirect.addFlashAttribute("message", message);
            return "redirect:/role/list";

		} catch (RoleExistsException e) {
            String message = messageSource.getMessage("ctrl.message.error.duplicate", 
                    new Object[] {businessObject, role.getName()}, Locale.US);
            redirect.addFlashAttribute("error", message);
            return "redirect:/role/list";
		}
	}
	
	@RequestMapping(value = "delete/{id}")
	@Secured("ROLE_ADMIN")
	public String delete(@PathVariable("id") final Role role) {
		this.roleService.deleteRole(role);
		return "redirect:/role/list";
		
	}

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	@Secured("ROLE_ADMIN")
//    @PreAuthorize("hasRole('CTRL_ROLE_EDIT_GET')")
    public String editRolePage(@PathVariable("id") Role role
           , ModelMap model, RedirectAttributes redirectAttrs) {
        
    	Long id = role.getId();
        logger.debug("IN: Role/edit-GET:  ID to query = {}", id);
        try {
            if (!model.containsAttribute("role")) {
    		    logger.debug("Adding Role object to model");
    		    role = roleService.findRoleById(id);
    		    logger.debug("Role/edit-GET:  " + role.toString());
    		    model.addAttribute("role", role);
    		}
    		return "tl/role-edit";        	
        } catch (RoleNotFoundException e) {
            String message = messageSource.getMessage("ctrl.message.error.notfound", 
                    new Object[] {"role id", id}, Locale.US);
            model.addAttribute("error", message);
            return "redirect:/role/list";
        }
    }
	
	@RequestMapping(value = "edit/{id}",method= RequestMethod.POST)
	@Secured("ROLE_ADMIN")
    public String editRole(@Valid @ModelAttribute Role role,
            BindingResult result, RedirectAttributes redirect,
            @RequestParam(value = "action", required = true) String action) {

        logger.debug("IN: Role/edit-POST: " + action);

        if (action.equals(messageSource.getMessage("button.action.cancel", null, Locale.US))) {
            String message = messageSource.getMessage("ctrl.message.success.cancel", 
                    new Object[] {"Edit", businessObject, role.getName()}, Locale.US);
            redirect.addFlashAttribute("message", message);
        } else if (result.hasErrors()) {
            logger.debug("Role-edit error: " + result.toString());
            redirect.addFlashAttribute("org.springframework.validation.BindingResult.role", result);
            redirect.addFlashAttribute("role", role);
            return "redirect:/role/edit?id=" + role.getId();
        } else if (action.equals(messageSource.getMessage("button.action.save",  null, Locale.US))) {
            logger.debug("Role/edit-POST:  " + role.toString());
            try {
                roleService.updateRole(role);
                String message = messageSource.getMessage("ctrl.message.success.update", 
                        new Object[] {businessObject, role.getName()}, Locale.US);
                redirect.addFlashAttribute("message", message);
            } catch (RoleNotFoundException snf) {
                String message = messageSource.getMessage("ctrl.message.error.notfound", 
                        new Object[] {businessObject, role.getName()}, Locale.US);
                redirect.addFlashAttribute("error", message);
                return "redirect:/role/list";
            } catch (RoleExistsException dse) {
                String message = messageSource.getMessage("ctrl.message.error.duplicate", 
                        new Object[] {businessObject, role.getName()}, Locale.US);
                redirect.addFlashAttribute("error", message);
                return "redirect:/role/list";
            }
        }
        return "redirect:/role/list";
    }
	
	
	
}
