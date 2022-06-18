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
	
	/*@GetMapping("/professionista/add/{id}")
	public String addGetDisponibilita(@PathVariable("id") Long id, Model model) {
		Professionista professionista = this.professionistaService.findById(id);
		model.addAttribute("professionista", professionista);
		model.addAttribute("disponibilita", new Disponibilita());
		
		return "formNuovaDisponibilita";
	}*/
	
	/*@PostMapping("/professionista/add/{id}")
	public String addDisponibilita(@Valid @ModelAttribute Disponibilita disponibilita, BindingResult bindingResult, 
									@PathVariable("id") Long id, Model model) {
		Professionista professionista = this.professionistaService.findById(id);
		
		this.disponibilitaValidator.validate(disponibilita, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.professionistaService.addDisponibilita(professionista, disponibilita);
			model.addAttribute("professionista", professionista);
			model.addAttribute("disponibilitaList", professionista.getDisponibilita());
			
			return "elencoDisponibilitaDelProfessionista";
		}
		
		model.addAttribute("professionista", professionista);
		return "formNuovaDisponibilita";
	}*/
	
	@GetMapping("/professionista/{idProfessionista}/delete/{idDisponibilita}")
	public String deleteDisponibilita(@PathVariable("idProfessionista") Long idProfessionista, @PathVariable("idDisponibilita") Long idDisponibilita, Model model) {
		Professionista professionista = this.professionistaService.findById(idProfessionista);
		Disponibilita disponibilita = this.disponibilitaService.findById(idDisponibilita);
		
		return "elencoDisponibilitaProfessionista(?)";
	}
	
	@GetMapping()
	public String editDisponibilita() {
		return "index";
	}
	
	@PostMapping()
	public String editDisponiblita() {
		return "index";
	}
}
