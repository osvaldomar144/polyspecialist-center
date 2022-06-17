package com.polyspecialistcenter.aws.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polyspecialistcenter.aws.model.Servizio;
import com.polyspecialistcenter.aws.repository.ServizioRepository;

@Service
public class ServizioService {

	@Autowired
	private ServizioRepository servizioRepository;
	
	public boolean alreadyExists(Servizio target) {
		return this.servizioRepository.existsByNomeAndProfessionista(target.getNome(), target.getProfessionista());
	}
	
	@Transactional
	public void delete(Servizio servizio) {
		this.servizioRepository.delete(servizio);
	}
	
	public void update(Servizio servizio, Servizio newServizio) {
		servizio.setNome(newServizio.getNome());
		servizio.setDescrizione(newServizio.getDescrizione());
		servizio.setPrezzo(newServizio.getPrezzo());
	}

	public Servizio findById(Long id) {
		return this.servizioRepository.findById(id).get();
	}

	public List<Servizio> findAll() {
		return (List<Servizio>) this.servizioRepository.findAll();
	}

}
