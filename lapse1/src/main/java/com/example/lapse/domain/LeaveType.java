package com.example.lapse.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class LeaveType {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@NotEmpty
	private String leaveType;
	@NotNull
	private float entitlement;
	
	@ManyToMany (mappedBy="leaveTypes")
	public List<Staff> staffList;
	
	public LeaveType() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LeaveType(String leaveType, float entitlement) {
		super();
		this.leaveType = leaveType;
		this.entitlement = entitlement;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public float getEntitlement() {
		return entitlement;
	}

	public void setEntitlement(float entitlement) {
		this.entitlement = entitlement;
	}

	public void setNoOfDays(float entitlement) {
		this.entitlement = entitlement;
	}

	@Override
	public String toString() {
		return "LeaveType [id=" + id + ", leaveType=" + leaveType + ", entitlement=" + entitlement + "]";
	}
	
	
}