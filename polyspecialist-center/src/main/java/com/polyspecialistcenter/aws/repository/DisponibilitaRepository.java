package com.polyspecialistcenter.aws.repository;

import org.springframework.data.repository.CrudRepository;

import com.polyspecialistcenter.aws.model.Disponibilita;
import com.polyspecialistcenter.aws.model.Professionista;

public interface DisponibilitaRepository extends CrudRepository<Disponibilita, Long> {

	public boolean existsByDataAndOraInizioAndOraFineAndProfessionista(String data, String oraInizio, String oraFine, Professionista professionista);

}
