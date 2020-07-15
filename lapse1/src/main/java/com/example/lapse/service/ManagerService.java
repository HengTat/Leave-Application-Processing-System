package com.example.lapse.service;

import java.util.ArrayList;

import com.example.lapse.domain.Manager;
import com.example.lapse.domain.Staff;


public interface ManagerService {
	
		public ArrayList<String> findAllManagerNames();
		public boolean saveManager(Manager manager);
		public Manager findManagerById(int id);
		public Manager findManagerByName(String name);
		public void deleteManager(Manager manager);
}
