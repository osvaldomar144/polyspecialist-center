package com.polyspecialistcenter.aws.repository;

import org.springframework.data.repository.CrudRepository;

import com.polyspecialistcenter.aws.model.Disponibilita;
import com.polyspecialistcenter.aws.model.Prenotazione;
import com.polyspecialistcenter.aws.model.Professionista;
import com.polyspecialistcenter.aws.model.Servizio;
import com.polyspecialistcenter.aws.model.Utente;

public interface PrenotazioneRepository extends CrudRepository<Prenotazione, Long>{

	public boolean existsByProfessionistaAndServizioAndDisponibilitaAndCliente(Professionista professionista, Servizio servizio, Disponibilita disponibilita, Utente cliente);

}
