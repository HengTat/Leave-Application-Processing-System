package com.example.lapse.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.lapse.domain.Staff;
import com.example.lapse.service.StaffService;
import com.example.lapse.service.StaffServiceImpl;
import com.example.lapse.utils.Login;

@Component
public class LoginValidator implements Validator {

	@Autowired
	private StaffService staffservice;
	
	@Autowired
	public void setStaffService(StaffServiceImpl sserviceImpl) {
		this.staffservice = sserviceImpl;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Login.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Login user = (Login) target;
		System.out.println(user);
		String email = user.getEmail();	
		Staff staff = staffservice.findStaffByEmail(email);
		if (staff == null) {
			errors.rejectValue("email", "email.error");
		}
		if (staff != null) {
			if (!(staff.getPassword().equals(user.getPassword()))) {
				errors.rejectValue("password", "password.error");
				}
			}
		}
}
