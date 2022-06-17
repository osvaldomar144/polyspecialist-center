package com.polyspecialistcenter.aws.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polyspecialistcenter.aws.model.Professionista;
import com.polyspecialistcenter.aws.repository.ProfessionistaRepository;

@Service
public class ProfessionistaService {

	@Autowired
	private ProfessionistaRepository professionistaRepository;
	
	public boolean alreadyExists(Professionista target) {
		return professionistaRepository.existsByPartitaIVA(target.getPartitaIVA());
	}

	public Professionista findById(Long id) {
		return professionistaRepository.findById(id).get();
	}

	@Transactional
	public void save(Professionista professionista) {
		professionistaRepository.save(professionista);
	}
	
	public List<Professionista> findAll() {
		return (List<Professionista>) professionistaRepository.findAll();
	}
	
	@Transactional
	public void delete(Professionista professionista) {
		this.professionistaRepository.delete(professionista);
	}
	
	public void update(Professionista professionista, Professionista newProfessionista) {
		professionista.setNome(newProfessionista.getNome());
		professionista.setCognome(newProfessionista.getCognome());
		professionista.setImg(newProfessionista.getImg());
		professionista.setPartitaIVA(newProfessionista.getPartitaIVA());
		this.professionistaRepository.save(professionista);
	}

}
