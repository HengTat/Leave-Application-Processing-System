package com.example.lapse.domain;

import javax.persistence.Entity;



@Entity
public class Admin extends User {
	
	public Admin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Admin(String name, String password, String email) {
		super(name, password, email);
		// TODO Auto-generated constructor stub
	}


	// Removed:
	
//	public void promotestaff(Staff staff) {
//		
//		Manager newManager = new Manager();
//	
//		newManager.setEmail(staff.getEmail());
//		newManager.setName(staff.getName());
//		newManager.setPassword(staff.getPassword());
//		newManager.setAnnualLeaveEntitlement(staff.getAnnualLeaveEntitlement());
//		newManager.setMedicalLeaveEntitment(staff.getMedicalLeaveEntitment());
//		newManager.setCompensationLeaveEntitlment(staff.getCompensationLeaveEntitlment());
//	}
//	

}
