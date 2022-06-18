package com.polyspecialistcenter.aws.repository;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.repository.CrudRepository;

import com.polyspecialistcenter.aws.model.Disponibilita;
import com.polyspecialistcenter.aws.model.Professionista;

public interface DisponibilitaRepository extends CrudRepository<Disponibilita, Long> {

	public boolean existsByDataAndOraInizioAndOraFineAndProfessionista(LocalDate data, LocalTime oraInizio, LocalTime oraFine, Professionista professionista);

}
