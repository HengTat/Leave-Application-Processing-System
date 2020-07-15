package com.example.lapse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lapse.domain.Admin;
import com.example.lapse.repo.AdminRepo;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	AdminRepo aRepo;
	
	@Override
	public Admin findAdminByEmail(String email) {
		return aRepo.findByEmail(email);
	}
	
}
