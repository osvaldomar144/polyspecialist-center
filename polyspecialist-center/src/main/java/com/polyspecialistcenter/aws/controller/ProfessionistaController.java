package com.polyspecialistcenter.aws.controller;

import static com.polyspecialistcenter.aws.model.Professionista.DIR_ADMIN_PAGES_PROF;
import static com.polyspecialistcenter.aws.model.Professionista.DIR_FOLDER_IMG;
import static com.polyspecialistcenter.aws.model.Professionista.DIR_PAGES_PROF;

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

import com.polyspecialistcenter.aws.controller.validator.ProfessionistaValidator;
import com.polyspecialistcenter.aws.model.Professionista;
import com.polyspecialistcenter.aws.service.ProfessionistaService;
import com.polyspecialistcenter.aws.utility.FileStore;

@Controller
public class ProfessionistaController {
	
	@Autowired
	private ProfessionistaService professionistaService;
	
	@Autowired
	private ProfessionistaValidator professionistaValidator;
	
	
	/* METHODS GENERIC_USER */
	
	@GetMapping("/professionista/{id}")
	public String getProfessionista(@PathVariable("id") Long id, Model model) {
		Professionista professionista = professionistaService.findById(id);
		model.addAttribute("professionista", professionista);
		
		return DIR_PAGES_PROF + "professionista";
	}
	
	@GetMapping("/professionisti")
	public String getProfessionisti(Model model) {
		model.addAttribute("professionisti", this.professionistaService.findAll());
		
		return DIR_PAGES_PROF + "elencoProfessionisti";
	}
	
	/* METHODS ADMIN */
	
	@GetMapping("/admin/professionista/{id}")
	public String getAdminProfessionista(@PathVariable("id") Long id, Model model) {
		Professionista professionista = professionistaService.findById(id);
		model.addAttribute("professionista", professionista);
		
		return DIR_ADMIN_PAGES_PROF + "adminProfessionista";
	}
	
	@GetMapping("/admin/professionisti")
	public String getAdminProfessionisti(Model model) {
		model.addAttribute("professionisti", this.professionistaService.findAll());
		
		return DIR_ADMIN_PAGES_PROF + "adminElencoProfessionisti";
	}
	
	// --- INSERIMENTO
	
	@GetMapping("/admin/professionista/add")
	public String addProfessionista(Model model) {
		model.addAttribute("professionista", new Professionista());
		
		return DIR_ADMIN_PAGES_PROF + "professionistaForm";
	}
	
	@PostMapping("/admin/professionista/add")
	public String addNewProfessionista(@Valid @ModelAttribute("professionista") Professionista professionista, 
									   BindingResult bindingResult, 
									   @RequestParam("file") MultipartFile file,
									   Model model) {
		this.professionistaValidator.validate(professionista, bindingResult);
		
		if(!bindingResult.hasErrors()) {
			professionista.setImg(FileStore.store(file, DIR_FOLDER_IMG));
			this.professionistaService.save(professionista);
			
			return this.getAdminProfessionisti(model);
		}
		
		return DIR_ADMIN_PAGES_PROF + "professionistaForm";
	}
	
	
	// --- ELIMINAZIONE
	
	/* root permesso / entita / azione / parametri */
	@GetMapping("/admin/professionista/delete/{id}")
	public String deleteProfessionista(@PathVariable("id") Long id, Model model) {
		Professionista professionista = professionistaService.findById(id);
		FileStore.removeImg(DIR_FOLDER_IMG, professionista.getImg());
		
		//eliminazione immagini a cascata
	    professionista.getServizi().stream().forEach(servizio -> servizio.eliminaImmagine());
		
		this.professionistaService.delete(professionista);
		
		return this.getAdminProfessionisti(model);
	}
	
	// --- MODIFICA
	
	@GetMapping("/admin/professionista/edit/{id}")
	public String getEditProfessionista(@PathVariable("id") Long id, Model model) {
		model.addAttribute("professionista",this.professionistaService.findById(id));	
		return DIR_ADMIN_PAGES_PROF + "editProfessionista";
	}
	
	@PostMapping("/admin/professionista/edit/{id}")
	public String modificaProfessionista(@Valid @ModelAttribute("professionista") Professionista professionista, 
							   BindingResult bindingResult, 
							   @PathVariable("id") Long id, 
							   Model model) {
		
		Professionista p = this.professionistaService.findById(id);
		if(professionista.getPartitaIVA().equals(p.getPartitaIVA())) {
			professionista.setPartitaIVA("DefPartIVA");
			this.professionistaValidator.validate(professionista, bindingResult);
			professionista.setPartitaIVA(p.getPartitaIVA());
		}else {
			this.professionistaValidator.validate(professionista, bindingResult);
		}
		
		professionista.setId(id);
		if(!bindingResult.hasErrors()) {
			this.professionistaService.update(professionista, professionista.getId());
			return this.getAdminProfessionisti(model);
		}
		professionista.setImg(p.getImg());
		return  DIR_ADMIN_PAGES_PROF + "editProfessionista";
	}
	
	@PostMapping("/admin/professionista/changeImg/{idProf}")
	public String changeImgChef(@PathVariable("idProf") Long idProf,
			   					@RequestParam("file") MultipartFile file, 
			   					Model model) {
		
		Professionista p = this.professionistaService.findById(idProf);
		if(!p.getImg().equals("profili")) {
			FileStore.removeImg(DIR_FOLDER_IMG, p.getImg());
		}

		p.setImg(FileStore.store(file, DIR_FOLDER_IMG));
		this.professionistaService.save(p);
		return this.getEditProfessionista(idProf, model);
	}
	
}
