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
	
	@GetMapping("/professionista/disponibilita/{id}")
	public String getDisponibilitaProfessionista(@PathVariable("id") Long id, Model model) {
		model.addAttribute("disponibilitaList", this.professionistaService.findById(id).getDisponibilita());
		
		return "elencoDisponibilitaProfessionista";
	}
	
	@GetMapping("/admin/disponibilita/add/{id}")
	public String addGetDisponibilita(@PathVariable("id") Long id, Model model) {
		model.addAttribute("idProfessionista", id);
		model.addAttribute("disponibilita", new Disponibilita());
		
		return "formNuovaDisponibilita";
	}
	
	@PostMapping("/admin/disponibilita/add/{id}")
	public String addDisponibilita(@Valid @ModelAttribute("disponibilita") Disponibilita disponibilita, BindingResult bindingResult, 
									@PathVariable("id") Long id, Model model) {
		
		this.disponibilitaValidator.validate(disponibilita, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.professionistaService.addDisponibilita(id, disponibilita);
			
			return this.getDisponibilitaProfessionista(id, model);
		}
		
		model.addAttribute("id", id);
		return "formNuovaDisponibilita";
	}
	
	@GetMapping("/admin/disponibilita/delete/{id}")
	public String deleteDisponibilita(@PathVariable("id") Long id, Model model) {
		Disponibilita disponibilita = this.disponibilitaService.findById(id);
		this.professionistaService.deleteDisponibilita(disponibilita);
		
		return this.getDisponibilitaProfessionista(disponibilita.getProfessionista().getId(), model);
	}
	
	@GetMapping("/admin/disponibilita/edit/{id}")
	public String editDisponibilita(@PathVariable("id") Long id, Model model) {
		model.addAttribute("disponibilita", this.disponibilitaService.findById(id));
		
		return "formModificaDisponibilita";
	}
	
	@PostMapping("/admin/disponibilita/edit/{id}")
	public String editDisponiblita(@Valid @ModelAttribute("disponibilita") Disponibilita newDisponibilita, BindingResult bindingResult, @PathVariable("id") Long id, Model model) {
		
		this.disponibilitaValidator.validate(newDisponibilita, bindingResult);
		if(!bindingResult.hasErrors()) {
			Disponibilita disponibilita = this.disponibilitaService.findById(id);
			this.disponibilitaService.update(disponibilita, newDisponibilita);
			
			return this.getDisponibilitaProfessionista(disponibilita.getProfessionista().getId(), model);
		}
		
		newDisponibilita.setId(id);
		model.addAttribute("disponibilita", newDisponibilita);
		
		return "formModificaDisponibilita";
	}
}
