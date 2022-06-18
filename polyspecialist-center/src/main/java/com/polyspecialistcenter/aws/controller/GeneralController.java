package com.polyspecialistcenter.aws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.polyspecialistcenter.aws.service.ProfessionistaService;
import com.polyspecialistcenter.aws.service.ServizioService;

@Controller
public class GeneralController {

	@Autowired
	private ServizioService servizioService;
	
	@Autowired
	private ProfessionistaService professionistaService;

	@GetMapping("/")
	public String getServiziAndProfessionisti(Model model) {
		
		model.addAttribute("professionisti", this.professionistaService.findLastProfessionisti());
		model.addAttribute("servizi", this.servizioService.findLastServizi());
		
		return "index";
	}
	
	@GetMapping("/admin")
	public String get() {
		return "redirect/" + "admin/professionisti";
	}
	
}
