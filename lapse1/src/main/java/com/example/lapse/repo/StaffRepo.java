package com.example.lapse.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.lapse.domain.Manager;
import com.example.lapse.domain.Staff;

public interface StaffRepo extends JpaRepository<Staff, Integer> {
	
	@Query("SELECT s.name from Staff AS s")
	ArrayList<String> findAllStaffNames();
	ArrayList<Staff> findByName(String name);

	@Modifying
	@Transactional
	@Query(value = "UPDATE staff SET dtype = 'Manager', role = 'Manager' WHERE id = ?",
			nativeQuery = true)
	int promoteUser(Integer id);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE staff SET dtype = 'Staff', role = 'Staff' WHERE id =?",
			nativeQuery = true)
	int demoteUser(Integer id);
	
	 Staff findByEmail(String email);
	 ArrayList<Staff> findByManagerId(Integer id);
}
