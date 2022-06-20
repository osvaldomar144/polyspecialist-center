package com.polyspecialistcenter.aws.repository;

import org.springframework.data.repository.CrudRepository;

import com.polyspecialistcenter.aws.model.Utente;

public interface UtenteRepository extends CrudRepository<Utente, Long> {

	boolean existsByNomeAndCognome(String nome, String cognome);

}
