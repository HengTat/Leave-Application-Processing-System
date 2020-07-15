 package com.example.lapse.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Staff extends User{
	
	@ManyToMany
	protected List<LeaveType> leaveTypes;
	protected String role = "Staff";
	
	@OneToMany (mappedBy="staff")
	private List<LeaveApplication> leaveTransactions;

	@ManyToOne
	private Manager manager;
	
	public Staff() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Staff(String name, String password, String email, List<LeaveType> leaveTypes, Manager manager) {
		super(name, password, email);
		this.leaveTypes = leaveTypes;
		this.manager = manager;
	}
	
	public Staff(String name, String password, String email, Manager manager) {
		super(name, password, email);
		this.manager = manager;
	}
	
	public Staff(String name, String password, String email) {
		super(name, password, email);
	}
	
	public Staff(String name, String password, String email, List<LeaveType> leaveTypes) {
		super(name, password, email);
		this.leaveTypes = leaveTypes;
	}

	public List<LeaveType> getLeaveTypes() {
		return leaveTypes;
	}

	public void setsLeaveTypes(List<LeaveType> leaveTypes) {
		this.leaveTypes = leaveTypes;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}
	
	public List<LeaveApplication> getLeaveTransactions() {
		return leaveTransactions;
	}

	public void setLeaveTransactions(List<LeaveApplication> leaveTransactions) {
		this.leaveTransactions = leaveTransactions;
	}
	

	
}
	
	

