package com.example.lapse.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.lapse.domain.LeaveType;
import com.example.lapse.domain.Staff;
import com.example.lapse.service.LeaveApplicationService;
import com.example.lapse.service.LeaveApplicationServiceImpl;
import com.example.lapse.service.LeaveTypeService;
import com.example.lapse.service.LeaveTypeServiceImpl;
import com.example.lapse.service.StaffService;
import com.example.lapse.service.StaffServiceImpl;
import com.example.lapse.utils.Login;

@Controller
@RequestMapping("/home")
public class LoginController {
	
	@Autowired
	private StaffService staffservice;
	
	@Autowired
	private LeaveApplicationService laservice;

	@Autowired
	private LeaveTypeService ltservice;

	@Autowired
	public void setStaffService(StaffServiceImpl sserviceImpl) {
		this.staffservice = sserviceImpl;
	}
	
	@Autowired
	public void setLeaveApplicationService(LeaveApplicationServiceImpl laserviceImpl) {
		this.laservice = laserviceImpl;
	}
	
	@Autowired
	public void setLeaveTypeService(LeaveTypeServiceImpl ltserviceImpl) {
		this.ltservice = ltserviceImpl;
	}
	
	@Autowired
	private LoginValidator loginValidator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(loginValidator);
	}
	
	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("login", new Login());
		return "login";
	}
	
	@RequestMapping("/submit")
	public String submit(@ModelAttribute("login") @Valid Login login, BindingResult bindingResult, HttpSession session, Model model) {
		System.out.println(login);
		if (bindingResult.hasErrors()) {
			return "login";
		}
		Staff currStaff = new Staff();
		currStaff.setEmail(login.getEmail());
		currStaff.setPassword(login.getPassword());
		currStaff = staffservice.findStaffByEmail(currStaff.getEmail());
		session.setAttribute("role", currStaff.getRole());
		session.setAttribute("id", currStaff.getId());
		session.setAttribute("name", currStaff.getName());
		return "redirect:/home/";
	}
	
//	@RequestMapping("/submit")
//	public String submit(@ModelAttribute("staff") Staff staff, HttpSession session) {
//		boolean staffstatus = staffservice.validateStaff(staff.getEmail(), staff.getPassword());
//	    if (staffstatus==true) {
//	    	Staff currStaff = staffservice.findStaffByEmail(staff.getEmail());
//	    	session.setAttribute("role", currStaff.getRole());
//	    	session.setAttribute("id", currStaff.getId());
//	    	return "homepage";
//	    }
//	    return "login";
//	  }
	
	
	@RequestMapping("/")
	public String index(Model model, HttpSession session) {
		if (session.getAttribute("id") == null) {
			return "redirect:/home/login";
		}
		
		int staffId = (int) session.getAttribute("id");
		
		List<LeaveType> leaveTypeArr = ltservice.findAllLeaveTypesEXCL();
		Iterator<LeaveType> leaveTypeIterator = leaveTypeArr.iterator();
		List<Float> balanceArr = new ArrayList<Float>();
		List<Float> UsedArr = new ArrayList<Float>();
		while(leaveTypeIterator.hasNext()) {
			LeaveType lt = leaveTypeIterator.next();
			Float leavesApplied = laservice.getSumOfLeavesAppliedByStaff(staffId, lt.getId());
			balanceArr.add(lt.getEntitlement() - leavesApplied);
			UsedArr.add (leavesApplied);
		}
		model.addAttribute("balanceArr", balanceArr);
		model.addAttribute("leaveTypes",leaveTypeArr);
				
		//add
			//first model	
			List<String> Listofleave=ltservice.findAllLeaveTypeNamesExCL();
			List<Float> ListEntitlement = new ArrayList<Float>();
			for (Iterator iterator = Listofleave.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				ListEntitlement.add(ltservice.findEntitlementByLeaveType(string));
			}
			model.addAttribute("ListofEntitlement", ListEntitlement);
			System.out.println(ListEntitlement);
			//second model					
			model.addAttribute("UsedLeave", UsedArr);
			ArrayList<String> List=ltservice.findAllLeaveTypeNamesExCL();
			model.addAttribute("TypesofLeave", List);
			return "homePage";
	}
	
	
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "logout";
	}
	
}