package com.polyspecialistcenter.aws.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.polyspecialistcenter.aws.model.Professionista;
import com.polyspecialistcenter.aws.service.ProfessionistaService;

@Component
public class ProfessionistaValidator implements Validator {

	@Autowired
	private ProfessionistaService professionistaService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Professionista.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(professionistaService.alreadyExists((Professionista) target))
			errors.reject("duplicate.professionista");
	}

}
