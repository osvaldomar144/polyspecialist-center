package com.polyspecialistcenter.aws.controller;

import static com.polyspecialistcenter.aws.model.Disponibilita.DIR_ADMIN_PAGES_DISP;
import static com.polyspecialistcenter.aws.model.Disponibilita.DIR_PAGES_DISP;

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
import com.polyspecialistcenter.aws.model.Professionista;
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
		model.addAttribute("idProfessionista", id);
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
		
		Professionista professionista = this.professionistaService.findById(id);
		disponibilita.setProfessionista(professionista);
		disponibilita.setActive(true);
		this.disponibilitaValidator.validate(disponibilita, bindingResult);
		
		if(!bindingResult.hasErrors()) {
			this.professionistaService.addDisponibilita(professionista, disponibilita);
			return this.getAdminDisponibilitaProfessionista(id, model);
		}
		
		model.addAttribute("id", id);
		return DIR_ADMIN_PAGES_DISP + "disponibilitaForm";
	}
	
	// --- ELIMINAZIONE
	
	@GetMapping("/admin/disponibilita/delete/{id}")
	public String deleteDisponibilita(@PathVariable("id") Long id, Model model) {
		Disponibilita disponibilita = this.disponibilitaService.findById(id);		
		Professionista p = this.professionistaService.findById(disponibilita.getProfessionista().getId());
		
		p.getDisponibilita().remove(disponibilita);
		this.disponibilitaService.delete(disponibilita);
		this.professionistaService.save(p);	
		
		return "redirect:/admin/disponibilita/" + p.getId();
	}
	
	// --- MODIFICA
	
	@GetMapping("/admin/disponibilita/edit/{id}")
	public String editDisponibilita(@PathVariable("id") Long id, Model model) {
		model.addAttribute("disponibilita", this.disponibilitaService.findById(id));
		
		return DIR_ADMIN_PAGES_DISP + "editDisponibilita";
	}
	
	@PostMapping("/admin/disponibilita/edit/{id}")
	public String editDisponiblita(@Valid @ModelAttribute("disponibilita") Disponibilita disponibilita, 
								   BindingResult bindingResult, 
								   @PathVariable("id") Long id, 
								   Model model) {
		
		Disponibilita d = this.disponibilitaService.findById(id);
		disponibilita.setProfessionista(d.getProfessionista());
		this.disponibilitaValidator.validate(disponibilita, bindingResult);
		if(!bindingResult.hasErrors()) {
			
			this.disponibilitaService.update(d, disponibilita);
			
			return this.getAdminDisponibilitaProfessionista(d.getProfessionista().getId(), model);
		}
		
		disponibilita.setId(id);
		return DIR_ADMIN_PAGES_DISP + "editDisponibilita";
	}
	
}
