package com.polyspecialistcenter.aws.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.polyspecialistcenter.aws.model.Disponibilita;
import com.polyspecialistcenter.aws.service.DisponibilitaService;

@Component
public class DisponibilitaValidator implements Validator {

	@Autowired
	private DisponibilitaService disponibilitaService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Disponibilita.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(this.disponibilitaService.alreadyExists((Disponibilita) target))
			errors.reject("disponivilita.duplicato");
	}

}
