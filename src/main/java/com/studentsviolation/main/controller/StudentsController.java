package com.studentsviolation.main.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;


import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/studentsViolation")
public class StudentsController {


	@GetMapping("/getHomepage")
	public String getLogin(HttpSession session) {
		return "students/homepage";
	}
//	@PostMapping("/studentsLogin")
//	public String loginMethod(@ModelAttribute Students student, HttpSession session, RedirectAttributes redirectAttributes) {
//	    String url = apiLoginUrl + "?txtUserName=" + student.getUsername() + "&txtPassword=" + student.getPassword();
//	    boolean isAuthenticated = apiAuthenticationService.authenticate(url);
//	    String isLogged = "true";
//	    if (isAuthenticated) {
//	        session.setAttribute("isLogged", isLogged);
//	        System.out.println("welcome!!");
//	        return "redirect:/studentsViolation/dashboard";
//	    } else {
//	        redirectAttributes.addFlashAttribute("errorMessage", "Invalid username or password");
//	        return "redirect:/studentsViolation";
//	    }
//	}
}
