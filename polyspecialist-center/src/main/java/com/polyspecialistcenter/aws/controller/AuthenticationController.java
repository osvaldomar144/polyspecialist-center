package com.polyspecialistcenter.aws.controller;

import static com.polyspecialistcenter.aws.model.Utente.DIR_FOLDER_IMG;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.polyspecialistcenter.aws.controller.validator.CredentialsValidator;
import com.polyspecialistcenter.aws.controller.validator.UtenteValidator;
import com.polyspecialistcenter.aws.model.Credentials;
import com.polyspecialistcenter.aws.model.Utente;
import com.polyspecialistcenter.aws.service.CredentialsService;
import com.polyspecialistcenter.aws.service.UtenteService;
import com.polyspecialistcenter.aws.utility.FileStore;

@Controller
public class AuthenticationController {
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private UtenteService utenteService;
	
	@Autowired
	private CredentialsValidator credentialsValidator;
	
	@Autowired
	private UtenteValidator utenteValidator;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
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
			return "redirect:/admin/professionisti";
		}
		
		return this.profileUser(model);
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
	
	/* PROFILE */
	@GetMapping("/profile")
	public String profileUser(Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		Utente user = utenteService.getUser(credentials.getUtente().getId());
		model.addAttribute("user", user);
		model.addAttribute("credentials", credentials);
		return "authentication/profile";
	}
	
	@PostMapping("/changeUserAndPass/{idCred}")
	public String changeUserAndPass(@Valid @ModelAttribute("credentials") Credentials credentials,
									BindingResult credentialsBindingResult,
									@PathVariable("idCred") Long id,
									@RequestParam(name = "confirmPass") String pass,								
									Model model) {
		
		credentials.setUsername("defaultUsernameForVa");
		credentialsValidator.validate(credentials, credentialsBindingResult);
		
		if(!credentials.getPassword().equals(pass)) {
			credentialsBindingResult.addError(new ObjectError("notMatchConfirmPassword", "Le password non coincidono"));
		}
		
		Credentials c = credentialsService.getCredentials(id);
		Utente user = utenteService.getUser(c.getUtente().getId());
		credentials.setUsername(c.getUsername());
		credentials.setId(id);
		
		if(!credentialsBindingResult.hasErrors()) {
			c.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
			credentialsService.save(c);
			model.addAttribute("user", user);
			model.addAttribute("credentials", c);
			model.addAttribute("okChange", true);
			return "authentication/profile";
		}	
		model.addAttribute("user", user);
		model.addAttribute("credentials", credentials);
		model.addAttribute("okChange", false);
		return "authentication/profile";
	}
	
	@PostMapping("/changeImgProfile/{idUser}")
	public String changeImgProfile(@PathVariable("idUser") Long id,
								   @RequestParam("file") MultipartFile file, Model model) {
		Utente user = utenteService.getUser(id);
		if(!user.getImg().equals("icon-user-default.png")) {
			FileStore.removeImg(DIR_FOLDER_IMG, user.getImg());
		}
		user.setImg(FileStore.store(file, DIR_FOLDER_IMG));
		utenteService.save(user);
		return this.profileUser(model);
	}
	
}
