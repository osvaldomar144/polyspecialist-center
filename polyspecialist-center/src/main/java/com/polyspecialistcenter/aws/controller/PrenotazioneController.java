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
import org.springframework.web.bind.annotation.PostMapping;

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
	
	@GetMapping("/profile/prenotazione/{idS}")
	public String selectDisponibilita(@PathVariable("idS") Long idServizio, Model model) {
		model.addAttribute("idServizio", idServizio);
		Long idP = (Long) model.getAttribute("idPrenotazione");
		Professionista p = this.servizioService.findById(idServizio).getProfessionista();
		
		if(idP==null) {
			model.addAttribute("prenotazione", new Prenotazione());
			model.addAttribute("disponibilitaList", this.disponibilitaService.findByProfAndActive(p));
			
			return DIR_PAGES_PREN + "elencoDisponibilitaPrenotazione";
		}
		
		model.addAttribute("idPrenotazione", idP);
		model.addAttribute("disponibilitaList", this.disponibilitaService.findByProfAndActive(p));
		
		return DIR_PAGES_PREN + "elencoDisponibilitaPrenotazione";
	}
	
	@PostMapping("/profile/prenotazione/add/{idS}/{idD}")
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
		
		return "";
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
	
	@GetMapping("/profile/prenotazione/edit/{id}")
	public String editPrenotazione(@PathVariable("id") Long id, Model model) {
		model.addAttribute("id", id);
		return this.addPrenotazione(model);
	}
	
	@GetMapping("/profile/prenotazione/edit/{idPrenotazione}/{idServizio}")
	public String editPrenotazioneDisponibilita(@PathVariable("idPrenotazione") Long idPrenotazione, @PathVariable("idServizio") Long idServizio, Model model) {
		model.addAttribute("idPrenotazione", idPrenotazione);
		model.addAttribute("idServizio", idServizio);
		
		return this.selectDisponibilita(idServizio, model);
	}
	
	@PostMapping("/profile/prenotazione/edit/{idPrenotazione}/{idServizio}/{idDisponibilita}")
	public String editPrenotazione(@PathVariable("idPrenotazione") Long idPrenotazione, @PathVariable("idServizio") Long idServizio, @PathVariable("idDisponibilita") Long idDisponibilita, Model model) {
		Servizio s = this.servizioService.findById(idServizio);
		Disponibilita d = this.disponibilitaService.findById(idDisponibilita);
		Professionista prof = s.getProfessionista();
		
		Prenotazione prenotazione = this.prenotazioneService.findById(idPrenotazione);
		prenotazione.setServizio(s);
		prenotazione.setProfessionista(prof);
		
		Disponibilita dispOld = prenotazione.getDisponibilita();
		dispOld.setActive(true);
		prenotazione.setDisponibilita(d);
		d.setActive(false);
		
		this.prenotazioneService.save(prenotazione);			
			
		return "redirect:/profile/prenotazioni";
	}
	
}