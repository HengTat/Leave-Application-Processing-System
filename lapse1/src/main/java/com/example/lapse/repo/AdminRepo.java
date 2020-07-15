package com.example.lapse.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lapse.domain.Admin;

public interface AdminRepo extends JpaRepository<Admin, Integer> {
	
	Admin findByEmail(String email);
	List<Admin> findByEmailLike(String email);

}
