package com.example.lapse.controller;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.lapse.domain.LeaveApplication;
import com.example.lapse.domain.Manager;
import com.example.lapse.domain.Staff;
import com.example.lapse.service.LeaveApplicationService;
import com.example.lapse.service.LeaveApplicationServiceImpl;
import com.example.lapse.service.ManagerService;
import com.example.lapse.service.ManagerServiceImpl;
import com.example.lapse.service.StaffService;
import com.example.lapse.service.StaffServiceImpl;

@Controller
@RequestMapping(value = "/staff")

public class StaffController {

	@Autowired
	private StaffService staffservice;
	
	@Autowired
	public void setStaffService(StaffServiceImpl sserviceImpl) {
		this.staffservice = sserviceImpl;
	}
	
	@Autowired
	private ManagerService mservice;
	
	
	@Autowired 
	public void setManagerService(ManagerServiceImpl mserviceImpl) {
		this.mservice = mserviceImpl;
	}
	
	@Autowired
	private LeaveApplicationService laservice;
	
	
	@Autowired 
	public void setLeaveApplicationService(LeaveApplicationServiceImpl laserviceImpl) {
		this.laservice=laserviceImpl;
	}
	
	
	
	  @RequestMapping(value = "/admin")
	  public String adminPage() {
		  return "admin-home";
	  }
	
	  //staff list
	  @RequestMapping(value = "/list")
	  public String list(Model model, HttpSession session) {
		  return listByPage(model,session,1,5);
	  }
	  
	  @GetMapping("/page")
	  public String listByPage(Model model,HttpSession session,@RequestParam("currentPage")int currentPage, 
			  @RequestParam("numberofitems") int numberofitems ) {
		  	  
		  Page<Staff>page=staffservice.findAll(currentPage,numberofitems);
		  List<Staff> listofstaff= page.getContent();		  
		  long totalItems=page.getTotalElements();
		  int totalPages=page.getTotalPages();
		  model.addAttribute("numberofitems",numberofitems);
		  model.addAttribute("currentPage",currentPage);
		  model.addAttribute("totalItems",totalItems);
		  model.addAttribute("totalPages",totalPages);
	    model.addAttribute("slist", listofstaff);
	    Object error = session.getAttribute("error");
	    if (error != null) {
	    	 model.addAttribute("error",String.valueOf(error));
	    	 session.setAttribute("error", null);
	    }
	    return "staff-list";
	  }
	  
	  //add staff
	  @RequestMapping(value = "/add")
	  public String addStaff(Model model) {
	    model.addAttribute("staff", new Staff());
	    model.addAttribute("mnames", mservice.findAllManagerNames());
	    return "staff-form";
	  }
	  
	  //save staff & redirect to staff list
	  @RequestMapping(value = "/save")
	  public String saveStaff(@ModelAttribute("staff") @Valid Staff staff, BindingResult result, Model model) {	
		  if (result.hasErrors()) { 
			  model.addAttribute("staff", staff);
			  model.addAttribute("mnames", mservice.findAllManagerNames());
			  return "staff-form"; 
		  }
		  if (staff.getManager() == null) {
			  staff.setManager(null);
		  } else {
			  Manager savedManager = mservice.findManagerByName(staff.getManager().getName());
			  staff.setManager(savedManager);
		  }
		  if (staff.getRole().equals("Manager")) {
			  Manager myManager = mservice.findManagerById(staff.getId());
			  myManager.setEmail(staff.getEmail());
			  myManager.setName(staff.getName());
			  myManager.setPassword(staff.getPassword());
			  mservice.saveManager(myManager);
		  } else {
			  staffservice.saveStaff(staff);
		  }
		  return "forward:/staff/list";
	  }
	  
	  
	  //edit
	  @RequestMapping(value = "/edit/{id}")
	  public String editForm(@PathVariable("id") Integer id, Model model) {
	    Staff staff = staffservice.findStafftById(id);
	    model.addAttribute("staff", staff);
	    model.addAttribute("mnames", mservice.findAllManagerNames());
	    return "staff-form";
	  }
	
	  @RequestMapping(value = "/delete/{id}")
	  public String deleteStaff(@PathVariable("id") Integer id) {
		Staff myStaff = staffservice.findStafftById(id);
		Collection<LeaveApplication> laList = myStaff.getLeaveTransactions(); 
		Iterator laListIterator = laList.iterator();
		while (laListIterator.hasNext()) {
			LeaveApplication staffLA = (LeaveApplication) laListIterator.next();
			staffLA.setStaff(null);
			laservice.saveLeaveApplication(staffLA);
		}
		if (myStaff.getRole().equals("Manager")) {
		  Manager myManager = mservice.findManagerById(myStaff.getId());
		  Collection<Staff> sList = myManager.getStaffList();
		  Iterator sListIterator = sList.iterator();
		  while (sListIterator.hasNext()) {
			  Staff managerStaff = (Staff) sListIterator.next();
			  managerStaff.setManager(null);
			  staffservice.saveStaff(managerStaff);
		  }
		  
		  mservice.deleteManager(myManager);
	  } else {
		  staffservice.deleteStaff(myStaff);
	  }
	    return "forward:/staff/list";
	  }
	  
	  @RequestMapping(value = "/promote/{id}")
	  public String promoteUser(@PathVariable("id") Integer id) {
		staffservice.promoteStaff(id);
		return "redirect:/staff/list";
	  }
	  
	  @RequestMapping(value = "/demote/{id}")
	  public String demoteUser(@PathVariable("id") Integer id, HttpSession session) {
		int result = staffservice.demoteStaff(id);
		if (result == 0) {
			session.setAttribute("error","User already has subordinates and cannot be demoted");
		}
		return "redirect:/staff/list";
	  }
	
	
	

}
