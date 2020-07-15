package com.example.lapse.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lapse.domain.PublicHoliday;


public interface PublicRepo extends JpaRepository<PublicHoliday, Integer> {
	
	List<PublicHoliday> findByName(String name);

}
