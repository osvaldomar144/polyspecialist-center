package com.polyspecialistcenter.aws.controller;

import static com.polyspecialistcenter.aws.model.Servizio.DIR_ADMIN_PAGES_SERVIZIO;
import static com.polyspecialistcenter.aws.model.Servizio.DIR_PAGES_SERVIZIO;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.polyspecialistcenter.aws.controller.validator.ServizioValidator;
import com.polyspecialistcenter.aws.model.Prenotazione;
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
		model.addAttribute("idProfessionista", id);
		return DIR_ADMIN_PAGES_SERVIZIO + "adminElencoServizi";
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
			
			return "redirect:/admin/servizi/" + id;
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
	public String editServizio(@Valid @ModelAttribute("servizio") Servizio servizio, 
							   BindingResult bindingResult, @PathVariable("id") Long id, 
							   Model model) {
		
		Servizio s = this.servizioService.findById(id);
		servizio.setProfessionista(s.getProfessionista());
		
		if(servizio.getNome().equals(s.getNome())) {
			servizio.setNome("nomeSerDef");
			this.servizioValidator.validate(servizio, bindingResult);
			servizio.setNome(s.getNome());
		}else {
			this.servizioValidator.validate(servizio, bindingResult);
		}
		
		
		if(!bindingResult.hasErrors()) {
			this.servizioService.update(servizio, servizio);
			
			return "redirect:/admin/servizi/" + servizio.getProfessionista().getId();
		}
		
		servizio.setId(id);
		model.addAttribute("servizio", servizio);
		
		return DIR_ADMIN_PAGES_SERVIZIO + "editServizio";
	}
	
	@GetMapping("/profile/prenotazione/servizio/{id}")
	public String selectProfessionista(@PathVariable("id") Long id, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("servizi", this.servizioService.findAll());
		
		Prenotazione prenotazione = (Prenotazione) model.getAttribute("prenotazione");
		model.addAttribute("prenotazione", prenotazione);
		return DIR_PAGES_SERVIZIO + "elencoServiziPrenotazione";
	}
	
	@PostMapping("/profile/prenotazione/professionista/{id}")
	public String selectProfessionista(@Valid @ModelAttribute("prenotazione") Prenotazione prenotazione, @RequestParam("idChecked") Servizio servizio, @PathVariable("id") Long id, RedirectAttributes redirect) {
		prenotazione.setServizio(servizio);
		prenotazione.setProfessionista(servizio.getProfessionista());
		redirect.addFlashAttribute("prenotazione", prenotazione);
		
		return "redirect:/profile/prenotazione/disponibilita" + id;
	}
	
}
