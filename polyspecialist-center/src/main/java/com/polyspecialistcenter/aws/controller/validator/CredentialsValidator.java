package com.polyspecialistcenter.aws.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.polyspecialistcenter.aws.model.Credentials;
import com.polyspecialistcenter.aws.service.CredentialsService;

@Component
public class CredentialsValidator implements Validator {

	@Autowired
	private CredentialsService credentialsService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Credentials.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(credentialsService.alreadyExists((Credentials) target))
			errors.reject("utente.duplicato");
	}

}
