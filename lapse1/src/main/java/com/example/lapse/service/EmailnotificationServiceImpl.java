package com.example.lapse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.lapse.domain.LeaveApplication;
import com.example.lapse.domain.LeaveType;
import com.example.lapse.domain.Staff;
import com.example.lapse.enums.LeaveStatus;

@Service
public class EmailnotificationServiceImpl implements Emailnotificationservice {
	
	private JavaMailSender javaMailSender;
		
	@Autowired
	public EmailnotificationServiceImpl(JavaMailSender javaMailSender) {		
		this.javaMailSender = javaMailSender;
	}
	
	//Leave Approval or Rejected Email
		@Override
		public void sendleavestatusemail(Staff staff,LeaveApplication Leave) {
			
			SimpleMailMessage mail= new SimpleMailMessage();
			mail.setTo(staff.getEmail());
			mail.setSubject("Leave Application ID:"+Leave.getId()+" " + Leave.getLeaveStatus());
			mail.setText("Dear "+staff.getName()+","+"\r\n"+"\r\n" +" Your Leave application ID:"+
			Leave.getId()+" has been "+Leave.getLeaveStatus() + ". Please login to find out more."
			+ "http://localhost:8080/home/login");
			
			if (Leave.getLeaveStatus()==LeaveStatus.REJECTED){
			mail.setText("Dear "+staff.getName()+","+"\r\n"+"\r\n" +" Your Leave application ID:"+
			Leave.getId()+" has been "+Leave.getLeaveStatus()+" REASON:"+Leave.getManagerComment() +
			". Please login to find out more."+ "http://localhost:8080/home/login");
			}
			javaMailSender.send(mail);
		}
		
		//Leave Creation Email Successful employee 
		@Override
		public void sendleavecreationsucessful(Staff staff, LeaveApplication Leave) {
			SimpleMailMessage mail= new SimpleMailMessage();
			mail.setTo(staff.getEmail());
			mail.setSubject("Leave Created Successfully");
			mail.setText("Dear "+staff.getName()+","+"\r\n"+"\r\n" +"Your Leave application ID:"
			+Leave.getId()+" for "+Leave.getLeaveType().getLeaveType() +" was created succesfully");
			javaMailSender.send(mail);
		}
		
		//Leave Creation Email Successful message manager
		@Override
		public void alertmanageofleaveapproval(Staff staff,LeaveApplication Leave) {
			SimpleMailMessage mail= new SimpleMailMessage();
			mail.setTo(staff.getManager().getEmail());
			mail.setSubject("Staff "+staff.getName()+" has applied "+Leave.getLeaveType().getLeaveType());
			mail.setText("Dear "+staff.getManager().getName()+","+ "\r\n"+"\r\n"+ staff.getName()+
					" has applied for "+Leave.getNoOfDays()+" Days of "+
					Leave.getLeaveType().getLeaveType()+". Please Login to approve or reject. "+
					"http://localhost:8080/home/login");
			javaMailSender.send(mail);
		}
}
