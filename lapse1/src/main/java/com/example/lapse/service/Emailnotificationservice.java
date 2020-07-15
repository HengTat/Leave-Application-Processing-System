package com.example.lapse.service;

import javax.mail.Message;

import org.springframework.mail.SimpleMailMessage;

import com.example.lapse.domain.LeaveApplication;
import com.example.lapse.domain.Staff;

public interface Emailnotificationservice {
	public void sendleavestatusemail(Staff staff,LeaveApplication Leave);
	public void sendleavecreationsucessful(Staff staff,LeaveApplication Leave);
	public void alertmanageofleaveapproval(Staff staff,LeaveApplication Leave);
	
}
