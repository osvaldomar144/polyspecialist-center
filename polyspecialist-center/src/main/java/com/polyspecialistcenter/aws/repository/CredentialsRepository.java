package com.polyspecialistcenter.aws.repository;

import org.springframework.data.repository.CrudRepository;

import com.polyspecialistcenter.aws.model.Credentials;

public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

}
