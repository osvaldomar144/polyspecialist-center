package com.polyspecialistcenter.aws.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.polyspecialistcenter.aws.controller.validator.ServizioValidator;
import com.polyspecialistcenter.aws.model.Professionista;
import static com.polyspecialistcenter.aws.model.Professionista.DIR_ADMIN_PAGES_PROF;
import com.polyspecialistcenter.aws.model.Servizio;
import static com.polyspecialistcenter.aws.model.Servizio.DIR_ADMIN_PAGES_SERVIZIO;
import static com.polyspecialistcenter.aws.model.Servizio.DIR_PAGES_SERVIZIO;
import com.polyspecialistcenter.aws.service.ProfessionistaService;
import com.polyspecialistcenter.aws.service.ServizioService;

@Controller
public class ServizioController {
	
	@Autowired
	private ServizioService servizioService;
	
	@Autowired
	private ServizioValidator servizioValidator;
	
	@Autowired
	private ProfessionistaService professionistaService;
	
	/* METHODS GENERIC_USER */
	
	@GetMapping("/servizio/{id}")
	public String getServizio(@PathVariable("id") Long id, Model model) {
		Servizio servizio = this.servizioService.findById(id);
		model.addAttribute("servizio", servizio);
		
		return DIR_PAGES_SERVIZIO + "/infoServizio";
	}
	
	@GetMapping("/servizi")
	public String getServizi(Model model) {
		model.addAttribute("servizi", this.servizioService.findAll());
		
		return DIR_PAGES_SERVIZIO + "elencoServizi";
	}
	
	/* METHODS ADMIN */
	
	@GetMapping("/admin/servizi/{id}")
	public String getServiziOfProfessionista(@PathVariable("id") Long id, Model model) {
		model.addAttribute("servizi", this.professionistaService.findById(id).getServizi());
		
		return DIR_PAGES_SERVIZIO + "adminElencoServizi";
	}
	
	// --- INSERIMENTO
	
	@GetMapping("/admin/servizio/add/{id}")
	public String selezionaServizio(@PathVariable("id") Long id, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("servizio", new Servizio());
		
		return DIR_ADMIN_PAGES_SERVIZIO + "servizioForm";
	}
	
	@PostMapping("/admin/servizio/add/{id}")
	public String addServizio(@Valid @ModelAttribute("servizio") Servizio servizio, BindingResult bindingResult, @PathVariable("id") Long id, Model model) {
		
		Professionista professionista = professionistaService.findById(id);
		servizio.setProfessionista(professionista);
		this.servizioValidator.validate(servizio, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.professionistaService.addServizio(professionista, servizio);
			
			return "redirect:/" + DIR_ADMIN_PAGES_PROF + id;
		}
		
		model.addAttribute("id", id);
		return DIR_ADMIN_PAGES_SERVIZIO + "servizioForm";
	}
	
	// --- ELIMINAZIONE
	
	@GetMapping("/admin/servizio/delete/{id}")
	public String deleteServizio(@PathVariable("id") Long id, Model model) {
		Servizio servizio = this.servizioService.findById(id);
		Professionista professionista = this.professionistaService.findById(servizio.getProfessionista().getId());
		professionista.getServizi().remove(servizio);
		this.servizioService.delete(servizio);
		this.professionistaService.save(professionista);
		
		return "redirect:/admin/servizi/" + professionista.getId();
	}
	
	// --- MODIFICA
	
	@GetMapping("/admin/servizio/edit/{id}")
	public String getEditServizio(@PathVariable("id") Long id, Model model) {
		Servizio servizio = this.servizioService.findById(id);
		model.addAttribute("servizio", servizio);
		
		return DIR_ADMIN_PAGES_SERVIZIO + "editServizio";
	}
	
	@PostMapping("/admin/servizio/edit/{id}")
	public String editServizio(@Valid @ModelAttribute("servizio") Servizio newServizio, BindingResult bindingResult, @PathVariable("id") Long id, Model model) {
		
		this.servizioValidator.validate(newServizio, bindingResult);
		if(!bindingResult.hasErrors()) {
			Servizio servizio = this.servizioService.findById(id);
			this.servizioService.update(servizio, newServizio);
			
			return "redirect:/" + DIR_ADMIN_PAGES_PROF + servizio.getProfessionista().getId();
		}
		
		newServizio.setId(id);
		model.addAttribute("servizio", newServizio);
		
		return DIR_ADMIN_PAGES_SERVIZIO + "editServizioForm";
	}
	
}
