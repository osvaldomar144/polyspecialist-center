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
	
	@Transactional
	public void delete(Professionista professionista) {
		this.professionistaRepository.delete(professionista);
	}
	
	@Transactional
	public void update(Professionista professionista, Professionista newProfessionista) {
		professionista.setNome(newProfessionista.getNome());
		professionista.setCognome(newProfessionista.getCognome());
		professionista.setImg(newProfessionista.getImg());
		professionista.setPartitaIVA(newProfessionista.getPartitaIVA());
		this.professionistaRepository.save(professionista);
	}
	
	@Transactional
	public void addServizio(Professionista professionista, Servizio servizio) {
		professionista.getServizi().add(servizio);
		servizio.setProfessionista(professionista);
		this.professionistaRepository.save(professionista);
	}
	
	@Transactional
	public void addDisponibilita(Long id, Disponibilita disponibilita) {
		Professionista professionista = this.findById(id);
		professionista.getDisponibilita().add(disponibilita);
		disponibilita.setProfessionista(professionista);
		this.professionistaRepository.save(professionista);
	}

	@Transactional
	public void deleteDisponibilita(Disponibilita disponibilita) {
		Professionista professionista = disponibilita.getProfessionista();
		professionista.getDisponibilita().remove(disponibilita);
		this.professionistaRepository.save(professionista);
	}

	@Transactional
	public void deleteServizio(Professionista professionista, Servizio servizio) {
		professionista.getServizi().remove(servizio);
		this.professionistaRepository.save(professionista);
	}

}
