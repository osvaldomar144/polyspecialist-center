package com.polyspecialistcenter.aws.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
	
	
	//vai alla pagina di login
	@GetMapping("/login")
    public String showLoginForm(Model model) {
        return "";
    }
	
	//vai alla pagina di logout
	@GetMapping("/logout")
	public String logout(Model model) {
		return "index.html";
	}
	
	
	//vai alla pagin index (o admin dashboard) dopo il login
	@GetMapping("/default")
	public String defaultAfterLogin(Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		
		if(credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			return "";
		}

		return "index.html";
	}
	
	//vai alla pagin index (o admin dashboard) dopo il login con OAuth ***DA MODELLARE BENE SE SI RITIENE UTILE***
	@GetMapping("/defaultOauth")
	public String oauthLogin(Model model) {
		OAuth2User userDetails = (OAuth2User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return "index.html";
	}
	
	//vai alla pgina di registrazione di un utente
	@GetMapping("/register")
	public String getCredentials(Model model) {
		model.addAttribute("utente", new Utente());
		model.addAttribute("credenziali", new Credentials());
		return "";
	}
	
	//configurazione form della pagina di registrazione di un utente
	@PostMapping("/register")
	public String addCredentials(@Valid @ModelAttribute ("utente") Utente utente, BindingResult utenteBindingResult, @Valid @ModelAttribute("credenziali") Credentials credenziali, BindingResult credenzialiBindingResult, Model model) {
		
		utente.setCognome(utente.getCognome().toLowerCase());
		utente.setNome(utente.getNome().toLowerCase());
		
		this.utenteValidator.validate(utente, utenteBindingResult);
		this.credentialsValidator.validate(credenziali, credenzialiBindingResult);
		
		if(!credenzialiBindingResult.hasErrors() && !utenteBindingResult.hasErrors()) {
			credenziali.setUtente(utente);  
			credentialsService.save(credenziali);  // this also stores the User, thanks to Cascade.ALL policy
			return "index.html";
		}
		
		return "";
	}
	
}
