package com.polyspecialistcenter.aws.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.polyspecialistcenter.aws.model.Credentials;

public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

	public Optional<Credentials> findByUsername(String username);

	public boolean existsByUsername(String username);

}
