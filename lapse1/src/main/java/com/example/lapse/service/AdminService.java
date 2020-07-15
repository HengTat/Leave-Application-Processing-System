package com.example.lapse.service;

import org.springframework.stereotype.Service;

import com.example.lapse.domain.Admin;

@Service
public interface AdminService {
	
	public Admin findAdminByEmail(String email);

}