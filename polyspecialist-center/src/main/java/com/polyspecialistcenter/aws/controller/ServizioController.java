package com.polyspecialistcenter.aws.controller;

import static com.polyspecialistcenter.aws.model.Servizio.DIR_ADMIN_PAGES_SERVIZIO;
import static com.polyspecialistcenter.aws.model.Servizio.DIR_FOLDER_IMG;
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
import org.springframework.web.multipart.MultipartFile;

import com.polyspecialistcenter.aws.controller.validator.ServizioValidator;
import com.polyspecialistcenter.aws.model.Professionista;
import com.polyspecialistcenter.aws.model.Servizio;
import com.polyspecialistcenter.aws.service.ProfessionistaService;
import com.polyspecialistcenter.aws.service.ServizioService;
import com.polyspecialistcenter.aws.utility.FileStore;

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
		
		return DIR_PAGES_SERVIZIO + "servizio";
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
	public String addServizio(@Valid @ModelAttribute("servizio") Servizio servizio, 
							  BindingResult bindingResult, 
							  @PathVariable("id") Long id,
							  @RequestParam("file") MultipartFile file,
							  Model model) {
		
		Professionista professionista = professionistaService.findById(id);
		servizio.setProfessionista(professionista);
		this.servizioValidator.validate(servizio, bindingResult);
		if(!bindingResult.hasErrors()) {
			servizio.setImg(FileStore.store(file,DIR_FOLDER_IMG));
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
		
		servizio.setId(id);
		if(!bindingResult.hasErrors()) {
			this.servizioService.update(s, servizio);
			
			return "redirect:/admin/servizi/" + servizio.getProfessionista().getId();
		}
		servizio.setImg(s.getImg());
		return DIR_ADMIN_PAGES_SERVIZIO + "editServizio";
	}
	
	@PostMapping("/admin/servizio/changeImg/{idS}")
	public String changeImgChef(@PathVariable("idS") Long idS,
			   					@RequestParam("file") MultipartFile file, 
			   					Model model) {
		
		Servizio s = this.servizioService.findById(idS);
		if(!s.getImg().equals("profili")) {
			FileStore.removeImg(DIR_FOLDER_IMG, s.getImg());
		}

		s.setImg(FileStore.store(file, DIR_FOLDER_IMG));
		this.servizioService.save(s);
		return this.getEditServizio(idS, model);
	}
	
	/*----*/
	
	
/*	@GetMapping("/profile/prenotazione/servizio")
	public String selectServizio(RedirectAttributes redirect, Model model) {
		model.addAttribute("servizi", this.servizioService.findAll());
		
		Prenotazione prenotazione = (Prenotazione) redirect.getFlashAttributes().get("prenotazione");
		model.addAttribute("prenotazione", prenotazione);
		return DIR_PAGES_SERVIZIO + "elencoServiziPrenotazione";
	}
	
	@PostMapping("/profile/servizio")
	public String selectServizio(@Valid @ModelAttribute("prenotazione") Prenotazione prenotazione, @RequestParam("idChecked") Servizio servizio, RedirectAttributes redirect, Model model) {
		prenotazione.setServizio(servizio);
		prenotazione.setProfessionista(servizio.getProfessionista());
		redirect.addFlashAttribute("prenotazione", prenotazione);
		
		return "redirect:/profile/prenotazione/disponibilita";
	}*/
	
}
