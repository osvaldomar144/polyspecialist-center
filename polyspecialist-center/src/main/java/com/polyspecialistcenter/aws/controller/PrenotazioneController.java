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

import com.polyspecialistcenter.aws.controller.validator.PrenotazioneValidator;
import com.polyspecialistcenter.aws.model.Disponibilita;
import com.polyspecialistcenter.aws.model.Prenotazione;
import com.polyspecialistcenter.aws.model.Servizio;
import com.polyspecialistcenter.aws.model.Utente;
import com.polyspecialistcenter.aws.service.DisponibilitaService;
import com.polyspecialistcenter.aws.service.PrenotazioneService;
import com.polyspecialistcenter.aws.service.ServizioService;
import com.polyspecialistcenter.aws.service.UtenteService;

@Controller
public class PrenotazioneController {
	
	@Autowired
	private PrenotazioneService prenotazioneService;
	
	@Autowired
	private ServizioService servizioService;
	
	@Autowired
	private DisponibilitaService disponibilitaService;
	
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
    public String addPrenotazione(@PathVariable("id") Long id, Model model) {
        model.addAttribute("servizi", this.servizioService.findAll());
        model.addAttribute("idUtente", id);
        return DIR_PAGES_PREN + "elencoServiziPrenotazione";
    }
	
	@GetMapping("/profile/prenotazione/disponibilita/{idU}/{idS}")
	public String selectDisponibilita(@PathVariable("idU") Long idUtente, 
									  @PathVariable("idS") Long idServizio, 
									  Model model) {
		model.addAttribute("idUtente", idUtente);
		model.addAttribute("idServizio", idServizio);
		model.addAttribute("prenotazione", new Prenotazione());
		model.addAttribute("disponibilitaList", this.servizioService.findById(idServizio)
																	.getProfessionista()
																	.getDisponibilita());
		
		
		return DIR_PAGES_PREN + "elencoDisponibilitaPrenotazione";
	}
	
	@GetMapping("/profile/prenotazione/add/{idU}/{idS}/{idD}")
	public String addPrenotazione(@Valid @ModelAttribute("prenotazione") Prenotazione p,
								  BindingResult bindingResult,
								  @PathVariable("idU") Long idUtente, 
								  @PathVariable("idS") Long idServizio,
								  @PathVariable("idD") Long idDisponibilita,
								  Model model) {
		
		Utente u = this.utenteService.getUser(idUtente);
		Servizio s = this.servizioService.findById(idServizio);
		Disponibilita d = this.disponibilitaService.findById(idDisponibilita);
		p.setProfessionista(s.getProfessionista());
		p.setCliente(u);
		p.setDisponibilita(d);
		p.setServizio(s);
		
		this.prenotazioneValidator.validate(p, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.utenteService.addPrenotazione(u, p);			
			return this.getPrenotazioni(u.getId(), model);
		}
		
		//da modellare in caso di errori
		return DIR_PAGES_PREN + "riepilogoPrenotazione";
	}
	
}