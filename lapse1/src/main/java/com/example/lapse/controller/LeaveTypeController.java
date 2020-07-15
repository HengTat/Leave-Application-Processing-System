package com.example.lapse.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.lapse.domain.LeaveType;
import com.example.lapse.service.LeaveTypeService;
import com.example.lapse.service.LeaveTypeServiceImpl;

@Controller
@RequestMapping("/leavetype")
public class LeaveTypeController {
	
	@Autowired
	LeaveTypeService ltservice;
	
	@Autowired
	public void LeaveTypeService(LeaveTypeServiceImpl ltserviceImpl) {
		this.ltservice = ltserviceImpl;
	}
	
	//list
		@RequestMapping(value = "/list")
		 public String list(Model model,HttpSession session) {
		  model.addAttribute("ltlist", ltservice.findAll());
		  Object error = session.getAttribute("error");
		  if (error != null) {
		  	 model.addAttribute("error",String.valueOf(error));
		  	 session.setAttribute("error", null);
		  }
		  return "leaveType-list";
		 }
	
	//add
	@RequestMapping(value = "/add")
	public String addForm(Model model) {
		model.addAttribute("leavetype", new LeaveType());
		return "leaveType-form";
	}
	
	//delete
	@RequestMapping(value = "/delete/{id}")
	public String deleteLeaveType(@PathVariable("id") Integer id) {
		ltservice.deleteLeaveType(ltservice.findLeaveTypeById(id));
	    return "forward:/leavetype/list";
	  }
	
	//save
	@RequestMapping(value="/save")
	public String saveLeaveType(@ModelAttribute("leavetype") @Valid LeaveType leavetype, BindingResult result, Model model) {
		  if (result.hasErrors()) {
		      model.addAttribute("leavetype", leavetype); 
		      return "leaveType-form";
		    }
	ltservice.saveLeaveType(leavetype);
	return "forward:/leavetype/list";
	}
}
