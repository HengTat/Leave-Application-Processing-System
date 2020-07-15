package com.example.lapse.domain;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.lapse.enums.LeaveStatus;
import com.example.lapse.enums.TimeOfDay;

@Entity
public class LeaveApplication {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern ="dd-MM-yyyy")
	private Date applicationDate;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="dd-MM-yyyy")
	private Date startDate;
	
	//@Enumerated(EnumType.STRING)
	//private TimeOfDay startTimeOfDay;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="dd-MM-yyyy")
	private Date endDate;
	
	//@Enumerated(EnumType.STRING)
	//private TimeOfDay endTimeOfDay;
	
	@ManyToOne
	private LeaveType leaveType;
		
	private float noOfDays;
	
	@Enumerated(EnumType.STRING)
	private LeaveStatus leaveStatus;

	private String workDissemination;
	private boolean overseasTrip;
	private boolean halfday;

	private String contactDetails; 
	private String managerComment;
	
	@ManyToOne
	private Staff staff;
	
	@NotNull
	private String reason;
			
	public LeaveApplication() {
		super();
		this.leaveStatus = LeaveStatus.APPLIED;
		this.applicationDate = Calendar.getInstance().getTime();
	}
	
	public LeaveApplication(Date applicationDate,  Date startDate, /*TimeOfDay startTimeOfDay,*/
			 Date endDate, /*TimeOfDay endTimeOfDay,*/ LeaveType leaveType, float noOfDays, LeaveStatus leaveStatus,String reason,
			String workDissemination, boolean overseasTrip, boolean halfday, String contactDetails, String managerComment, Staff staff) {
		super();
		this.applicationDate = applicationDate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.leaveType = leaveType;
		this.noOfDays = noOfDays;
		this.leaveStatus = leaveStatus;
		this.workDissemination = workDissemination;
		this.overseasTrip = overseasTrip;
		this.contactDetails = contactDetails;
		this.managerComment = managerComment;
		this.staff = staff;
		/*this.startTimeOfDay = startTimeOfDay;
		this.endTimeOfDay = endTimeOfDay;*/
		this.reason = reason;
		this.halfday = halfday;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

/*	public TimeOfDay getStartTimeOfDay() {
		return startTimeOfDay;
	}

	public void setStartTimeOfDay(TimeOfDay startTimeOfDay) {
		this.startTimeOfDay = startTimeOfDay;
	}
	public TimeOfDay getEndTimeOfDay() {
		return endTimeOfDay;
	}

	public void setEndTimeOfDay(TimeOfDay endTimeOfDay) {
		this.endTimeOfDay = endTimeOfDay;
	}*/

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}



	public LeaveType getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}

	public float getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(float noOfDays) {
		this.noOfDays = noOfDays;
	}

	public LeaveStatus getLeaveStatus() {
		return leaveStatus;
	}

	public void setLeaveStatus(LeaveStatus leaveStatus) {
		this.leaveStatus = leaveStatus;
	}

	public String getWorkDissemination() {
		return workDissemination;
	}

	public void setWorkDissemination(String workDissemination) {
		this.workDissemination = workDissemination;
	}

	public boolean isOverseasTrip() {
		return overseasTrip;
	}

	public void setOverseasTrip(boolean overseasTrip) {
		this.overseasTrip = overseasTrip;
	}

	public String getContactDetails() {
		return contactDetails;
	}

	public void setContactDetails(String contactDetails) {
		this.contactDetails = contactDetails;
	}

	public String getManagerComment() {
		return managerComment;
	}

	public void setManagerComment(String managerComment) {
		this.managerComment = managerComment;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public boolean isHalfday() {
		return halfday;
	}

	public void setHalfday(boolean halfday) {
		this.halfday = halfday;
	}
		

}
