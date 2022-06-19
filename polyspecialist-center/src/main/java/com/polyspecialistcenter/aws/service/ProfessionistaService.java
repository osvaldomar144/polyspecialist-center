package com.polyspecialistcenter.aws.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polyspecialistcenter.aws.model.Disponibilita;
import com.polyspecialistcenter.aws.model.Professionista;
import com.polyspecialistcenter.aws.model.Servizio;
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
	
	/*public List<Professionista> findByServizio(Servizio servizio) {
		return this.professionistaRepository.findByServizio(servizio);
	}*/
	
	@Transactional
	public void delete(Professionista professionista) {
		this.professionistaRepository.delete(professionista);
	}
	
	
	@Transactional
	public void update(Professionista professionista, Long id) {
		Professionista p = this.professionistaRepository.findById(id).get();
		p.setNome(professionista.getNome());
		p.setCognome(professionista.getCognome());
		p.setProfessione(professionista.getProfessione());
		p.setPartitaIVA(professionista.getPartitaIVA());
		this.professionistaRepository.save(p);
	}
	
	
	@Transactional
	public void addServizio(Professionista professionista, Servizio servizio) {
		professionista.getServizi().add(servizio);
		this.professionistaRepository.save(professionista);
	}
	
	@Transactional
	public void addDisponibilita(Professionista professionista, Disponibilita disponibilita) {
		professionista.getDisponibilita().add(disponibilita);
		this.professionistaRepository.save(professionista);
	}


	public List<Professionista> findLastProfessionisti() {
		return this.professionistaRepository.findTopN(6);
	}

}
