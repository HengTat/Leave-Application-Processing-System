package com.example.lapse.domain;

import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;


@Entity
public class Manager extends Staff {

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "manager")
	private Collection<Staff> staffList;

	public Manager() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Manager(String name, String password, String email, List<LeaveType> leaveTypes) {
		super(name, password, email, leaveTypes);
		this.setRole("Manager");
	}
	
	public Manager(String name, String password, String email) {
		super(name, password, email);
		this.setRole("Manager");
	}

	public Collection<Staff> getStaffList() {
		return staffList;
	}

	public void setStaffList(List<Staff> staffList) {
		this.staffList = staffList;
	}

}
