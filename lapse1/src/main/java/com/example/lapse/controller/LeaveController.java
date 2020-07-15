package com.example.lapse.controller;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.lapse.domain.LeaveApplication;
import com.example.lapse.domain.LeaveType;
import com.example.lapse.domain.PublicHoliday;
import com.example.lapse.domain.Staff;
import com.example.lapse.enums.LeaveStatus;
import com.example.lapse.service.Emailnotificationservice;
import com.example.lapse.service.LeaveApplicationService;
import com.example.lapse.service.LeaveApplicationServiceImpl;
import com.example.lapse.service.LeaveTypeService;
import com.example.lapse.service.LeaveTypeServiceImpl;
import com.example.lapse.service.PublicHolidayService;
import com.example.lapse.service.PublicHolidayServiceImpl;
import com.example.lapse.service.StaffService;
import com.example.lapse.service.StaffServiceImpl;
import com.example.lapse.utils.DateUtils;

@Controller
@RequestMapping("/leave")
public class LeaveController {

	@Autowired 
	private LeaveApplicationService lservice;
	
	@Autowired
	public void setLeaveApplicationService (LeaveApplicationServiceImpl lserviceImpl) {
		this.lservice = lserviceImpl;
	}
	
	@Autowired
	private LeaveTypeService ltservice;
	
	@Autowired
	public void setLeaveTypeService(LeaveTypeServiceImpl ltserviceImpl) {
		this.ltservice = ltserviceImpl;
	}
	
	@Autowired
	private StaffService staffservice;

	@Autowired
	public void setStaffService(StaffServiceImpl sserviceImpl) {
		this.staffservice = sserviceImpl;
	}
	
	@Autowired
	private Emailnotificationservice emailservice;
	
	@Autowired
	private PublicHolidayService pubservice;
	@Autowired
	public void setPublicHolidayService(PublicHolidayServiceImpl phserviceImpl) {
		this.pubservice = phserviceImpl;
	}
	
	@Autowired
	public void setEmailnotificationservice(Emailnotificationservice emailservice) {
		this.emailservice = emailservice;
	}
	
	@Autowired
	private LeaveValidator leaveValidator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(leaveValidator);
	}
//	@RequestMapping(value = "/list")
//	public String list(Model model) {
//		model.addAttribute("leaveapplications", lservice.listAllLeaveApplications());
//		return "leaveapplications";
//	}

	
	//view history
	
	@RequestMapping(value = "/viewhistory")
	public String viewMyLeaveHistory(Model model, HttpSession session) {
		int id = (int) session.getAttribute("id");
		List<LeaveApplication> leaves = lservice.findApplicationByStaffId(id);
		model.addAttribute("leaves", leaves);
		
		ArrayList<Boolean> status=new ArrayList<Boolean>();
		for(LeaveApplication l:leaves) {
			boolean s=lservice.validateforCancel(l);
			status.add(s);
		}
		model.addAttribute("status", status);
		
		return "viewMyHistory";
	}
	
	@RequestMapping(value = "/viewSubHistory")
	public String viewSubHistory(Model model, HttpSession session) {
		int id = (int) session.getAttribute("id");
		List<LeaveApplication> leaves = lservice.findSubLeaveAppByManagerId(id);
		model.addAttribute("leaves", leaves);
		return "viewSubHistory";
	}
	
	
	@RequestMapping(value = "/add")
	public String addForm(Model model) {
		model.addAttribute("leaveapplication", new LeaveApplication());
		model.addAttribute("leavetypes", ltservice.findAllLeaveTypeNamesExCL());
		return "applyLeave";

	}
	
	//for compensation leave
	@RequestMapping(value = "/addcompensation")
	public String addCompensation(Model model) {
		model.addAttribute("compensationapplication", new LeaveApplication());
		model.addAttribute("leavetype", "Compensation Leave");
		return "claimCompensation";

	}
	@RequestMapping(value ="/submitCompensation")
	public String submitCompensation(@ModelAttribute("compensationapplication") @Valid LeaveApplication application, BindingResult bindingResult, HttpSession session, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("compensationapplication", application);
			model.addAttribute("leavetype", "Compensation Leave");
			return "claimCompensation";
		}
		
		Staff currStaff = staffservice.findStafftById((Integer)session.getAttribute("id"));
		LeaveType leaveType = ltservice.findLeaveTypeByLeaveType("Compensation Leave");
		application.setStaff(currStaff);
		application.setLeaveType(leaveType);
		
		Calendar calStart = DateUtils.dateToCalendar(application.getStartDate());
	    Calendar calEnd = DateUtils.dateToCalendar(application.getEndDate());
		float daysBetween = (ChronoUnit.DAYS.between(calStart.toInstant(), calEnd.toInstant()) + 1f);
		System.out.println("after chro days between " + daysBetween );

		if(daysBetween <= 14) {
			daysBetween = DateUtils.removeWeekends(calStart, calEnd);
			System.out.println("inside < 14 if condition " + daysBetween );
		}
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
		//factoring public holidays on weekdays
		daysBetween = daysBetween - WeekdayPH;
		application.setNoOfDays(daysBetween);
		if(application.isHalfday() == true) {
			daysBetween = daysBetween - 0.5f;
			System.out.println("inside if " + daysBetween);
		}
		application.setNoOfDays(daysBetween);
		lservice.addLeaveApplication(application);
		
		return "redirect:/home/";
	}

	@RequestMapping("/submit")
	public String submit(@ModelAttribute("leaveapplication") @Valid LeaveApplication application, BindingResult bindingResult, HttpSession session, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("leaveapplication", application);
			model.addAttribute("leavetypes", ltservice.findAllLeaveTypeNamesExCL());
			return "applyLeave";
		}

		Staff currStaff = staffservice.findStafftById((Integer)session.getAttribute("id"));
		LeaveType leaveType = ltservice.findLeaveTypeByLeaveType(application.getLeaveType().getLeaveType());
		application.setStaff(currStaff);
		application.setLeaveType(leaveType);
		Calendar calStart = DateUtils.dateToCalendar(application.getStartDate());
	    Calendar calEnd = DateUtils.dateToCalendar(application.getEndDate());
	    
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
	    System.out.println("total PH weekdays " + WeekdayPH);
		float daysBetween = ChronoUnit.DAYS.between(calStart.toInstant(), calEnd.toInstant()) + 1;
		if(daysBetween <= 14) {
			daysBetween = DateUtils.removeWeekends(calStart, calEnd);
		}
		//factoring public holidays on weekdays
		daysBetween = daysBetween - WeekdayPH;
		application.setNoOfDays(daysBetween);
		if (currStaff.getRole().equals("Manager")) {
			application.setLeaveStatus(LeaveStatus.APPROVED);
		}
		lservice.addLeaveApplication(application);
		emailservice.sendleavecreationsucessful(currStaff, application);
		if(currStaff.getManager()!=null) {
			emailservice.alertmanageofleaveapproval(currStaff, application);
			}
		return "redirect:/home/";
	}
	
	 @RequestMapping(value = "/viewallpending")
	 public String viewpendingleaveapproval(Model model,HttpSession session) {	
		 int id=(int) session.getAttribute("id");
		 List<LeaveApplication> PendingLeaveList=lservice.findpendingleaveapproval(id);					  		 			 					
		 model.addAttribute(("LeaveApplication"), PendingLeaveList);
		 return"Managerapproval";
	 }
	 
	 @RequestMapping(value = "/approve/{id}")
	 public String approveleaveapplication(@PathVariable("id") Integer id) {
	 lservice.approveleaveapplication(id);
	 return "Managerapproval";
}

    @RequestMapping(value = "/reject/{id}")
    public String rejectleaveapplication(@PathVariable("id") Integer id) {
    lservice.rejectleaveapplication(id);
    return "Managerapproval";	
    } 
    
    @RequestMapping(value="/viewdetails/{id}")
	public String viewDetailPending(@PathVariable("id") int id,Model model, HttpSession session)
	{
		LeaveApplication leave=lservice.findApplicationById(id);
		model.addAttribute("leaveapplication", leave);
		
		//retrieve leave applications between start and end date of current application
		
		Date currStartDate=leave.getStartDate();
		Date currEndDate=leave.getEndDate();		
		int currUserId=(int) session.getAttribute("id");
		
		List<LeaveApplication> leaveList=lservice.findApplicationByManagerId(currUserId);
		ArrayList<LeaveApplication> finalLeaveAppList=new ArrayList<LeaveApplication>();
		
		for (Iterator<LeaveApplication> iterator=leaveList.iterator();iterator.hasNext();) {
			 LeaveApplication la=(LeaveApplication)iterator.next();
			 if(la.getId()!=leave.getId()) {
				 boolean isTrue=lservice.isWithinDateRange(currStartDate, currEndDate, la.getStartDate(), la.getEndDate());
				 if(isTrue) {
					 finalLeaveAppList.add(la);
				 }
			 }			 
		}
		
		model.addAttribute("leaveListExceptApproveAndReject",finalLeaveAppList);
		return "viewDetailPending";
	}
	
	@RequestMapping(value = "/updateStatus")
	public String updatePendingStatus(@ModelAttribute("leaveapplication") LeaveApplication leaveApp, Model model) {
		if(leaveApp.getLeaveStatus()==LeaveStatus.REJECTED) {
			if(!(leaveApp.getManagerComment()!=null) || leaveApp.getManagerComment()=="") {
				model.addAttribute("error", "ERROR: Please add comment!");
				return "forward:/leave/viewdetails/"+leaveApp.getId();
			}
		}
		
		lservice.updateLeaveStatus(leaveApp.getId(), leaveApp.getLeaveStatus(), leaveApp.getManagerComment());
		emailservice.sendleavestatusemail(staffservice.findStafftById((leaveApp.getStaff().getId())),leaveApp);
		return "forward:/leave/viewallpending";
	}
	
	@RequestMapping(value = "/delete/{id}")
	 public String deleteLeaveapplication(@PathVariable("id") Integer id) {
	  lservice.deleteLeaveApplication(lservice.findApplicationById(id));
	  return "forward:/leave/viewhistory";
	 }
	
	  @RequestMapping(value="/cancel/{id}")
	  public String cancel(@PathVariable("id") Integer id,Model model)
	  {
	    LeaveApplication leaveapplication= lservice.findApplicationById(id);
	    lservice.cancelLeaveApplication(leaveapplication);
	    return "forward:/leave/viewhistory";
	  }
	  
	  @RequestMapping(value="/edit/{id}")
	  public String edit(@PathVariable("id") Integer id,Model model)
	  {
	    LeaveApplication leaveapplication= lservice.findApplicationById(id);
	    model.addAttribute("leaveapplication", leaveapplication);
	    return "updateLeaveApplication";
	  }
	  
	  @RequestMapping("/updateLA")
		public String updateLA(@ModelAttribute("leaveapplication") @Valid LeaveApplication application, BindingResult bindingResult, HttpSession session, Model model) {
			if (bindingResult.hasErrors()) {
				model.addAttribute("leaveapplication", application);
				model.addAttribute("leavetypes", ltservice.findAllLeaveTypeNamesExCL());
				return "updateLeaveApplication";
			}
				
			Staff currStaff = staffservice.findStafftById((Integer)session.getAttribute("id"));
			LeaveType leaveType = ltservice.findLeaveTypeByLeaveType(application.getLeaveType().getLeaveType());
			application.setStaff(currStaff);
			application.setLeaveType(leaveType);
			Calendar calStart = DateUtils.dateToCalendar(application.getStartDate());
		    Calendar calEnd = DateUtils.dateToCalendar(application.getEndDate());
			float daysBetween = ChronoUnit.DAYS.between(calStart.toInstant(), calEnd.toInstant()) + 1;
			if(daysBetween <= 14) {
				daysBetween = DateUtils.removeWeekends(calStart, calEnd);
			}
			application.setNoOfDays(daysBetween);
			if (currStaff.getRole().equals("Manager")) {
				application.setLeaveStatus(LeaveStatus.APPROVED);
			}
			lservice.addLeaveApplication(application);
			emailservice.sendleavecreationsucessful(currStaff, application);
			emailservice.alertmanageofleaveapproval(currStaff, application);
			return "redirect:/home/";
		}
}
