package com.polyspecialistcenter.aws.repository;

import org.springframework.data.repository.CrudRepository;

import com.polyspecialistcenter.aws.model.Professionista;
import com.polyspecialistcenter.aws.model.Servizio;

public interface ServizioRepository extends CrudRepository<Servizio, Long> {
	
	public boolean existsByNomeAndProfessionista(String nome, Professionista professionista);
}
