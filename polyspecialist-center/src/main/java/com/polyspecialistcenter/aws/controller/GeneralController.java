package com.polyspecialistcenter.aws.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.polyspecialistcenter.aws.model.Professionista;
import com.polyspecialistcenter.aws.model.Servizio;
import com.polyspecialistcenter.aws.service.ProfessionistaService;
import com.polyspecialistcenter.aws.service.ServizioService;

@Controller
public class GeneralController {

	@Autowired
	private ServizioService servizioService;
	
	@Autowired
	private ProfessionistaService professionistaService;

	@GetMapping("/")
	public String getServiziAndProfessionisti() {
		List<Professionista> professionisti = this.professionistaService.findAll();
		List<Servizio> servizi = this.servizioService.findAll();
		
		List<Professionista> lastProfessionisti()
		for(int i = 0; i < 6; i++) {
			
		}
		
		return "";
	}
	
	@GetMapping("/admin")
	public String get() {
		return "";
	}
	
}
