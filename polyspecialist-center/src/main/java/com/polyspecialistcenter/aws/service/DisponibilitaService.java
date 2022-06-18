package com.polyspecialistcenter.aws.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polyspecialistcenter.aws.model.Disponibilita;
import com.polyspecialistcenter.aws.repository.DisponibilitaRepository;

@Service
public class DisponibilitaService {

	@Autowired
	private DisponibilitaRepository disponibilitaRepository;
	
	public boolean alreadyExists(Disponibilita target) {
		return this.disponibilitaRepository.existsByDataAndOraInizioAndOraFineAndProfessionista(target.getData(), target.getOraInizio(), target.getOraFine(), target.getProfessionista());
	}

	public Disponibilita findById(Long idDisponibilita) {
		return this.disponibilitaRepository.findById(idDisponibilita).get();
	}
	
	@Transactional
	public void update(Disponibilita disponibilita, Disponibilita newDisponibilita) {
		disponibilita.setData(newDisponibilita.getData());
		disponibilita.setOraInizio(newDisponibilita.getOraInizio());
		disponibilita.setOraFine(newDisponibilita.getOraFine());
		this.disponibilitaRepository.save(disponibilita);
	}

}
