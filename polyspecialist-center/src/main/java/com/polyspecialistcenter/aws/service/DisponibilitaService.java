package com.polyspecialistcenter.aws.service;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polyspecialistcenter.aws.model.Disponibilita;
import com.polyspecialistcenter.aws.model.Professionista;
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
	
	public List<Disponibilita> findByProfAndActive(Professionista professionista) {
		return this.disponibilitaRepository.findByProfessionistaAndActiveTrueOrderByDataAscOraInizio(professionista);
	}
	
	public List<Disponibilita> findByProfessionista(Professionista professionista) {
		return this.disponibilitaRepository.findByProfessionista(professionista);
	}
	
	@Transactional
	public void update(Disponibilita disponibilita, Disponibilita newDisponibilita) {
		disponibilita.setData(newDisponibilita.getData());
		disponibilita.setOraInizio(newDisponibilita.getOraInizio());
		disponibilita.setOraFine(newDisponibilita.getOraFine());
		this.disponibilitaRepository.save(disponibilita);
	}
	
	@Transactional
	public void delete(Disponibilita disponibilita) {
		this.disponibilitaRepository.delete(disponibilita);
	}
	
	public List<Disponibilita> disponibilitaValide(List<Disponibilita> lista) {
		Iterator<Disponibilita> i = lista.iterator();
		while(i.hasNext()) {
			Disponibilita d = i.next();
			if(LocalDate.parse(d.getData()).isBefore(LocalDate.now())) {
				i.remove();
				this.disponibilitaRepository.delete(d);
			}
			
			i.next();
		}
		
		return lista;
	}

}
