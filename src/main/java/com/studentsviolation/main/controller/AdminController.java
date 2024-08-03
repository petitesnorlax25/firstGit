package com.studentsviolation.main.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.studentsviolation.main.entity.Admin;
import com.studentsviolation.main.entity.Students;
import com.studentsviolation.main.entity.StudentsData;
import com.studentsviolation.main.service.AdminService;
import com.studentsviolation.main.service.ApiAuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/studentsViolation")
public class AdminController {
	@Autowired
	private AdminService adminService;
	@Value("${api.login.url}")
    private String apiLoginUrl;

    private final ApiAuthenticationService apiAuthenticationService;

    public AdminController(ApiAuthenticationService apiAuthenticationService) {
        this.apiAuthenticationService = apiAuthenticationService;
    }
	
	@GetMapping("/register")
	public String showRegistration(Model model, HttpSession session) {	
		return "users/registration";
		
	}
	@GetMapping
	public String showLogin(Model model, HttpSession session) {
		return "users/login";
		
	}
	@GetMapping("/dashboard")
	public String showDashboard(Model model, HttpSession session) {
		String isLogged = (String) session.getAttribute("isLogged");
		Admin currentUser = (Admin) session.getAttribute("currentUser");
		if (isLogged == null) {
			return "redirect:/studentsViolation?unauthorizedAccess=You are not signed in, Please Log-in!";
		}
		model.addAttribute("isLogged", isLogged);
		model.addAttribute("currentUser", currentUser);
		return "users/dashboard";
		
	}
	@PostMapping("/registration")
	public String registration(@ModelAttribute Admin registerAdmin, Model model) {
		Admin usernameFromDb = adminService.getAdminByUsername(registerAdmin.getUsername());
		if (usernameFromDb!=null) {
			return "redirect:/studentsViolation/register?errorMessage=Username is already taken!";
		}
		registerAdmin.setUserType("admin");
		registerAdmin.setStatus(1);
		adminService.createAdmin(registerAdmin);
		return "redirect:/studentsViolation";
		
	}
	@PostMapping("/login")
	public String loginMethod(@ModelAttribute Admin admin, HttpSession session, RedirectAttributes redirectAttributes, Model model) {
	    int status = 1;
	    Admin validateAdmin = adminService.getAdminByUsernameAndPassword(admin.getUsername(), admin.getPassword(), status);
	    String url = apiLoginUrl + "?txtUserName=" + admin.getUsername() + "&txtPassword=" + admin.getPassword();
	    Students students = new Students();
	    StudentsData studentData = apiAuthenticationService.authenticate(url);
	    if (validateAdmin==null && (studentData != null && studentData.isLogin())) {
	    	System.out.println("students welcome!!");
	    	students.setFirstname(studentData.getFirst_name());
	    	students.setLastname(studentData.getLast_name());
	    	students.setCourseYear(studentData.getYear_level()+studentData.getSection());
	    	session.setAttribute("isLogged", "true");
	    	System.out.println("afgassasdhgfdtg: "+studentData.getFirst_name());
	    	model.addAttribute("students", students);// Store student data in session if needed
	    	session.setAttribute("name", studentData.getFirst_name()+"\n"+studentData.getLast_name());
	    	session.setAttribute("courseYear", studentData.getYear_level()+"\n"+studentData.getSection());
	    	session.setAttribute("program", studentData.getProgram_description());
	    	return "redirect:/studentsViolation/getHomepage";
	    }
	    else if (validateAdmin != null) {
	        session.setAttribute("currentUser", validateAdmin);

	        session.setAttribute("isLogged", "true");
	        System.out.println("welcome user!");
	        return "redirect:/studentsViolation/dashboard";
	    } else {
	        redirectAttributes.addFlashAttribute("errorMessage", "Invalid username or password");
	        return "redirect:/studentsViolation";
	    }
	}



	@PostMapping("/logout")
	public String logoutMethod(HttpSession session) {
		session.invalidate();
		return "redirect:/studentsViolation";
	}
	@RequestMapping(value = {"/registration", "/login", "/updateUser", "/softDeleteUser", "/hardDeleteUser", "/restoreDeletedUser"}, method = RequestMethod.GET)
    public String handlePostDirectAccess() {
        // Redirect to the registration form if accessed directly
		return "redirect:/studentsViolation";
    }
	@GetMapping("/users") 
	public String getUsers (HttpSession session, Model Model, HttpServletRequest request) {
		String isLogged = (String) session.getAttribute("isLogged");
		Admin currentUser = (Admin) session.getAttribute("currentUser");
		if (isLogged == null ||currentUser==null) {
			return "redirect:/studentsViolation?unauthorizedAccess=You are not signed in, Please Log-in!";
		}
		
		String status = request.getParameter("status");
		System.out.println("Status: "+status);
		if (status.equals("enabled")) {
			List<Admin> getAllUsers = adminService.getAdminsByStatus(1);
			Model.addAttribute("users", getAllUsers);
			Model.addAttribute("status", status);
			return "users/users";
			
		}else {
			List<Admin> getAllUsers = adminService.getAdminsByStatus(0);
			Model.addAttribute("users", getAllUsers);
			Model.addAttribute("status", status);
			return "users/users";
		}

		
	}
	@PostMapping("/updateUser")
	@ResponseBody
	public String updateUser(@RequestParam("id") Long id,
	                         @RequestParam("name") String name,
	                         @RequestParam("username") String username,
	                         @RequestParam("password") String password,
	                         @RequestParam("userType") String userType)  throws IOException, SQLException {
	    Admin existingAdmin = adminService.getAdminById(id);
	    if (existingAdmin != null) {
	        existingAdmin.setName(name);
	        existingAdmin.setUsername(username);
	        existingAdmin.setPassword(password);
	        existingAdmin.setUserType(userType);
	        adminService.updateAdmin(id, existingAdmin);
	        return "success";
	    }
	    return "error";
	}
	@PostMapping("/softDeleteUser")
	public String softDeleteUsersMethod(@ModelAttribute Admin user, HttpSession session, Model model) {
		Long id = user.getId();
		Admin getUser = adminService.getAdminById(id);
		if (getUser!=null) {
			getUser.setStatus(0);
			adminService.updateAdmin(id, getUser);
			return "redirect:/studentsViolation/users?status=enabled";
		}
		return "redirect:/studentsViolation/users?status=enabled";
	}
	@PostMapping("/hardDeleteUser")
	public String hardDeleteUsersMethod(@ModelAttribute Admin user, HttpSession session, Model model) {
		Long id = user.getId();
		adminService.deleteAdmin(id);
		
		return "redirect:/studentsViolation/users?status=disabled";

	}
	@PostMapping("/restoreDeletedUser")
	public String restoreDeletedUsersMethod(@ModelAttribute Admin user, HttpSession session, Model model) {
		Long id = user.getId();
		Admin getUser = adminService.getAdminById(id);
		if (getUser!=null) {
			getUser.setStatus(1);
			adminService.updateAdmin(id, getUser);
			return "redirect:/studentsViolation/users?status=disabled";
		}
		return "redirect:/studentsViolation/users?status=disabled";

	}
}
