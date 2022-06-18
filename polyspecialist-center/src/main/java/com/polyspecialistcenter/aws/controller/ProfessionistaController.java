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

import com.polyspecialistcenter.aws.controller.validator.ProfessionistaValidator;
import com.polyspecialistcenter.aws.model.Professionista;
import com.polyspecialistcenter.aws.service.ProfessionistaService;

@Controller
public class ProfessionistaController {
	
	@Autowired
	private ProfessionistaService professionistaService;
	
	@Autowired
	private ProfessionistaValidator professionistaValidator;
	
	@GetMapping("/professionista/{id}")
	public String getProfessionista(@PathVariable("id") Long id, Model model) {
		Professionista professionista = professionistaService.findById(id);
		model.addAttribute("professionista", professionista);
		
		return "paginaProfessionista";
	}
	
	@GetMapping("/professionisti")
	public String getProfessionisti(Model model) {
		model.addAttribute("professionisti", this.professionistaService.findAll());
		
		return "listaProfessionisti";
	}
	
	@GetMapping("/admin/professionista/add")
	public String addProfessionista(Model model) {
		model.addAttribute("professionista", new Professionista());
		
		return "formNuovoProfessionista";
	}
	
	@PostMapping("/admin/professionista/add")
	public String addNewProfessionista(@Valid @ModelAttribute("professionista") Professionista professionista, BindingResult bindingResult, Model model) {
		this.professionistaValidator.validate(professionista, bindingResult);
		
		if(!bindingResult.hasErrors()) {
			this.professionistaService.save(professionista);
			model.addAttribute("professionisti", this.professionistaService.findAll());
			
			return "listaProfessionisti";
		}
		
		return "formNuovoProfessionista";
	}
	/* root permesso / entita / azione / parametri */
	@GetMapping("/admin/professionista/delete/{id}")
	public String deleteProfessionista(@PathVariable("id") Long id, Model model) {
		Professionista professionista = professionistaService.findById(id);
		this.professionistaService.delete(professionista);
		model.addAttribute("professionisti", this.professionistaService.findAll());
		
		return "listaProfessionisti";
	}
	
	@GetMapping("/admin/professionista/edit/{id}")
	public String getEditProfessionista(@PathVariable("id") Long id, Model model) {
		Professionista professionista = professionistaService.findById(id);
		model.addAttribute("professionista", professionista);
		
		return "formModificaProfessionista";
	}
	
	@PostMapping("/admin/professionista/edit/{id}")
	public String editProfessionista(@Valid @ModelAttribute("professionista") Professionista newProfessionista, BindingResult bindingResult, @PathVariable("id") Long id, Model model) {
		Professionista professionista = professionistaService.findById(id);
		
		this.professionistaValidator.validate(newProfessionista, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.professionistaService.update(professionista, newProfessionista);
			model.addAttribute("professionisti", this.professionistaService.findAll());
			
			return "listaProfessionisti";
		}
		
		newProfessionista.setId(id);
		model.addAttribute("professionista", professionista);
		
		return "formModificaProfessionista";
	}
	
}
