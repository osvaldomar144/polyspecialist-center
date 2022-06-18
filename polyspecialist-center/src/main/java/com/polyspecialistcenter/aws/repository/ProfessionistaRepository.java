package com.polyspecialistcenter.aws.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.polyspecialistcenter.aws.model.Professionista;

public interface ProfessionistaRepository extends CrudRepository<Professionista, Long> {

	boolean existsByPartitaIVA(String partitaIVA);

	public List<Professionista> findTopN(@Param("limit") int limit);

}
