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

import com.polyspecialistcenter.aws.controller.validator.DisponibilitaValidator;
import com.polyspecialistcenter.aws.model.Disponibilita;
import static com.polyspecialistcenter.aws.model.Disponibilita.DIR_ADMIN_PAGES_DISP;
import static com.polyspecialistcenter.aws.model.Disponibilita.DIR_PAGES_DISP;
import com.polyspecialistcenter.aws.service.DisponibilitaService;
import com.polyspecialistcenter.aws.service.ProfessionistaService;

@Controller
public class DisponibilitaController {
	
	@Autowired
	private DisponibilitaService disponibilitaService;
	
	@Autowired
	private DisponibilitaValidator disponibilitaValidator;
	
	@Autowired
	private ProfessionistaService professionistaService;
	
	
	/* METHODS GENERIC_USER */
	
	@GetMapping("/disponibilita/{id}")
	public String getDisponibilitaProfessionista(@PathVariable("id") Long id, Model model) {
		model.addAttribute("disponibilitaList", this.professionistaService.findById(id).getDisponibilita());
		
		return DIR_PAGES_DISP + "elencoDisponibilita";
	}
	
	/* METHODS ADMIN */
	
	@GetMapping("/admin/disponibilita/{id}")
	public String getAdminDisponibilitaProfessionista(@PathVariable("id") Long id, Model model) {
		model.addAttribute("disponibilitaList", this.professionistaService.findById(id).getDisponibilita());
		
		return DIR_ADMIN_PAGES_DISP + "adminElencoDisponibilita";
	}
	
	// --- INSERIMENTO
	
	@GetMapping("/admin/disponibilita/add/{id}")
	public String addGetDisponibilita(@PathVariable("id") Long id, Model model) {
		model.addAttribute("idProfessionista", id);
		model.addAttribute("disponibilita", new Disponibilita());
		
		return DIR_ADMIN_PAGES_DISP + "disponibilitaForm";
	}
	
	@PostMapping("/admin/disponibilita/add/{id}")
	public String addDisponibilita(@Valid @ModelAttribute("disponibilita") Disponibilita disponibilita, BindingResult bindingResult, 
									@PathVariable("id") Long id, Model model) {
		
		this.disponibilitaValidator.validate(disponibilita, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.professionistaService.addDisponibilita(id, disponibilita);
			
			return this.getAdminDisponibilitaProfessionista(id, model);
		}
		
		model.addAttribute("id", id);
		return DIR_ADMIN_PAGES_DISP + "disponibilitaForm";
	}
	
	// --- ELIMINAZIONE
	
	@GetMapping("/admin/disponibilita/delete/{id}")
	public String deleteDisponibilita(@PathVariable("id") Long id, Model model) {
		Disponibilita disponibilita = this.disponibilitaService.findById(id);
		this.professionistaService.deleteDisponibilita(disponibilita);
		
		return this.getAdminDisponibilitaProfessionista(disponibilita.getProfessionista().getId(), model);
	}
	
	// --- MODIFICA
	
	@GetMapping("/admin/disponibilita/edit/{id}")
	public String editDisponibilita(@PathVariable("id") Long id, Model model) {
		model.addAttribute("disponibilita", this.disponibilitaService.findById(id));
		
		return DIR_ADMIN_PAGES_DISP + "editDisponibilita";
	}
	
	@PostMapping("/admin/disponibilita/edit/{id}")
	public String editDisponiblita(@Valid @ModelAttribute("disponibilita") Disponibilita newDisponibilita, BindingResult bindingResult, @PathVariable("id") Long id, Model model) {
		
		this.disponibilitaValidator.validate(newDisponibilita, bindingResult);
		if(!bindingResult.hasErrors()) {
			Disponibilita disponibilita = this.disponibilitaService.findById(id);
			this.disponibilitaService.update(disponibilita, newDisponibilita);
			
			return this.getAdminDisponibilitaProfessionista(disponibilita.getProfessionista().getId(), model);
		}
		
		newDisponibilita.setId(id);
		model.addAttribute("disponibilita", newDisponibilita);
		
		return DIR_ADMIN_PAGES_DISP + "editDisponibilita";
	}
}
