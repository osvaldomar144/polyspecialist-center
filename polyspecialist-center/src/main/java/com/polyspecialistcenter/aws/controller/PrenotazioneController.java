package com.polyspecialistcenter.aws.controller;

import static com.polyspecialistcenter.aws.model.Prenotazione.DIR_PAGES_PREN;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.polyspecialistcenter.aws.controller.validator.PrenotazioneValidator;
import com.polyspecialistcenter.aws.model.Credentials;
import com.polyspecialistcenter.aws.model.Disponibilita;
import com.polyspecialistcenter.aws.model.Prenotazione;
import com.polyspecialistcenter.aws.model.Professionista;
import com.polyspecialistcenter.aws.model.Servizio;
import com.polyspecialistcenter.aws.model.Utente;
import com.polyspecialistcenter.aws.service.CredentialsService;
import com.polyspecialistcenter.aws.service.DisponibilitaService;
import com.polyspecialistcenter.aws.service.PrenotazioneService;
import com.polyspecialistcenter.aws.service.ServizioService;
import com.polyspecialistcenter.aws.service.UtenteService;

@Controller
public class PrenotazioneController {
	
	@Autowired
	private PrenotazioneService prenotazioneService;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private ServizioService servizioService;
	
	@Autowired
	private DisponibilitaService disponibilitaService;
	
	@Autowired
	private PrenotazioneValidator prenotazioneValidator;
	
	@Autowired
	private UtenteService utenteService;
	
	@GetMapping("/profile/prenotazioni")
	public String getPrenotazioni(Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		model.addAttribute("prenotazioni", credentials.getUtente().getPrenotazioni());
		
		return DIR_PAGES_PREN + "elencoPrenotazioni";
	}
	
	@GetMapping("/profile/prenotazione/add")
    public String addPrenotazione(Model model) {
        model.addAttribute("servizi", this.servizioService.findAll());
        return DIR_PAGES_PREN + "elencoServiziPrenotazione";
    }
	
	@GetMapping("/profile/prenotazione/disponibilita/{idS}")
	public String selectDisponibilita(@PathVariable("idS") Long idServizio, Model model) {
		model.addAttribute("idServizio", idServizio);
		model.addAttribute("prenotazione", new Prenotazione());
		
		Professionista p = this.servizioService.findById(idServizio).getProfessionista();
		
		model.addAttribute("disponibilitaList", this.disponibilitaService.findByProfAndActive(p));
		
		
		return DIR_PAGES_PREN + "elencoDisponibilitaPrenotazione";
	}
	
	@GetMapping("/profile/prenotazione/add/{idS}/{idD}")
	public String addPrenotazione(@Valid @ModelAttribute("prenotazione") Prenotazione p,
								  BindingResult bindingResult, 
								  @PathVariable("idS") Long idServizio,
								  @PathVariable("idD") Long idDisponibilita,
								  Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		
		Utente u = credentials.getUtente();
		Servizio s = this.servizioService.findById(idServizio);
		Disponibilita d = this.disponibilitaService.findById(idDisponibilita);
		Professionista prof = s.getProfessionista();
		p.setProfessionista(prof);
		p.setCliente(u);
		p.setDisponibilita(d);
		p.setServizio(s); 
		d.setActive(false);
		
		this.prenotazioneValidator.validate(p, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.utenteService.addPrenotazione(u, p);			
			return "redirect:/profile/prenotazioni";
		}
		
		//da modellare in caso di errori
		return DIR_PAGES_PREN + "riepilogoPrenotazione";
	}
	
	@GetMapping("/profile/delete/{id}")
	public String deletePrenotazione(@PathVariable("id") Long id, Model model) {
		Prenotazione p = this.prenotazioneService.findById(id);
		Utente u = p.getCliente();
		Disponibilita d = p.getDisponibilita();
		//Professionista prof = p.getProfessionista();
		d.setActive(true);
		
		this.utenteService.deletePrenotazione(u, p);
		this.prenotazioneService.delete(p);
		
		return this.getPrenotazioni(model);
	}
	
}