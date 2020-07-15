package com.example.lapse.service;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.lapse.domain.Staff;

@Service
public interface StaffService {
	public Page<Staff> findAll(int pageNumber,int numberofitems);
	public boolean saveStaff(Staff staff);
	public void deleteStaff(Staff staff);
	
	public ArrayList<String> findAllStaffNames();
	public Staff findStafftById(Integer id);
	public Staff findStaffByName(String name);
	
	public int promoteStaff(Integer id);
	public int demoteStaff(Integer id);
	
	public Staff findStaffByEmail(String email);
	public boolean validateStaff(String email, String password);
	public ArrayList<Staff> findSubordinateByManager(int managerId);
}
