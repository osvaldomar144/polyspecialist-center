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
import com.polyspecialistcenter.aws.model.Servizio;
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
	
	@GetMapping("/servizio/{id}")
	public String getServizio(@PathVariable("id") Long id, Model model) {
		Servizio servizio = this.servizioService.findById(id);
		model.addAttribute("servizio", servizio);
		
		return "paginaServizio";
	}
	
	@GetMapping("/servizi")
	public String getServizi(Model model) {
		model.addAttribute("servizi", this.servizioService.findAll());
		
		return "elencoServizi";
	}
	
	@GetMapping("/admin/servizio/add/{id}")
	public String selezionaServizio(@PathVariable("id") Long id, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("servizio", new Servizio());
		
		return "formServizioNuovo";
	}
	
	@PostMapping("/admin/servizio/add/{id}")
	public String addServizio(@Valid @ModelAttribute("servizio") Servizio servizio, BindingResult bindingResult, @PathVariable("id") Long id, Model model) {
		
		this.servizioValidator.validate(servizio, bindingResult);
		if(!bindingResult.hasErrors()) {
			Professionista professionista = professionistaService.findById(id);
			this.professionistaService.addServizio(professionista, servizio);
			
			return "redirect:/" + DIR_PAGES_PROF;
		}
		
		model.addAttribute("id", id);
		return "formServizioNuovo";
	}
	
	@GetMapping("/admin/servizio/delete/{id}")
	public String deleteServizio(@PathVariable("id") Long id, Model model) {
		Servizio servizio = this.servizioService.findById(id);
		Professionista professionista = servizio.getProfessionista();
		this.professionistaService.deleteServizio(professionista, servizio);
		model.addAttribute("professionista", professionista);
		
		return "professionista";
	}
	
	@GetMapping("/admin/servizio/edit/{id}")
	public String getEditServizio(@PathVariable("id") Long id, Model model) {
		Servizio servizio = this.servizioService.findById(id);
		model.addAttribute("servizio", servizio);
		
		return "formModificaServizio";
	}
	
	@PostMapping("/admin/servizio/edit/{id}")
	public String editServizio(@Valid @ModelAttribute("servizio") Servizio newServizio, BindingResult bindingResult, @PathVariable("id") Long id, Model model) {
		
		this.servizioValidator.validate(newServizio, bindingResult);
		if(!bindingResult.hasErrors()) {
			Servizio servizio = this.servizioService.findById(id);
			this.servizioService.update(servizio, newServizio);
			model.addAttribute("professionista", servizio.getProfessionista());
			
			return "professionista";
		}
		
		newServizio.setId(id);
		model.addAttribute("servizio", newServizio);
		
		return "formModificaServizio";
	}
	
}
