package com.polyspecialistcenter.aws.controller;

import static com.polyspecialistcenter.aws.model.Prenotazione.DIR_PAGES_PREN;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.polyspecialistcenter.aws.controller.validator.PrenotazioneValidator;
import com.polyspecialistcenter.aws.model.Prenotazione;
import com.polyspecialistcenter.aws.model.Utente;
import com.polyspecialistcenter.aws.service.PrenotazioneService;
import com.polyspecialistcenter.aws.service.UtenteService;

@Controller
public class PrenotazioneController {
	
	@Autowired
	private PrenotazioneService prenotazioneService;
	
	@Autowired
	private PrenotazioneValidator prenotazioneValidator;
	
	@Autowired
	private UtenteService utenteService;
	
	@GetMapping("/profile/prenotazioni/{id}")
	public String getPrenotazioni(@PathVariable("id") Long id, Model model) {
		Utente utente = this.utenteService.getUser(id);
		model.addAttribute("prenotazioni", utente.getPrenotazioni());
		
		return DIR_PAGES_PREN + "elencoPrenotazioni";
	}
	
	@GetMapping("/profile/prenotazione/add/{id}")
	public String addPrenotazione(@PathVariable("id") Long id, RedirectAttributes redirect) {
		Prenotazione prenotazione = new Prenotazione();
		prenotazione.setCliente(this.utenteService.getUser(id));
		redirect.addFlashAttribute("prenotazione", prenotazione);
		
		return "redirect:/profile/prenotazione/servizio";
	}
	
	@PostMapping("/profile/prenotazione/add")
	public String addPrenotazione(BindingResult bindingResult, Model model) {
		
		Prenotazione prenotazione = (Prenotazione) model.getAttribute("prenotazione");
		this.prenotazioneValidator.validate(prenotazione, bindingResult);
		if(!bindingResult.hasErrors()) {
			Utente cliente = prenotazione.getCliente();
			this.utenteService.addPrenotazione(cliente, prenotazione);
			
			return this.getPrenotazioni(cliente.getId(), model);
		}
		
		//da modellare in caso di errori
		return DIR_PAGES_PREN + "riepilogoPrenotazione";
	}
	
}