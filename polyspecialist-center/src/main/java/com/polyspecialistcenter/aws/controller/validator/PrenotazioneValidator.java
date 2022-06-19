package com.polyspecialistcenter.aws.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.polyspecialistcenter.aws.model.Prenotazione;
import com.polyspecialistcenter.aws.service.PrenotazioneService;

@Component
public class PrenotazioneValidator implements Validator {

	@Autowired
	private PrenotazioneService prenotazioneService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Prenotazione.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(this.prenotazioneService.alreadyExists((Prenotazione) target))
			errors.reject("prenotazione.duplicato");
	}
	
}
