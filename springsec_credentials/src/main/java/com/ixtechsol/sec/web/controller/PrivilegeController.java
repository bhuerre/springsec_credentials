package com.ixtechsol.sec.web.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.ixtechsol.sec.persistence.PrivilegeRepository;
import com.ixtechsol.sec.service.IPrivilegeService;
import com.ixtechsol.sec.service.IRoleService;
import com.ixtechsol.sec.validation.PrivilegeExistsException;
import com.ixtechsol.sec.validation.PrivilegeNotFoundException;
import com.ixtechsol.sec.validation.RoleNotFoundException;

@Controller
@RequestMapping("/privilege")
//@PreAuthorize("denyAll")
public class PrivilegeController {
	
	private final static Logger logger = LoggerFactory.getLogger(PrivilegeController.class);
    static String businessObject = "permission"; //used in RedirectAttributes messages 
	
    @Autowired
    private IPrivilegeService privilegeService;
    
    @Autowired
    private IRoleService roleService;
    

    @Autowired
    private MessageSource messageSource;

    @ModelAttribute("allRoles")
    //@PreAuthorize("hasAnyRole('CTRL_PERM_LIST_GET','CTRL_PERM_EDIT_GET')")
    public List<com.ixtechsol.sec.model.Role> getAllRoles() {
        return roleService.getAll();
    }

    @RequestMapping(value = {"/", "/list"}, method = RequestMethod.GET)
    //@PreAuthorize("hasRole('CTRL_PERM_LIST_GET')")
    public String listPrivilege(Model model) {
        logger.debug("IN: Privilege/list-GET");

        List<Privilege> allPrivileges = privilegeService.getAll();
        model.addAttribute("allPrivileges", allPrivileges);

        // if there was an error in /add, we do not want to overwrite
        // the existing privilege object containing the errors.
        if (!model.containsAttribute("privilege")) {
            logger.debug("Adding Privilege object to model");
            Privilege privilege = new Privilege();
            model.addAttribute("privilege", privilege);
        }
        return "tl/privilege-list";
    }
    
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    //@PreAuthorize("hasRole('CTRL_PERM_ADD_POST')")
    public String addPrivilege(@Valid @ModelAttribute Privilege privilege,
            BindingResult result, RedirectAttributes redirectAttrs) {
        
        logger.debug("IN: Privilege/add-POST");
        logger.debug("  Object: " + privilege.toString());

        if (result.hasErrors()) {
            logger.debug("Privilege add error: " + result.toString());
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.privilege", result);
            redirectAttrs.addFlashAttribute("privilege", privilege);
            return "redirect:/privilege/list";
        } else {
            try {
                privilegeService.addPrivilege(privilege);
                String message = messageSource.getMessage("ctrl.message.success.add", 
                        new Object[] {businessObject, privilege.getName()}, Locale.US);
                redirectAttrs.addFlashAttribute("message", message);
                return "redirect:/privilege/list";
            } catch (PrivilegeExistsException e) {
                String message = messageSource.getMessage("ctrl.message.error.duplicate", 
                        new Object[] {businessObject, privilege.getName()}, Locale.US);
                redirectAttrs.addFlashAttribute("error", message);
                return "redirect:/privilege/list";
           } 
        }
    }

    @RequestMapping(value = "edit/{id}", method = RequestMethod.POST)
//    @PreAuthorize("hasRole('CTRL_PERM_EDIT_POST')")
    public String editPrivilege(@Valid @ModelAttribute Privilege privilege,
            BindingResult result, RedirectAttributes redirectAttrs,
            @RequestParam(value = "action", required = true) String action) {

        logger.debug("IN: Privilege/edit-POST: " + action);

        if (action.equals(messageSource.getMessage("button.action.cancel", null, Locale.US))) {
            String message = messageSource.getMessage("ctrl.message.success.cancel", 
                    new Object[] {"Edit", businessObject, privilege.getName()}, Locale.US);
            redirectAttrs.addFlashAttribute("message", message);
        } else if (result.hasErrors()) {
            logger.debug("Privilege-edit error: " + result.toString());
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.privilege", result);
            redirectAttrs.addFlashAttribute("privilege", privilege);
            return "redirect:/privilege/edit/" + privilege.getId();
        } else if (action.equals(messageSource.getMessage("button.action.save",  null, Locale.US))) {
            logger.debug("Privilege/edit-POST:  " + privilege.toString());
            try {
                //Privilege privilege = getPrivilege(privilege);
                privilegeService.updatePrivilege(privilege);
                String message = messageSource.getMessage("ctrl.message.success.update", 
                        new Object[] {businessObject, privilege.getName()}, Locale.US);
                redirectAttrs.addFlashAttribute("message", message);
            } catch (PrivilegeExistsException unf) {
                String message = messageSource.getMessage("ctrl.message.error.duplicate", 
                        new Object[] {businessObject, privilege.getName()}, Locale.US);
                redirectAttrs.addFlashAttribute("error", message);
                return "redirect:/privilege/list";
            } catch (PrivilegeNotFoundException unf) {
                String message = messageSource.getMessage("ctrl.message.error.notfound", 
                        new Object[] {businessObject, privilege.getName()}, Locale.US);
                redirectAttrs.addFlashAttribute("error", message);
                return "redirect:/privilege/list";
            }
        }
        return "redirect:/privilege/list";
    }
    @RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
    //@PreAuthorize("hasRole('CTRL_PERM_EDIT_GET')")

    public String editPrivilegePage(@PathVariable("id") Privilege privilege
            , ModelMap model, RedirectAttributes redirect) {
    	
    	Long id = privilege.getId();
    
    	logger.debug("IN: Privilege/edit-GET:  ID to query = " + id);

        try {
            if (!model.containsAttribute("privilege")) {
                logger.debug("Adding privilege object to model");
                privilege = privilegeService.findPrivilegeById(id);
                logger.debug("Privilege/edit-GET:  " + privilege.toString());
                model.addAttribute("privilege", privilege);
            }
            return "tl/privilege-edit";
        } catch (PrivilegeNotFoundException e) {
            String message = messageSource.getMessage("ctrl.message.error.notfound", 
                    new Object[] {"user id", id}, Locale.US);
            model.addAttribute("error", message);
            return "redirect:/privilege/list";
        }
    }
    
    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    //@PreAuthorize("hasRole('CTRL_PERM_DELETE_GET')")
    public String deletePrivilege(
    		@PathVariable("id") final long id,
            Model model, RedirectAttributes redirectAttrs) {
        Privilege privilege;
        try {
            privilege = privilegeService.findPrivilegeById(id);
        } catch (PrivilegeNotFoundException e) {
            String message = messageSource.getMessage("ctrl.message.error.notfound", 
                    new Object[] {"privilege id", id}, Locale.US);
            redirectAttrs.addFlashAttribute("error", message);
            return "redirect:/privilege/list";
        }

        logger.debug("IN: Privilege/delete-GET | id = " + id + privilege.toString());

            try {
                privilegeService.deletePrivilege(id);
                String message = messageSource.getMessage("ctrl.message.success.delete", 
                        new Object[] {businessObject, privilege.getName()}, Locale.US);
                redirectAttrs.addFlashAttribute("message", message);
                return "redirect:/privilege/list";
            } catch (PrivilegeNotFoundException e) {
                String message = messageSource.getMessage("ctrl.message.error.notfound", 
                        new Object[] {"privilege id", id}, Locale.US);
               redirectAttrs.addFlashAttribute("error", message);
                return "redirect:/privilege/list";
           }
    }

//    @PreAuthorize("hasAnyRole('CTRL_PERM_EDIT_GET','CTRL_PERM_DELETE_GET')")
//    public Privilege getPrivilege(Privilege priv) {
//        List<Integer> roleIdList = new ArrayList<Integer>();
//        Privilege priv = new Privilege();
//        priv.setId(priv.getId());
//        priv.setName(priv.getName());
//        for (Role role : priv.getRoles()) {
//            roleIdList.add(role.getId().intValue());
//        }
//        priv.setRoles(roleIdList);
//        return priv;
//    }

////    @PreAuthorize("hasAnyRole('CTRL_PERM_ADD_POST','CTRL_PERM_EDIT_POST')")
//    public Privilege getPrivilege(Privilege privilege) throws RoleNotFoundException {
//        Set<Role> roleList = new HashSet<Role>();
//        Privilege priv = new Privilege();
//        Role role = new Role();
//        priv.setId(privilege.getId());
//        priv.setName(privilege.getName());
//        if (privilege.getRoles() != null) {
//            priv.setRoles(roleList);
//        }
//        logger.debug("  PRIV: " + priv.toString());
//        return priv;
//    }
    

}
    
