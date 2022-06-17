package com.polyspecialistcenter.aws.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.polyspecialistcenter.aws.model.Credentials;
import com.polyspecialistcenter.aws.repository.CredentialsRepository;

@Service
public class CredentialsService {

	@Autowired
	private CredentialsRepository credentialsRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Transactional
	public Credentials save(Credentials c) {
		c.setRole(Credentials.DEFAULT_ROLE);
		c.setPassword(this.passwordEncoder.encode(c.getPassword()));
		return credentialsRepository.save(c);
	}
	
	public Credentials getCredentials(String username) {
		Optional<Credentials> result = this.credentialsRepository.findByUsername(username);
		return result.orElse(null);
	}
	
	public Credentials getCredentials(Long id) {
        Optional<Credentials> result = this.credentialsRepository.findById(id);
        return result.orElse(null);
    }
	
	public boolean alreadyExists(Credentials target) {
		return credentialsRepository.existsByUsername(target.getUsername());
	}

}
