package com.polyspecialistcenter.aws.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polyspecialistcenter.aws.model.Prenotazione;
import com.polyspecialistcenter.aws.model.Utente;
import com.polyspecialistcenter.aws.repository.UtenteRepository;

@Service
public class UtenteService {
	
	@Autowired
	private UtenteRepository utenteRepository;
	
    @Transactional
    public Utente save(Utente utente) {
        return this.utenteRepository.save(utente);
    }
    
    public Utente getUser(Long id) {
        Optional<Utente> result = this.utenteRepository.findById(id);
        return result.orElse(null);
    }
	
    public List<Utente> getAllUsers() {
        List<Utente> result = new ArrayList<>();
        Iterable<Utente> iterable = this.utenteRepository.findAll();
        for(Utente user : iterable)
            result.add(user);
        return result;
    }
    
	public boolean alreadyExists(Utente u) {
		return utenteRepository.existsByNomeAndCognome(u.getNome(), u.getCognome());
	}
	
	@Transactional
	public void addPrenotazione(Utente u, Prenotazione prenotazione) {
		u.getPrenotazioni().add(prenotazione);
		this.utenteRepository.save(u);
	}
	
}
