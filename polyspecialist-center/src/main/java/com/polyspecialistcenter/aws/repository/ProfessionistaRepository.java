package com.polyspecialistcenter.aws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.polyspecialistcenter.aws.model.Professionista;

public interface ProfessionistaRepository extends CrudRepository<Professionista, Long> {

	boolean existsByPartitaIVA(String partitaIVA);

	@Query(value = "SELECT * FROM professionista order by id limit :limit", nativeQuery = true)
	public List<Professionista> findTopN(@Param("limit") int limit);

}
