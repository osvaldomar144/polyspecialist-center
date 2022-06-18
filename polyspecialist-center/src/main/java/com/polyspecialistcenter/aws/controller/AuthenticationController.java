package com.polyspecialistcenter.aws.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.polyspecialistcenter.aws.controller.validator.CredentialsValidator;
import com.polyspecialistcenter.aws.controller.validator.UtenteValidator;
import com.polyspecialistcenter.aws.model.Credentials;
import com.polyspecialistcenter.aws.model.Utente;
import com.polyspecialistcenter.aws.service.CredentialsService;

@Controller
public class AuthenticationController {
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private CredentialsValidator credentialsValidator;
	
	@Autowired
	private UtenteValidator utenteValidator;
	
	
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String showRegisterForm(Model model) {
		model.addAttribute("utente", new Utente());
		model.addAttribute("credentials", new Credentials());
		return "authentication/registerForm";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String showLoginForm(Model model) {
		return "authentication/loginForm";
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public String logout(Model model) {
		return "index";
	}
	
	
	@RequestMapping(value="/default", method=RequestMethod.GET)
	public String defaultAfterLogin(Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		if(credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			return "redirect:/" + "admin/professionisti";
		}
		
		return "authentication/profile";
	}
	
	@PostMapping(value= {"/register"})
	public String registerUser(@Valid @ModelAttribute("utente") Utente user,
								BindingResult utenteBindingResult, 
							   @Valid @ModelAttribute("credentials") Credentials credentials,
							    BindingResult credentialsBindingResult,
							    Model model) {
		
        // validazione user e credenziali
        this.utenteValidator.validate(user, utenteBindingResult);
        this.credentialsValidator.validate(credentials, credentialsBindingResult);

		if(!utenteBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
			user.setImg("icon-default-user.png");
			credentials.setUtente(user);
			credentialsService.saveCredentials(credentials);
			return "authentication/registrationSuccessful";
		}
		
		return "authentication/registerForm";
	}
	
	/*//vai alla pagin index (o admin dashboard) dopo il login con OAuth ***DA MODELLARE BENE SE SI RITIENE UTILE***
	@GetMapping("/defaultOauth")
	public String oauthLogin(Model model) {
		OAuth2User userDetails = (OAuth2User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return "index.html";
	}*/
	
}
