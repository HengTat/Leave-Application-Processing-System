package com.example.lapse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.lapse.domain.Admin;
import com.example.lapse.service.AdminService;
import com.example.lapse.service.AdminServiceImpl;
import com.example.lapse.utils.AdminLogin;

@Component
public class AdminLoginValidator implements Validator {
	
	@Autowired
	private AdminService adminservice;
	
	@Autowired
	public void setAdminService(AdminServiceImpl aserviceImpl) {
		this.adminservice = aserviceImpl;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return AdminLogin.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		AdminLogin user = (AdminLogin) target;
		String email = user.getEmail();	
		Admin admin = adminservice.findAdminByEmail(email);
		if (admin == null) {
			errors.rejectValue("email", "email.error");
		}
		if (admin != null) {
			if (!(admin.getPassword().equals(user.getPassword()))) {
				errors.rejectValue("password", "password.error");
				}
			}
		}

}
