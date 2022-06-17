package com.polyspecialistcenter.aws.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polyspecialistcenter.aws.model.Disponibilita;
import com.polyspecialistcenter.aws.repository.DisponibilitaRepository;

@Service
public class DisponibilitaService {

	@Autowired
	private DisponibilitaRepository disponibilitaRepository;
	
	public void delete(Disponibilita disponibilita) {
		this.disponibilitaRepository.delete(disponibilita);
	}
	
	public boolean alreadyExists(Disponibilita target) {
		return this.disponibilitaRepository.existsByDataAndOraInizioAndOraFine(target.getData(), target.getOraInizio(), target.getOraFine());
	}

	public Disponibilita findById(Long idDisponibilita) {
		return this.disponibilitaRepository.findById(idDisponibilita).get();
	}

}
