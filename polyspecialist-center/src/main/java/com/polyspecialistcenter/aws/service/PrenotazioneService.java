package com.polyspecialistcenter.aws.service;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polyspecialistcenter.aws.model.Prenotazione;
import com.polyspecialistcenter.aws.repository.PrenotazioneRepository;

@Service
public class PrenotazioneService {

	@Autowired
	private PrenotazioneRepository prenotazioneRepository;
	
	public boolean alreadyExists(Prenotazione target) {
		return this.prenotazioneRepository.existsByProfessionistaAndServizioAndDisponibilitaAndCliente(target.getProfessionista(), target.getServizio(), target.getDisponibilita(), target.getCliente());
	}
	
	public Prenotazione findById(Long id) {
		return this.prenotazioneRepository.findById(id).get();
	}

	public void delete(Prenotazione p) {
		this.prenotazioneRepository.delete(p);
	}
	
	public List<Prenotazione> prenotazioniValide(List<Prenotazione> lista) {
		Iterator<Prenotazione> i = lista.iterator();
		while(i.hasNext()) {
			Prenotazione p = i.next();
			if(LocalDate.parse(i.next().getDisponibilita().getData()).isBefore(LocalDate.now())) {
				i.remove();
				this.prenotazioneRepository.delete(p);
			}
			
			i.next();
		}
		
		return lista;
	}

}
