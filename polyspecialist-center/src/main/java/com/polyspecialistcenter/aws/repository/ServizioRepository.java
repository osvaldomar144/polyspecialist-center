package com.polyspecialistcenter.aws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.polyspecialistcenter.aws.model.Professionista;
import com.polyspecialistcenter.aws.model.Servizio;

public interface ServizioRepository extends CrudRepository<Servizio, Long> {
	
	public boolean existsByNomeAndProfessionista(String nome, Professionista professionista);
	
	@Query(value = "SELECT * FROM servizio order by id limit :limit", nativeQuery = true)
	public List<Servizio> findTopN(@Param("limit") int limit);
}
