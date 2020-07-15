package com.example.lapse.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.lapse.domain.Manager;

public interface ManagerRepo extends JpaRepository<Manager, Integer> {

	  List<Manager> findByEmail(String email);

	  @Query("Select m.name from Manager m")
	  ArrayList<String> findAllManagerNames();

	  List<Manager> findByName(String name);
}
