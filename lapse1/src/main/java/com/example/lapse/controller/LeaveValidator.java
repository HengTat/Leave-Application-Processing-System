package com.example.lapse.controller;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.lapse.domain.LeaveApplication;
import com.example.lapse.domain.PublicHoliday;
import com.example.lapse.service.LeaveApplicationService;
import com.example.lapse.service.LeaveApplicationServiceImpl;
import com.example.lapse.service.LeaveTypeService;
import com.example.lapse.service.LeaveTypeServiceImpl;
import com.example.lapse.service.PublicHolidayService;
import com.example.lapse.service.PublicHolidayServiceImpl;
import com.example.lapse.utils.DateUtils;

@Component
public class LeaveValidator implements Validator {
	
	  @Autowired
	  private LeaveApplicationService laservice;
	  
	  @Autowired
	  public void setLeaveApplicationService(LeaveApplicationServiceImpl laserviceImpl) {
	    this.laservice = laserviceImpl;
	  }
	  
	  @Autowired
	  private LeaveTypeService ltservice;
	  
	  @Autowired
	  public void setLeaveTypeService(LeaveTypeServiceImpl ltserviceImpl) {
	    this.ltservice = ltserviceImpl;
	  }
	  
	  @Autowired
	  private PublicHolidayService pubservice;
	  
	  @Autowired 
	  public void setPublicHoldidayService(PublicHolidayServiceImpl pubserviceImpl) {
		  this.pubservice = pubserviceImpl;
	  }

	  @Override
	  public boolean supports(Class<?> clazz) {
	    return LeaveApplication.class.equals(clazz);
	  }
	  

	  @Override
	  public void validate(Object target, Errors errors) {
		  LeaveApplication application = (LeaveApplication) target;

		  // Compensation Leave Validation		
		  if (application.getLeaveType().getLeaveType().equals("Compensation Leave")) {
			  if (application.getStartDate() != null && application.getEndDate() != null) {
				  Calendar calStart = DateUtils.dateToCalendar(application.getStartDate());
				  Calendar calEnd = DateUtils.dateToCalendar(application.getEndDate());
				  Date trimmedAppDate = DateUtils.trim(application.getApplicationDate());
				  Calendar calApp = DateUtils.dateToCalendar(trimmedAppDate);

				  //		Backdate check
				  boolean todayAndUp = DateUtils.startDateBeforeEndDate(calApp, calStart);
				  if (todayAndUp == false) {
					  errors.rejectValue("startDate", "leave.date.past");
				  }

				  if (todayAndUp) {

					  //	    Applied startdate <= endDate (min 1 day)
					  boolean status = DateUtils.startDateBeforeEndDate(calStart, calEnd);
					  if (status == false) { 
						  errors.rejectValue("startDate", "leave.date.conflict");
					  }

					  if (status) {
						  //get a list of public holidays
						  List<PublicHoliday> phList = pubservice.findAll();
						  //iterate through each public holiday with dates applied
						  float WeekdayPH = 0f;

						  for (Iterator<PublicHoliday> iterator = phList.iterator(); iterator.hasNext();) {
							  PublicHoliday publicHoliday = (PublicHoliday) iterator.next();

							  Calendar calPHStart = DateUtils.dateToCalendar(publicHoliday.getStartDate());
							  Calendar calPHEnd = DateUtils.dateToCalendar(publicHoliday.getEndDate());

							  //when applied start date = ph start date or applied end date = ph end date
							  if (application.getStartDate().equals(publicHoliday.getStartDate()) || 
									  application.getEndDate().equals(publicHoliday.getEndDate())) {
								  // if PH start != sunday or sat, add 1 (phStart on weekdayday)
								  // if PH end != PHstart && PH end !=sunday or sat, add 1 (phEnd on weekdays)				
								  WeekdayPH = DateUtils.countWeekDayPH(calPHStart, calPHEnd);
							  }

							  if (application.getStartDate().before(publicHoliday.getStartDate()) && 
									  application.getEndDate().after(publicHoliday.getEndDate())) {
								  WeekdayPH =  WeekdayPH + DateUtils.countWeekDayPH(calPHStart, calPHEnd);
							  }
						  }
						  
						  float daysBetween = ChronoUnit.DAYS.between(calStart.toInstant(), calEnd.toInstant()) + 1;
						  if (daysBetween <= 14) {
							  daysBetween = DateUtils.removeWeekends(calStart, calEnd);
						  }
						  
						  if (daysBetween - WeekdayPH == 0) {
							  errors.rejectValue("startDate", "leave.date.zero");
						  }
						  else {
							  //		Current application period not overlapping with applied, updated, approved applications
							  ArrayList<LeaveApplication> lalist = laservice.findApplicationsExCancelDeleteReject((Integer) application.getStaff().getId());

							  for (Iterator<LeaveApplication> iterator = lalist.iterator(); iterator.hasNext();) {
								  LeaveApplication application2 = (LeaveApplication) iterator.next();
								  if ((application2.getEndDate().after(application.getStartDate()) && 
										  application2.getStartDate().before(application.getEndDate()) || 
										  application2.getStartDate().equals(application.getStartDate())) && 
										  application2.getId()!=application.getId()) {
									  errors.rejectValue("startDate", "leave.date.repeat");
								  }
							  }
						  }
					  }
				  }
			  }
		  }

		  //	Other Leave Types Validation
		  if (!application.getLeaveType().getLeaveType().equals("Compensation Leave")) {
//			 	Process dates for checks	    
			  if (application.getStartDate() != null && application.getEndDate() != null) {
				  Calendar calStart = DateUtils.dateToCalendar(application.getStartDate());
				  Calendar calEnd = DateUtils.dateToCalendar(application.getEndDate());
				  Date trimmedAppDate = DateUtils.trim(application.getApplicationDate());
				  Calendar calApp = DateUtils.dateToCalendar(trimmedAppDate);

				  //		Backdate check
				  boolean todayAndUp = DateUtils.startDateBeforeEndDate(calApp, calStart);
				  if (todayAndUp == false) {
					  errors.rejectValue("startDate", "leave.date.past");
				  }

				  if (todayAndUp) {

					  //	    Applied startdate <= endDate (min 1 day)
					  boolean status = DateUtils.startDateBeforeEndDate(calStart, calEnd);
					  if (status == false) { 
						  errors.rejectValue("startDate", "leave.date.conflict");
					  }
					  //		Zero noOfDays check
					  if (status) {
						  //get a list of public holidays
						  List<PublicHoliday> phList = pubservice.findAll();
						  //iterate through each public holiday with dates applied
						  float WeekdayPH = 0f;

						  for (Iterator<PublicHoliday> iterator = phList.iterator(); iterator.hasNext();) {
							  PublicHoliday publicHoliday = (PublicHoliday) iterator.next();

							  Calendar calPHStart = DateUtils.dateToCalendar(publicHoliday.getStartDate());
							  Calendar calPHEnd = DateUtils.dateToCalendar(publicHoliday.getEndDate());

							  //when applied start date = ph start date or applied end date = ph end date
							  if (application.getStartDate().equals(publicHoliday.getStartDate()) || 
									  application.getEndDate().equals(publicHoliday.getEndDate())) {
								  // if PH start != sunday or sat, add 1 (phStart on weekdayday)
								  // if PH end != PHstart && PH end !=sunday or sat, add 1 (phEnd on weekdays)				
								  WeekdayPH = DateUtils.countWeekDayPH(calPHStart, calPHEnd);
							  }

							  if (application.getStartDate().before(publicHoliday.getStartDate()) && 
									  application.getEndDate().after(publicHoliday.getEndDate())) {
								  WeekdayPH =  WeekdayPH + DateUtils.countWeekDayPH(calPHStart, calPHEnd);
							  }
						  }

						  float daysBetween = ChronoUnit.DAYS.between(calStart.toInstant(), calEnd.toInstant()) + 1;
						  if (daysBetween <= 14) {
							  daysBetween = DateUtils.removeWeekends(calStart, calEnd);
						  }
						  
						  if (daysBetween - WeekdayPH == 0) {
							  errors.rejectValue("startDate", "leave.date.zero");
						  }
						  else {
							  //		Current application period not overlapping with applied, updated, approved applications
							  ArrayList<LeaveApplication> lalist = laservice.findApplicationsExCancelDeleteReject((Integer) application.getStaff().getId());

							  for (Iterator<LeaveApplication> iterator = lalist.iterator(); iterator.hasNext();) {
								  LeaveApplication application2 = (LeaveApplication) iterator.next();
								  if ((application2.getEndDate().after(application.getStartDate()) && 
										  application2.getStartDate().before(application.getEndDate()) || 
										  application2.getStartDate().equals(application.getStartDate())) && 
										  application2.getId()!=application.getId()) {
									  errors.rejectValue("startDate", "leave.date.repeat");
								  }
							  }

							  //	 	Check for sufficient leave balance 
							  float entitlement = ltservice.findEntitlementByLeaveType(application.getLeaveType().getLeaveType());
							  Integer leaveTypeId = ltservice.findIdByLeaveType(application.getLeaveType().getLeaveType());
							  float consumed = laservice.getSumOfLeavesAppliedByStaff((Integer) application.getStaff().getId(), leaveTypeId);
							  if ((entitlement - consumed  - daysBetween) < 0) {
								  errors.rejectValue("endDate", "leave.balance");
							  };  

							  //		Overseas Trip true, contact details required	    
							  if (application.isOverseasTrip() && application.getContactDetails().isEmpty()) {
								  errors.rejectValue("contactDetails", "leave.contact.empty");
							  }
						  }
					  }
				  }
			  }
		  }  
	  }
}